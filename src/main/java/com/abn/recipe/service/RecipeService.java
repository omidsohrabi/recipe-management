package com.abn.recipe.service;

import com.abn.recipe.domain.model.RecipeSearch;
import com.abn.recipe.domain.exception.RecipeNotFoundException;
import com.abn.recipe.domain.mapper.RecipeAdapter;
import com.abn.recipe.domain.model.Recipe;
import com.abn.recipe.repository.RecipeEntity;
import com.abn.recipe.repository.RecipeEntity_;
import com.abn.recipe.repository.RecipeRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    private final RecipeAdapter recipeAdapter;
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeAdapter recipeAdapter, RecipeRepository recipeRepository) {
        this.recipeAdapter = recipeAdapter;
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    public long saveRecipe(Recipe recipe) {
        RecipeEntity recipeEntity = recipeAdapter.toEntity(recipe);
        return recipeRepository.save(recipeEntity).getId();
    }

    public Recipe getRecipeById(Long id) {
        Optional<RecipeEntity> entity = recipeRepository.findById(id);
        if (entity.isEmpty()) {
            throw new RecipeNotFoundException(id);
        }
        return recipeAdapter.toDomain(entity.get());
    }

    @Transactional
    public void updateRecipe(Long id, Recipe recipe) {
        if (!recipeRepository.existsById(id)) {
            throw new RecipeNotFoundException(id);
        }
        RecipeEntity recipeEntity = recipeAdapter.toEntity(recipe);
        recipeEntity.setId(id);
        recipeRepository.save(recipeEntity);
    }

    @Transactional
    public void deleteRecipe(Long id) {
        if (!recipeRepository.existsById(id)) {
            throw new RecipeNotFoundException(id);
        }
        recipeRepository.deleteById(id);
    }

    public List<Recipe> getAllRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable).stream().map(recipeAdapter::toDomain).toList();
    }

    public List<Recipe> searchRecipes(RecipeSearch recipeSearch, Pageable pageable) {
        return recipeRepository.findAll(createSearchQuery(recipeSearch), pageable).stream()
                .map(recipeAdapter::toDomain)
                .toList();
    }

    public long countRecipes(RecipeSearch recipeSearch) {
        return recipeRepository.count(createSearchQuery(recipeSearch));
    }

    private static Specification<RecipeEntity> createSearchQuery(
            RecipeSearch recipeSearch) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (recipeSearch.getName() != null) {
                predicates.add(criteriaBuilder.equal(root.get(RecipeEntity_.name), recipeSearch.getName()));
            }

            if (recipeSearch.getVegetarian() != null) {
                predicates.add(criteriaBuilder.equal(root.get(RecipeEntity_.vegetarian), recipeSearch.getVegetarian()));
            }

            if (recipeSearch.getServings() != null) {
                predicates.add(criteriaBuilder.equal(root.get(RecipeEntity_.servings), recipeSearch.getServings()));
            }

            if (recipeSearch.getIncludeIngredients() != null && !recipeSearch.getIncludeIngredients().isEmpty()) {
                recipeSearch.getIncludeIngredients().forEach(ingredient ->
                        predicates.add(criteriaBuilder.isMember(ingredient, root.get(RecipeEntity_.ingredients)))
                );
            }

            if (recipeSearch.getExcludeIngredients() != null && !recipeSearch.getExcludeIngredients().isEmpty()) {
                recipeSearch.getExcludeIngredients().forEach(ingredient ->
                        predicates.add(criteriaBuilder.isNotMember(ingredient, root.get(RecipeEntity_.ingredients)))
                );
            }

            if (recipeSearch.getInstructionText() != null && !recipeSearch.getInstructionText().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get(RecipeEntity_.instructions), "%" + recipeSearch.getInstructionText() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
