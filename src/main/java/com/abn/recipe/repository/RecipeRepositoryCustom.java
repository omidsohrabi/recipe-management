package com.abn.recipe.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecipeRepositoryCustom {
    private final EntityManager entityManager;

    public RecipeRepositoryCustom(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Page<Long> findRecipeIds(Specification<RecipeEntity> spec, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<RecipeEntity> root = query.from(RecipeEntity.class);
        query.select(root.get(RecipeEntity_.ID)).where(spec.toPredicate(root, query, cb));

        TypedQuery<Long> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Long> ids = typedQuery.getResultList();
        return new PageImpl<>(ids);
    }
}
