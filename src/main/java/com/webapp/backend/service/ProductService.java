package com.webapp.backend.service;

import com.webapp.backend.common.CustomException;
import com.webapp.backend.dto.ProductDto;
import com.webapp.backend.entity.Category;
import com.webapp.backend.entity.ImportProduct;
import com.webapp.backend.entity.Product;
import com.webapp.backend.repository.CategoryRepository;
import com.webapp.backend.repository.ImportProductRepository;
import com.webapp.backend.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger("info_trace");

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ImportProductRepository importProductRepository;

    @Transactional
    public Product addProduct(ProductDto productDto) throws CustomException {

        Product product = new Product();


        Optional<Category> categoryOptional = categoryRepository.findById(productDto.getCategoryId());

        if(!categoryOptional.isPresent()){
            throw new CustomException("This category is not existed");
        }

        if(productDto.getQuantity() <= 0){
            throw new CustomException("The quantity must not be blank");
        }

        product.setCategory(categoryOptional.get());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setQuantity(productDto.getQuantity());


        Product savedProcduct = productRepository.save(product);


        ImportProduct importProduct = new ImportProduct();
        importProduct.setProduct(savedProcduct);
        importProduct.setQuantity(savedProcduct.getQuantity());

        importProductRepository.save(importProduct);

        LOGGER.info("Add Product: ID = {}, Name = {}, Quantity = {}", savedProcduct.getId(), savedProcduct.getName(), savedProcduct.getQuantity());

        return savedProcduct;

    }

    public void deleteProduct(Long id) throws Exception {

        Optional<Product> existingProductOptional = productRepository.findById(id);

        if (!(existingProductOptional.isPresent())) {

            throw new CustomException("This product is not existed");
        }

        productRepository.deleteById(id);
    }

    @Transactional
    public Product updateProduct(ProductDto productDto) throws Exception {

        Optional<Product> productOptional = productRepository.findById(productDto.getId());

        if (!productOptional.isPresent()) {
            throw new CustomException("This prduct is not existed");
        }
        Product product = productOptional.get();

        product.setDescription(productDto.getDescription());
        product.setName(productDto.getName());

        product.setQuantity(product.getQuantity() + productDto.getQuantity());

        Product savedProduct = productRepository.save(product);

        ImportProduct importProduct = new ImportProduct();
        importProduct.setProduct(savedProduct);
        importProduct.setQuantity(productDto.getQuantity());

        importProductRepository.save(importProduct);

        LOGGER.info("Change warehouse" + savedProduct.getId());

        return savedProduct;

    }

    public void deleteAllProduct() {
        productRepository.deleteAll();
    }

    public List<ProductDto> getAllProduct(Long categoryID) {

        List<Product> products = productRepository.findAll();

        return products.stream()
                .filter(product -> categoryID == null || Objects.equals(product.getCategory().getId(), categoryID))
                .map(product -> ProductDto.builder()
                                        .name(product.getName())
                                        .id(product.getId())
                                        .quantity(product.getQuantity())
                                        .categoryId(product.getCategory().getId())
                                        .categoryDto(product.getCategory().mapEntityToDto())
                                        .description(product.getDescription())
                        .build()).collect(Collectors.toList());
    }

    public Product getProduct(Long productId) {
        Optional<Product> warehouseOptional = productRepository.findById(productId);
        if(warehouseOptional.isPresent())
            return warehouseOptional.get();
        return null;
    }
}
