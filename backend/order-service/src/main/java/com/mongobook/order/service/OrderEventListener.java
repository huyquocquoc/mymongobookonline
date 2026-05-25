package com.mongobook.order.service;

import com.mongobook.order.event.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {
    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    @KafkaListener(topics = "${app.topics.order-events}")
    public void handle(OrderEvent event) {
        log.info("Received order event type={} orderId={} total={}", event.type(), event.orderId(), event.totalAmount());
    }
}

