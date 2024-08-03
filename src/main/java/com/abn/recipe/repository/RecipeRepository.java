package com.abn.recipe.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long>, JpaSpecificationExecutor<RecipeEntity> {

    @EntityGraph(value = "Recipe.ingredients", type = EntityGraph.EntityGraphType.LOAD)
    @Nonnull Optional<RecipeEntity> findById(@Nonnull Long id);

    boolean existsByName(@Nonnull String name);

    boolean existsByNameAndIdNot(@Nonnull String name, @Nonnull Long id);

    @Query("SELECT r.id FROM RecipeEntity r")
    Page<Long> findRecipeIds(Pageable pageable);

    @EntityGraph(value = "Recipe.ingredients", type = EntityGraph.EntityGraphType.LOAD)
    List<RecipeEntity> findByIdIn(List<Long> ids);

}
