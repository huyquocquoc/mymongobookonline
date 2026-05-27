package com.mongobook.notification.event;

import java.math.BigDecimal;

public record OrderItem(
        String bookId,
        String title,
        int quantity,
        BigDecimal unitPrice
) {
}
