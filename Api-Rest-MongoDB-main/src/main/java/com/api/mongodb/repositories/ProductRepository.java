package com.api.mongodb.repositories;

import com.api.mongodb.models.Product;
import com.api.mongodb.models.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    @Query("{ 'price' : {$gt : ?0, $lt : ?1 } }")
    Page<ProductDTO> findByMinAndMaxPrice(Pageable pageable, Double min_price, Double max_price);

    @Query("{ $or: [ { 'name': { $regex: ?0, $options : i }  }, { 'description': { $regex: ?0, $options : i }  } ] }")
    Page<ProductDTO> findByNameOrDescription(Pageable pageable, String query);

    @Query(" { $and : [ { $or : [ { 'name': { $regex: ?0, $options : i }  }, { 'description': { $regex: ?0, $options : i } } ] }, { $or : [ { 'price' : {$gt : ?1, $lt : ?2 } } ] } ] } ")
    Page<ProductDTO> findByNameOrDescriptionAndMinMaxPrice(Pageable pageable, String query, Double min_price, Double max_price);

}
