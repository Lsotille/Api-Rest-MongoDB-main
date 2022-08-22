package com.api.mongodb.services;


import com.api.mongodb.exceptions.ObjectNotFoundException;
import com.api.mongodb.models.Product;
import com.api.mongodb.models.dto.ProductDTO;
import com.api.mongodb.models.filters.ProductFilter;
import com.api.mongodb.repositories.ProductRepository;
import com.api.mongodb.util.Constants;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ModelMapper mapper;

    private final ProductRepository productRepository;

    public Page<ProductDTO> findAll(Pageable pageable) {

        return productRepository.findAll(pageable)
                .map(product -> mapper.map(product, ProductDTO.class));
    }

    public Page<ProductDTO> searchByFilter(Pageable pageable, ProductFilter filter) {

        Page<ProductDTO> productDTO = Page.empty();

        if(filter.filterByMinAndMaxPrice()) {

            productDTO = productRepository.findByMinAndMaxPrice(pageable, filter.getMin_price(), filter.getMax_price());

        } else if (filter.filterByNameOrDescription()) {

            productDTO = productRepository.findByNameOrDescription(pageable, filter.getQuery());

        } else if (filter.filterByNameOrDescriptionAndMinMaxPrice()) {

            productDTO = productRepository.findByNameOrDescriptionAndMinMaxPrice(pageable, filter.getQuery(), filter.getMin_price(), filter.getMax_price());
        }

        return productDTO;
    }

    public ProductDTO findById(String id) {

        var product = findProduct(id);

        return mapper.map(product, ProductDTO.class);
    }

    public ProductDTO create(ProductDTO productDTO) {

        var productMap = mapper.map(productDTO, Product.class);

        productMap.setCreatedAt(LocalDateTime.now());

        var product = productRepository.save(productMap);

        return mapper.map(product, ProductDTO.class);
    }

    public ProductDTO update(String id, ProductDTO productDTO) {

         var updateProduct = findProduct(id);

             updateProduct.setId(id);
             updateProduct.setName(productDTO.getName());
             updateProduct.setDescription(productDTO.getDescription());
             updateProduct.setPrice(productDTO.getPrice());
             updateProduct.setUpdatedAt(LocalDateTime.now());

         var product = productRepository.save(updateProduct);

         return mapper.map(product, ProductDTO.class);
    }

    public void delete(String id) {
           productRepository.deleteById(findProduct(id).getId());
    }

    private Product findProduct(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Constants.MESSAGE_NOT_FOUND));
    }
}
