package com.telemetryvault.event.config;

import com.telemetryvault.event.model.FileVaultEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

    @Bean
    public NewTopic vaultEventsTopic() {
        return TopicBuilder.name("file-vault-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic vaultEventsDlqTopic() {
        return TopicBuilder.name("file-vault-events.DLQ")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, FileVaultEvent> kafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, ex) -> {
                    log.error("Retries exhausted for record in topic [{}] partition [{}] offset [{}]. Routing to DLQ...",
                            record.topic(), record.partition(), record.offset());
                    return new TopicPartition("file-vault-events.DLQ", 0);
                });

        FixedBackOff backOff = new FixedBackOff(1000L, 2L);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);

        errorHandler.setRetryListeners((record, ex, deliveryAttempt) ->
                log.warn("Retry attempt #{} failed for Event ID [{}] in topic [{}]: {}",
                        deliveryAttempt, record.key(), record.topic(), ex.getMessage())
        );

        return errorHandler;
    }
}
