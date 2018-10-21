package com.chandrachud.malaligowda.ingredient.Search;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.chandrachud.malaligowda.ingredient.Adapter.FavoriteListAdapter;
import com.chandrachud.malaligowda.ingredient.Adapter.RecipeAdapter;
import com.chandrachud.malaligowda.ingredient.R;
import com.chandrachud.malaligowda.ingredient.Services.CheckNetwork;
import com.chandrachud.malaligowda.ingredient.utils.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import cz.msebera.android.httpclient.Header;
import hotchemi.android.rate.AppRate;


public class Search extends AppCompatActivity {

    final int REQUEST_CODE = 123;

    final String TITLE_KEY = "Title";
    final String RECIPE_URL_KEY = "Recipe Url";
    final String IMAGE_URL_KEY = "Image Url";
    final String ON_SAVE_TITLE = "saveTitle";
    final String ON_SAVE_RECIPEURL = "saveRecipeUrl";
    final String ON_SAVE_IMAGEURL = "saveImageUrl";
    final String ON_SAVE_COOKTIME = "saveSocialRank";
    final String ON_SAVE_SEARCH = "saveSearch";
    final String ON_SAVE_FAVORITE_TITLE = "saveFavoriteTitle";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;


    private MultiAutoCompleteTextView searchView;
    private ImageButton searchButton;
    private Button favoriteMenu;
    private ImageButton aboutButton;
    private TextView aboutText;
    private RecyclerView mRecyclerView;
    private ImageButton voiceButton;
    private TextView appName;

    private Spinner sortDiet;


    private String mSearchText;
    private String health = "";

    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> sourceUrl = new ArrayList<>();
    private ArrayList<String> imageUrl = new ArrayList<>();
    private ArrayList<String> cookTime = new ArrayList<>();
    private ArrayList<String> favoriteTitle = new ArrayList<>();
    private ArrayList<String> favoriteImageUrl = new ArrayList<>();
    private ArrayList<String> favoriteRecipeUrl = new ArrayList<>();
    private ArrayAdapter<CharSequence> adapter;
    private boolean restart = false;
    private int range;


    private static final int NUM_COLUMNS = 2;


