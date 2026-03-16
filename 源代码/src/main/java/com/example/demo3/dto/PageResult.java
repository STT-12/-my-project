package com.example.demo3.dto;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private List<T> records;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalRow;
    private Integer totalPage;

    public PageResult(List<T> records, Integer pageNumber, Integer pageSize, Integer totalRow) {
        this.records = records;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalRow = totalRow;
        this.totalPage = (int) Math.ceil((double) totalRow / pageSize);
    }
}