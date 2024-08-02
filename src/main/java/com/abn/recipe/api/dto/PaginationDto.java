package com.abn.recipe.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationDto {
    private int page = 0;
    private int size = 10;
}
