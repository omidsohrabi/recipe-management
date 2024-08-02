package com.abn.recipe.api;

import com.abn.recipe.api.dto.RecipeDto;
import com.abn.recipe.api.dto.RecipeSearchDto;
import com.abn.recipe.domain.model.RecipeSearch;
import com.abn.recipe.api.mapper.RecipeDtoMapper;
import com.abn.recipe.domain.model.Recipe;
import com.abn.recipe.api.dto.PaginationDto;
import com.abn.recipe.service.RecipeService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeDtoMapper recipeDtoMapper;
    private final RecipeService recipeService;

    public RecipeController(RecipeDtoMapper recipeDtoMapper, RecipeService recipeService) {
        this.recipeDtoMapper = recipeDtoMapper;
        this.recipeService = recipeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long addRecipe(@RequestBody RecipeDto recipeDto) {
        Recipe recipe = recipeDtoMapper.toDomain(recipeDto);
        recipeService.saveRecipe(recipe);
        return recipe.getId();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable Long id) {
        Recipe recipe = recipeService.getRecipeById(id);
        return ResponseEntity.ok(recipeDtoMapper.toDto(recipe));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRecipe(@PathVariable Long id, @RequestBody RecipeDto recipeDto) {
        Recipe recipe = recipeDtoMapper.toDomain(recipeDto);
        recipeService.updateRecipe(id, recipe);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RecipeDto> getAllRecipes(PaginationDto paginationDto) {
        Pageable pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getSize());
        List<Recipe> recipes = recipeService.getAllRecipes(pageable);
        return recipes.stream().map(recipeDtoMapper::toDto).toList();
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RecipeDto> searchRecipes(RecipeSearchDto searchDto) {
        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize());
        RecipeSearch recipeSearch = recipeDtoMapper.toRecipeSearch(searchDto);
        return recipeService.searchRecipes(recipeSearch, pageable).stream()
                .map(recipeDtoMapper::toDto)
                .toList();
    }
}
