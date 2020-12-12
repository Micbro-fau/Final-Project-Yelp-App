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
import com.example.yelpfinalproject.ui.dashboard.DashboardFragment;

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
       // final TextView textView = root.findViewById(R.id.text_home);
        /*
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
         */
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
                Fragment fragment = null;
                fragment = new DashboardFragment();
                replaceFragment(fragment);


            }
        });


        //Name Search Button
        NameSearch = (ImageButton) root.findViewById(R.id.NameSearchButton);

        final EditText searchText = (EditText) root.findViewById(R.id.NameSearch);

        NameSearch.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View arg0) {

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

                String value = searchText2.getText().toString(); //gets the data from the text bar
                String text = spinner.getSelectedItem().toString();

                MainActivity main = (MainActivity) getActivity();
                main.yelpSearchCategory(value,text); //sends text field string to HomeSearch located in the main activity

            }
        });

        CategorySearchGPS = (ImageButton) root.findViewById(R.id.GPSLocationSearch);

        CategorySearchGPS.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View arg0) {

                String text = spinner.getSelectedItem().toString();

                MainActivity main = (MainActivity) getActivity();
                main.yelpSearchCategoryGPS(text); //sends text field string to HomeSearch located in the main activity

            }
        });



        return root;
    }

    public void replaceFragment(Fragment someFragment) {
        //FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.navigation_dashboard, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}