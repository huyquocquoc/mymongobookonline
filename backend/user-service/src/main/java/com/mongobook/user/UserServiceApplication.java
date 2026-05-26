package com.mongobook.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {
    private static final Log logger = LogFactory.getLog(UserServiceApplication.class);

    public static void main(String[] args) {
        logger.info("Starting UserServiceApplication");
        SpringApplication.run(UserServiceApplication.class, args);
    }
}

