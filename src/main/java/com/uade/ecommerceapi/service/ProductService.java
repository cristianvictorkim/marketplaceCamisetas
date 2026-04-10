package com.uade.ecommerceapi.service;

import com.uade.ecommerceapi.dto.ProductRequest;
import com.uade.ecommerceapi.dto.ProductResponse;
import com.uade.ecommerceapi.exception.ResourceNotFoundException;
import com.uade.ecommerceapi.model.Product;
import com.uade.ecommerceapi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        return toResponse(product);
    }

    public ProductResponse create(ProductRequest request) {
        Product product = new Product(null, request.getName(), request.getDescription(), request.getPrice());
        return toResponse(productRepository.save(product));
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }
}
