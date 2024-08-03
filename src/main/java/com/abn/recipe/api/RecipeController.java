package com.abn.recipe.api;

import com.abn.recipe.api.dto.CreateRecipeDto;
import com.abn.recipe.api.dto.PageableRecipeSearchDto;
import com.abn.recipe.api.dto.RecipeDto;
import com.abn.recipe.api.dto.RecipeSearchDto;
import com.abn.recipe.api.exception.ErrorResponse;
import com.abn.recipe.domain.model.RecipeSearch;
import com.abn.recipe.api.mapper.RecipeDtoMapper;
import com.abn.recipe.domain.model.Recipe;
import com.abn.recipe.api.dto.PaginationDto;
import com.abn.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

@Tag(name = "Recipe API", description = "API for managing recipes")
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeDtoMapper recipeDtoMapper;
    private final RecipeService recipeService;

    public RecipeController(RecipeDtoMapper recipeDtoMapper, RecipeService recipeService) {
        this.recipeDtoMapper = recipeDtoMapper;
        this.recipeService = recipeService;
    }

    @Operation(summary = "Add a new recipe", description = "Creates a new recipe and returns its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recipe created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input or recipe name already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Long addRecipe(@Valid @RequestBody CreateRecipeDto recipeDto) {
        Recipe recipe = recipeDtoMapper.toDomain(recipeDto);
        return recipeService.saveRecipe(recipe);
    }

    @Operation(summary = "Get a recipe by ID", description = "Retrieves a recipe by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe found",
                    content = @Content(schema = @Schema(implementation = RecipeDto.class))),
            @ApiResponse(responseCode = "404", description = "Recipe not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RecipeDto getRecipeById(@PathVariable Long id) {
        Recipe recipe = recipeService.getRecipeById(id);
        return recipeDtoMapper.toDto(recipe);
    }

    @Operation(summary = "Update a recipe", description = "Updates an existing recipe by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recipe updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input or recipe name already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recipe not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRecipe(@PathVariable Long id, @Valid @RequestBody CreateRecipeDto recipeDto) {
        Recipe recipe = recipeDtoMapper.toDomain(recipeDto);
        recipeService.updateRecipe(id, recipe);
    }

    @Operation(summary = "Delete a recipe", description = "Deletes a recipe by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recipe deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Recipe not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
    }

    @Operation(summary = "Get all recipes", description = "Retrieves all recipes with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipes retrieved",
                    content = @Content(schema = @Schema(implementation = RecipeDto.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RecipeDto> getAllRecipes(@ParameterObject @Valid PaginationDto paginationDto) {
        Pageable pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getSize());
        List<Recipe> recipes = recipeService.getAllRecipes(pageable);
        return recipes.stream().map(recipeDtoMapper::toDto).toList();
    }

    @Operation(summary = "Search recipes", description = "Search for recipes based on criteria with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipes found",
                    content = @Content(schema = @Schema(implementation = RecipeDto.class)))
    })
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RecipeDto> searchRecipes(@ParameterObject @Valid PageableRecipeSearchDto searchDto) {
        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize());
        RecipeSearch recipeSearch = recipeDtoMapper.toRecipeSearch(searchDto);
        return recipeService.searchRecipes(recipeSearch, pageable).stream()
                .map(recipeDtoMapper::toDto)
                .toList();
    }

    @Operation(summary = "Count recipes", description = "Counts the number of recipes matching the search criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved",
                    content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long countRecipes(@Valid RecipeSearchDto searchDto) {
        RecipeSearch recipeSearch = recipeDtoMapper.toRecipeSearch(searchDto);
        return recipeService.countRecipes(recipeSearch);
    }
}
