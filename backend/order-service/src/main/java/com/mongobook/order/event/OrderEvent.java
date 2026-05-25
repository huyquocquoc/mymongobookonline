package com.mongobook.order.event;

import com.mongobook.order.domain.OrderItem;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderEvent(
        String orderId,
        String userId,
        OrderEventType type,
        List<OrderItem> items,
        BigDecimal totalAmount,
        Instant occurredAt
) {
}

