package com.recipe.spring5recipeapp.service;

import com.recipe.spring5recipeapp.domain.Recipe;
import com.recipe.spring5recipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void saveImageFile(Long recipeId, MultipartFile file) {
      log.debug("Received a File");
      try {
          Recipe recipe = recipeRepository.findById(recipeId).get();

          Byte[] byteObjects = new Byte[file.getBytes().length];

          int i = 0;

          for (byte b : file.getBytes()){
              byteObjects[i++] = b;
          }

          recipe.setImage(byteObjects);

          recipeRepository.save(recipe);
      } catch (Exception e) {
          //todo handle error better
          log.error("error occured saving file", e);
          e.printStackTrace();
      }
    }
}
