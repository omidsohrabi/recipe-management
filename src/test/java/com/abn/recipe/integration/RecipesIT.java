package com.abn.recipe.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RecipesIT {

    @Autowired
    private MockMvc mockMvc;

    private final static String BASE_URL = "/api/recipes";

    @Test
    void addRecipe_shouldReturnCreatedRecipeId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Recipe\", \"vegetarian\": true, \"servings\": 4, " +
                                "\"ingredients\": [\"ing\"], \"instructions\": \"New instructions\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("4"));
    }

    @Test
    void addRecipe_shouldThrowDuplicateRecipeException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Vegetable Soup\", \"vegetarian\": true, \"servings\": 4, " +
                                "\"ingredients\": [\"ing\"], \"instructions\": \"New instructions\"}"))
                .andExpect(jsonPath("$.errorCode").value("DUPLICATE_RECIPE_ERROR"))
                .andExpect(jsonPath("$.errorDetails").value("Recipe with this name already exists: Vegetable Soup"));
    }

    @Test
    void getRecipeById_happyFlow() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Vegetable Soup"))
                .andExpect(jsonPath("$.vegetarian").value(true))
                .andExpect(jsonPath("$.servings").value(4))
                .andExpect(jsonPath("$.ingredients").isArray())
                .andExpect(jsonPath("$.instructions").value("Chop vegetables and cook in broth until tender."));
    }

    @Test
    void getRecipeById_notFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/9")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RECIPE_NOT_FOUND"))
                .andExpect(jsonPath("$.errorDetails").value("Recipe not found with id: 9"));
    }

    @Test
    void updateRecipe_shouldUpdateRecipe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Recipe\", \"vegetarian\": true, \"servings\": 4, " +
                                "\"ingredients\": [\"ing\"], \"instructions\": \"Updated instructions\"}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateRecipe_notFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Recipe\", \"vegetarian\": true, \"servings\": 4, \"" +
                                "ingredients\": [\"ing\"], \"instructions\": \"Updated instructions\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RECIPE_NOT_FOUND"))
                .andExpect(jsonPath("$.errorDetails").value("Recipe not found with id: 9"));
    }

    @Test
    void updateRecipe_duplicateRecipeError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Vegetable Soup\", \"vegetarian\": true, \"servings\": 4, \"" +
                                "ingredients\": [\"ing\"], \"instructions\": \"Updated instructions\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("DUPLICATE_RECIPE_ERROR"))
                .andExpect(jsonPath("$.errorDetails").value("Recipe with this name already exists: Vegetable Soup"));
    }

    @Test
    void deleteRecipe_shouldDeleteRecipe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/3"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteRecipe_notFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/9"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RECIPE_NOT_FOUND"))
                .andExpect(jsonPath("$.errorDetails").value("Recipe not found with id: 9"));
    }

    @Test
    void getAllRecipes_shouldReturnAllRecipes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Vegetable Soup"))
                .andExpect(jsonPath("$[0].vegetarian").value(true))
                .andExpect(jsonPath("$[0].servings").value(4))
                .andExpect(jsonPath("$[0].instructions").value("Chop vegetables and cook in broth until tender."));
    }

    @Test
    void searchRecipes_shouldReturnMatchingRecipes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/search")
                        .param("page", "0")
                        .param("size", "10")
                        .param("includeIngredients", "Potatoes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Vegetable Soup"))
                .andExpect(jsonPath("$[0].vegetarian").value(true))
                .andExpect(jsonPath("$[0].servings").value(4))
                .andExpect(jsonPath("$[0].ingredients").isArray())
                .andExpect(jsonPath("$[0].instructions").value("Chop vegetables and cook in broth until tender."));
    }

    @Test
    void countRecipes_shouldReturnRecipeCount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/count")
                        .param("page", "0")
                        .param("size", "10")
                        .param("includeIngredients", "Potatoes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

}
