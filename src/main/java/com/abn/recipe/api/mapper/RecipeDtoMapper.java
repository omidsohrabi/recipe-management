package com.abn.recipe.api.mapper;

import com.abn.recipe.api.dto.CreateRecipeDto;
import com.abn.recipe.api.dto.RecipeDto;
import com.abn.recipe.api.dto.RecipeSearchDto;
import com.abn.recipe.domain.model.Recipe;
import com.abn.recipe.domain.model.RecipeSearch;
import org.springframework.stereotype.Service;

@Service
public class RecipeDtoMapper {
    public RecipeDto toDto(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .vegetarian(recipe.isVegetarian())
                .servings(recipe.getServings())
                .ingredients(recipe.getIngredients())
                .instructions(recipe.getInstructions())
                .build();
    }

    public Recipe toDomain(CreateRecipeDto createRecipeDto) {
        return Recipe.builder()
                .name(createRecipeDto.getName())
                .vegetarian(createRecipeDto.isVegetarian())
                .servings(createRecipeDto.getServings())
                .ingredients(createRecipeDto.getIngredients())
                .instructions(createRecipeDto.getInstructions())
                .build();
    }

    public RecipeSearch toRecipeSearch(RecipeSearchDto searchDto) {
        return RecipeSearch.builder()
                .name(searchDto.getName())
                .vegetarian(searchDto.getVegetarian())
                .servings(searchDto.getServings())
                .includeIngredients(searchDto.getIncludeIngredients())
                .excludeIngredients(searchDto.getExcludeIngredients())
                .instructionText(searchDto.getInstructionText())
                .build();
    }
}
