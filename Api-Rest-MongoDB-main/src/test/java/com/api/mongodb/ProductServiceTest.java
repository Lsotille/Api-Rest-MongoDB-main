package com.api.mongodb;

import com.api.mongodb.models.Product;
import com.api.mongodb.models.dto.ProductDTO;
import com.api.mongodb.models.filters.ProductFilter;
import com.api.mongodb.repositories.ProductRepository;
import com.api.mongodb.services.ProductService;
import com.api.mongodb.util.Constants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Spy
    protected ModelMapper mapper;

    private static Product PRODUCT;

    private static ProductDTO PRODUCT_DTO;

    private static ProductFilter FILTER;

    private static PageRequest DEFAULT_PAGEABLE;

    @Before
    public void setup() {
        PRODUCT = Product.builder()
                .id("62eff2aa4e6fc45b97ab3d84")
                .name("Parafuso 1/5 mm")
                .description("Parafuso de 1/5 mm soberbo")
                .price(1.2)
                .createdAt(LocalDateTime.parse("2022-08-07T14:16:23.442816"))
                .updatedAt(LocalDateTime.parse("2022-08-08T14:11:22.232816"))
                .build();

        PRODUCT_DTO = ProductDTO.builder()
                .id("62eff2aa4e6fc45b97ab3d84")
                .name("Parafuso 1/5 mm")
                .description("Parafuso de 1/5 mm soberbo")
                .price(1.2)
                .createdAt(LocalDateTime.parse("2022-08-07T14:16:23.442816"))
                .updatedAt(LocalDateTime.parse("2022-08-08T14:11:22.232816"))
                .build();

        FILTER = ProductFilter.builder().build();

        DEFAULT_PAGEABLE = PageRequest.of(0, 10);

        // when(productRepository.findAll(DEFAULT_PAGEABLE)).thenReturn(new PageImpl<>(List.of(PRODUCT)));

        when(productRepository.findById(PRODUCT.getId())).thenReturn(Optional.ofNullable(PRODUCT));

        //when(mapper.map(PRODUCT, ProductDTO.class)).thenReturn(PRODUCT_DTO);

        when(productRepository.save(PRODUCT)).thenReturn(PRODUCT);

    }

    @Test
    public void whenFindAllReturnProductPage() {

        var products = productService.findAll(DEFAULT_PAGEABLE);

        verify(productRepository, times(1)).findAll(DEFAULT_PAGEABLE);

        Assert.assertNotNull(products);
        assertEquals(1, products.getTotalElements());
        assertEquals(PRODUCT_DTO, products.getContent().get(0));

    }

    @Test
    public void whenSearchByFilter_And_FindMinAndMaxPrice_ReturnProductPage() {
        FILTER.setQuery(null);
        FILTER.setMin_price(2.0);
        FILTER.setMax_price(6.0);

        when(productRepository.findByMinAndMaxPrice(DEFAULT_PAGEABLE, FILTER.getMin_price(), FILTER.getMax_price()))
                .thenReturn(new PageImpl<>(List.of(PRODUCT_DTO)));

        var products = productService.searchByFilter(DEFAULT_PAGEABLE, FILTER);

        verify(productRepository, times(1)).findByMinAndMaxPrice(DEFAULT_PAGEABLE, FILTER.getMin_price(), FILTER.getMax_price());

        Assert.assertNotNull(products);
        assertEquals(1, products.getTotalElements());
        assertEquals(PRODUCT_DTO, products.getContent().get(0));

    }

    @Test
    public void whenSearchByFilter_And_FindByNameOrDescription_ReturnProductPage() {
        FILTER.setQuery("soberbo");
        FILTER.setMin_price(null);
        FILTER.setMax_price(null);

        when(productRepository.findByNameOrDescription(DEFAULT_PAGEABLE, FILTER.getQuery()))
                .thenReturn(new PageImpl<>(List.of(PRODUCT_DTO)));

        var products = productService.searchByFilter(DEFAULT_PAGEABLE, FILTER);

        verify(productRepository, times(1)).findByNameOrDescription(DEFAULT_PAGEABLE, FILTER.getQuery());

        Assert.assertNotNull(products);
        assertEquals(1, products.getTotalElements());
        assertEquals(PRODUCT_DTO, products.getContent().get(0));

    }

    @Test
    public void whenSearchByFilter_And_FindByNameOrDescriptionAndMinMaxPrice_ReturnProductPage() {
        FILTER.setQuery("soberbo");
        FILTER.setMin_price(2.0);
        FILTER.setMax_price(6.0);

        when(productRepository.findByNameOrDescriptionAndMinMaxPrice(DEFAULT_PAGEABLE, FILTER.getQuery(), FILTER.getMin_price(), FILTER.getMax_price()))
                .thenReturn(new PageImpl<>(List.of(PRODUCT_DTO)));

        var products = productService.searchByFilter(DEFAULT_PAGEABLE, FILTER);

        verify(productRepository, times(1))
                .findByNameOrDescriptionAndMinMaxPrice(DEFAULT_PAGEABLE, FILTER.getQuery(), FILTER.getMin_price(), FILTER.getMax_price());

        assertNotNull(products);
        assertEquals(1, products.getTotalElements());
        assertEquals(PRODUCT_DTO, products.getContent().get(0));

    }

    @Test
    public void whenFindByIdReturnProduct() {

       var product = productService.findById(PRODUCT_DTO.getId());

        verify(productRepository, times(1)).findById(PRODUCT_DTO.getId());

       assertEquals(product.getId(), "62eff2aa4e6fc45b97ab3d84");
       assertEquals(product.getName(), "Parafuso 1/5 mm");
       assertEquals(product.getDescription(), "Parafuso de 1/5 mm soberbo");
       assertEquals(Optional.ofNullable(product.getPrice()), Optional.of(1.2));
    }

    @Test
    public void whenCreateReturnProduct() {

        when(mapper.map(PRODUCT_DTO, Product.class)).thenReturn(PRODUCT);

        var product = productService.create(PRODUCT_DTO);

        verify(productRepository, times(1)).save(PRODUCT);


        assertEquals(product.getId(), "62eff2aa4e6fc45b97ab3d84");
        assertEquals(product.getName(), "Parafuso 1/5 mm");
        assertEquals(product.getDescription(), "Parafuso de 1/5 mm soberbo");
        assertEquals(Optional.ofNullable(product.getPrice()), Optional.of(1.2));
    }

    @Test
    public void whenUpdateReturnProduct() {

        var product = productService.update(PRODUCT_DTO.getId(), PRODUCT_DTO);

        verify(productRepository, times(1)).save(PRODUCT);

        assertEquals(product.getId(), "62eff2aa4e6fc45b97ab3d84");
        assertEquals(product.getName(), "Parafuso 1/5 mm");
        assertEquals(product.getDescription(), "Parafuso de 1/5 mm soberbo");
        assertEquals(Optional.ofNullable(product.getPrice()), Optional.of(1.2));

    }

    @Test
    public void whenDeleteById_DoNotReturnContent() {
        productService.delete(PRODUCT_DTO.getId());

        verify(productRepository, times(1)).deleteById(PRODUCT_DTO.getId());
    }

    @Test
    public void whenCreateadExpectedException(){

        when(mapper.map(PRODUCT_DTO, Product.class)).thenReturn(PRODUCT);

        when(productRepository.save(PRODUCT)).thenThrow(new RuntimeException(Constants.MESSAGE_INVALID_REQUEST));

       var exception = Assertions.assertThrows(RuntimeException.class,() -> {
            productService.create(PRODUCT_DTO);
        });

       assertEquals(Constants.MESSAGE_INVALID_REQUEST, exception.getMessage());

       verify(productRepository, times(1)).save(PRODUCT);
    }

    @Test
    public void whenUpdateExpectedException() {

        when(productService.update(PRODUCT_DTO.getId(), PRODUCT_DTO)).thenThrow(new RuntimeException(Constants.ERROR_NOT_FOUND));

        var exception = Assertions.assertThrows(RuntimeException.class,() -> {
            productService.update(PRODUCT_DTO.getId(),PRODUCT_DTO);
        });

        assertEquals(Constants.ERROR_NOT_FOUND, exception.getMessage());

        verify(productRepository, times(2)).save(PRODUCT);
    }

    @Test
    public void whenFindByIdReturnException() {
        when(productService.findById(PRODUCT_DTO.getId())).thenThrow(new RuntimeException(Constants.ERROR_NOT_FOUND));

        var exception = Assertions.assertThrows(RuntimeException.class,() -> {
            productService.findById(PRODUCT_DTO.getId());
        });

        assertEquals(Constants.ERROR_NOT_FOUND, exception.getMessage());
        verify(productRepository, times(2)).findById(PRODUCT_DTO.getId());
    }

    @Test
    public void whenFindAllReturnEmptyPage() {

        when(productRepository.findAll(DEFAULT_PAGEABLE)).thenReturn(new PageImpl<>(List.of()));

        var products = productService.findAll(DEFAULT_PAGEABLE);

        verify(productRepository, times(1)).findAll(DEFAULT_PAGEABLE);

        assertEquals(0, products.getTotalElements());

    }

    @Test
    public void whenSearchByFilter_And_FindMinAndMaxPrice_ReturnEmptyPage() {
        FILTER.setQuery(null);
        FILTER.setMin_price(2.0);
        FILTER.setMax_price(6.0);

        when(productRepository.findByMinAndMaxPrice(DEFAULT_PAGEABLE, FILTER.getMin_price(), FILTER.getMax_price()))
                .thenReturn(new PageImpl<>(List.of()));

        var products = productService.searchByFilter(DEFAULT_PAGEABLE, FILTER);

        verify(productRepository, times(1)).findByMinAndMaxPrice(DEFAULT_PAGEABLE, FILTER.getMin_price(), FILTER.getMax_price());

        assertEquals(0, products.getTotalElements());

    }

    @Test
    public void whenSearchByFilter_And_FindByNameOrDescription_ReturnEmptyPage() {
        FILTER.setQuery("soberbo");
        FILTER.setMin_price(null);
        FILTER.setMax_price(null);

        when(productRepository.findByNameOrDescription(DEFAULT_PAGEABLE, FILTER.getQuery()))
                .thenReturn(new PageImpl<>(List.of()));

        var products = productService.searchByFilter(DEFAULT_PAGEABLE, FILTER);

        verify(productRepository, times(1)).findByNameOrDescription(DEFAULT_PAGEABLE, FILTER.getQuery());

        assertEquals(0, products.getTotalElements());

    }

    @Test
    public void whenSearchByFilter_And_FindByNameOrDescriptionAndMinMaxPrice_ReturnEmptyPage() {
        FILTER.setQuery("soberbo");
        FILTER.setMin_price(2.0);
        FILTER.setMax_price(6.0);

        when(productRepository.findByNameOrDescriptionAndMinMaxPrice(DEFAULT_PAGEABLE, FILTER.getQuery(), FILTER.getMin_price(), FILTER.getMax_price()))
                .thenReturn(new PageImpl<>(List.of()));

        var products = productService.searchByFilter(DEFAULT_PAGEABLE, FILTER);

        verify(productRepository, times(1))
                .findByNameOrDescriptionAndMinMaxPrice(DEFAULT_PAGEABLE, FILTER.getQuery(), FILTER.getMin_price(), FILTER.getMax_price());

        assertEquals(0, products.getTotalElements());

    }

    @Test
    public void whenDeleteReturnException() {

        when(ReflectionTestUtils.invokeMethod(productService, "findProduct", PRODUCT_DTO.getId()))
                .thenThrow(new RuntimeException(Constants.ERROR_NOT_FOUND));
        var exception = Assertions.assertThrows(RuntimeException.class,() -> {
            productService.findById(PRODUCT_DTO.getId());
        });
        verify(productRepository, times(1)).findById(PRODUCT_DTO.getId());
        assertEquals(Constants.ERROR_NOT_FOUND, exception.getMessage());
    }
}