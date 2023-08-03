package com.ahmedukamel.productservice.service;

import com.ahmedukamel.productservice.dto.ProductRequest;
import com.ahmedukamel.productservice.dto.ProductResponse;
import com.ahmedukamel.productservice.model.Product;
import com.ahmedukamel.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {'id':'{}', 'name':'{}','description':'{}','price':'{}'} is Saved.",
                product.getId(), product.getName(), productRequest.getDescription(), productRequest.getPrice());
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(
                this::mapProductToProductResponse).toList();
    }

    public ProductResponse mapProductToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
