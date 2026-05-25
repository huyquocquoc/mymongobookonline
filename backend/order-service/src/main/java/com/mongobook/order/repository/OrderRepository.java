package com.mongobook.order.repository;

import com.mongobook.order.domain.BookOrder;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<BookOrder, String> {
    List<BookOrder> findByUserId(String userId);
}