    String[] ingredientList = {

            "Whole wheat flour",
            "Bread flour",
            "Cake flour",
            "Pastry flour",
            "Self-rising flour",
            "Cornmeal",
            "Cornstarch",
            "Baking soda",
            "Baking powder",
            "Yeast",
            "Superfine sugar",
            "castor sugar",
            "Honey",
            "Molasses",
            "Cane syrup",
            "Maple syrup",
            "Agave syrup",
            "Butter",
            "eEggs",
            "Buttermilk",
            "Cream cheese",
            "Vegetable oil",
            "Shortening",
            "vanilla extract",
            "Almond extract",
            "Lemon extract",
            "Mint extract",
            "Rum flavoring",
            "Brandy flavoring",
            "Whole vanilla bean",
            "Cinnamon",
            "Cloves",
            "Allspice",
            "Ginger",
            "Chocolate",
            "Raisins",
            "Cranberries",
            "Oats",
            "Nuts",
            "Jams",
            "Jellies",
            "Food coloring",
            "Sprinkles",
            "Crystallized ginger",
            "Matcha powder",
            "Chia seeds",
            "Coconut",
            "Bacon",
            "Beef fat",
            "Chicken fat",
            "Cocoa butter",
            "coconut oil",
            "Lard",
            "Palm",
            "Palm kernel oil",
            "Suet",
            "Tallow",
            "Hard margarine",
            "Margarine",
            "Brine",
            "Soy sauce",
            "Brown sugar",
            "Cheddar",
            "Parmesan",
            "Yogurt",
            "Whipped cream",
            "Mozzarella",
            "Sour cream",
            "Condensed milk",
            "Cottage cheese",
            "Swiss cheese",
            "Ice cream",
            "Blue cheese",
            "Italian cheese",
            "Colby cheese",
            "Goat cheese",
            "Velveeta",
            "Pizza cheese",
            "Ghee",
            "Custard",
            "Soft cheese",
            "Goat milk",
            "Onion",
            "Garlic",
            "Tomato",
            "Potato",
            "Carrot",
            "Bell pepper",
            "Basil",
            "Parsley",
            "Broccoli",
            "Corn",
            "Spinach",
            "Mushroom",
            "Green beans",
            "Chili pepper",
            "Celery",
            "Rosemary",
            "Cucumber",
            "Red onion",
            "Sweet potato",
            "Pickle",
            "Avocado",
            "Zucchini",
            "Cilantro",
            "Olive",
            "Asparagus",
            "Cabbage",
            "Cauliflower",
            "Pumpkin",
            "Squash",
            "Eggplant",
            "Beet",
            "Horseradish",
            "Radish",
            "Leek",
            "Sweet pepper",
            "Capsicum",
            "Yam",
            "Turnip",
            "Parsnip",
            "Snow peas",
            "Bean sprouts",
            "Seaweed",
            "Canned tomato",
            "Chinese broccoli",
            "Sweet corn",
            "Lemon",
            "Apple",
            "Banana",
            "Lime",
            "Strawberries",
            "Orange",
            "Pineapple",
            "Blueberries",
            "Grape",
            "Peach",
            "Raspberries",
            "Mango",
            "Pear",
            "Blackberries",
            "Cherry",
            "Date",
            "Watermelon",
            "Kiwi",
            "Grapefruit",
            "Mandarin",
            "Cantaloupe",
            "Plum",
            "Clementine",
            "Prunes",
            "Pomegranate",
            "Fig",
            "Papaya",
            "Guava",
            "Passion fruit",
            "Lychee",
            "Star fruit",
            "Rice",
            "Pasta",
            "Noodle",
            "Tortillas",
            "Pancake",
            "Brown rice",
            "Popcorn",
            "Macaroni",
            "Macaroni cheese",
            "Ramen",
            "Cereal",
            "Biscuits",
            "Croutons",
            "Coconut flake",
            "Pizza dough",
            "Bagel",
            "Lasagna",
            "Barley",
            "Muffin",
            "Wheat",
            "Pita",
            "Ravioli",
            "Baguette",
            "Gnocchi",
            "Vermicelli",
            "Yeast flake",
            "Rye",
            "Pierogi",
            "Breadsticks",
            "Potato starch",
            "Tapioca starch",
            "Wafer",
            "Bran",
            "Malt extract",
            "Oregano",
            "Chili powder",
            "Nutmeg",
            "Bay leaf",
            "Curry",
            "Clove",
            "Turmeric",
            "Chive",
            "Coriander",
            "Mustard seed",
            "Italian spice",
            "Chinese five spice",
            "Saffron",
            "Chipotle",
            "Chili paste",
            "Tamarind",
            "Lavender",
            "Chicken breast",
            "Ground beef",
            "Sausage",
            "Beef steak",
            "Ham",
            "Hot dog",
            "Ground turkey",
            "Pork",
            "Pepperoni",
            "Chicken leg",
            "Ground pork",
            "Chicken wings",
            "Salami",
            "Ground chicken",
            "Pork ribs",
            "Lamb",
            "Lamb chops",
            "Chicken thighs",
            "Ground Lamb",
            "Beef ribs",
            "Duck",
            "Pork belly",
            "Veal",
            "Lamb shoulder",
            "Elk",
            "Beef suet",
            "Chicken tenders",
            "Lamb liver",
            "Turkey liver",
            "Pork liver",
            "Canned tuna",
            "Salmon",
            "Canned salmon",
            "Smoked salmon",
            "Sardines",
            "Anchovies",
            "Whitefish",
            "Swordfish",
            "Catfish",
            "Tuna",
            "Caviar",
            "Shrimp",
            "Crab",
            "Prawns",
            "Scallop",
            "Clam",
            "Lobster",
            "Mussel",
            "Oyster",
            "Squid",
            "Crawfish",
            "Crayfish",
            "Octopus",
            "Conch",
            "Mayonnaise",
            "Ketchup",
            "Mustard",
            "Vinegar",
            "Hot sauce",
            "Barbecue sauce",
            "Salad dressing",
            "Tabasco",
            "Fish sauce",
            "Honey mustard",
            "Taco sauce",
            "Blue cheese dressing",
            "Sweet and sour sauce",
            "Thousand island",
            "French dressing",
            "Sesame dressing",
            "Olive oil",
            "Peanut oil",
            "Sunflower oil",
            "Corn oil",
            "Almond oil",
            "Safflower oil",
            "Palm oil",
            "Fish stock",
            "Wasabi",
            "Rose water",
            "Mustard powder",
            "Tomato sauce",
            "Tomato paste",
            "Salsa",
            "Pesto",
            "Curry paste",
            "Black beans",
            "Lentil",
            "Hummus",
            "Kidney beans",
            "Lima beans",
            "Red beans",
            "French beans",
            "Beer",
            "Red wine",
            "White wine",
            "Gin",
            "Tomato soup",
            "Vegetable soup",
            "Peanut butter",
            "Peanut",
            "Cashew",
            "Pistachio",
            "Chestnut",
            "Marshmallow",
            "Nutella",
            "Chocolate syrup",
            "Dark chocolate",
            "Chocolate chips",
            "Brownie",
            "Jello",
            "Candy",
            "Caramel",
            "Cookies",
            "Oreo",
            "Butterscotch",
            "Doritos",
            "Cookie dough",
            "Plum jam",
            "Blueberry jam",
            "Doughnut",
            "Black pudding",
            "Apple jelly",
            "Cookie crumb",
            "Coffee",
            "Tea",
            "Pizza",
            "Jalapeño",
            "Artichoke",
            "Tortilla",
            "Sushi",
            "Dumplings",
            "Cocoa",
            "Quinoa",
            "Mexican cheese",
            "Cloves",
            "Cloves garlic",
            "Paprika",
            "Parsley",
            "Red Pepper Sauce",
            "Green pepper",
            "Black olives",
            "Red pepper flakes",
            "Heavy cream",
            "Yellow Squash",
            "All purpose Flour",
            "Paprika",
            "Thyme",
            "Cumin",
            "Red Bell Pepper",
            "Green chilies",
            "Masa",
            "Cornmeal",
            "Pico De Gallo",
            "Macaroni",
            "Pickle juice",
            "Fennel seeds",
            "Ricotta cheese",
            "Sweet pickle",
            "American cheese",
            "Hamburger buns",
            "Buns",
            "Yellow mustard",
            "Sesame seeds",
            "Oregano",
            "Sugar",
            "Jaggery",


    };


    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_search);

        if (savedInstanceState != null) {

            title = savedInstanceState.getStringArrayList(ON_SAVE_TITLE);
            cookTime = savedInstanceState.getStringArrayList(ON_SAVE_COOKTIME);
            imageUrl = savedInstanceState.getStringArrayList(ON_SAVE_IMAGEURL);
            sourceUrl = savedInstanceState.getStringArrayList(ON_SAVE_RECIPEURL);
            mSearchText = savedInstanceState.getString(ON_SAVE_SEARCH);
            favoriteTitle = savedInstanceState.getStringArrayList(ON_SAVE_FAVORITE_TITLE);
            searchView = findViewById(R.id.searchView);

            searchView.setText(mSearchText);
            getRecipes();
            closeKeyboard();


        }


        if (CheckNetwork.isInternetAvailable(Search.this)) //returns true if internet available
        {

            Toast.makeText(Search.this, "Connected to internet", Toast.LENGTH_LONG).show();
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } else {

            final AlertDialog.Builder Alert = new AlertDialog.Builder(this);
            Alert.setTitle("Attention!");
            Alert.setCancelable(true);
            Alert.setMessage("There is no active internet connection. Do you wish to proceed?");
            Alert.setPositiveButton("Yes", (dialog, which) -> dialog.dismiss());

            Alert.setNegativeButton("No", (dialog, which) -> {

                dialog.dismiss();
                finish();

            });

            Alert.show();

        }



        AppRate.with(this)
                .setTitle("Rate This App")
                .setMessage("If you enjoyed this app, would you mind taking a moment to rate it? It won't take more than a minute. Thanks for your support!")
                .setInstallDays(0)
                .setLaunchTimes(2)
                .setRemindInterval(1)
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);









        mRecyclerView = findViewById(R.id.recyclerView);
        aboutText = findViewById(R.id.aboutText);
        aboutText.setText(R.string.about_description);
        aboutText.setVisibility(View.INVISIBLE);
        appName = findViewById(R.id.appName);
        appName.setVisibility(View.VISIBLE);
        appName.setText(R.string.appName);
        favoriteMenu = findViewById(R.id.FavoriteRecipes);
        voiceButton = findViewById(R.id.voiceRecorder);
        searchView = findViewById(R.id.searchView);
        int lineheight = searchView.getLineHeight();
        Log.d("Ingredient", "Line Height: "+lineheight);
        searchView.setDropDownBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        searchButton = findViewById(R.id.searchButton);
        aboutButton = findViewById(R.id.aboutButton);
        sortDiet = findViewById(R.id.sortSpinner);

        adapter = ArrayAdapter.createFromResource(this, R.array.Diet, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortDiet.setAdapter(adapter);

        sortDiet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0: {
                        health = "balanced";
                        Toast.makeText(getBaseContext(), "Diet Set to Balanced", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 1: {
                        health = "balanced";
                        Toast.makeText(getBaseContext(), "Diet Set to Balanced", Toast.LENGTH_SHORT).show();

                        break;
                    }
                    case 2: {
                        health = "high-protein";
                        Toast.makeText(getBaseContext(), "Diet Set to High-Protein", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 3: {
                        health = "high-fiber";
                        Toast.makeText(getBaseContext(), "Diet Set to High-Fiber", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 4: {
                        health = "low-fat";
                        Toast.makeText(getBaseContext(), "Diet Set to Low-Fat", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case 5: {
                        health = "low-carb";
                        Toast.makeText(getBaseContext(), "Diet Set to Low-Carb", Toast.LENGTH_SHORT).show();

                        break;
                    }
                    case 6: {
                        health = "low-sodium";
                        Toast.makeText(getBaseContext(), "Diet Set to Low-Sodium", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    default: {
                        health = "";
                    }


                }




                mSearchText = searchView.getText().toString();
                if (mSearchText.isEmpty()) {
                    Log.d("Ingredient", "No ingredients entered");

                } else {
                    restart = true;
                    closeKeyboard();
                    getRecipes();
                    initRecyclerViewSearch();

                }


            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter searchAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, ingredientList);

        searchView.setThreshold(2);
        searchView.setAdapter(searchAdapter);

        searchView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        searchView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d("Ingredient", "You have selected " + parent.getItemAtPosition(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Log.d("Ingredient", "No ingredient selected");


            }
        });


        searchButton.setOnClickListener(v -> {

            YoYo.with(Techniques.FadeOut)
                    .duration(700)
                    .repeat(0)
                    .playOn(aboutText);

            mSearchText = searchView.getText().toString();
            Log.d("Ingredient", mSearchText);
            Toast.makeText(getBaseContext(), "Loading Recipes", Toast.LENGTH_SHORT).show();

            getRecipes();
            closeKeyboard();


        });

        favoriteMenu.setOnClickListener(v -> {

            YoYo.with(Techniques.FadeOut)
                    .duration(700)
                    .repeat(0)
                    .playOn(aboutText);


            try {
                initRecyclerViewFavorite();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        });


        aboutButton.setOnClickListener(v -> {

            Log.d("Ingredient", "aboutButton:  clicked");
            mRecyclerView.setVisibility(View.INVISIBLE);
            aboutText.setText(R.string.about_description);
            appName.setVisibility(View.INVISIBLE);
            YoYo.with(Techniques.Landing)
                    .duration(700)
                    .repeat(0)
                    .playOn(aboutText);

            aboutText.setVisibility(View.VISIBLE);

        });



    }

    @Override
    protected void onResume() {
        super.onResume();







        ArrayAdapter searchAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, ingredientList);

        searchView.setThreshold(2);
        searchView.setAdapter(searchAdapter);

        searchView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        searchView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d("Ingredient", "You have selected " + parent.getItemAtPosition(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Log.d("Ingredient", "No ingredient selected");


            }
        });
        searchButton.setOnClickListener(v -> {

            YoYo.with(Techniques.FadeOut)
                    .duration(700)
                    .repeat(0)
                    .playOn(aboutText);

            mSearchText = searchView.getText().toString();
            Log.d("Ingredient", mSearchText);
            Toast.makeText(getBaseContext(), "Loading Recipes", Toast.LENGTH_SHORT).show();
            closeKeyboard();
            getRecipes();


        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                title.removeAll(title);
                sourceUrl.removeAll(sourceUrl);
                imageUrl.removeAll(imageUrl);
                cookTime.removeAll(cookTime);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        aboutButton.setOnClickListener(v -> {

            Log.d("Ingredient", "aboutButton:  clicked");
            appName.setVisibility(View.INVISIBLE);
            YoYo.with(Techniques.Landing)
                    .duration(700)
                    .repeat(0)
                    .playOn(aboutText);
            Log.d("Ingredient", "aboutButton:  clicked");

            mRecyclerView.setVisibility(View.INVISIBLE);
            aboutText.setText(R.string.about_description);
            aboutText.setVisibility(View.VISIBLE);

        });

        favoriteMenu.setOnClickListener(v -> {

            YoYo.with(Techniques.FadeOut)
                    .duration(700)
                    .repeat(0)
                    .playOn(aboutText);


            try {
                initRecyclerViewFavorite();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });


    }



    private void getRecipes() {


        title.removeAll(title);
        sourceUrl.removeAll(sourceUrl);
        imageUrl.removeAll(imageUrl);
        cookTime.removeAll(cookTime);
        mRecyclerView.removeAllViewsInLayout();
        RequestParams params = new RequestParams();
        params.put("diet", health);


        letsDoSomeNetworking(params);


    }


    private void letsDoSomeNetworking(RequestParams params) {


        AsyncHttpClient client = new AsyncHttpClient();

        client.get(Constants.SEARCH_URL_1 + mSearchText, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.d("Ingredient", "letsDoSomeNetworking1 function executed");
                Log.d("Ingredient", "Success! JSON " + response.toString());

                try {

                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray array = obj.getJSONArray("hits");
                    int count = obj.getInt("count");

                    if (count == 0) {
                        Toast.makeText(getBaseContext(), "No Recipes Found", Toast.LENGTH_SHORT).show();

                    } else {
                        if (count>=40){
                            range = 40;
                        }
                        else {
                            range = count;
                        }


                        for (int i = 0; i < range; i++) {

                            JSONObject recipeArrayJSON = array.getJSONObject(i);
                            JSONObject recipeJSON = recipeArrayJSON.getJSONObject("recipe");
                            String Title = recipeJSON.getString("label");
                            String source_url = recipeJSON.getString("url");
                            String image_url = recipeJSON.getString("image");
                            int time = recipeJSON.getInt("totalTime");
                            String cook_time = Integer.toString(time);

                            title.add(Title);
                            sourceUrl.add(source_url);
                            imageUrl.add(image_url);
                            cookTime.add(cook_time);




                        }
                        initRecyclerViewSearch();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {

                super.onFailure(statusCode, headers, responseString, throwable);
                RequestParams params = new RequestParams();
                params.put("diet", health);
                letsDoSomeNetworking2(params);
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);

            }
        });


    }


    private void alertDialog(String message) {

        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }


    private void initRecyclerViewSearch() {

        Log.d("Ingredient", "initRecyclerViewSearch: initializing staggered recyclerView for Searching Recipes");


        if (CheckNetwork.isInternetAvailable(Search.this)) {


            RecipeAdapter recipeAdapter = new RecipeAdapter(this, title, imageUrl, cookTime, sourceUrl, favoriteTitle, favoriteRecipeUrl, favoriteImageUrl);
            if (recipeAdapter.getItemCount() == 0) {
                if (restart = false) {

                    Toast.makeText(getBaseContext(), "No Recipes Found", Toast.LENGTH_SHORT).show();
                    appName.setVisibility(View.VISIBLE);
                    aboutText.setVisibility(View.INVISIBLE);
                }
            } else {
                RecipeAdapter recipe_Adapter = new RecipeAdapter(this, title, imageUrl, cookTime, sourceUrl, favoriteTitle, favoriteRecipeUrl, favoriteImageUrl);

                aboutText.setVisibility(View.INVISIBLE);
                appName.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                mRecyclerView.setAdapter(recipeAdapter);

            }
        } else {

            Toast.makeText(getBaseContext(), "There is no Internet Connection", Toast.LENGTH_LONG).show();
        }
    }


    private void initRecyclerViewFavorite() throws JSONException {

        Log.d("Ingredient", "initRecyclerViewFavorite: initializing staggered recyclerView for Favorite Recipes");


        SharedPreferences prefs = getSharedPreferences("PREFS", MODE_PRIVATE);


        String favTitle = prefs.getString(TITLE_KEY, null);
        String favRecipeUrl = prefs.getString(RECIPE_URL_KEY, null);
        String favImageUrl = prefs.getString(IMAGE_URL_KEY, null);



        favoriteTitle.removeAll(favoriteTitle);
        favoriteRecipeUrl.removeAll(favoriteRecipeUrl);
        favoriteImageUrl.removeAll(favoriteImageUrl);

        if (favTitle != ""  && favRecipeUrl != ""  && favImageUrl != "") {
            if (favTitle != null) {
                favoriteTitle = new ArrayList<>(Arrays.asList(favTitle.split(",")));
            }
            if (favRecipeUrl != null) {
                favoriteRecipeUrl = new ArrayList<>(Arrays.asList(favRecipeUrl.split(",")));
            }
            if (favImageUrl != null) {
                favoriteImageUrl = new ArrayList<>(Arrays.asList(favImageUrl.split(",")));
            }
        } else {

            Toast.makeText(getBaseContext(), "No Saved Recipes", Toast.LENGTH_SHORT).show();

        }


        FavoriteListAdapter favoriteListAdapter = new FavoriteListAdapter(this, favoriteTitle, favoriteRecipeUrl, favoriteImageUrl);

        if (favoriteListAdapter.getItemCount() == 0) {


            Toast.makeText(getBaseContext(), "No Saved Recipes", Toast.LENGTH_SHORT).show();
        } else {
            aboutText.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            appName.setVisibility(View.INVISIBLE);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
            mRecyclerView.setAdapter(favoriteListAdapter);
        }
    }


    public void closeKeyboard() {

        View view = this.getCurrentFocus();
        if (view != null) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList(ON_SAVE_RECIPEURL, sourceUrl);
        outState.putStringArrayList(ON_SAVE_IMAGEURL, imageUrl);
        outState.putStringArrayList(ON_SAVE_TITLE, title);
        outState.putStringArrayList(ON_SAVE_COOKTIME, cookTime);
        outState.putString(ON_SAVE_SEARCH, mSearchText);
        outState.putStringArrayList(ON_SAVE_FAVORITE_TITLE, favoriteTitle);

    }





    private void letsDoSomeNetworking2(RequestParams params) {


        AsyncHttpClient client = new AsyncHttpClient();

        client.get(Constants.SEARCH_URL_2 + mSearchText, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.d("Ingredient", "letsDoSomeNetworking2 function executed");
                Log.d("Ingredient", "Success! JSON " + response.toString());

                try {

                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray array = obj.getJSONArray("hits");
                    int count = obj.getInt("count");

                    if (count == 0) {
                        Toast.makeText(getBaseContext(), "No Recipes Found", Toast.LENGTH_SHORT).show();

                    } else {

                        if (count>=40){
                            range = 40;
                        }
                        else {
                            range = count;
                        }


                        for (int i = 0; i < range; i++) {

                            JSONObject recipeArrayJSON = array.getJSONObject(i);
                            JSONObject recipeJSON = recipeArrayJSON.getJSONObject("recipe");
                            String Title = recipeJSON.getString("label");
                            String source_url = recipeJSON.getString("url");
                            String image_url = recipeJSON.getString("image");
                            int time = recipeJSON.getInt("totalTime");
                            String cook_time = Integer.toString(time);

                            title.add(Title);
                            sourceUrl.add(source_url);
                            imageUrl.add(image_url);
                            cookTime.add(cook_time);

                            Log.d("Ingredient", "Success! JSON " + response.toString());
                            Log.d("Ingredient", (i + 1) + ". " + Title + " " + source_url + " " + image_url + " " + time);


                        }
                        initRecyclerViewSearch();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {

                super.onFailure(statusCode, headers, responseString, throwable);
                RequestParams params = new RequestParams();
                params.put("diet", health);
                letsDoSomeNetworking3(params);
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);

            }
        });


    }

    private void letsDoSomeNetworking3(RequestParams params) {


        AsyncHttpClient client = new AsyncHttpClient();

        client.get(Constants.SEARCH_URL_3 + mSearchText, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.d("Ingredient", "letsDoSomeNetworking3 function executed");
                Log.d("Ingredient", "Success! JSON " + response.toString());

                try {

                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray array = obj.getJSONArray("hits");
                    int count = obj.getInt("count");

                    if (count == 0) {
                        Toast.makeText(getBaseContext(), "No Recipes Found", Toast.LENGTH_SHORT).show();

                    } else {
                        if (count>=40){
                            range = 40;
                        }
                        else {
                            range = count;
                        }


                        for (int i = 0; i < range; i++) {

                            JSONObject recipeArrayJSON = array.getJSONObject(i);
                            JSONObject recipeJSON = recipeArrayJSON.getJSONObject("recipe");
                            String Title = recipeJSON.getString("label");
                            String source_url = recipeJSON.getString("url");
                            String image_url = recipeJSON.getString("image");
                            int time = recipeJSON.getInt("totalTime");
                            String cook_time = Integer.toString(time);

                            title.add(Title);
                            sourceUrl.add(source_url);
                            imageUrl.add(image_url);
                            cookTime.add(cook_time);

                            Log.d("Ingredient", "Success! JSON " + response.toString());
                            Log.d("Ingredient", (i + 1) + ". " + Title + " " + source_url + " " + image_url + " " + time);


                        }
                        initRecyclerViewSearch();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {

                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getBaseContext(), "Error in connecting to Servers, please try again later", Toast.LENGTH_LONG).show();
                Log.d("Failed: ", "" + statusCode);
                Log.d("Error : ", "" + throwable);

            }
        });


    }


    public void getSpeechInput(View view){

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());


        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {

            Toast.makeText(getBaseContext(), "Your Device does not support Speech Input", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 10:
                if (resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String voiceText = searchView.getText().toString() + result.get(0) + ",";
                    searchView.setText(voiceText);

                }


                break;



        }

    }
}















