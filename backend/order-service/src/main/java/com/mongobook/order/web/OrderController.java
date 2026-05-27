package com.mongobook.order.web;

import com.mongobook.order.domain.BookOrder;
import com.mongobook.order.service.OrderService;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.stripe.secret:}")
    private String stripeSecret;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<BookOrder> list(@RequestParam(name = "q", required = false) String q)  {
        logger.info("OrderController.list called with q=" + q);
        return orderService.list(q);
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

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, String>> checkout(@Valid @RequestBody BookOrder order) throws Exception {
        logger.info("OrderController.checkout called for userId=" + order.getUserId());
        // persist order first
        BookOrder saved = orderService.create(order);

        if (stripeSecret == null || stripeSecret.isBlank()) {
            Map<String, String> resp = new HashMap<>();
            resp.put("error", "Stripe secret not configured");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }

        Stripe.apiKey = stripeSecret;

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:4200/?canceled=true");

        for (var item : saved.getItems()) {
            long amount = item.getUnitPrice().multiply(BigDecimal.valueOf(100)).longValue();
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(item.getTitle())
                            .build();

            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount(amount)
                            .setProductData(productData)
                            .build();

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setPriceData(priceData)
                    .setQuantity(Long.valueOf(item.getQuantity()))
                    .build();

            paramsBuilder.addLineItem(lineItem);
        }

        SessionCreateParams params = paramsBuilder.build();
        Session session = Session.create(params);
        orderService.publishCheckoutStarted(saved);

        Map<String, String> resp = new HashMap<>();
        resp.put("url", session.getUrl());
        resp.put("orderId", saved.getId());
        return ResponseEntity.ok(resp);
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
