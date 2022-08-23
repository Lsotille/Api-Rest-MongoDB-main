package com.api.mongodb.models.filters;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LivroFilter {

    private static final long serialVersionUID = -5930278399953270569L;

    @ApiModelProperty(value = "Parâmetro de busca por nome ou descrição ou genero do Livro", example = "Livro terror")
    private String query;

    @ApiModelProperty(value = "Parâmetro de busca por preço mínimo do Livro", example = "1.20")
    private Double min_price;

    @ApiModelProperty(value = "Parâmetro de busca por preço máximo do Livro", example = "2.20")
    private Double max_price;

    // Retorna verdadeiro se a Query estiver null e os valores nao nulos
    public boolean filterByMinAndMaxPrice() {
        return query == null && min_price != null && max_price != null;
    }
    // Retorna verdadeiro se a Query estiver preenchida e os numeros nulos
    public boolean filterByNameOrDescriptionOrGenre() {return query != null && min_price == null && max_price == null;}

    //Retorna Verdadeiro se a Query estiver preenchida e os numeros tambem.
    public boolean filterByNameOrDescriptionAndMinMaxPrice() {
        return query != null && min_price != null && max_price != null;
    }

}
