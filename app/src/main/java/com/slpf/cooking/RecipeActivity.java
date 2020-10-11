package com.slpf.cooking;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


import dao.DBHelper;
import util.ImageLoader;
import util.Recipe;
import util.RecipeDataWrapper;


public class RecipeActivity extends Activity {

    public TextView descriptionView;
    public TextView ingredientsView;
    public TextView procedureView;
    public TextView lablel;
    public TextView ingredientsLablel;
    public TextView prepLablel;
    public ImageView recipeImageView;
    public ImageView favourite;
    public ImageView backButton;
    DBHelper db;

    private InterstitialAd mInterstitialAd;


    public Recipe recipe;
    private boolean favFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bridge.saveInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mInterstitialAd = new InterstitialAd(RecipeActivity.this);

        mInterstitialAd.setAdUnitId(getString(R.string.interstial_ad_unit_id));

        ImageLoader imageLoader = new ImageLoader(this.getApplicationContext());
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd.loadAd(adRequest);

        db = new DBHelper(getApplicationContext());
        // Defined in res/values/strings.xml


      /*  mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                displayInterstitial();
            }
        });
*/
        Intent intent = getIntent();
        RecipeDataWrapper recipeDataWrapper = (RecipeDataWrapper) intent.getSerializableExtra("recipe");
        recipe = db.getRecipeById(recipeDataWrapper.getRecipe().getId());

        lablel = (TextView) findViewById(R.id.text_id);
        descriptionView = (TextView) findViewById(R.id.recipedescription);
        ingredientsView = (TextView) findViewById(R.id.ingreditens);
        procedureView = (TextView) findViewById(R.id.procedure);
        recipeImageView = (ImageView) findViewById(R.id.recipeimageView);
        favourite = (ImageView) findViewById(R.id.favourite);
        backButton = (ImageView) findViewById(R.id.back_id);

        String imageName = recipe.getReceipeImageUrl().toLowerCase().replaceAll("[^\\W\\S.]+", "");

        if (recipe.isFavourite()) favourite.setImageResource(R.mipmap.ic_favourite);
        else favourite.setImageResource(R.mipmap.ic_non_favourite);

        String uri = imageName;
        String PACKAGE_NAME = this.getPackageName();
        int imageResource = this.getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + uri, null, null);
        recipeImageView.setImageResource(imageResource);
        recipeImageView.setScaleType(ImageView.ScaleType.FIT_XY);


        String input = recipe.getRecipeName().toLowerCase();
        String output = input.substring(0, 1).toUpperCase() + input.substring(1);
        recipe.setRecipeName(output);
        String description = recipe.getRecipeDesc();
        String ingredients = recipe.getIngreditents();
        String convertedtInmgredients = ingredients;
        String procedure = recipe.getProceDure();
        lablel.setText(output.toCharArray(), 0, output.length());
        if (null != description)
            descriptionView.setText(description.toCharArray(), 0, description.length());
        else
            descriptionView.setText(output.toCharArray(), 0, output.length());
        ingredientsView.setText(convertedtInmgredients.toCharArray(), 0, convertedtInmgredients.length());

        procedureView.setText(procedure.toCharArray(), 0, procedure.length());

        favFlag = recipe.isFavourite();

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();

            }
        });


        favourite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                boolean isFav = toggleFavFlag();
                favourite.setImageResource(isFav ? R.mipmap.ic_favourite : R.mipmap.ic_non_favourite);
                db.updateRecipe(recipe.getId(), isFav);

            }
        });

    }

    private boolean toggleFavFlag() {
        favFlag = !favFlag;
        return favFlag;
    }

    public void displayInterstitial() {
        // If Ads are loaded, show Interstitial else show nothing.
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
