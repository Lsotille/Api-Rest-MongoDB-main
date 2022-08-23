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
public class LivroDTO implements Serializable {

    //Seta o ID Automatico
    private static final long serialVersionUID = -4011786761694912983L;

    //Cria o campo Id no DTO
    @ApiModelProperty(position = 0)
    private String id;

    //Cria o campo name no DTO
    @ApiModelProperty(position = 1)
    private String name;

    //Cria o campo description no DTO
    @ApiModelProperty(position = 2)
    private String description;

    //Cria o campo price no DTO
    @ApiModelProperty(position = 3)
    private Double price;

    //Cria o campo created at(data da criacao) no DTO
    @ApiModelProperty(position = 4)
    private LocalDateTime createdAt;

    //Cria o campo update at(se o livro for atualizado) no DTO
    @ApiModelProperty(position = 5)
    private LocalDateTime updatedAt;

    //Cria o campo genre no DTO
    @ApiModelProperty(position = 6)
    private String genre;
}