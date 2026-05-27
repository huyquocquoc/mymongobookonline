package com.mongobook.order.service;

import com.mongobook.order.domain.BookOrder;
import com.mongobook.order.domain.OrderStatus;
import com.mongobook.order.event.OrderEventType;
import com.mongobook.order.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {
    private static final Log logger = LogFactory.getLog(OrderService.class);
    private final OrderRepository orders;
    private final OrderEventPublisher eventPublisher;

    public OrderService(OrderRepository orders, OrderEventPublisher eventPublisher) {
        this.orders = orders;
        this.eventPublisher = eventPublisher;
    }

    public List<BookOrder> list(String userId) {
        logger.info("OrderService.list called with userId=" + userId);
        if (userId == null || userId.isBlank()) {
            return orders.findAll();
        }
        return orders.findByUserId(userId);
    }

    public BookOrder get(String id) {
        logger.info("OrderService.get called with id=" + id);
        return orders.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    public BookOrder create(BookOrder order) {
        logger.info("OrderService.create called for userId=" + order.getUserId());
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
        logger.info("OrderService.confirm called with id=" + id);
        BookOrder order = get(id);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setUpdatedAt(Instant.now());
        BookOrder saved = orders.save(order);
        eventPublisher.publish(saved, OrderEventType.ORDER_CONFIRMED);
        return saved;
    }

    public void publishCheckoutStarted(BookOrder order) {
        logger.info("OrderService.publishCheckoutStarted called for orderId=" + order.getId());
        eventPublisher.publish(order, OrderEventType.CHECKOUT_STARTED);
    }

    public BookOrder cancel(String id) {
        logger.info("OrderService.cancel called with id=" + id);
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
