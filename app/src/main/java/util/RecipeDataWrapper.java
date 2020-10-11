package util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vin on 5/21/2015.
 */
public class RecipeDataWrapper implements Serializable{
    private Recipe recipe;

    public RecipeDataWrapper(Recipe data) {
        this.recipe = data;
    }

    public Recipe getRecipe() {
        return this.recipe;
    }
}