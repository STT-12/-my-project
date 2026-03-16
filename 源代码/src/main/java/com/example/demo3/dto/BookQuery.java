package com.example.demo3.dto;

import lombok.Data;

@Data
public class BookQuery extends PageQuery {
    private Integer categoryId = 0;
}
