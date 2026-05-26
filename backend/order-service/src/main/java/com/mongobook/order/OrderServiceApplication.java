package com.mongobook.order;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderServiceApplication {
    private static final Log logger = LogFactory.getLog(OrderServiceApplication.class);

    public static void main(String[] args) {
        logger.info("Starting OrderServiceApplication");
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}

