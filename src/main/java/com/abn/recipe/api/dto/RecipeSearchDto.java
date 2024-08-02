package com.abn.recipe.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeSearchDto {
    private String name;
    private Boolean vegetarian;
    private Integer servings;
    private List<String> includeIngredients;
    private List<String> excludeIngredients;
    private String instructionText;
    private int page = 0;
    private int size = 10;
}
