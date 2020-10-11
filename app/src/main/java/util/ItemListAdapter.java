package util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.browse.MediaBrowser;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.slpf.cooking.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vin on 5/21/2015.
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.MyViewHolder> {


    List<Recipe> recipeResult = new ArrayList<Recipe>();
    List<Recipe> origRecipeResult;
    Context context;
    private final Integer[] imageId;
    private Filter recipeFilter;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    private LruCache<String, Bitmap> mMemoryCache;
    OnItemClickListener mItemClickListener;


    public ItemListAdapter(Activity context, List<Recipe> recipeList, Integer[] imgid) {
        // TODO Auto-generated constructor stub
//        super(context, R.layout.recipe_list, recipeList);
        this.context = context;
        recipeResult = recipeList;
        this.imageId = imgid;
        origRecipeResult = recipeList;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in bytes rather than number
                // of items.
                return bitmap.getByteCount();
            }

        };


    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv;
        ImageView img;
        ImageView recipeImage;

        TextView duration;
        TextView servings;

        public MyViewHolder(View view) {
            super(view);

            tv = (TextView) view.findViewById(R.id.item);
            img = (ImageView) view.findViewById(R.id.icon);
            duration = (TextView) view.findViewById(R.id.duration);
            servings = (TextView) view.findViewById(R.id.servings);
            recipeImage = (ImageView) view.findViewById(R.id.recipeimage);


            recipeImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(final View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }


    @Override
    public ItemListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        String PACKAGE_NAME = context.getPackageName();
        if (recipeResult.get(position).getReceipeImageUrl() == null)
            System.out.println("when image url is null " + recipeResult.get(position));
        String imageName = recipeResult.get(position).getReceipeImageUrl().toLowerCase().replaceAll("[^\\W\\S.]+", "");
        String uri = imageName;
        int imageResource = context.getResources().getIdentifier(PACKAGE_NAME + ":drawable/" + uri, null, null);
        loadBitmap(imageResource, holder.recipeImage);


        Recipe recipe = recipeResult.get(position);
        String input = recipe.getRecipeName().toLowerCase();
        String output = input.substring(0, 1).toUpperCase() + input.substring(1);
        holder.tv.setText(output);
        holder.duration.setText("Duration :" + recipe.getDuration());
        holder.servings.setText("Servings :" + recipe.getServings());


    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if(recipeResult != null)
        return recipeResult.size();
        else{
            return 0;
        }
    }


    public Filter getFilter() {
        if (recipeFilter == null)
            recipeFilter = new RecipeFilter();

        return recipeFilter;
    }

    public void resetData() {
        recipeResult = origRecipeResult;
    }

    private class RecipeFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origRecipeResult;
                results.count = origRecipeResult.size();
//                results = null;
            } else {
                // We perform filtering operation
                List<Recipe> nSongList = new ArrayList<Recipe>();

                for (Recipe p : recipeResult) {
                    if (p.getRecipeName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        nSongList.add(p);
                   /* if (p.getAlbum().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        nSongList.add(p);*/
                }

                results.values = nSongList;
                results.count = nSongList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
//            System.out.println("result +count " +results.count);

            // Now we have to inform the adapter about the new list filtered
            if (results == null || results.count == 0) {
//                origPlanetList.clear();
//                origPlanetList.addAll(tempList);
                notifyDataSetChanged();
            } else {
                recipeResult = (List<Recipe>) results.values;
                notifyDataSetChanged();
            }

        }
    }

    public void loadBitmap(int resId, ImageView imageView) {
        if (cancelPotentialWork(resId, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
//            imageView.setBackgroundResource(R.drawable.logo);
            task.execute(resId);
        }
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(
                    bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    public static boolean cancelPotentialWork(int data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final int bitmapData = bitmapWorkerTask.data;
            if (bitmapData != data) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was
        // cancelled
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return (Bitmap) mMemoryCache.get(key);
    }

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        public int data = 0;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage
            // collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            final Bitmap bitmap;
            if (data == 0) {
                bitmap = decodeSampledBitmapFromResource(
                        context.getResources(), R.drawable.logo, 100, 100);
            } else {
                bitmap = decodeSampledBitmapFromResource(
                        context.getResources(), data, 100, 100);
            }

            addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
            return bitmap;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    public Recipe getItem(int position) {
        return recipeResult.get(position);
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}


