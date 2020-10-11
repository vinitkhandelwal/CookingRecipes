package util;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vin on 5/13/2015.
 */
public class Recipe implements Serializable {

    private int id;
    private String recipeName;
    private String recipeDesc;
    private String category;
    private String subCategoryType;

    private String ingreditents;
    private String proceDure;
    private String receipeImageUrl;
    private boolean isFavourite;
    private String duration;
    private String Servings;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeDesc() {
        return recipeDesc;
    }

    public void setRecipeDesc(String recipeDesc) {
        this.recipeDesc = recipeDesc;
    }

    public String getReceipeImageUrl() {
        return receipeImageUrl;
    }

    public void setReceipeImageUrl(String receipeImageUrl) {
        this.receipeImageUrl = receipeImageUrl;
    }

    public String getSubCategoryType() {
        return subCategoryType;
    }

    public void setSubCategoryType(String subCategoryType) {
        this.subCategoryType = subCategoryType;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIngreditents() {
        return ingreditents;
    }

    public void setIngreditents(String ingreditents) {
        this.ingreditents = ingreditents;
    }

    public String getProceDure() {
        return proceDure;
    }

    public void setProceDure(String proceDure) {
        this.proceDure = proceDure;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getServings() {
        return Servings;
    }

    public void setServings(String servings) {
        Servings = servings;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", recipeName='" + recipeName + '\'' +
                ", recipeDesc='" + recipeDesc + '\'' +
                ", category='" + category + '\'' +
                ", subCategoryType='" + subCategoryType + '\'' +
                ", ingreditents='" + ingreditents + '\'' +
                ", proceDure='" + proceDure + '\'' +
                ", receipeImageUrl='" + receipeImageUrl + '\'' +
                ", isFavourite=" + isFavourite +
                ", duration='" + duration + '\'' +
                ", Servings='" + Servings + '\'' +
                '}';
    }
}
