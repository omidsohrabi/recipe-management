package com.abn.recipe.service;

import com.abn.recipe.domain.exception.DuplicateRecipeException;
import com.abn.recipe.domain.exception.RecipeNotFoundException;
import com.abn.recipe.domain.mapper.RecipeAdapter;
import com.abn.recipe.domain.model.Recipe;
import com.abn.recipe.domain.model.RecipeSearch;
import com.abn.recipe.repository.RecipeEntity;
import com.abn.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {
    @Mock
    private RecipeAdapter recipeAdapter;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void saveRecipe_shouldSaveAndReturnId() {
        when(recipeRepository.existsByName(sampleRecipe.getName())).thenReturn(false);
        when(recipeAdapter.toEntity(any())).thenReturn(sampleRecipeEntity);
        when(recipeRepository.save(any(RecipeEntity.class))).thenReturn(sampleRecipeEntity);

        long id = recipeService.saveRecipe(sampleRecipe);

        assertEquals(1L, id);
        verify(recipeRepository).existsByName(sampleRecipe.getName());
        verify(recipeAdapter).toEntity(sampleRecipe);
        verify(recipeRepository).save(sampleRecipeEntity);
    }

    @Test
    void saveRecipe_shouldThrowExceptionWhenRecipeExists() {
        when(recipeRepository.existsByName(sampleRecipe.getName())).thenReturn(true);

        assertThrows(DuplicateRecipeException.class, () -> recipeService.saveRecipe(sampleRecipe));
        verify(recipeRepository).existsByName(sampleRecipe.getName());
    }

    @Test
    void getRecipeById_shouldReturnRecipe() {
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(sampleRecipeEntity));
        when(recipeAdapter.toDomain(any(RecipeEntity.class))).thenReturn(sampleRecipe);

        Recipe foundRecipe = recipeService.getRecipeById(1L);

        assertEquals(sampleRecipe, foundRecipe);
        verify(recipeRepository).findById(1L);
        verify(recipeAdapter).toDomain(sampleRecipeEntity);
    }

    @Test
    void getRecipeById_shouldThrowExceptionWhenNotFound() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipeById(1L));
        verify(recipeRepository).findById(1L);
    }

    @Test
    void updateRecipe_shouldUpdateRecipe() {
        when(recipeRepository.existsById(1L)).thenReturn(true);
        when(recipeRepository.existsByNameAndIdNot(sampleRecipe.getName(), 1L)).thenReturn(false);
        when(recipeAdapter.toEntity(any(Recipe.class))).thenReturn(sampleRecipeEntity);

        recipeService.updateRecipe(1L, sampleRecipe);

        verify(recipeRepository).existsById(1L);
        verify(recipeAdapter).toEntity(sampleRecipe);
        verify(recipeRepository).save(sampleRecipeEntity);
    }

    @Test
    void updateRecipe_shouldThrowExceptionWhenNotFound() {
        when(recipeRepository.existsById(1L)).thenReturn(false);

        assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(1L, sampleRecipe));
        verify(recipeRepository).existsById(1L);
    }

    @Test
    void updateRecipe_shouldThrowExceptionWhenRecipeNameExists() {
        when(recipeRepository.existsById(1L)).thenReturn(true);
        when(recipeRepository.existsByNameAndIdNot(sampleRecipe.getName(), 1L)).thenReturn(true);

        assertThrows(DuplicateRecipeException.class, () -> recipeService.updateRecipe(1L, sampleRecipe));
        verify(recipeRepository).existsById(1L);
        verify(recipeRepository).existsByNameAndIdNot(sampleRecipe.getName(), 1L);
    }

    @Test
    void deleteRecipe_shouldDeleteRecipe() {
        when(recipeRepository.existsById(anyLong())).thenReturn(true);

        recipeService.deleteRecipe(1L);

        verify(recipeRepository).existsById(1L);
        verify(recipeRepository).deleteById(1L);
    }

    @Test
    void deleteRecipe_shouldThrowExceptionWhenNotFound() {
        when(recipeRepository.existsById(1L)).thenReturn(false);

        assertThrows(RecipeNotFoundException.class, () -> recipeService.deleteRecipe(1L));
        verify(recipeRepository).existsById(1L);
    }

    @Test
    void getAllRecipes_shouldReturnAllRecipes() {
        List<RecipeEntity> entities = new ArrayList<>();
        entities.add(sampleRecipeEntity);
        Page<RecipeEntity> page = new PageImpl<>(entities);
        when(recipeRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(recipeAdapter.toDomain(any(RecipeEntity.class))).thenReturn(sampleRecipe);

        List<Recipe> recipes = recipeService.getAllRecipes(Pageable.unpaged());

        assertEquals(1, recipes.size());
        assertEquals(sampleRecipe, recipes.get(0));
        verify(recipeRepository).findAll(any(Pageable.class));
        verify(recipeAdapter).toDomain(sampleRecipeEntity);
    }

    @Test
    void searchRecipes_shouldReturnFilteredRecipes() {
        List<RecipeEntity> entities = new ArrayList<>();
        entities.add(sampleRecipeEntity);
        Page<RecipeEntity> page = new PageImpl<>(entities);
        when(recipeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(recipeAdapter.toDomain(any(RecipeEntity.class))).thenReturn(sampleRecipe);

        List<Recipe> recipes = recipeService.searchRecipes(sampleRecipeSearch, Pageable.unpaged());

        assertEquals(1, recipes.size());
        assertEquals(sampleRecipe, recipes.get(0));
        verify(recipeRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(recipeAdapter).toDomain(sampleRecipeEntity);
    }

    @Test
    void countRecipes_shouldReturnNumberOfRecipes() {
        when(recipeRepository.count(any(Specification.class))).thenReturn(10L);

        Long count = recipeService.countRecipes(sampleRecipeSearch);

        assertEquals(10, count);
        verify(recipeRepository).count(any(Specification.class));
    }

    private final RecipeEntity sampleRecipeEntity = RecipeEntity.builder()
            .id(1L)
            .name("Recipe")
            .vegetarian(true)
            .servings(4)
            .ingredients(List.of("Ingredient 1", "Ingredient 2"))
            .instructions("Instructions")
            .build();

    private final Recipe sampleRecipe = Recipe.builder()
            .id(1L)
            .name("Recipe")
            .vegetarian(true)
            .servings(4)
            .ingredients(List.of("Ingredient 1", "Ingredient 2"))
            .instructions("Instructions")
            .build();

    private final RecipeSearch sampleRecipeSearch = RecipeSearch.builder()
            .name("Recipe")
            .vegetarian(true)
            .servings(4)
            .includeIngredients(List.of("Ingredient 1", "Ingredient 2"))
            .instructionText("Instructions")
            .build();
}