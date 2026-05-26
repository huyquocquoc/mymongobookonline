package com.mongobook.order.web;

import com.mongobook.order.domain.BookOrder;
import com.mongobook.order.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {
    private static final Log logger = LogFactory.getLog(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<BookOrder> list(@RequestParam(required = false) String userId) {
        logger.info("OrderController.list called with userId=" + userId);
        return orderService.list(userId);
    }

    @GetMapping("/{id}")
    public BookOrder get(@PathVariable String id) {
        logger.info("OrderController.get called with id=" + id);
        return orderService.get(id);
    }

    @PostMapping
    public ResponseEntity<BookOrder> create(@Valid @RequestBody BookOrder order) {
        logger.info("OrderController.create called for userId=" + order.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(order));
    }

    @PostMapping("/{id}/confirm")
    public BookOrder confirm(@PathVariable String id) {
        logger.info("OrderController.confirm called with id=" + id);
        return orderService.confirm(id);
    }

    @PostMapping("/{id}/cancel")
    public BookOrder cancel(@PathVariable String id) {
        logger.info("OrderController.cancel called with id=" + id);
        return orderService.cancel(id);
    }
}

