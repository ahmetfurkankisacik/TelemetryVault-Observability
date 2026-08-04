package com.telemetryvault.event.consumer;

import com.telemetryvault.event.model.FileVaultEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class VaultEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(VaultEventConsumer.class);
    private final List<FileVaultEvent> processedEvents = Collections.synchronizedList(new ArrayList<>());

    @KafkaListener(topics = "file-vault-events", groupId = "telemetryvault-group")
    public void consume(FileVaultEvent event) {
        log.info("Received FileVaultEvent from Kafka: EventID={}, FileID={}, Type={}, SimulateFailure={}",
                event.getEventId(), event.getFileId(), event.getEventType(), event.isSimulateFailure());

        if (event.isSimulateFailure()) {
            log.error("Simulated Failure triggered for Event ID [{}]! Throwing EventProcessingException...", event.getEventId());
            throw new RuntimeException("Simulated processing exception for event: " + event.getEventId());
        }

        processedEvents.add(event);
        log.info("FileVaultEvent processed successfully: EventID={}", event.getEventId());
    }

    public List<FileVaultEvent> getProcessedEvents() {
        return new ArrayList<>(processedEvents);
    }
}
