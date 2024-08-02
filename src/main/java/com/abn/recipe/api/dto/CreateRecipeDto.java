package com.abn.recipe.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CreateRecipeDto {
    @NotNull(message = "name cannot be null")
    private String name;
    private boolean vegetarian;
    @NotNull(message = "servings cannot be null")
    @Min(value = 1, message = "servings must be greater than 0")
    private Integer servings;
    private List<String> ingredients;
    private String instructions;

}
