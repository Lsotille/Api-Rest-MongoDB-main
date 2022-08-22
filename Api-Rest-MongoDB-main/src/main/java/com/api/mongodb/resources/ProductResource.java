package com.api.mongodb.resources;

import com.api.mongodb.models.dto.ProductDTO;
import com.api.mongodb.models.filters.ProductFilter;
import com.api.mongodb.services.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;


@RestController
@Api(tags = "Products")
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductResource {
    private final ProductService productService;

    @GetMapping
    @ApiOperation("Busca paginada de todos os produtos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    public ResponseEntity<Page<ProductDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                                                    @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                                    @RequestParam(value = "orderBy", defaultValue = "id") String orderBy) {
        return ResponseEntity.ok().body(productService.findAll(
                PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy))
        );
    }

    @GetMapping("/search")
    @ApiOperation(value = "Busca paginada de produtos por filtros")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    public ResponseEntity<Page<ProductDTO>> searchByFilter(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                           @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                                                           @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                                           @RequestParam(value = "orderBy", defaultValue = "id") String orderBy, ProductFilter filter) {
        return ResponseEntity.ok().body(productService.searchByFilter(
                PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy), filter)
        );
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Busca um produto por id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Não encontrado")
    })
    public ResponseEntity<ProductDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok().body(productService.findById(id));
    }

    @PostMapping
    @ApiOperation(value = "Criação de um novo produto")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Criado com sucesso"),
            @ApiResponse(code = 400, message = "Requisição Inválida")
    })
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(productDTO.getId()).toUri()).body(productService.create(productDTO));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Edição de um produto por id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Requisição Inválida"),
            @ApiResponse(code = 404, message = "Não encontrado")
    })
    public ResponseEntity<ProductDTO> update(@Valid @RequestBody ProductDTO productDTO, @PathVariable String id) {
        return ResponseEntity.ok().body(productService.update(id, productDTO));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Exclusão de um produto por id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Sem conteúdo"),
            @ApiResponse(code = 404, message = "Não encontrado")
    })
    public ResponseEntity<ProductDTO> delete(@Valid @PathVariable String id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}