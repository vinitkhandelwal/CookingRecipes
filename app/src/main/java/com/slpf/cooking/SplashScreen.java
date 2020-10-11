package com.slpf.cooking;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;




import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import dao.DBHelper;

import util.CryptographyFiles;
import util.Recipe;
import util.XMLParser;

/**
 * Created by user on 16-05-2017.
 */
public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    DBHelper db;
    List<Recipe> recipeList = new ArrayList<Recipe>();
    HashMap<String,HashMap<String,List<Recipe>>> parentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        db = new DBHelper(getApplicationContext());
//        new PrefetchData().execute();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
//                i.putExtra("parentMap", parentMap);

                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if(db.numberOfRows() == 0) {
                listFileAndparsing();
                db.insertAllRecipe(recipeList);
                recipeList = db.getALLRecipes();
            }
            else{
                recipeList = db.getALLRecipes();
            }

//            listFileAndparsing();
//            db.insertAllRecipe(recipeList);

            if(parentMap.isEmpty())
                populateParentMap();

            return null;
        }

        public void listFileAndparsing(){
            AssetManager assetManager = getBaseContext().getAssets();

            try {
                String[] files = assetManager.list("");
                parsingFunction(assetManager,files[0]);
                parsingFunction(assetManager,files[1]);
                parsingFunction(assetManager,files[2]);
                parsingFunction(assetManager,files[3]);
                parsingFunction(assetManager,files[4]);
                parsingFunction(assetManager, files[5]);
                parsingFunction(assetManager, files[6]);
                parsingFunction(assetManager, files[7]);
                parsingFunction(assetManager, files[8]);
                parsingFunction(assetManager, files[9]);
                parsingFunction(assetManager, files[10]);
                parsingFunction(assetManager, files[11]);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void  parsingFunction(AssetManager assetManager,String fileName){
            InputSource is = null;
            StringBuilder sb = null;
            CryptographyFiles cry = new CryptographyFiles();
            try {
                sb =  cry.deCipherFile(assetManager.open(fileName));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                BufferedReader br=new BufferedReader(new StringReader(sb.toString()));
                is=new InputSource(br);
                XMLParser parser = new XMLParser();
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser sp = factory.newSAXParser();
                XMLReader reader = sp.getXMLReader();
                reader.setContentHandler(parser);
                reader.parse((is));
                if(null ==recipeList)
                    recipeList = parser.list;
                else{
                    recipeList.addAll(parser.list);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void populateParentMap() {

            for (Recipe recipe : recipeList) {
                if (parentMap.containsKey(recipe.getCategory())) {
                    HashMap<String, List<Recipe>> tempMap = parentMap.get(recipe.getCategory());
                    if (tempMap.containsKey(recipe.getSubCategoryType())) {
                        tempMap.get(recipe.getSubCategoryType()).add(recipe);
                    } else {
                        List<Recipe> recipes = new ArrayList<Recipe>();
                        recipes.add(recipe);
                        tempMap.put(recipe.getSubCategoryType(), recipes);
                    }
                    parentMap.put(recipe.getCategory(), tempMap);
                } else {
                    HashMap<String, List<Recipe>> tempMap = new HashMap<>();
                    List<Recipe> recipes = new ArrayList<Recipe>();
                    recipes.add(recipe);
                    tempMap.put(recipe.getSubCategoryType(), recipes);
                    parentMap.put(recipe.getCategory(), tempMap);
                }

            }
        }


            @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // After completing http call
            // will close this activity and lauch main activity
//            Intent i = new Intent(SplashScreen.this, MainActivity.class);
//            i.putExtra("parentMap", parentMap);
//            startActivity(i);
//
//            // close this activity
            finish();
        }

    }

}
