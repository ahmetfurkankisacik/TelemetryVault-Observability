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
public class DlqConsumer {

    private static final Logger log = LoggerFactory.getLogger(DlqConsumer.class);
    private final List<FileVaultEvent> dlqEvents = Collections.synchronizedList(new ArrayList<>());

    @KafkaListener(topics = "file-vault-events.DLQ", groupId = "telemetryvault-dlq-group")
    public void consumeDlq(FileVaultEvent event) {
        log.warn("🚨 ALERT: Received event in Dead Letter Queue (DLQ)! EventID={}, FileID={}, Filename={}",
                event.getEventId(), event.getFileId(), event.getFilename());
        dlqEvents.add(event);
    }

    public List<FileVaultEvent> getDlqEvents() {
        return new ArrayList<>(dlqEvents);
    }
}
