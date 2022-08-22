package com.api.mongodb.models.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements Serializable {

    private static final long serialVersionUID = -4011786761694912983L;

    @ApiModelProperty(position = 0)
    private String id;

    @ApiModelProperty(position = 1)
    private String name;

    @ApiModelProperty(position = 2)
    private String description;

    @ApiModelProperty(position = 3)
    private Double price;

    @ApiModelProperty(position = 4)
    private LocalDateTime createdAt;

    @ApiModelProperty(position = 5)
    private LocalDateTime updatedAt;
}