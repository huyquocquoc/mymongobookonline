package com.mongobook.notification.service;

import com.mongobook.notification.event.OrderEvent;
import java.text.NumberFormat;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {
    private static final Log logger = LogFactory.getLog(EmailNotificationService.class);

    public void sendCheckoutProcessingEmail(OrderEvent event) {
        String recipient = recipientFor(event);
        if (recipient == null || recipient.isBlank()) {
            logger.warn("Skipping checkout notification because no email address was provided for orderId=" + event.orderId());
            return;
        }

        String subject = "Mongo Book Online checkout is processing";
        String body = "Your checkout for order " + event.orderId()
                + " is now being processed. Total: "
                + NumberFormat.getCurrencyInstance(Locale.US).format(event.totalAmount()) + ".";

        logger.info("Checkout processing email to " + recipient
                + " subject=\"" + subject + "\" body=\"" + body + "\"");
    }

    private String recipientFor(OrderEvent event) {
        if (event.notificationEmail() != null && !event.notificationEmail().isBlank()) {
            return event.notificationEmail();
        }
        if (event.userId() != null && event.userId().contains("@")) {
            return event.userId();
        }
        return null;
    }
}
