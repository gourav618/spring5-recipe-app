package com.recipe.spring5recipeapp.service;

import com.recipe.spring5recipeapp.domain.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();
}