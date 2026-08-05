package com.telemetryvault.event.producer;

import com.telemetryvault.event.model.FileVaultEvent;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class VaultEventProducer {

    private static final Logger log = LoggerFactory.getLogger(VaultEventProducer.class);
    private final KafkaTemplate<String, FileVaultEvent> kafkaTemplate;
    private final Tracer tracer;

    public VaultEventProducer(KafkaTemplate<String, FileVaultEvent> kafkaTemplate, Tracer tracer) {
        this.kafkaTemplate = kafkaTemplate;
        this.tracer = tracer;
    }

    public CompletableFuture<SendResult<String, FileVaultEvent>> sendEvent(String topic, FileVaultEvent event) {
        Span newSpan = tracer.nextSpan().name("kafka-publish-span").tag("file.id", event.getFileId()).start();
        try (Tracer.SpanInScope ws = tracer.withSpan(newSpan)) {
            log.info("Publishing FileVaultEvent to Kafka topic [{}]: EventID={}, FileID={}",
                    topic, event.getEventId(), event.getFileId());

            CompletableFuture<SendResult<String, FileVaultEvent>> future = kafkaTemplate.send(topic, event.getFileId(), event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Event successfully published to topic [{}] partition [{}] at offset [{}]",
                            topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                } else {
                    log.error("Failed to publish Event [{}] to topic [{}]: {}", event.getEventId(), topic, ex.getMessage());
                }
            });

            return future;
        } finally {
            newSpan.end();
        }
    }
}
