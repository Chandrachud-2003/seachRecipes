package com.chandrachud.malaligowda.ingredient.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.chandrachud.malaligowda.ingredient.R;
import com.chandrachud.malaligowda.ingredient.Services.CheckNetwork;
import java.util.ArrayList;


/**
 * Created by malaligowda on 5/8/18.
 */

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder> {


    private ArrayList<String> mFavoriteTitle;
    private ArrayList<String> mFavoriteRecipeUrl;
    private ArrayList<String> mFavoriteImageUrl;
    private Context mContext;



    private final String TITLE_KEY = "Title";
    private final String RECIPE_URL_KEY = "Recipe Url";
    private final String IMAGE_URL_KEY = "Image Url";






    public FavoriteListAdapter(Context context, ArrayList<String> favoriteTitle, ArrayList<String> favoriteRecipeUrl, ArrayList<String> favoriteImageUrl){

        mFavoriteTitle = favoriteTitle;
        mFavoriteRecipeUrl = favoriteRecipeUrl;
        mFavoriteImageUrl = favoriteImageUrl;
        mContext = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_rows, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            Log.d("Ingredient", "onBindViewHolder: called");



            final RequestOptions requestOptions = new RequestOptions().placeholder(R.color.white);
        YoYo.with(Techniques.Landing)
                .duration(700)
                .repeat(0)
                .playOn(holder.image);

        Glide.with(mContext)
                        .load(mFavoriteImageUrl.get(position))
                        .apply(requestOptions)
                        .into(holder.image);
        YoYo.with(Techniques.Landing)
                .duration(700)
                .repeat(0)
                .playOn(holder.name);
            holder.name.setText(mFavoriteTitle.get(position));


        if (CheckNetwork.isInternetAvailable(mContext)) {


            holder.image.setOnClickListener(v -> {
                Log.d("Ingredient", "onClick: clicked on: " + mFavoriteTitle.get(position));
                Toast.makeText(mContext, "Loading Page...", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(mFavoriteRecipeUrl.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);



            });
        }

        else {

            Toast.makeText(mContext, "There is no Internet Connection", Toast.LENGTH_LONG).show();

        }


        YoYo.with(Techniques.Landing)
                .duration(700)
                .repeat(0)
                .playOn(holder.delete);

        holder.delete.setOnClickListener(view -> removeItem(holder.getAdapterPosition()));

            holder.delete.setOnClickListener(v -> {

                    final AlertDialog.Builder Alert = new AlertDialog.Builder(mContext);

                    Alert.setTitle("Delete Recipe");
                    Alert.setCancelable(false);
                    Alert.setMessage("Are you sure you want to delete this recipe?");
                    Alert.setPositiveButton("Yes", (dialog, which) -> {


                        try {



                            int positionDelete = holder.getAdapterPosition();
                            removeItem(positionDelete);
                            SharedPreferences prefs = mContext.getSharedPreferences("PREFS", 0);




                            String favTitle = android.text.TextUtils.join(",", mFavoriteTitle);
                            String favRecipeUrl = android.text.TextUtils.join(",", mFavoriteRecipeUrl);
                            String favImageUrl = android.text.TextUtils.join(",", mFavoriteImageUrl);

                            prefs.edit().putString(TITLE_KEY, favTitle).apply();

                            prefs.edit().putString(RECIPE_URL_KEY, favRecipeUrl).apply();

                            prefs.edit().putString(IMAGE_URL_KEY, favImageUrl).apply();

                            notifyItemRemoved(positionDelete);
                            notifyDataSetChanged();

                            if (getItemCount() == 0){

                                Toast.makeText(mContext, "All Saved Recipes Removed", Toast.LENGTH_SHORT).show();

                            }




                        }

                        catch (ArrayIndexOutOfBoundsException e){e.printStackTrace();}


                    });
                    Alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                    Alert.show();


                });
            }






        @Override
        public int getItemCount() {
            return mFavoriteTitle.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            ImageButton image;
            TextView name;
            ImageButton delete;
            RecyclerView recycler;



            ViewHolder(View itemView){
                super(itemView);

                this.image = itemView.findViewById(R.id.favorite_recipe_img);
                this.name = itemView.findViewById(R.id.favorite_title_text);
                this.delete = itemView.findViewById(R.id.deleteButton);
                this.recycler = itemView.findViewById(R.id.recyclerView);


            }

    }

    private void removeItem(int position){

        mFavoriteImageUrl.remove(position);
        mFavoriteTitle.remove(position);
        mFavoriteRecipeUrl.remove(position);



    }





}
