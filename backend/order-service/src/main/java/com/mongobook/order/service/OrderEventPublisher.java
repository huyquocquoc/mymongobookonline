package com.mongobook.order.service;

import com.mongobook.order.domain.BookOrder;
import com.mongobook.order.event.OrderEvent;
import com.mongobook.order.event.OrderEventType;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final String topicName;

    public OrderEventPublisher(KafkaTemplate<String, OrderEvent> kafkaTemplate,
                               @Value("${app.topics.order-events}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void publish(BookOrder order, OrderEventType eventType) {
        OrderEvent event = new OrderEvent(
                order.getId(),
                order.getUserId(),
                eventType,
                order.getItems(),
                order.getTotalAmount(),
                Instant.now()
        );
        kafkaTemplate.send(topicName, order.getId(), event);
    }
}

