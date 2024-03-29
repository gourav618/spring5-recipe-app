package com.recipe.spring5recipeapp.service;

import com.recipe.spring5recipeapp.commands.IngredientCommand;
import com.recipe.spring5recipeapp.converters.IngredientCommandToIngredient;
import com.recipe.spring5recipeapp.converters.IngredientToIngredientCommand;
import com.recipe.spring5recipeapp.domain.Ingredient;
import com.recipe.spring5recipeapp.domain.Recipe;
import com.recipe.spring5recipeapp.repositories.RecipeRepository;
import com.recipe.spring5recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(long recipeId, long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()){
            //todo impl error handling
            log.error("recipe id not found . Id: "+recipeId);
        }

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

        if (!ingredientCommandOptional.isPresent()){
            //todo impl error handling
            log.error("ingredient not found with id: "+ingredientId);
        }

        return ingredientCommandOptional.get();
    }

    @Transactional
    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());

        if (!recipeOptional.isPresent()){
            //todo toss error if not found
            log.error("recipe not found for id: " +ingredientCommand.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                    .findFirst();

            if (ingredientOptional.isPresent()){
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(ingredientCommand.getDescription());
                ingredientFound.setAmount(ingredientCommand.getAmount());
                ingredientFound.setUom(unitOfMeasureRepository.findById(ingredientCommand.getUom().getId())
                        .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); //todo handle this
            }else {
                //add new ingredient
                Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);
                ingredient.setRecipe(recipe);
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedOptionalIngredient = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredient -> recipeIngredient.getId().equals(ingredientCommand.getId()))
                    .findFirst();

            //check by description
            if (!savedOptionalIngredient.isPresent()){
                //not save but best guess
                savedOptionalIngredient = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredient -> recipeIngredient.getDescription().equals(ingredientCommand.getDescription()))
                        .filter(recipeIngredient -> recipeIngredient.getAmount().equals(ingredientCommand.getAmount()))
                        .filter(recipeIngredient -> recipeIngredient.getUom().getId()   .equals(ingredientCommand.getUom().getId()))
                        .findFirst();
            }

            //to do check for fail
            return ingredientToIngredientCommand.convert(savedOptionalIngredient.get());

        }
    }

    @Override
    public void deleteById(Long recipeId, Long ingredientId) {
        log.debug("delete ingredient with recipeId: " +recipeId + " and ingredientId: "+ingredientId);
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if (optionalRecipe.isPresent()){
            Recipe recipe = optionalRecipe.get();
            log.debug("found recipe");

            Optional<Ingredient> optionalIngredient = recipe.getIngredients()
                    .stream().filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();

            if (optionalIngredient.isPresent()){
                log.debug("found ingredient");
                Ingredient ingredientToDelete = optionalIngredient.get();
                ingredientToDelete.setRecipe(null);
                recipe.getIngredients().remove(optionalIngredient.get());
                recipeRepository.save(recipe);
            }
        }else {
            log.debug("recipe Id not found. Id: "+recipeId);
        }
    }
}
