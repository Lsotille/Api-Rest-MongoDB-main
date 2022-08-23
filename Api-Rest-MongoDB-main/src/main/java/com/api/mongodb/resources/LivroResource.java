package com.api.mongodb.resources;

import com.api.mongodb.models.dto.LivroDTO;
import com.api.mongodb.models.filters.LivroFilter;
import com.api.mongodb.services.LivroService;
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
public class LivroResource {
    private final LivroService livroService;

    @GetMapping
    @ApiOperation("Busca paginada de todos os Livros")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    public ResponseEntity<Page<LivroDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                  @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                                                  @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                                  @RequestParam(value = "orderBy", defaultValue = "id") String orderBy) {
        return ResponseEntity.ok().body(livroService.findAll(
                PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy))
        );
    }

    @GetMapping("/search")
    @ApiOperation(value = "Busca paginada de Livros por filtros")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    public ResponseEntity<Page<LivroDTO>> searchByFilter(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                         @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
                                                         @RequestParam(value = "direction", defaultValue = "ASC") String direction,
                                                         @RequestParam(value = "orderBy", defaultValue = "id") String orderBy, LivroFilter filter) {
        return ResponseEntity.ok().body(livroService.searchByFilter(
                PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy), filter)
        );
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Busca um Livro por id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Não encontrado")
    })
    public ResponseEntity<LivroDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok().body(livroService.findById(id));
    }

    @PostMapping
    @ApiOperation(value = "Criação de um novo Livro")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Criado com sucesso"),
            @ApiResponse(code = 400, message = "Requisição Inválida")
    })
    public ResponseEntity<LivroDTO> create(@Valid @RequestBody LivroDTO livroDTO) {
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(livroDTO.getId()).toUri()).body(livroService.create(livroDTO));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Edição de um Livro por id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Requisição Inválida"),
            @ApiResponse(code = 404, message = "Não encontrado")
    })
    public ResponseEntity<LivroDTO> update(@Valid @RequestBody LivroDTO livroDTO, @PathVariable String id) {
        return ResponseEntity.ok().body(livroService.update(id, livroDTO));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Exclusão de um Livro por id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Sem conteúdo"),
            @ApiResponse(code = 404, message = "Não encontrado")
    })
    public ResponseEntity<LivroDTO> delete(@Valid @PathVariable String id) {
        livroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}