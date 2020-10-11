package dao;

/**
 * Created by user on 20-04-2017.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.slpf.cooking.DataHolder;

import util.Recipe;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "cookingRecipe.db";
    public static final String TABLE_NAME = "recipes";
    public static final String ID = "id";
    public static final String RECIPE_NAME = "RecipeName";
    public static final String DESCRIPTION = "RecipeDescription";
    public static final String CATEGORY = "Category";
    public static final String SUBCATEGORY = "Subcategory";
    public static final String INGREDIENTS = "Ingredients";
    public static final String PROCEDURE = "Procedure";
    public static final String IMG_URL = "receipeImageUrl";
    public static final String IS_FAVOURITE = "isFavourite";
    public static final String DURATION = "duration";
    public static final String SERVING = "serving";

    private static final String CREATE_TABLE_RECIPE = "CREATE TABLE "
            + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY," + RECIPE_NAME
            + " TEXT," + DESCRIPTION + " TEXT,"
            +CATEGORY + " TEXT," +SUBCATEGORY + " TEXT," +INGREDIENTS + " TEXT,"
            +PROCEDURE + " TEXT," +IMG_URL + " TEXT," +IS_FAVOURITE + " TEXT,"
            +DURATION + " TEXT," +SERVING + " TEXT"
            + ")";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE_RECIPE
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertAllRecipe (List<Recipe> recipes) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        db.execSQL(CREATE_TABLE_RECIPE);
        Iterator<Recipe> recipeIterator = recipes.iterator();
        int i = 1;
        while(recipeIterator.hasNext()) {
            Recipe recipe = recipeIterator.next();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, i++);
            contentValues.put(RECIPE_NAME, recipe.getRecipeName());
            contentValues.put(DESCRIPTION, recipe.getRecipeDesc());
            contentValues.put(CATEGORY, recipe.getCategory());
            contentValues.put(SUBCATEGORY, recipe.getSubCategoryType());
            contentValues.put(INGREDIENTS, recipe.getIngreditents());
            contentValues.put(PROCEDURE, recipe.getProceDure());
            contentValues.put(IMG_URL, recipe.getReceipeImageUrl());
            contentValues.put(IS_FAVOURITE, false);
            contentValues.put(DURATION, recipe.getDuration());
            contentValues.put(SERVING, recipe.getServings());
            db.insert(TABLE_NAME, null, contentValues);
        }
        return true;
    }

    public List<Recipe> getALLRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String SELECT_QUERY = "select * from "+ TABLE_NAME ;
        Cursor res =  db.rawQuery(SELECT_QUERY, null );
        List<Recipe> recipes = new ArrayList<Recipe>();

        while(res.moveToNext()){
            Recipe recipe = new Recipe();
            recipe.setId(res.getInt((res.getColumnIndex(ID))));
            recipe.setRecipeDesc((res.getString(res.getColumnIndex(DESCRIPTION))));
            recipe.setCategory((res.getString(res.getColumnIndex(CATEGORY))));
            recipe.setRecipeName((res.getString(res.getColumnIndex(RECIPE_NAME))));
            recipe.setSubCategoryType((res.getString(res.getColumnIndex(SUBCATEGORY))));
            recipe.setIngreditents((res.getString(res.getColumnIndex(INGREDIENTS))));
            recipe.setProceDure((res.getString(res.getColumnIndex(PROCEDURE))));
            recipe.setReceipeImageUrl((res.getString(res.getColumnIndex(IMG_URL))));
            recipe.setFavourite(Boolean.parseBoolean((res.getString(res.getColumnIndex(IS_FAVOURITE)))));
            recipe.setDuration((res.getString(res.getColumnIndex(DURATION))));
            recipe.setServings((res.getString(res.getColumnIndex(SERVING))));
            recipes.add(recipe);
        }

        DataHolder.allRecipes = recipes;
        return recipes;

    }

    public List<Recipe> getFavouriteRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String SELECT_QUERY = "select * from "+ TABLE_NAME + " where " + IS_FAVOURITE + " = 1" ;
        Cursor res =  db.rawQuery(SELECT_QUERY, null );
        List<Recipe> recipes = new ArrayList<Recipe>();

        while(res.moveToNext()){
            Recipe recipe = new Recipe();
            recipe.setId(res.getInt((res.getColumnIndex(ID))));
            recipe.setRecipeDesc((res.getString(res.getColumnIndex(DESCRIPTION))));
            recipe.setCategory((res.getString(res.getColumnIndex(CATEGORY))));
            recipe.setRecipeName((res.getString(res.getColumnIndex(RECIPE_NAME))));
            recipe.setSubCategoryType((res.getString(res.getColumnIndex(SUBCATEGORY))));
            recipe.setIngreditents((res.getString(res.getColumnIndex(INGREDIENTS))));
            recipe.setProceDure((res.getString(res.getColumnIndex(PROCEDURE))));
            recipe.setReceipeImageUrl((res.getString(res.getColumnIndex(IMG_URL))));
            recipe.setFavourite(Boolean.parseBoolean((res.getString(res.getColumnIndex(IS_FAVOURITE)))));
            recipe.setDuration((res.getString(res.getColumnIndex(DURATION))));
            recipe.setServings((res.getString(res.getColumnIndex(SERVING))));

            recipes.add(recipe);
        }
        DataHolder.favRecipes = recipes;
        return recipes;

    }


    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateRecipe (Integer id, boolean isFavourite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_FAVOURITE, isFavourite);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Recipe getRecipeById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String SELECT_QUERY = "select * from "+ TABLE_NAME + " where " + ID + "= " + id ;
        Cursor res =  db.rawQuery(SELECT_QUERY, null );

        if (res != null)
            res.moveToFirst();


            Recipe recipe = new Recipe();
            recipe.setId(res.getInt((res.getColumnIndex(ID))));
            recipe.setRecipeDesc((res.getString(res.getColumnIndex(DESCRIPTION))));
            recipe.setCategory((res.getString(res.getColumnIndex(CATEGORY))));
            recipe.setRecipeName((res.getString(res.getColumnIndex(RECIPE_NAME))));
            recipe.setSubCategoryType((res.getString(res.getColumnIndex(SUBCATEGORY))));
            recipe.setIngreditents((res.getString(res.getColumnIndex(INGREDIENTS))));
            recipe.setProceDure((res.getString(res.getColumnIndex(PROCEDURE))));
            recipe.setReceipeImageUrl((res.getString(res.getColumnIndex(IMG_URL))));
            recipe.setFavourite(((res.getString(res.getColumnIndex(IS_FAVOURITE))).equals("1")?true:false));
            recipe.setDuration((res.getString(res.getColumnIndex(DURATION))));
            recipe.setServings((res.getString(res.getColumnIndex(SERVING))));



        return recipe;

    }



    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}