package com.telemetryvault.event.service;

import com.telemetryvault.event.consumer.DlqConsumer;
import com.telemetryvault.event.consumer.VaultEventConsumer;
import com.telemetryvault.event.dto.EventResponse;
import com.telemetryvault.event.dto.PublishEventRequest;
import com.telemetryvault.event.model.FileVaultEvent;
import com.telemetryvault.event.producer.VaultEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class VaultEventService {

    private static final Logger log = LoggerFactory.getLogger(VaultEventService.class);

    private final VaultEventProducer producer;
    private final VaultEventConsumer consumer;
    private final DlqConsumer dlqConsumer;

    public VaultEventService(VaultEventProducer producer, VaultEventConsumer consumer, DlqConsumer dlqConsumer) {
        this.producer = producer;
        this.consumer = consumer;
        this.dlqConsumer = dlqConsumer;
    }

    public EventResponse publishEvent(PublishEventRequest request) {
        String eventId = request.getEventId() != null ? request.getEventId() : UUID.randomUUID().toString();

        FileVaultEvent event = new FileVaultEvent(
                eventId,
                request.getFileId(),
                request.getUserId(),
                request.getEventType(),
                request.getFilename(),
                request.getFileSize(),
                request.isSimulateFailure(),
                Instant.now().toString()
        );

        log.info("Processing event publication request for FileID: {}", event.getFileId());
        producer.sendEvent("file-vault-events", event);

        return new EventResponse("ACCEPTED", "Event successfully published to Kafka topic", "file-vault-events", event);
    }

    public List<FileVaultEvent> getProcessedEvents() {
        return consumer.getProcessedEvents();
    }

    public List<FileVaultEvent> getDlqEvents() {
        return dlqConsumer.getDlqEvents();
    }
}
