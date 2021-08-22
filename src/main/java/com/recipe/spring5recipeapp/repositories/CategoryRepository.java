package com.recipe.spring5recipeapp.repositories;

import com.recipe.spring5recipeapp.domain.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    
    Optional<Category> findByDescription(String description);
}
