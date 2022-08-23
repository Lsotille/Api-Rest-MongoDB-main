package com.api.mongodb;

import com.api.mongodb.models.Livro;
import com.api.mongodb.models.dto.LivroDTO;
import com.api.mongodb.models.filters.LivroFilter;
import com.api.mongodb.repositories.LivroRepository;
import com.api.mongodb.services.LivroService;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LivroServiceTest {
    // Injeta o mock para que aja como um LivroService
    @InjectMocks
    private LivroService livroService;
    // injeta o mock para que aja como um LivroRepository
    @Mock
    private LivroRepository livroRepository;
    // Seta Spy para simular um ambiente real
    @Spy
    protected ModelMapper mapper;

    // Cria uma instancia de livro
    private static Livro Livro;

    // Cria uma instancia de Livro DTO
    private static LivroDTO livroDTO;

    // Cria uma instancia de Livro Filter
    private static LivroFilter FILTER;

    // Cria uma intancia de Paginacao
    private static PageRequest DEFAULT_PAGEABLE;

    // Seta antes dos teste um livro e um livro DTO para serem usados como parametro
    @Before
    public void setup() {
        //Seta um Livro base
        Livro = Livro.builder()
                .id("62eff2aa4e6fc45b97ab3d84")
                .name("Contos Fantasmas")
                .description("Livro broxura")
                .price(1.2)
                .createdAt(LocalDateTime.parse("2022-08-07T14:16:23.442816"))
                .updatedAt(LocalDateTime.parse("2022-08-08T14:11:22.232816"))
                .genre("Terror")
                .build();
        //Seta um livro DTO
        livroDTO = LivroDTO.builder()
                .id("62eff2aa4e6fc45b97ab3d84")
                .name("Contos Fantasmas")
                .description("Livro broxura")
                .price(1.2)
                .createdAt(LocalDateTime.parse("2022-08-07T14:16:23.442816"))
                .updatedAt(LocalDateTime.parse("2022-08-08T14:11:22.232816"))
                .genre("Terror")
                .build();
        //Builda o filter
        FILTER = LivroFilter.builder().build();
        //Cria a pagina default
        DEFAULT_PAGEABLE = PageRequest.of(0, 10);
        //Quando findall retorna uma lista de todos os livros
        when(livroRepository.findAll(DEFAULT_PAGEABLE)).thenReturn(new PageImpl<>(List.of(Livro)));
        //Quando find by id retorna um livro ou um nullable.
        when(livroRepository.findById(Livro.getId())).thenReturn(Optional.ofNullable(Livro));
        //Quando o model mapper for usado retorna um LivroDTO
        when(mapper.map(Livro, LivroDTO.class)).thenReturn(livroDTO);
        //Quando salva um livro retorna o Livro
        when(livroRepository.save(Livro)).thenReturn(Livro);

    }

    //Retorna todos os livros com sucesso
    @Test
    public void whenFindAllReturnLivroPage() {

        var products = livroService.findAll(DEFAULT_PAGEABLE);

        verify(livroRepository, times(1)).findAll(DEFAULT_PAGEABLE);

        Assert.assertNotNull(products);
        assertEquals(1, products.getTotalElements());
        assertEquals(livroDTO, products.getContent().get(0));

    }
    //Retorna todos os livros com filtro com sucesso
    @Test
    public void whenSearchByFilter_And_FindMinAndMaxPrice_ReturnLivroPage() {
        FILTER.setQuery(null);
        FILTER.setMin_price(2.0);
        FILTER.setMax_price(6.0);

        when(livroRepository.findByMinAndMaxPrice(DEFAULT_PAGEABLE, FILTER.getMin_price(), FILTER.getMax_price()))
                .thenReturn(new PageImpl<>(List.of(livroDTO)));

        var products = livroService.searchByFilter(DEFAULT_PAGEABLE, FILTER);

        verify(livroRepository, times(1)).findByMinAndMaxPrice(DEFAULT_PAGEABLE, FILTER.getMin_price(), FILTER.getMax_price());

        Assert.assertNotNull(products);
        assertEquals(1, products.getTotalElements());
        assertEquals(livroDTO, products.getContent().get(0));

    }
    //Retorna todos os livros com filtro com sucesso
    @Test
    public void whenSearchByFilter_And_FindByNameOrDescription_ReturnLivroPage() {
        FILTER.setQuery("broxura");
        FILTER.setMin_price(null);
        FILTER.setMax_price(null);

        when(livroRepository.findByNameOrDescriptionOrGenre(DEFAULT_PAGEABLE, FILTER.getQuery()))
                .thenReturn(new PageImpl<>(List.of(livroDTO)));

        var products = livroService.searchByFilter(DEFAULT_PAGEABLE, FILTER);

        verify(livroRepository, times(1)).findByNameOrDescriptionOrGenre(DEFAULT_PAGEABLE, FILTER.getQuery());

        Assert.assertNotNull(products);
        assertEquals(1, products.getTotalElements());
        assertEquals(livroDTO, products.getContent().get(0));

    }
    //Retorna todos os livros com filtro com sucesso
    @Test
    public void whenSearchByFilter_And_FindByNameOrDescriptionAndMinMaxPrice_ReturnLivroPage() {
        FILTER.setQuery("broxura");
        FILTER.setMin_price(2.0);
        FILTER.setMax_price(6.0);

        when(livroRepository.findByNameOrDescriptionAndMinMaxPrice(DEFAULT_PAGEABLE, FILTER.getQuery(), FILTER.getMin_price(), FILTER.getMax_price()))
                .thenReturn(new PageImpl<>(List.of(livroDTO)));

        var products = livroService.searchByFilter(DEFAULT_PAGEABLE, FILTER);

        verify(livroRepository, times(1))
                .findByNameOrDescriptionAndMinMaxPrice(DEFAULT_PAGEABLE, FILTER.getQuery(), FILTER.getMin_price(), FILTER.getMax_price());

        assertNotNull(products);
        assertEquals(1, products.getTotalElements());
        assertEquals(livroDTO, products.getContent().get(0));

    }
    //Retorna o livro com o id passado com sucesso
    @Test
    public void whenFindByIdReturnLivro() {

       var product = livroService.findById(livroDTO.getId());

        verify(livroRepository, times(1)).findById(livroDTO.getId());

       assertEquals(product.getId(), "62eff2aa4e6fc45b97ab3d84");
       assertEquals(product.getName(), "Contos Fantasmas");
       assertEquals(product.getDescription(), "Livro broxura");
       assertEquals(product.getGenre(),"Terror");
       assertEquals(Optional.ofNullable(product.getPrice()), Optional.of(1.2));
    }
    //Retorna o livro Criado com sucesso
    @Test
    public void whenCreateReturnLivro() {

        when(mapper.map(livroDTO, Livro.class)).thenReturn(Livro);

        var product = livroService.create(livroDTO);

        verify(livroRepository, times(1)).save(Livro);


        assertEquals(product.getId(), "62eff2aa4e6fc45b97ab3d84");
        assertEquals(product.getName(), "Contos Fantasmas");
        assertEquals(product.getDescription(), "Livro broxura");
        assertEquals(product.getGenre(),"Terror");
        assertEquals(Optional.ofNullable(product.getPrice()), Optional.of(1.2));
    }
    //Retorna o livro quando atualizado com sucesso
    @Test
    public void whenUpdateReturnLivro() {

        var product = livroService.update(livroDTO.getId(), livroDTO);

        verify(livroRepository, times(1)).save(Livro);

        assertEquals(product.getId(), "62eff2aa4e6fc45b97ab3d84");
        assertEquals(product.getName(), "Contos Fantasmas");
        assertEquals(product.getDescription(), "Livro broxura");
        assertEquals(product.getGenre(),"Terror");
        assertEquals(Optional.ofNullable(product.getPrice()), Optional.of(1.2));

    }
    //Nao retorna nada quando excluido com sucesso
    @Test
    public void whenDeleteById_DoNotReturnContent() {
        livroService.delete(livroDTO.getId());

        verify(livroRepository, times(1)).deleteById(livroDTO.getId());
    }
    //Retorna uma exception quando Criado com erro
    @Test
    public void whenCreateadExpectedException(){

        when(mapper.map(livroDTO, Livro.class)).thenReturn(Livro);

        when(livroRepository.save(Livro)).thenThrow(new RuntimeException(Constants.MESSAGE_INVALID_REQUEST));

       var exception = Assertions.assertThrows(RuntimeException.class,() -> {
            livroService.create(livroDTO);
        });

       assertEquals(Constants.MESSAGE_INVALID_REQUEST, exception.getMessage());

       verify(livroRepository, times(1)).save(Livro);
    }
    //Retorna uma exception quando atualizado com erro
    @Test
    public void whenUpdateExpectedException() {

        when(livroService.update(livroDTO.getId(), livroDTO)).thenThrow(new RuntimeException(Constants.ERROR_NOT_FOUND));

        var exception = Assertions.assertThrows(RuntimeException.class,() -> {
            livroService.update(livroDTO.getId(), livroDTO);
        });

        assertEquals(Constants.ERROR_NOT_FOUND, exception.getMessage());

        verify(livroRepository, times(2)).save(Livro);
    }
    //Retorna uma exception quando nao encontrado
    @Test
    public void whenFindByIdReturnException() {
        when(livroService.findById(livroDTO.getId())).thenThrow(new RuntimeException(Constants.ERROR_NOT_FOUND));

        var exception = Assertions.assertThrows(RuntimeException.class,() -> {
            livroService.findById(livroDTO.getId());
        });

        assertEquals(Constants.ERROR_NOT_FOUND, exception.getMessage());
        verify(livroRepository, times(2)).findById(livroDTO.getId());
    }
    //Retorna uma pagina em branco
    @Test
    public void whenFindAllReturnEmptyPage() {

        when(livroRepository.findAll(DEFAULT_PAGEABLE)).thenReturn(new PageImpl<>(List.of()));

        var products = livroService.findAll(DEFAULT_PAGEABLE);

        verify(livroRepository, times(1)).findAll(DEFAULT_PAGEABLE);

        assertEquals(0, products.getTotalElements());

    }
    //Retorna uma pagina em branco
    @Test
    public void whenSearchByFilter_And_FindMinAndMaxPrice_ReturnEmptyPage() {
        FILTER.setQuery(null);
        FILTER.setMin_price(2.0);
        FILTER.setMax_price(6.0);

        when(livroRepository.findByMinAndMaxPrice(DEFAULT_PAGEABLE, FILTER.getMin_price(), FILTER.getMax_price()))
                .thenReturn(new PageImpl<>(List.of()));

        var products = livroService.searchByFilter(DEFAULT_PAGEABLE, FILTER);

        verify(livroRepository, times(1)).findByMinAndMaxPrice(DEFAULT_PAGEABLE, FILTER.getMin_price(), FILTER.getMax_price());

        assertEquals(0, products.getTotalElements());

    }
    //Retorna uma pagina em branco
    @Test
    public void whenSearchByFilter_And_FindByNameOrDescription_ReturnEmptyPage() {
        FILTER.setQuery("broxura");
        FILTER.setMin_price(null);
        FILTER.setMax_price(null);

        when(livroRepository.findByNameOrDescriptionOrGenre(DEFAULT_PAGEABLE, FILTER.getQuery()))
                .thenReturn(new PageImpl<>(List.of()));

        var products = livroService.searchByFilter(DEFAULT_PAGEABLE, FILTER);

        verify(livroRepository, times(1)).findByNameOrDescriptionOrGenre(DEFAULT_PAGEABLE, FILTER.getQuery());

        assertEquals(0, products.getTotalElements());

    }
    //Retorna uma pagina em branco
    @Test
    public void whenSearchByFilter_And_FindByNameOrDescriptionAndMinMaxPrice_ReturnEmptyPage() {
        FILTER.setQuery("broxura");
        FILTER.setMin_price(2.0);
        FILTER.setMax_price(6.0);

        when(livroRepository.findByNameOrDescriptionAndMinMaxPrice(DEFAULT_PAGEABLE, FILTER.getQuery(), FILTER.getMin_price(), FILTER.getMax_price()))
                .thenReturn(new PageImpl<>(List.of()));

        var products = livroService.searchByFilter(DEFAULT_PAGEABLE, FILTER);

        verify(livroRepository, times(1))
                .findByNameOrDescriptionAndMinMaxPrice(DEFAULT_PAGEABLE, FILTER.getQuery(), FILTER.getMin_price(), FILTER.getMax_price());

        assertEquals(0, products.getTotalElements());

    }
    //Retorna uma exception quando nao conseguir excluir
    @Test
    public void whenDeleteReturnException() {

        when(ReflectionTestUtils.invokeMethod(livroService, "findLivro", livroDTO.getId()))
                .thenThrow(new RuntimeException(Constants.ERROR_NOT_FOUND));
        var exception = Assertions.assertThrows(RuntimeException.class,() -> {
            livroService.findById(livroDTO.getId());
        });
        verify(livroRepository, times(1)).findById(livroDTO.getId());
        assertEquals(Constants.ERROR_NOT_FOUND, exception.getMessage());
    }
}