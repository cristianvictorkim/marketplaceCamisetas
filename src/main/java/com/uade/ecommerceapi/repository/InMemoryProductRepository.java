package com.uade.ecommerceapi.repository;

import com.uade.ecommerceapi.model.Product;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final Map<Long, Product> storage = new ConcurrentHashMap<Long, Product>();
    private final AtomicLong sequence = new AtomicLong(0);

    @PostConstruct
    void seedData() {
        save(new Product(null, "Notebook", "Base product to test the layered architecture", new BigDecimal("1200.00")));
        save(new Product(null, "Mouse", "Second sample product", new BigDecimal("25.50")));
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<Product>(storage.values());
        products.sort(Comparator.comparing(Product::getId));
        return products;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Product save(Product product) {
        Long id = product.getId() != null ? product.getId() : sequence.incrementAndGet();
        Product storedProduct = new Product(id, product.getName(), product.getDescription(), product.getPrice());
        storage.put(id, storedProduct);
        return storedProduct;
    }
}
