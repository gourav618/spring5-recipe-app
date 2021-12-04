package com.recipe.spring5recipeapp.service;

import com.recipe.spring5recipeapp.commands.IngredientCommand;

public interface IngredientService {

    IngredientCommand findByRecipeIdAndIngredientId(long anyLong, long anyLong1);

}
