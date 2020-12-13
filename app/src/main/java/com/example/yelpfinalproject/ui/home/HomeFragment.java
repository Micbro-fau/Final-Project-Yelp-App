package com.example.yelpfinalproject.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.yelpfinalproject.MainActivity;
import com.example.yelpfinalproject.R;
import com.example.yelpfinalproject.YelpData;
import com.example.yelpfinalproject.ui.dashboard.DashboardFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static java.lang.String.valueOf;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button FavoritesButton;
    ImageButton NameSearch;
    ImageButton CategorySearch;
    ImageButton CategorySearchGPS;


    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        //Category Spinner code
        final Spinner spinner = (Spinner) root.findViewById(R.id.category);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Activating a Fragment Switch when the Favorites button is pressed
        FavoritesButton = (Button) root.findViewById(R.id.FavoritesButton);
        FavoritesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                //insert way to update search data with Favorites stored in the Firebase Database
                /*
                Fragment fragment = null;
                fragment = new DashboardFragment();
                replaceFragment(fragment);

                 */
                YelpData yelpData = YelpData.getInstance();
                yelpData.setFavoritesRun(true);

                //wipes the data before any volleys are ran
                yelpData.setResultNames(new String[]{"1", "1", "1", "1", "1"});
                yelpData.setResultRatings(new double[]{1, 1, 1, 1, 1});
                yelpData.setResultLat(new double[]{1, 1, 1, 1, 1});
                yelpData.setResultLon(new double[]{1, 1, 1, 1, 1});
                yelpData.setResultPrice(new String[]{"1", "1", "1", "1", "1"});
                yelpData.setResultPhone(new String[]{"1", "1", "1", "1", "1"});
                yelpData.setResultID(new String[]{"1", "1", "1", "1", "1"});

                if(yelpData.getCurrentUserID().equals("1")){
                    //do not run this step
                }else{


                    //removes the dummy values established when an user first logs in
                    DatabaseReference refInit = FirebaseDatabase.getInstance().getReference("userData").child(yelpData.getCurrentUserID()).child("Favorites").child("12345");
                    refInit.removeValue();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userData").child(yelpData.getCurrentUserID()).child("Favorites");

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            YelpData yelpData = YelpData.getInstance();
                            yelpData.setDatabaseResults(map);

                            int i = 0;
                                for (Object o : map.keySet()) {
                                    if(i != map.size()){ //put here to skip the dummy value at the beginning of every user
                                        MainActivity main = (MainActivity) getActivity();
                                        main.yelpFavoriteReturn(valueOf(map.get(o)),i);
                                    }
                                    i++;
                                }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }



            }
        });


        //Name Search Button
        NameSearch = (ImageButton) root.findViewById(R.id.NameSearchButton);

        final EditText searchText = (EditText) root.findViewById(R.id.NameSearch);

        NameSearch.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View arg0) {

                //gets the current favorite values IF the user is logged in. This is important to make sure that favorites do not overlap
                YelpData yelpData = YelpData.getInstance();
                yelpData.setFavoritesRun(false);

                //wipes the data before any volleys are ran
                yelpData.setResultNames(new String[]{"1", "1", "1", "1", "1"});
                yelpData.setResultRatings(new double[]{1, 1, 1, 1, 1});
                yelpData.setResultLat(new double[]{1, 1, 1, 1, 1});
                yelpData.setResultLon(new double[]{1, 1, 1, 1, 1});
                yelpData.setResultPrice(new String[]{"1", "1", "1", "1", "1"});
                yelpData.setResultPhone(new String[]{"1", "1", "1", "1", "1"});
                yelpData.setResultID(new String[]{"1", "1", "1", "1", "1"});

                if(yelpData.getCurrentUserID().equals("1")){
                    //do not run this step
                }else{
                    DatabaseReference refInit = FirebaseDatabase.getInstance().getReference("userData").child(yelpData.getCurrentUserID()).child("Favorites").child("12345");
                    refInit.setValue("dummy");
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userData").child(yelpData.getCurrentUserID()).child("Favorites");

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            YelpData yelpData = YelpData.getInstance();
                            yelpData.setDatabaseResults(map);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


                String value = searchText.getText().toString(); //gets the data from the text bar

                MainActivity main = (MainActivity) getActivity();
                main.yelpSearchName(value); //sends text field string to HomeSearch located in the main activity

            }
        });

        //Category Search Button
        CategorySearch = (ImageButton) root.findViewById(R.id.LocationSearchButton);
        final EditText searchText2 = (EditText) root.findViewById(R.id.LocationSearch);

        CategorySearch.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View arg0) {

                //gets the current favorite values IF the user is logged in. This is important to make sure that favorites do not overlap
                YelpData yelpData = YelpData.getInstance();
                yelpData.setFavoritesRun(false);

                //wipes the data before any volleys are ran
                yelpData.setResultNames(new String[]{"1", "1", "1", "1", "1"});
                yelpData.setResultRatings(new double[]{1, 1, 1, 1, 1});
                yelpData.setResultLat(new double[]{1, 1, 1, 1, 1});
                yelpData.setResultLon(new double[]{1, 1, 1, 1, 1});
                yelpData.setResultPrice(new String[]{"1", "1", "1", "1", "1"});
                yelpData.setResultPhone(new String[]{"1", "1", "1", "1", "1"});
                yelpData.setResultID(new String[]{"1", "1", "1", "1", "1"});

                if(yelpData.getCurrentUserID().equals("1")){
                    //do not run this step
                }else{
                    DatabaseReference refInit = FirebaseDatabase.getInstance().getReference("userData").child(yelpData.getCurrentUserID()).child("Favorites").child("12345");
                    refInit.setValue("dummy");
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userData").child(yelpData.getCurrentUserID()).child("Favorites");

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            YelpData yelpData = YelpData.getInstance();
                            yelpData.setDatabaseResults(map);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                String value = searchText2.getText().toString(); //gets the data from the text bar
                String text = spinner.getSelectedItem().toString(); //gets the current spinner value

                MainActivity main = (MainActivity) getActivity();
                main.yelpSearchCategory(value,text); //sends text field string to HomeSearch located in the main activity

            }
        });

        CategorySearchGPS = (ImageButton) root.findViewById(R.id.GPSLocationSearch);

        CategorySearchGPS.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View arg0) {

                //gets the current favorite values IF the user is logged in. This is important to make sure that favorites do not overlap
                YelpData yelpData = YelpData.getInstance();
                yelpData.setFavoritesRun(false);

                //wipes the data before any volleys are ran
                yelpData.setResultNames(new String[]{"1", "1", "1", "1", "1"});
                yelpData.setResultRatings(new double[]{1, 1, 1, 1, 1});
                yelpData.setResultLat(new double[]{1, 1, 1, 1, 1});
                yelpData.setResultLon(new double[]{1, 1, 1, 1, 1});
                yelpData.setResultPrice(new String[]{"1", "1", "1", "1", "1"});
                yelpData.setResultPhone(new String[]{"1", "1", "1", "1", "1"});
                yelpData.setResultID(new String[]{"1", "1", "1", "1", "1"});

                if(yelpData.getCurrentUserID().equals("1")){
                    //do not run this step
                }else{

                    DatabaseReference refInit = FirebaseDatabase.getInstance().getReference("userData").child(yelpData.getCurrentUserID()).child("Favorites").child("12345");
                    refInit.setValue("dummy");

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userData").child(yelpData.getCurrentUserID()).child("Favorites");

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            YelpData yelpData = YelpData.getInstance();
                            yelpData.setDatabaseResults(map);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                String text = spinner.getSelectedItem().toString();

                MainActivity main = (MainActivity) getActivity();
                main.yelpSearchCategoryGPS(text); //sends text field string to HomeSearch located in the main activity

            }
        });



        return root;
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.navigation_dashboard, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}