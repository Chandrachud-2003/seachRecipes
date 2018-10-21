package com.chandrachud.malaligowda.ingredient.Adapter;

import android.annotation.SuppressLint;
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
 * Created by malaligowda on 5/7/18.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>{

    private ArrayList<String> mNames;
    private ArrayList<String> mImageUrls;
    private ArrayList<String> mCookTime;
    private ArrayList<String> mRecipeUrls;
    private ArrayList<String> mFavoriteTitle;
    private ArrayList<String> mFavoriteRecipeUrl;
    private ArrayList<String> mFavoriteImageUrl;
    private Context mContext;
    private final String TITLE_KEY = "Title";
    private final String RECIPE_URL_KEY = "Recipe Url";
    private final String IMAGE_URL_KEY = "Image Url";


    public RecipeAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls, ArrayList<String> cookTime, ArrayList<String> recipeUrls, ArrayList<String> favoriteTitle, ArrayList<String> favoriteRecipeUrl, ArrayList<String>favoriteImageUrl) {
        mNames = names;
        mImageUrls = imageUrls;
        mCookTime = cookTime;
        mRecipeUrls = recipeUrls;
        mFavoriteTitle = favoriteTitle;
        mFavoriteRecipeUrl = favoriteRecipeUrl;
        mFavoriteImageUrl = favoriteImageUrl;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_rows, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Log.d("Ingredient", "onBindViewHolder: called");

        RequestOptions requestOptions = new RequestOptions().placeholder(R.color.white);
        YoYo.with(Techniques.Landing)
                .duration(700)
                .repeat(0)
                .playOn(holder.image);
        Glide.with(mContext)
                .load(mImageUrls.get(position))
                .apply(requestOptions)
                .into(holder.image);

        YoYo.with(Techniques.Landing)
                .duration(700)
                .repeat(0)
                .playOn(holder.name);
        holder.name.setText(mNames.get(position));

        YoYo.with(Techniques.Landing)
                .duration(700)
                .repeat(0)
                .playOn(holder.cookTime);
        if (mCookTime.get(position).equals("0")){

            holder.cookTime.setText(R.string.unknownCookTime);
        }
        else {
            holder.cookTime.setText(mCookTime.get(position) + " Min");
        }

        if (mFavoriteTitle.contains(mNames.get(position))){

            YoYo.with(Techniques.Landing)
                    .duration(700)
                    .repeat(0)
                    .playOn(holder.favorite);
            Glide.with(mContext)
                    .load(R.drawable.added_to_favorite)
                    .apply(requestOptions)
                    .into(holder.favorite);


        }

        else {

            YoYo.with(Techniques.Landing)
                    .duration(700)
                    .repeat(0)
                    .playOn(holder.favorite);
            Glide.with(mContext)
                    .load(R.drawable.add_to_favorite)
                    .apply(requestOptions)
                    .into(holder.favorite);

            holder.favorite.setOnClickListener((View v) -> {

                Log.d("Ingredient", "Added to favorites");
                Toast.makeText(mContext, "Recipe added to Favorites", Toast.LENGTH_SHORT).show();

                YoYo.with(Techniques.Landing)
                        .duration(700)
                        .repeat(0)
                        .playOn(holder.favorite);
               holder.favorite.setImageResource(R.drawable.added_to_favorite);



                mFavoriteRecipeUrl.add(mRecipeUrls.get(position));
                mFavoriteTitle.add(mNames.get(position).replace(",", " "));
                mFavoriteImageUrl.add(mImageUrls.get(position));


                SharedPreferences prefs = mContext.getSharedPreferences("PREFS", 0);

                String favTitle = android.text.TextUtils.join(",", mFavoriteTitle);
                String favRecipeUrl = android.text.TextUtils.join(",", mFavoriteRecipeUrl);
                String favImageUrl = android.text.TextUtils.join(",", mFavoriteImageUrl);

                prefs.edit().putString(TITLE_KEY, favTitle).apply();

                prefs.edit().putString(RECIPE_URL_KEY, favRecipeUrl).apply();

                prefs.edit().putString(IMAGE_URL_KEY, favImageUrl).apply();





            });


        }

        if (CheckNetwork.isInternetAvailable(mContext)) {


            holder.image.setOnClickListener(v -> {
                Log.d("Ingredient", "onClick: clicked on: " + mNames.get(position));
                Toast.makeText(mContext, "Loading Page...", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(mRecipeUrls.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);

            });
        }

        else {

            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public int getItemCount() {

        return mImageUrls.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{


        ImageButton image;
        TextView name;
        TextView cookTime;
        ImageButton favorite;
        RecyclerView mRecyclerView;

        ViewHolder(View itemView) {
            super(itemView);

            this.image = itemView.findViewById(R.id.recipe_img);
            this.name = itemView.findViewById(R.id.title_text);
            this.cookTime = itemView.findViewById(R.id.cookTime_text);
            this.favorite = itemView.findViewById(R.id.favoriteButton);
            mRecyclerView = itemView.findViewById(R.id.recyclerView);


        }



        }




}


