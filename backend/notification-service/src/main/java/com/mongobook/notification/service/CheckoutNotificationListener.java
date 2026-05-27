package com.mongobook.notification.service;

import com.mongobook.notification.event.OrderEvent;
import com.mongobook.notification.event.OrderEventType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CheckoutNotificationListener {
    private static final Log logger = LogFactory.getLog(CheckoutNotificationListener.class);
    private final EmailNotificationService emailNotificationService;

    public CheckoutNotificationListener(EmailNotificationService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
    }

    @KafkaListener(topics = "${app.topics.order-events}")
    public void onOrderEvent(OrderEvent event) {
        logger.info("Notification service received order event type=" + event.type() + " orderId=" + event.orderId());
        if (event.type() == OrderEventType.CHECKOUT_STARTED) {
            emailNotificationService.sendCheckoutProcessingEmail(event);
        }
    }
}
