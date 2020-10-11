package com.slpf.cooking;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import dao.DBHelper;
import util.CryptographyFiles;
import util.MenuListAdaptor;
import util.PopulateData;
import util.Recipe;

import util.XMLParser;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    List<Recipe> recipeList = new ArrayList<Recipe>();
    HashMap<String, HashMap<String, List<Recipe>>> parentMap = new HashMap<>();
    ProgressDialog pd;
    ImageView splash;
    MenuListAdaptor menuListAdapter;

    DBHelper db;

    private RecyclerView recyclerView;


    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWindow().setBackgroundDrawable(null);
        db = new DBHelper(getApplicationContext());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(mToolbar);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        this.pd = ProgressDialog.show(this, "Loading Recipes", "", true, false);

        new DownloadTask().execute();
//        displayView(0);


    }


    private void displayView(final int position) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                Fragment fragment = null;
                String title = getString(R.string.app_name);
                switch (position) {
                    case 0:
                        fragment = new HomeFragment();
                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("parentMap", parentMap);
//                        Bridge.saveInstanceState(this, bundle);
                        fragment.setArguments(bundle);
                        fragment.setRetainInstance(true);
                        title = getString(R.string.app_name);
                        break;
                    case 1:
                        fragment = new AllRecipeFragment();
                        title = getString(R.string.title_allrecipes);
                        Bundle allRecipe = new Bundle();
//                        Bridge.saveInstanceState(this, allRecipe);
                        fragment.setRetainInstance(true);
//                        allRecipe.putSerializable("allRecipe", (Serializable) db.getALLRecipes());
                        fragment.setArguments(allRecipe);
                        break;
                    case 2:
                        fragment = new FavouritesFragment();
                        title = getString(R.string.title_favourites);
                        Bundle favRecipes = new Bundle();
//                        Bridge.saveInstanceState(this, favRecipes);
                        fragment.setRetainInstance(true);
//                        favRecipes.putSerializable("favRecipes", (Serializable) db.getFavouriteRecipes());
                        DataHolder.favRecipes = db.getFavouriteRecipes();
                        fragment.setArguments(favRecipes);
                        break;
                    default:
                        break;
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commitAllowingStateLoss();

                    // set the toolbar title
                    getSupportActionBar().setTitle(title);
                }

            }
        });

    }

//    @Override
//    public Parcelable saveState() {
//        Bundle bundle = (Bundle) super.saveState();
//        bundle.putParcelableArray("states", null); // Never maintain any states from the base class, just null it out
//        return bundle;
//    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private class DownloadTask extends AsyncTask<String, Void, Object> {
        protected String doInBackground(String... args) {


            if (db.numberOfRows() == 0) {
                listFileAndparsing();
                db.insertAllRecipe(recipeList);
                recipeList = db.getALLRecipes();
            } else {
                recipeList = db.getALLRecipes();
            }

//            listFileAndparsing();vgggggggvvvvfvc
//            db.insertAllRecipe(recipeList);


            if (parentMap.isEmpty())
                populateParentMap();

//            displayView(0);

            return "done";
        }

        protected void onPostExecute(Object result) {
            // Pass the result data back to the main activity


            if (MainActivity.this.pd != null) {
                MainActivity.this.pd.dismiss();
                displayView(0);
//            }
            }
        }

        public void listFileAndparsing() {
            AssetManager assetManager = getBaseContext().getAssets();

            try {
                String[] files = assetManager.list("");
                parsingFunction(assetManager, files[0]);
                parsingFunction(assetManager, files[1]);
                parsingFunction(assetManager, files[2]);
                parsingFunction(assetManager, files[3]);
                parsingFunction(assetManager, files[4]);
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

        public void parsingFunction(AssetManager assetManager, String fileName) {
            InputSource is = null;
            StringBuilder sb = null;
            CryptographyFiles cry = new CryptographyFiles();
            try {
                sb = cry.deCipherFile(assetManager.open(fileName));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                BufferedReader br = new BufferedReader(new StringReader(sb.toString()));
                is = new InputSource(br);
                XMLParser parser = new XMLParser();
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser sp = factory.newSAXParser();
                XMLReader reader = sp.getXMLReader();
                reader.setContentHandler(parser);
                reader.parse((is));
                if (null == recipeList)
                    recipeList = parser.list;
                else {
                    recipeList.addAll(parser.list);
                }
            } catch (Exception e) {
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

            DataHolder.parentMap = parentMap;




        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
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

   /* private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
*/

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}



