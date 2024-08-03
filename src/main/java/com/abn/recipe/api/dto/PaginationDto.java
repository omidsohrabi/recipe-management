package com.abn.recipe.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationDto {
    @Schema(defaultValue = "0")
    @Min(value = 0, message = "Page must be greater than or equal to 0")
    private int page = 0;
    @Schema(defaultValue = "10")
    @Min(value = 1, message = "Size must be greater than 0")
    private int size = 10;
}
