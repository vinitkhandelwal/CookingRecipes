package util;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.slpf.cooking.MainActivity;
import com.slpf.cooking.R;

import java.util.List;

/**
 * Created by vin on 5/20/2015.
 */
public class SubCategoryListAdapter extends RecyclerView.Adapter<SubCategoryListAdapter.MyViewHolder> {


    List<String> result;
    Context context;
    private final Integer[] imageId;
    private static LayoutInflater inflater = null;

    OnItemClickListener mItemClickListener;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv;
        ImageView img;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.item);
            img = (ImageView) view.findViewById(R.id.icon);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(final View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    public SubCategoryListAdapter(Activity context, List<String> prgmNameList, Integer[] imgid) {
        // TODO Auto-generated constructor stub

        this.context = context;
        this.imageId = imgid;
        result = prgmNameList;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sub_category_list, parent, false);

            return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(result.get(position).toUpperCase());

        Glide.with(context).load(imageId[0]).into(holder.img);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public String getItem(int position) {
        return result.get(position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
