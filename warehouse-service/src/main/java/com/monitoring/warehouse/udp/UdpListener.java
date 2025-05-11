package com.monitoring.warehouse.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.monitoring.common.dto.SensorData;
import com.monitoring.warehouse.kafka.SensorDataProducer;
import com.monitoring.warehouse.properties.WarehouseProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

@Slf4j
@Component
public class UdpListener {

    private static final Pattern MESSAGE_PATTERN =
            Pattern.compile("sensor_id=(\\w+);\\s*value=(\\d+\\.?\\d*)");

    private final String warehouseId;
    private final Map<String, Integer> typePortMap;
    private final SensorDataProducer producer;
    private final Disposable.Composite disposables = Disposables.composite();

    public UdpListener(WarehouseProperties config, SensorDataProducer producer) {
        this.warehouseId = config.id();
        this.producer = producer;
        this.typePortMap = Map.of(
                SensorData.TEMPERATURE, config.udp().temperature(),
                SensorData.HUMIDITY, config.udp().humidity()
        );
    }

    @PostConstruct
    public void startListeners() {
        log.info("Starting UDP listeners for warehouse {}", warehouseId);

        typePortMap.forEach((type, port) -> {
            Disposable disposable = listen(port, type)
                    .doOnNext(data -> log.info("Publishing {} data: {}", type, data))
                    .retryWhen(Retry.backoff(Long.MAX_VALUE, Duration.ofSeconds(1))
                            .maxBackoff(Duration.ofMinutes(1))
                            .doBeforeRetry(rs ->
                                    log.warn("Retrying listener for {} after failure (attempt #{})",
                                            type, rs.totalRetriesInARow() + 1))
                    )
                    .subscribe(
                            producer::publish,
                            err -> log.error("Listener for {} on port {} terminated: {}",
                                    type, port, err.getMessage(), err)
                    );

            disposables.add(disposable);
        });
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down UDP listeners...");
        disposables.dispose();
    }

    private Flux<SensorData> listen(int port, String type) {
        return Flux.create(emitter -> {
            new Thread(() -> {
                try (DatagramSocket socket = new DatagramSocket(null)) {
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress("127.0.0.1", port));
                    log.info("Bound UDP socket for {} on port {}", type, port);

                    byte[] buffer = new byte[1024];

                    while (!emitter.isCancelled()) {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet); // blocking

                        String msg = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                        log.debug("Port {} received: {}", port, msg);

                        SensorData data = parseMessage(msg, type);
                        if (data != null) {
                            emitter.next(data);
                        } else {
                            log.debug("Unmatched message on port {}: {}", port, msg);
                        }
                    }
                } catch (Exception e) {
                    if (!emitter.isCancelled()) {
                        emitter.error(e);
                    }
                }
            }, "udp-listener-" + port).start(); // use named thread for clarity
        });
    }

    private SensorData parseMessage(String message, String type) {
        Matcher matcher = MESSAGE_PATTERN.matcher(message.trim());
        if (!matcher.matches()) {
            return null;
        }
        String sensorId = matcher.group(1);
        double value = Double.parseDouble(matcher.group(2));
        return new SensorData(warehouseId, sensorId, type, value);
    }
}
