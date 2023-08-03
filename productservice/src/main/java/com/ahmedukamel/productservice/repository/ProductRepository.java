package com.ahmedukamel.productservice.repository;

import com.ahmedukamel.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
