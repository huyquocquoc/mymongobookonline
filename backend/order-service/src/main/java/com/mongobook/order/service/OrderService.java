package com.mongobook.order.service;

import com.mongobook.order.domain.BookOrder;
import com.mongobook.order.domain.OrderStatus;
import com.mongobook.order.event.OrderEventType;
import com.mongobook.order.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {
    private final OrderRepository orders;
    private final OrderEventPublisher eventPublisher;

    public OrderService(OrderRepository orders, OrderEventPublisher eventPublisher) {
        this.orders = orders;
        this.eventPublisher = eventPublisher;
    }

    public List<BookOrder> list(String userId) {
        if (userId == null || userId.isBlank()) {
            return orders.findAll();
        }
        return orders.findByUserId(userId);
    }

    public BookOrder get(String id) {
        return orders.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    public BookOrder create(BookOrder order) {
        Instant now = Instant.now();
        order.setId(null);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(now);
        order.setUpdatedAt(now);
        order.setTotalAmount(calculateTotal(order));
        BookOrder saved = orders.save(order);
        eventPublisher.publish(saved, OrderEventType.ORDER_CREATED);
        return saved;
    }

    public BookOrder confirm(String id) {
        BookOrder order = get(id);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setUpdatedAt(Instant.now());
        BookOrder saved = orders.save(order);
        eventPublisher.publish(saved, OrderEventType.ORDER_CONFIRMED);
        return saved;
    }

    public BookOrder cancel(String id) {
        BookOrder order = get(id);
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(Instant.now());
        BookOrder saved = orders.save(order);
        eventPublisher.publish(saved, OrderEventType.ORDER_CANCELLED);
        return saved;
    }

    private BigDecimal calculateTotal(BookOrder order) {
        return order.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

