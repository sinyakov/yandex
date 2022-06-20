package com.sinyakov.yandex.repository;

import com.sinyakov.yandex.domain.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ItemRepository extends MongoRepository<Item, String> {
    public List<Item> findByParentId(String parentId);
}
