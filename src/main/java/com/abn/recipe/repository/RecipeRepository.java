package com.abn.recipe.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long>, JpaSpecificationExecutor<RecipeEntity> {

    @EntityGraph(value = "Recipe.ingredients", type = EntityGraph.EntityGraphType.LOAD)
    @Nonnull Optional<RecipeEntity> findById(@Nonnull Long id);

    @EntityGraph(value = "Recipe.ingredients", type = EntityGraph.EntityGraphType.LOAD)
    @Nonnull Page<RecipeEntity> findAll(@Nonnull Pageable pageable);

    @EntityGraph(value = "Recipe.ingredients", type = EntityGraph.EntityGraphType.LOAD)
    @Nonnull Page<RecipeEntity> findAll(Specification<RecipeEntity> spec, @Nonnull Pageable pageable);

}
