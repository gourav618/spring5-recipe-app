package com.recipe.spring5recipeapp.controllers;

import com.recipe.spring5recipeapp.service.RecipeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IndexController {

    private RecipeServiceImpl recipeService;

    public IndexController(RecipeServiceImpl recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"", "/","/index"})
    public String getIndexPage(Model model){
        log.debug("Getting index page");
        model.addAttribute("recipes", recipeService.getRecipes());
        return "index";
    }
}
