package com.example.yelpfinalproject.ui;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.yelpfinalproject.MainActivity;
import com.example.yelpfinalproject.R;
import com.example.yelpfinalproject.YelpData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static java.lang.String.valueOf;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ViewHolder>{
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView locationName;
        TextView number;
        TextView cost;
        TextView rating;
        ImageButton GPS;
        ToggleButton Favorite;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            locationName = itemView.findViewById(R.id.locationName);
            number = itemView.findViewById(R.id.number);
            cost = itemView.findViewById(R.id.cost);
            rating = itemView.findViewById(R.id.rating);
            GPS = itemView.findViewById(R.id.GPS);
            Favorite = itemView.findViewById(R.id.Favorite);

            YelpData yelpData = YelpData.getInstance();
            //if the user isn't logged in, make the Favorite toggle invisible. If the user is logged in, make it visible
            if(yelpData.getCurrentUserID().equals("1")){
                Favorite.setVisibility(View.INVISIBLE);
            }else{
                Favorite.setVisibility(View.VISIBLE);
            }



        }
    }

    public ProgramAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ProgramAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_listview,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramAdapter.ViewHolder holder, int position){
        YelpData yelpData = YelpData.getInstance();
        String number[] = {"1.","2.","3.","4.","5."};
        holder.locationName.setText(yelpData.getResultNames()[position]);
        holder.cost.setText(yelpData.getResultPrice()[position]);
        holder.number.setText(number[position]);
        holder.rating.setText(valueOf(yelpData.getResultRatings()[position]));

        final int current = position;
        //if the GPS button is clicked, save the current lat, lon, and company name so it can be used on the Map fragment
        holder.GPS.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View arg0) {
                YelpData yelpData = YelpData.getInstance();
                yelpData.setCurrentLat(yelpData.getResultLat()[current]);
                yelpData.setCurrentLon(yelpData.getResultLon()[current]);
                yelpData.setCurrentLocationName(yelpData.getResultNames()[current]);

            }
        });

        //if the ID of the location is already in the database
        Map<String, Object> databaseResults = yelpData.getDatabaseResults();
        String pushReference = null;
        if(yelpData.getCurrentUserID() != "1"){ //if an account is currently logged in
            if(databaseResults.containsValue(yelpData.getResultID()[current]) == true) { //if the Location ID already exists in the Firebase Database
                //make the toggle on
                holder.Favorite.setChecked(true);
                //and saves the Push value. This is necessary if the favorite wants to be deleted on a reload

            }
        }

            holder.Favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                YelpData yelpData = YelpData.getInstance();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("userData");
                //the data is stored up here for the database location so the push's ID can be consistent during the instance of running the command
                Map<String, Object> databaseResults = yelpData.getDatabaseResults();

                DatabaseReference pushedPostRef = myRef.child(yelpData.getCurrentUserID()).child("Favorites").push();
                String postId = pushedPostRef.getKey();

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // The toggle is enabled

                        //gets all the results from the database and saves it to a Hash Table in Yelp Data

                        //gets the database results saved earlier
                        Map<String, Object> databaseResults = yelpData.getDatabaseResults();
                        //if the ID doesn't exist within the database
                        if(databaseResults.containsValue(yelpData.getResultID()[current]) == false){
                            //then add it to the database
                            pushedPostRef.setValue(yelpData.getResultID()[current]);
                        }else{ //but if it does, just reuse the ID when adding it back in
                            String pushReference = null;
                            for (Object o : databaseResults.keySet()) {
                                if (databaseResults.get(o).equals(yelpData.getResultID()[current])) {
                                    DatabaseReference pushedPostRef = myRef.child(yelpData.getCurrentUserID()).child("Favorites").child(valueOf(o));
                                    pushedPostRef.setValue(yelpData.getResultID()[current]);
                                }
                            }
                        }

                    } else {
                        // The toggle is disabled
                        //remove the value that is specifically tied to that same push
                        if(databaseResults.containsValue(yelpData.getResultID()[current]) == false) {
                            myRef.child(yelpData.getCurrentUserID()).child("Favorites").child(postId).removeValue();
                        }else{ //remove the value that is specifically tied to the push that happened in the past
                            String pushReference = null;
                            for (Object o : databaseResults.keySet()) {
                                if (databaseResults.get(o).equals(yelpData.getResultID()[current])) {
                                    myRef.child(yelpData.getCurrentUserID()).child("Favorites").child(valueOf(o)).removeValue();

                                }
                            }
                        }
                    }
                }
            });


    }

    @Override
    public int getItemCount(){
        return 5;

    }
}
