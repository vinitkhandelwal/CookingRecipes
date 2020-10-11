package com.slpf.cooking;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.ItemListAdapter;
import util.ItemListDataWrapper;
import util.Recipe;
import util.RecipeDataWrapper;
import util.SubCategoryDataWrapper;
import util.SubCategoryListAdapter;


public class ItemList extends Activity implements ItemListAdapter.OnItemClickListener {

    public static ListView list;
    public TextView label;
    public String subCategoryName;
    public List<Recipe> recipeList;
    private int selectPosition;
    EditText inputSearch;
    ItemListAdapter itemListAdapter ;
    Integer[] imgid = {
            R.drawable.arrow

    };
    private InterstitialAd mInterstitialAd;
    private RecyclerView recyclerView;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bridge.saveInstanceState(this, savedInstanceState);
//         Bridge.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_sub_sub_category_list);

        mInterstitialAd = new InterstitialAd(ItemList.this);

        mInterstitialAd.setAdUnitId(getString(R.string.interstial_ad_unit_id));


        Intent intent = getIntent();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd.loadAd(adRequest);

        backButton = (ImageView) findViewById(R.id.back_id);


        ItemListDataWrapper itemListDataWrapper = (ItemListDataWrapper) intent.getSerializableExtra("itemList");
        recipeList = itemListDataWrapper.getitemList();
        subCategoryName = intent.getStringExtra("subCategoryName");

        label = (TextView) findViewById(R.id.text_id);
        label.setText((subCategoryName.toUpperCase()).toCharArray(), 0, subCategoryName.length());

        inputSearch = (EditText) findViewById(R.id.inputSearch);

        itemListAdapter = new ItemListAdapter(this, recipeList,imgid);

        recyclerView = (RecyclerView) findViewById(R.id.list1);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        itemListAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(itemListAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();

            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                if (count < before) {
                    // We're deleting char so we need to reset the adapter data
                    itemListAdapter.resetData();
                }
                // When user changed the Text
                ItemList.this.itemListAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }

        }   );
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
        getMenuInflater().inflate(R.menu.menu_sub_sub_category_list, menu);
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


    @Override
    public void onItemClick(View view, int position) {
                                            selectPosition = position;
                                            itemListAdapter.getItem(position);
                                            Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
//                                            intent.putExtra("recipe", parent.getAdapter().getItem(position).toString());
                                            intent.putExtra("recipe", new RecipeDataWrapper((Recipe) itemListAdapter.getItem(position)));
                                            intent.putExtra("selectPoint", "" + position);
                                            startActivity(intent);

    }


}
