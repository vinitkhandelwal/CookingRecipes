package util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.slpf.cooking.HomeFragment;
import com.slpf.cooking.ItemList;
import com.slpf.cooking.MainActivity;
import com.slpf.cooking.R;
import com.slpf.cooking.SubCategoryList;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vin on 6/4/2015.
 */
public class MenuListAdaptor extends RecyclerView.Adapter<MenuListAdaptor.MyViewHolder> {

    String[] result;
    String[] showMenu;
    Context context;
    int[] imageId;
    HashMap<String, HashMap<String, List<Recipe>>> parentMap;
    private static LayoutInflater inflater = null;
    private static final int MENU_ITEM_VIEW_TYPE = 0;

    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
//        public TextView title, count;
        TextView tv;
        ImageView img;
//        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.title);
//            count = (TextView) view.findViewById(R.id.count);
            img = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    public MenuListAdaptor(HomeFragment mainActivity, String[] showMenu, String[] menuNameList, int[] menuImages, HashMap<String, HashMap<String, List<Recipe>>> map) {
        // TODO Auto-generated constructor stub
        result = menuNameList;
        this.showMenu = showMenu;
        context = mainActivity.getContext();
        imageId = menuImages;
        parentMap = map;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:

                break;
            case MENU_ITEM_VIEW_TYPE:
                // fall through
            default:

                holder.tv.setText(showMenu[position].toUpperCase());

                Glide.with(context).load(imageId[position]).into(holder.img);

                holder.img.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = null;
                        if (parentMap.get(result[position]).get("default") == null) {
                            intent = new Intent(context, SubCategoryList.class);
                            if (intent.hasExtra("subCategoryMap") && intent.hasExtra("subCategory")) {
                                intent.removeExtra("subCategoryMap");
                                intent.removeExtra("subCategory");
                            }

                            intent.putExtra("subCategoryMap", new SubCategoryDataWrapper(parentMap.get(result[position])));
                            intent.putExtra("subCategory", showMenu[position]);
                        } else {
                            intent = new Intent(context, ItemList.class);
                            intent.putExtra("subCategoryName", result[position]);
                            intent.putExtra("itemList", new ItemListDataWrapper(parentMap.get(result[position]).get("default")));
                            intent.putExtra("selectPoint", "" + position);
                        }
                        context.startActivity(intent);
                    }
                });

        }
    }



    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getItemCount() {
        return showMenu.length;
    }

    public class Holder {
        TextView tv;
        ImageView img;
    }

//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        // TODO Auto-generated method stub
//        Holder holder = new Holder();
//        View rowView;
//
//        rowView = inflater.inflate(R.layout.menu_list, null);
//        holder.tv = (TextView) rowView.findViewById(R.id.textView1);
//        holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
//
//        holder.tv.setText(showMenu[position].toUpperCase());
//        holder.img.setImageResource(imageId[position]);
//
//        rowView.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
//
//                Intent intent = new Intent(context, SubCategoryList.class);
//                if (intent.hasExtra("subCategoryMap") && intent.hasExtra("subCategory")) {
//                    intent.removeExtra("subCategoryMap");
//                    intent.removeExtra("subCategory");
//                }
///*                String bev = null;
//                if(result[position].equalsIgnoreCase("beverages")){
//                     bev = "bevarges";
//                }
//                else{
//                    bev = result[position];
//                }*/
//                intent.putExtra("subCategoryMap", new SubCategoryDataWrapper(parentMap.get(result[position])));
//                intent.putExtra("subCategory", showMenu[position]);
//                context.startActivity(intent);
//                System.out.println("image clicked...");
//            }
//        });
//
//        return rowView;
//    }
}
