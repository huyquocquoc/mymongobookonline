package com.mongobook.notification.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderEvent(
        String orderId,
        String userId,
        String notificationEmail,
        OrderEventType type,
        List<OrderItem> items,
        BigDecimal totalAmount,
        Instant occurredAt
) {
}
