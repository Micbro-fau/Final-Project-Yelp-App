package com.example.yelpfinalproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    GoogleApiClient mGoogleApiClient;
    SignInButton signInButton;
    Button signOutButton;

    //for Volley code
    private static JSONObject jsonObj;

    String urlSearch;

    GPSTracker gps;

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //Sign In Features
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient =  new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this);
        signOutButton.setVisibility(View.GONE); //sets its visibility as GONE initally so you are forced to sign in



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void yelpSearchName(String search){

        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            final double latitude = gps.getLatitude();
            final double longitude = gps.getLongitude();
        //Start of Volley Code
        RequestQueue queue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Making a request to url and getting response
        queue = new RequestQueue(cache,network);

        boolean valueResult;
        //checks if the string is numeric or not


            urlSearch = "https://api.yelp.com/v3/businesses/search?term=" + search + "&latitude=" + latitude + "&longitude=" + longitude;
            //urlSearch = "https://api.yelp.com/v3/businesses/search?term=delis&latitude=37.786882&longitude=-122.399972";


        queue.start();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSearch,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //textView.setText("Response is: "+ response.substring(0,500));
                        try {
                            YelpData yelpData = YelpData.getInstance();
                            String [] resultNames = {"1","1","1","1","1"};
                            double [] resultRatings = {1,1,1,1,1};
                            double [] resultLon = {1,1,1,1,1};
                            double [] resultLat = {1,1,1,1,1};
                            String [] resultPrice = {"1","1","1","1","1"};
                            String [] resultPhone = {"1","1","1","1","1"};
                            String [] resultID = {"1","1","1","1","1"};


                            jsonObj = new JSONObject(response);
                            JSONArray businessJSON = jsonObj.getJSONArray("businesses");

                            //first result
                            JSONObject business = businessJSON.getJSONObject(0);
                            resultNames[0] = business.getString("name");
                            resultRatings[0] = parseDouble(business.getString("rating"));
                            JSONObject coordinates = business.getJSONObject("coordinates");
                            resultLat[0] = parseDouble(coordinates.getString("latitude"));
                            resultLon[0] = parseDouble(coordinates.getString("longitude"));
                            resultRatings[0] = parseDouble(business.getString("rating"));
                            //resultPrice[0] = business.getString("price");
                            resultPhone[0] = business.getString("phone");
                            resultID[0] = business.getString("id");

                            //second result
                            business = businessJSON.getJSONObject(1);
                            resultNames[1] = business.getString("name");
                            resultRatings[1] = parseDouble(business.getString("rating"));
                            coordinates = business.getJSONObject("coordinates");
                            resultLat[1] = parseDouble(coordinates.getString("latitude"));
                            resultLon[1] = parseDouble(coordinates.getString("longitude"));
                            //resultPrice[1] = business.getString("price");
                            resultPhone[1] = business.getString("phone");
                            resultID[1] = business.getString("id");


                            //third result
                            business = businessJSON.getJSONObject(2);
                            resultNames[2] = business.getString("name");
                            resultRatings[2] = parseDouble(business.getString("rating"));
                            coordinates = business.getJSONObject("coordinates");
                            resultLat[2] = parseDouble(coordinates.getString("latitude"));
                            resultLon[2] = parseDouble(coordinates.getString("longitude"));
                            //resultPrice[2] = business.getString("price");
                            resultPhone[2] = business.getString("phone");
                            resultID[2] = business.getString("id");

                            //fourth result
                            business = businessJSON.getJSONObject(3);
                            resultNames[3] = business.getString("name");
                            resultRatings[3] = parseDouble(business.getString("rating"));
                            coordinates = business.getJSONObject("coordinates");
                            resultLat[3] = parseDouble(coordinates.getString("latitude"));
                            resultLon[3] = parseDouble(coordinates.getString("longitude"));
                            //resultPrice[3] = business.getString("price");
                            resultPhone[3] = business.getString("phone");
                            resultID[3] = business.getString("id");

                            //fifth result
                            business = businessJSON.getJSONObject(4);
                            resultNames[4] = business.getString("name");
                            resultRatings[4] = parseDouble(business.getString("rating"));
                            coordinates = business.getJSONObject("coordinates");
                            resultLat[4] = parseDouble(coordinates.getString("latitude"));
                            resultLon[4] = parseDouble(coordinates.getString("longitude"));
                            //resultPrice[4] = business.getString("price");
                            resultPhone[4] = business.getString("phone");
                            resultID[4] = business.getString("id");


                            yelpData.setResultNames(resultNames);
                            yelpData.setResultRatings(resultRatings);
                            yelpData.setResultLat(resultLat);
                            yelpData.setResultLon(resultLon);
                            yelpData.setResultPrice(resultPrice);
                            yelpData.setResultPhone(resultPhone);
                            yelpData.setResultID(resultID);

                        }catch (final JSONException e) {
                            Log.e(TAG, "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Search was unsucessful",Toast.LENGTH_LONG).show();
            }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    // Basic Authentication
                    String accesstoken = "DR2CvgZx0jlvAx7HPdLT9Zv5GXNcyT-IdmmFpO88m1T20NtxX8fsUeJZGnuNhzr4S0fsbckQLupJSU1A8Nh7ZGUwXJ6GQA6Bo0nR3z_cSIsNQZZ3qr8_v-3hB03NX3Yx";
                    headers.put("Authorization", "Bearer " + accesstoken);
                    return headers;
                }
            };

        queue.add(stringRequest);
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

    }

    //triggered on the bottom search button
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void yelpSearchCategory (String location, String category){

            //Start of Volley Code
            RequestQueue queue;
            Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

            // Set up the network to use HttpURLConnection as the HTTP client.
            Network network = new BasicNetwork(new HurlStack());
            // Making a request to url and getting response
            queue = new RequestQueue(cache,network);

            boolean valueResult;
            //checks if the string is numeric or not

        String CategoryConverted;
        //converts the Spinner elements into readable text for the API search
        if(category.equals("Categories")){
            Toast.makeText(getApplicationContext(), "Pick a Category", Toast.LENGTH_LONG).show();
        } else if(category.equals("Chinese (Restaurants)")) {
            //Toast.makeText(getApplicationContext(), "Chinese Was Selected", Toast.LENGTH_LONG).show();
            CategoryConverted = "chinese";
            urlSearch = "https://api.yelp.com/v3/businesses/search?location=" + location + "&categories=" + CategoryConverted;
        } else if(category.equals("Diners")){
            CategoryConverted = "diners";
            urlSearch = "https://api.yelp.com/v3/businesses/search?location=" + location + "&categories=" + CategoryConverted;
        }else if(category.equals("Electronics")){
            CategoryConverted = "electronics";
            urlSearch = "https://api.yelp.com/v3/businesses/search?location=" + location + "&categories=" + CategoryConverted;
        }else if(category.equals("Sporting Goods")){
            CategoryConverted = "sportgoods";
            urlSearch = "https://api.yelp.com/v3/businesses/search?location=" + location + "&categories=" + CategoryConverted;
        }else if(category.equals("Pet Stores (Pets)")){
            CategoryConverted = "petstore";
            urlSearch = "https://api.yelp.com/v3/businesses/search?location=" + location + "&categories=" + CategoryConverted;
        }else if(category.equals("Orthodontists (Dentists)")){
            CategoryConverted = "orthodontists";
            urlSearch = "https://api.yelp.com/v3/businesses/search?location=" + location + "&categories=" + CategoryConverted;
        }else if(category.equals("Ice Cream And Frozen Yogurt (Food)")){
            CategoryConverted = "icecream";
            urlSearch = "https://api.yelp.com/v3/businesses/search?location=" + location + "&categories=" + CategoryConverted;
        }else if(category.equals("Bubble Tea (Food)")){
            CategoryConverted = "bubbletea";
            urlSearch = "https://api.yelp.com/v3/businesses/search?location=" + location + "&categories=" + CategoryConverted;
        }

            queue.start();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSearch,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //textView.setText("Response is: "+ response.substring(0,500));
                            try {
                                YelpData yelpData = YelpData.getInstance();
                                String [] resultNames = {"1","1","1","1","1"};
                                double [] resultRatings = {1,1,1,1,1};
                                double [] resultLon = {1,1,1,1,1};
                                double [] resultLat = {1,1,1,1,1};
                                String [] resultPrice = {"1","1","1","1","1"};
                                String [] resultPhone = {"1","1","1","1","1"};
                                String [] resultID = {"1","1","1","1","1"};


                                jsonObj = new JSONObject(response);
                                JSONArray businessJSON = jsonObj.getJSONArray("businesses");

                                //first result
                                JSONObject business = businessJSON.getJSONObject(0);
                                resultNames[0] = business.getString("name");
                                resultRatings[0] = parseDouble(business.getString("rating"));
                                JSONObject coordinates = business.getJSONObject("coordinates");
                                resultLat[0] = parseDouble(coordinates.getString("latitude"));
                                resultLon[0] = parseDouble(coordinates.getString("longitude"));
                                resultRatings[0] = parseDouble(business.getString("rating"));
                                //resultPrice[0] = business.getString("price");
                                resultPhone[0] = business.getString("phone");
                                resultID[0] = business.getString("id");

                                //second result
                                business = businessJSON.getJSONObject(1);
                                resultNames[1] = business.getString("name");
                                resultRatings[1] = parseDouble(business.getString("rating"));
                                coordinates = business.getJSONObject("coordinates");
                                resultLat[1] = parseDouble(coordinates.getString("latitude"));
                                resultLon[1] = parseDouble(coordinates.getString("longitude"));
                                //resultPrice[1] = business.getString("price");
                                resultPhone[1] = business.getString("phone");
                                resultID[1] = business.getString("id");


                                //third result
                                business = businessJSON.getJSONObject(2);
                                resultNames[2] = business.getString("name");
                                resultRatings[2] = parseDouble(business.getString("rating"));
                                coordinates = business.getJSONObject("coordinates");
                                resultLat[2] = parseDouble(coordinates.getString("latitude"));
                                resultLon[2] = parseDouble(coordinates.getString("longitude"));
                                //resultPrice[2] = business.getString("price");
                                resultPhone[2] = business.getString("phone");
                                resultID[2] = business.getString("id");

                                //fourth result
                                business = businessJSON.getJSONObject(3);
                                resultNames[3] = business.getString("name");
                                resultRatings[3] = parseDouble(business.getString("rating"));
                                coordinates = business.getJSONObject("coordinates");
                                resultLat[3] = parseDouble(coordinates.getString("latitude"));
                                resultLon[3] = parseDouble(coordinates.getString("longitude"));
                                //resultPrice[3] = business.getString("price");
                                resultPhone[3] = business.getString("phone");
                                resultID[3] = business.getString("id");

                                //fifth result
                                business = businessJSON.getJSONObject(4);
                                resultNames[4] = business.getString("name");
                                resultRatings[4] = parseDouble(business.getString("rating"));
                                coordinates = business.getJSONObject("coordinates");
                                resultLat[4] = parseDouble(coordinates.getString("latitude"));
                                resultLon[4] = parseDouble(coordinates.getString("longitude"));
                                //resultPrice[4] = business.getString("price");
                                resultPhone[4] = business.getString("phone");
                                resultID[4] = business.getString("id");


                                yelpData.setResultNames(resultNames);
                                yelpData.setResultRatings(resultRatings);
                                yelpData.setResultLat(resultLat);
                                yelpData.setResultLon(resultLon);
                                yelpData.setResultPrice(resultPrice);
                                yelpData.setResultPhone(resultPhone);
                                yelpData.setResultID(resultID);

                            }catch (final JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "Json parsing error: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Search was unsucessful",Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    // Basic Authentication
                    String accesstoken = "DR2CvgZx0jlvAx7HPdLT9Zv5GXNcyT-IdmmFpO88m1T20NtxX8fsUeJZGnuNhzr4S0fsbckQLupJSU1A8Nh7ZGUwXJ6GQA6Bo0nR3z_cSIsNQZZ3qr8_v-3hB03NX3Yx";
                    headers.put("Authorization", "Bearer " + accesstoken);
                    return headers;
                }
            };

            queue.add(stringRequest);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    //Function that is called when the Category's GPS button is pressed
    public void yelpSearchCategoryGPS(String category){

        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            final double latitude = gps.getLatitude();
            final double longitude = gps.getLongitude();
            //Start of Volley Code
            RequestQueue queue;
            Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

            // Set up the network to use HttpURLConnection as the HTTP client.
            Network network = new BasicNetwork(new HurlStack());
            // Making a request to url and getting response
            queue = new RequestQueue(cache,network);

            boolean valueResult;
            //checks if the string is numeric or not


            String CategoryConverted;
            //converts the Spinner elements into readable text for the API search
            if(category.equals("Categories")){
                Toast.makeText(getApplicationContext(), "Pick a Category", Toast.LENGTH_LONG).show();
            } else if(category.equals("Chinese (Restaurants)")) {
                //Toast.makeText(getApplicationContext(), "Chinese Was Selected", Toast.LENGTH_LONG).show();
                CategoryConverted = "chinese";
                urlSearch = "https://api.yelp.com/v3/businesses/search?&latitude=" + latitude + "&longitude=" + longitude + "&categories=" + CategoryConverted;
            } else if(category.equals("Diners")){
                CategoryConverted = "diners";
                urlSearch = "https://api.yelp.com/v3/businesses/search?&latitude=" + latitude + "&longitude=" + longitude + "&categories=" + CategoryConverted;
            }else if(category.equals("Electronics")){
                CategoryConverted = "electronics";
                urlSearch = "https://api.yelp.com/v3/businesses/search?&latitude=" + latitude + "&longitude=" + longitude + "&categories=" + CategoryConverted;
            }else if(category.equals("Sporting Goods")){
                CategoryConverted = "sportgoods";
                urlSearch = "https://api.yelp.com/v3/businesses/search?&latitude=" + latitude + "&longitude=" + longitude + "&categories=" + CategoryConverted;
            }else if(category.equals("Pet Stores (Pets)")){
                CategoryConverted = "petstore";
                urlSearch = "https://api.yelp.com/v3/businesses/search?&latitude=" + latitude + "&longitude=" + longitude + "&categories=" + CategoryConverted;
            }else if(category.equals("Orthodontists (Dentists)")){
                CategoryConverted = "orthodontists";
                urlSearch = "https://api.yelp.com/v3/businesses/search?&latitude=" + latitude + "&longitude=" + longitude + "&categories=" + CategoryConverted;
            }else if(category.equals("Ice Cream And Frozen Yogurt (Food)")){
                CategoryConverted = "icecream";
                urlSearch = "https://api.yelp.com/v3/businesses/search?&latitude=" + latitude + "&longitude=" + longitude + "&categories=" + CategoryConverted;
            }else if(category.equals("Bubble Tea (Food)")){
                CategoryConverted = "bubbletea";
                urlSearch = "https://api.yelp.com/v3/businesses/search?&latitude=" + latitude + "&longitude=" + longitude + "&categories=" + CategoryConverted;
            }

            queue.start();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSearch,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //textView.setText("Response is: "+ response.substring(0,500));
                            try {
                                YelpData yelpData = YelpData.getInstance();
                                String [] resultNames = {"1","1","1","1","1"};
                                double [] resultRatings = {1,1,1,1,1};
                                double [] resultLon = {1,1,1,1,1};
                                double [] resultLat = {1,1,1,1,1};
                                String [] resultPrice = {"1","1","1","1","1"};
                                String [] resultPhone = {"1","1","1","1","1"};
                                String [] resultID = {"1","1","1","1","1"};


                                jsonObj = new JSONObject(response);
                                JSONArray businessJSON = jsonObj.getJSONArray("businesses");

                                //first result
                                JSONObject business = businessJSON.getJSONObject(0);
                                resultNames[0] = business.getString("name");
                                resultRatings[0] = parseDouble(business.getString("rating"));
                                JSONObject coordinates = business.getJSONObject("coordinates");
                                resultLat[0] = parseDouble(coordinates.getString("latitude"));
                                resultLon[0] = parseDouble(coordinates.getString("longitude"));
                                resultRatings[0] = parseDouble(business.getString("rating"));
                                //resultPrice[0] = business.getString("price");
                                resultPhone[0] = business.getString("phone");
                                resultID[0] = business.getString("id");

                                //second result
                                business = businessJSON.getJSONObject(1);
                                resultNames[1] = business.getString("name");
                                resultRatings[1] = parseDouble(business.getString("rating"));
                                coordinates = business.getJSONObject("coordinates");
                                resultLat[1] = parseDouble(coordinates.getString("latitude"));
                                resultLon[1] = parseDouble(coordinates.getString("longitude"));
                                //resultPrice[1] = business.getString("price");
                                resultPhone[1] = business.getString("phone");
                                resultID[1] = business.getString("id");


                                //third result
                                business = businessJSON.getJSONObject(2);
                                resultNames[2] = business.getString("name");
                                resultRatings[2] = parseDouble(business.getString("rating"));
                                coordinates = business.getJSONObject("coordinates");
                                resultLat[2] = parseDouble(coordinates.getString("latitude"));
                                resultLon[2] = parseDouble(coordinates.getString("longitude"));
                                //resultPrice[2] = business.getString("price");
                                resultPhone[2] = business.getString("phone");
                                resultID[2] = business.getString("id");

                                //fourth result
                                business = businessJSON.getJSONObject(3);
                                resultNames[3] = business.getString("name");
                                resultRatings[3] = parseDouble(business.getString("rating"));
                                coordinates = business.getJSONObject("coordinates");
                                resultLat[3] = parseDouble(coordinates.getString("latitude"));
                                resultLon[3] = parseDouble(coordinates.getString("longitude"));
                                //resultPrice[3] = business.getString("price");
                                resultPhone[3] = business.getString("phone");
                                resultID[3] = business.getString("id");

                                //fifth result
                                business = businessJSON.getJSONObject(4);
                                resultNames[4] = business.getString("name");
                                resultRatings[4] = parseDouble(business.getString("rating"));
                                coordinates = business.getJSONObject("coordinates");
                                resultLat[4] = parseDouble(coordinates.getString("latitude"));
                                resultLon[4] = parseDouble(coordinates.getString("longitude"));
                                //resultPrice[4] = business.getString("price");
                                resultPhone[4] = business.getString("phone");
                                resultID[4] = business.getString("id");


                                yelpData.setResultNames(resultNames);
                                yelpData.setResultRatings(resultRatings);
                                yelpData.setResultLat(resultLat);
                                yelpData.setResultLon(resultLon);
                                yelpData.setResultPrice(resultPrice);
                                yelpData.setResultPhone(resultPhone);
                                yelpData.setResultID(resultID);

                            }catch (final JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "Json parsing error: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Search was unsucessful",Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    // Basic Authentication
                    String accesstoken = "DR2CvgZx0jlvAx7HPdLT9Zv5GXNcyT-IdmmFpO88m1T20NtxX8fsUeJZGnuNhzr4S0fsbckQLupJSU1A8Nh7ZGUwXJ6GQA6Bo0nR3z_cSIsNQZZ3qr8_v-3hB03NX3Yx";
                    headers.put("Authorization", "Bearer " + accesstoken);
                    return headers;
                }
            };

            queue.add(stringRequest);
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

    }

    public void yelpFavoriteReturn(String ID, final int arrayCurrentValue){

            RequestQueue queue;
            Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

            // Set up the network to use HttpURLConnection as the HTTP client.
            Network network = new BasicNetwork(new HurlStack());
            // Making a request to url and getting response
            queue = new RequestQueue(cache,network);

            boolean valueResult;
            //checks if the string is numeric or not


            String CategoryConverted;
            //converts the Spinner elements into readable text for the API search

            urlSearch = "https://api.yelp.com/v3/businesses/" + ID;


            queue.start();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSearch,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //textView.setText("Response is: "+ response.substring(0,500));
                            try {
                                YelpData yelpData = YelpData.getInstance();
                                String [] resultNames = {"1","1","1","1","1"};
                                double [] resultRatings = {1,1,1,1,1};
                                double [] resultLon = {1,1,1,1,1};
                                double [] resultLat = {1,1,1,1,1};
                                String [] resultPrice = {"1","1","1","1","1"};
                                String [] resultPhone = {"1","1","1","1","1"};
                                String [] resultID = {"1","1","1","1","1"};


                                jsonObj = new JSONObject(response);
                                resultNames[arrayCurrentValue] = jsonObj.getString("name");
                                resultRatings[arrayCurrentValue] = parseDouble(jsonObj.getString("rating"));
                                JSONObject coordinates = jsonObj.getJSONObject("coordinates");
                                resultLat[arrayCurrentValue] = parseDouble(coordinates.getString("latitude"));
                                resultLon[arrayCurrentValue] = parseDouble(coordinates.getString("longitude"));
                                resultRatings[arrayCurrentValue] = parseDouble(jsonObj.getString("rating"));
                                resultPrice[arrayCurrentValue] = jsonObj.getString("price");
                                resultPhone[arrayCurrentValue] = jsonObj.getString("phone");
                                resultID[arrayCurrentValue] = jsonObj.getString("id");

                                //this Yelp call has "specific" function setters in order to be able to search for each Favorite ID individually
                                yelpData.setResultNamesSpecific(resultNames[arrayCurrentValue],arrayCurrentValue);
                                yelpData.setResultRatingsSpecific(resultRatings[arrayCurrentValue],arrayCurrentValue);
                                yelpData.setResultLatSpecific(resultLat[arrayCurrentValue],arrayCurrentValue);
                                yelpData.setResultLonSpecific(resultLon[arrayCurrentValue],arrayCurrentValue);
                                yelpData.setResultPriceSpecific(resultPrice[arrayCurrentValue],arrayCurrentValue);
                                yelpData.setResultPhoneSpecific(resultPhone[arrayCurrentValue],arrayCurrentValue);
                                yelpData.setResultIDSpecific(resultID[arrayCurrentValue],arrayCurrentValue);

                            }catch (final JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "Json parsing error: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Search was unsucessful",Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    // Basic Authentication
                    String accesstoken = "DR2CvgZx0jlvAx7HPdLT9Zv5GXNcyT-IdmmFpO88m1T20NtxX8fsUeJZGnuNhzr4S0fsbckQLupJSU1A8Nh7ZGUwXJ6GQA6Bo0nR3z_cSIsNQZZ3qr8_v-3hB03NX3Yx";
                    headers.put("Authorization", "Bearer " + accesstoken);
                    return headers;
                }
            };

            queue.add(stringRequest);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.signOutButton:
                signOut();
                break;
        }

    }

    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleSignInResult(GoogleSignInResult result){
        Log.d(TAG,"handleSIgnInResult:" + result.isSuccess());
        if (result.isSuccess()){
            //Sign in worked; showing aunthentication UI
            GoogleSignInAccount acct = result.getSignInAccount();

            //Makes the Sign Out button visible  and the Sign In button invisible
            signInButton = (SignInButton) findViewById(R.id.sign_in_button);
            signInButton.setVisibility(View.GONE);

            signOutButton = (Button) findViewById(R.id.signOutButton);
            signOutButton.setVisibility(View.VISIBLE);

            Toast.makeText(getApplicationContext(), "Hello, " + acct.getDisplayName(), Toast.LENGTH_LONG).show();

            //gets the current UserID and saves it so it can be used in the Favorites implementation
            YelpData yelpData = YelpData.getInstance();
            yelpData.setCurrentUserID(acct.getId());




        }else{

        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"onConnectionFailed:" + connectionResult);
    }

    //Signing Out of Account
    private void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResult(@NonNull Status status) {
                //statusTextView.setText("Signed out");
                Toast.makeText(getApplicationContext(), "Signed Out", Toast.LENGTH_LONG).show();


                //Makes the Sign In button visible  and the Sign Out button invisible
                signInButton = (SignInButton) findViewById(R.id.sign_in_button);
                signInButton.setVisibility(View.VISIBLE);

                signOutButton = (Button) findViewById(R.id.signOutButton);
                signOutButton.setVisibility(View.GONE);

                YelpData yelpData = YelpData.getInstance();
                double[] resultLat = {1,1,1,1,1};
                resultLat = yelpData.getResultLat();
                //wipes the userID on logout
                yelpData.setCurrentUserID("1");


            }
        });
    }
}