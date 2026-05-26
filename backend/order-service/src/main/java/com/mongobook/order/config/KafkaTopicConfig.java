package com.mongobook.order.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    private static final Log logger = LogFactory.getLog(KafkaTopicConfig.class);

    @Bean
    public NewTopic orderEventsTopic(@Value("${app.topics.order-events}") String topicName) {
        logger.info("KafkaTopicConfig.orderEventsTopic creating topic=" + topicName);
        return TopicBuilder.name(topicName).partitions(3).replicas(1).build();
    }
}

