package com.example.demo3.dto;

import lombok.Data;
import jakarta.validation.constraints.Min;

@Data
public class PageQuery {
    @Min(value = 1, message = "页码不能小于1")
    private Integer page = 1;

    @Min(value = 1, message = "每页大小不能小于1")
    private Integer limit = 10;

    private String keyword;
}

