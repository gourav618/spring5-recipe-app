package com.recipe.spring5recipeapp.domain;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryTest {

    Category category;

//    @Before
//    public void setUp(){
//       category = new Category();
//    }

    @BeforeAll
    void beforeAll() {
        category = new Category();
    }

    @Test
    public void getId() throws Exception {
        Long idValue = 4L;
        category.setId(idValue);
        assertEquals(idValue, category.getId());
    }

    @Test
    public void getDescription() throws Exception{

    }

    @Test
    public void getRecipes() throws Exception{

    }

}