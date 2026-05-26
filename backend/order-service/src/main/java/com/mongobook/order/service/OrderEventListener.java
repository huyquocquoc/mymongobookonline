package com.mongobook.order.service;

import com.mongobook.order.event.OrderEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {
    private static final Log logger = LogFactory.getLog(OrderEventListener.class);

    @KafkaListener(topics = "${app.topics.order-events}")
    public void handle(OrderEvent event) {
        logger.info("Received order event type=" + event.type() + " orderId=" + event.orderId() + " total=" + event.totalAmount());
    }
}

