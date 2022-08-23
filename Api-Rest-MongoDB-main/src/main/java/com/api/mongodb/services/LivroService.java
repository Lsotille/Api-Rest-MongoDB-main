package com.api.mongodb.services;


import com.api.mongodb.exceptions.ObjectNotFoundException;
import com.api.mongodb.models.Livro;
import com.api.mongodb.models.dto.LivroDTO;
import com.api.mongodb.models.filters.LivroFilter;
import com.api.mongodb.repositories.LivroRepository;
import com.api.mongodb.util.Constants;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class LivroService {

    // Cria o Mapper do modelMapper
    private final ModelMapper mapper;

    //Seta o Repositorio para o Service usar
    private final LivroRepository livroRepository;

    //Manda uma Pagina em branco e retorna todos os livros
    public Page<LivroDTO> findAll(Pageable pageable) {

        return livroRepository.findAll(pageable)
                .map(livro -> mapper.map(livro, LivroDTO.class));
    }

    //Manda uma Pagina em branco e os parametros a serem os filtros. Retorna o resultado da consulta
    public Page<LivroDTO> searchByFilter(Pageable pageable, LivroFilter filter) {

        Page<LivroDTO> productDTO = Page.empty();

        if(filter.filterByMinAndMaxPrice()) {

            productDTO = livroRepository.findByMinAndMaxPrice(pageable, filter.getMin_price(), filter.getMax_price());

        } else if (filter.filterByNameOrDescriptionOrGenre()) {

            productDTO = livroRepository.findByNameOrDescriptionOrGenre(pageable, filter.getQuery());

        } else if (filter.filterByNameOrDescriptionAndMinMaxPrice()) {

            productDTO = livroRepository.findByNameOrDescriptionAndMinMaxPrice(pageable, filter.getQuery(), filter.getMin_price(), filter.getMax_price());
        }

        return productDTO;
    }

    //Manda um Id e retorna o Livro correspondente
    public LivroDTO findById(String id) {

        var product = findLivro(id);

        return mapper.map(product, LivroDTO.class);
    }

    //Manda um Livro DTO para criar uma instancia de livro no banco de dado
    public LivroDTO create(LivroDTO livroDTO) {

        var productMap = mapper.map(livroDTO, Livro.class);

        productMap.setCreatedAt(LocalDateTime.now());

        var product = livroRepository.save(productMap);

        return mapper.map(product, LivroDTO.class);
    }

    //Manda um id e o Livro DTO para mudar a instancia de mesmo ID pelo novo DTO
    public LivroDTO update(String id, LivroDTO livroDTO) {

         var updateProduct = findLivro(id);

             updateProduct.setId(id);
             updateProduct.setName(livroDTO.getName());
             updateProduct.setDescription(livroDTO.getDescription());
             updateProduct.setPrice(livroDTO.getPrice());
             updateProduct.setUpdatedAt(LocalDateTime.now());

         var product = livroRepository.save(updateProduct);

         return mapper.map(product, LivroDTO.class);
    }

    //manda um ID e deleta a instancia associada com esse ID
    public void delete(String id) {
           livroRepository.deleteById(findLivro(id).getId());
    }

    //metodo do delete.
    private Livro findLivro(String id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Constants.MESSAGE_NOT_FOUND));
    }
}
