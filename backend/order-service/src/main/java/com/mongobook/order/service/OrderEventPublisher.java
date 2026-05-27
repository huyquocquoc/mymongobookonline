package com.mongobook.order.service;

import com.mongobook.order.domain.BookOrder;
import com.mongobook.order.event.OrderEvent;
import com.mongobook.order.event.OrderEventType;
import java.time.Instant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {
    private static final Log logger = LogFactory.getLog(OrderEventPublisher.class);
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final String topicName;

    public OrderEventPublisher(KafkaTemplate<String, OrderEvent> kafkaTemplate,
                               @Value("${app.topics.order-events}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void publish(BookOrder order, OrderEventType eventType) {
        logger.info("OrderEventPublisher.publish called for orderId=" + order.getId() + " eventType=" + eventType);
        OrderEvent event = new OrderEvent(
                order.getId(),
                order.getUserId(),
                order.getNotificationEmail(),
                eventType,
                order.getItems(),
                order.getTotalAmount(),
                Instant.now()
        );
        kafkaTemplate.send(topicName, order.getId(), event);
    }
}
