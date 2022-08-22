package com.api.mongodb.models.filters;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilter {

    private static final long serialVersionUID = -5930278399953270569L;

    @ApiModelProperty(value = "Parâmetro de busca por nome ou descrição do produto", example = "Toalha de banho")
    private String query;

    @ApiModelProperty(value = "Parâmetro de busca por preço mínimo do produto", example = "1.20")
    private Double min_price;

    @ApiModelProperty(value = "Parâmetro de busca por preço máximo do produto", example = "2.20")
    private Double max_price;

    public boolean filterByMinAndMaxPrice() {
        return query == null && min_price != null && max_price != null;
    }

    public boolean filterByNameOrDescription() {
        return query != null && min_price == null && max_price == null;
    }

    public boolean filterByNameOrDescriptionAndMinMaxPrice() {
        return query != null && min_price != null && max_price != null;
    }

}
