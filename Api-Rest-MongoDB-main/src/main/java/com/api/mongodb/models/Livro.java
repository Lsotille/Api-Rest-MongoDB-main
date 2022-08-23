package com.api.mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Livro implements Serializable {

    private static final long serialVersionUID = -8271925344794286698L;

    @Id
    private String id;

    private String name;

    private String description;

    private Double price;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String genre;

}
