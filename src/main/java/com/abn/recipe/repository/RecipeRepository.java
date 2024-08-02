package com.abn.recipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long>, JpaSpecificationExecutor<RecipeEntity> {

    @EntityGraph(value = "Recipe.ingredients", type = EntityGraph.EntityGraphType.LOAD)
    Optional<RecipeEntity> findById(Long id);

    @EntityGraph(value = "Recipe.ingredients", type = EntityGraph.EntityGraphType.LOAD)
    Page<RecipeEntity> findAll(Pageable pageable);

}
