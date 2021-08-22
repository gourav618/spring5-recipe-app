package com.recipe.spring5recipeapp.repositories;

import com.recipe.spring5recipeapp.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface categoryRepository extends CrudRepository<Category, Long> {
}
