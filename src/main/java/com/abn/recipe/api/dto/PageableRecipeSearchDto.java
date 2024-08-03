package com.abn.recipe.api.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageableRecipeSearchDto extends RecipeSearchDto {
    @Min(value = 0, message = "Page must be greater than or equal to 0")
    private int page = 0;
    @Min(value = 1, message = "Size must be greater than 0")
    private int size = 10;
}
