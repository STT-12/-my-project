package com.example.demo3.dto;

import lombok.Data;

@Data
public class UserQuery extends PageQuery {
    private Integer privilege;
}
