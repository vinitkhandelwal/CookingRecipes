package com.slpf.cooking;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

import util.ItemListAdapter;
import util.Recipe;
import util.RecipeDataWrapper;

/**
 * Created by user on 19-04-2017.
 */
public class FavouritesFragment extends Fragment implements ItemListAdapter.OnItemClickListener{

    public List<Recipe> recipeList;
    private int selectPosition;
    EditText inputSearch;
    ItemListAdapter itemListAdapter ;
    Integer[] imgid = {
            R.drawable.arrow

    };
    private InterstitialAd mInterstitialAd;
    private RecyclerView recyclerView;


    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);


        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        inputSearch = (EditText) rootView.findViewById(R.id.inputSearch);
        recipeList = DataHolder.favRecipes;

        itemListAdapter = new ItemListAdapter(this.getActivity(), recipeList,imgid);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.list1);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemListAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(itemListAdapter);


        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                if (count < before) {
                    // We're deleting char so we need to reset the adapter data
                    itemListAdapter.resetData();
                }
                // When user changed the Text
                FavouritesFragment.this.itemListAdapter.getFilter().filter(cs);
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


        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(View view, int position) {
        selectPosition = position;
        itemListAdapter.getItem(position);
        Intent intent = new Intent(getContext(), RecipeActivity.class);
        intent.putExtra("recipe", new RecipeDataWrapper((Recipe) itemListAdapter.getItem(position)));
        intent.putExtra("selectPoint", "" + position);
        startActivity(intent);

    }
}
