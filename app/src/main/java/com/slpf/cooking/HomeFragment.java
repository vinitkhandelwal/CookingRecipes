package com.slpf.cooking;

/**
 * Created by user on 19-04-2017.
 */
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import java.util.HashMap;
import java.util.List;

import util.MenuListAdaptor;
import util.Recipe;


public class HomeFragment extends Fragment {

    MenuListAdaptor menuListAdapter;

    private RecyclerView recyclerView;

    HashMap<String,HashMap<String,List<Recipe>>> parentMap;

    public static String [] menuNameList={"bevarges","Main Course","Italian","Cake","Salad","Sandwich","Kid's Recipes","Chutney"};
    public static String [] showMenuNameList={"beverages","Main Course","Italian","Cake","Salad","Sandwich","Kid's Recipes","Chutney"};
    public static int [] menuImage={R.drawable.beverages1,R.drawable.maincourse,R.drawable.italian,
            R.drawable.cake,R.drawable.salad,R.drawable.sandwich,R.drawable.kids_snacks,R.drawable.chutney_home};


    public HomeFragment() {
        // Required empty public constructor

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

         AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//


//
        parentMap = (HashMap<String, HashMap<String, List<Recipe>>>) DataHolder.parentMap;

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        menuListAdapter = new MenuListAdaptor(this,showMenuNameList, menuNameList,menuImage,parentMap);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(menuListAdapter);



        // Inflate the layout for this fragment
        return rootView;
    }

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public HashMap<String, HashMap<String, List<Recipe>>> getParentMap() {
        return parentMap;
    }

    public void setParentMap(HashMap<String, HashMap<String, List<Recipe>>> parentMap) {
        this.parentMap = parentMap;
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}