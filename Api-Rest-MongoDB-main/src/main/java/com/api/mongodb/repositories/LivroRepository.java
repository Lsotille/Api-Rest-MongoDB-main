package com.api.mongodb.repositories;

import com.api.mongodb.models.Livro;
import com.api.mongodb.models.dto.LivroDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface LivroRepository extends MongoRepository<Livro, String> {
    // Seta a query especial para Min e Max preco para procurar o Livro
    @Query("{ 'price' : {$gt : ?0, $lt : ?1 } }")
    Page<LivroDTO> findByMinAndMaxPrice(Pageable pageable, Double min_price, Double max_price);
    // Seta a query especial para Procurar por nome, descricao ou genero do livro
    @Query("{ $or: [ { 'name': { $regex: ?0, $options : i }  }, { 'description': { $regex: ?0, $options : i }  }, { 'genre': { $regex: ?0, $options : i }  } ] }")
    Page<LivroDTO> findByNameOrDescriptionOrGenre(Pageable pageable, String query);
    //Seta a query especial para procurar por nome... e preco especificos
    @Query(" { $and : [ { $or : [ { 'name': { $regex: ?0, $options : i }  }, { 'description': { $regex: ?0, $options : i } } ] }, { $or : [ { 'price' : {$gt : ?1, $lt : ?2 } } ] } ] } ")
    Page<LivroDTO> findByNameOrDescriptionAndMinMaxPrice(Pageable pageable, String query, Double min_price, Double max_price);

}
