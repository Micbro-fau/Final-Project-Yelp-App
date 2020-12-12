package com.example.yelpfinalproject.ui;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yelpfinalproject.R;
import com.example.yelpfinalproject.YelpData;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ViewHolder>{
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView locationName;
        TextView number;
        TextView cost;
        TextView rating;
        ImageButton GPS;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            locationName = itemView.findViewById(R.id.locationName);
            number = itemView.findViewById(R.id.number);
            cost = itemView.findViewById(R.id.cost);
            rating = itemView.findViewById(R.id.rating);
            GPS = itemView.findViewById(R.id.GPS);

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
        holder.rating.setText(String.valueOf(yelpData.getResultRatings()[position]));

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



    }

    @Override
    public int getItemCount(){
        return 5;

    }
}
