package com.slpf.cooking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import util.ItemListDataWrapper;
import util.Recipe;
import util.SubCategoryDataWrapper;
import util.SubCategoryListAdapter;


public class SubCategoryList extends Activity implements SubCategoryListAdapter.OnItemClickListener {

    public static ListView list;
    public TextView label;
    public HashMap<String, List<Recipe>> subCategoryMap;
    public  List<String> subCategoryList =null;
    public String subCategory;
    private int selectPosition;
    Integer[] imgid = {
            R.drawable.arrow

    };
    private RecyclerView recyclerView;
    SubCategoryListAdapter subCategoryListAdapter;
    ImageView backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        
        super.onCreate(savedInstanceState);
//        Bridge.restoreInstanceState(this, savedInstanceState);

        setContentView(R.layout.activity_subcategory_list);
        Intent intet = getIntent();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        SubCategoryDataWrapper subCategoryDataWrapper = (SubCategoryDataWrapper) intet.getSerializableExtra("subCategoryMap");
        subCategoryMap = subCategoryDataWrapper.getsubCategoryMap();
        subCategory = intet.getStringExtra("subCategory");
        if (null == subCategoryList)
            populateSubCategoryList(subCategoryMap);

        label = (TextView) findViewById(R.id.text_id);
        backButton = (ImageView) findViewById(R.id.back_id);
        label.setText((subCategory.toUpperCase()).toCharArray(), 0, subCategory.length());

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();

            }
        });


        subCategoryListAdapter = new SubCategoryListAdapter(this, subCategoryList,imgid);
        subCategoryListAdapter.setOnItemClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.sub_category_Recycler);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(subCategoryListAdapter);



    }

    public void populateSubCategoryList(HashMap<String, List<Recipe>> map) {
        Set<String> subCategories = map.keySet();
        subCategoryList = new ArrayList<>();
        int i = 0;
        for (String subCategory : subCategories)
            subCategoryList.add(subCategory);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subcategory_list, menu);
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
        Intent intent = new Intent(getApplicationContext(), ItemList.class);
        intent.putExtra("subCategoryName", subCategoryListAdapter.getItem(position).toString());
        intent.putExtra("itemList", new ItemListDataWrapper(subCategoryMap.get(subCategoryListAdapter.getItem(position))));
        intent.putExtra("selectPoint", "" + position);
        startActivity(intent);
    }
}
