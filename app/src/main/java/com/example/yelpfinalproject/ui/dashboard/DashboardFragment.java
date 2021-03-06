package com.example.yelpfinalproject.ui.dashboard;

        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.EditText;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import androidx.lifecycle.Observer;
        import androidx.lifecycle.ViewModelProviders;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.yelpfinalproject.R;
        import com.example.yelpfinalproject.YelpData;
        import com.example.yelpfinalproject.ui.ProgramAdapter;

public class DashboardFragment extends Fragment {

    RecyclerView simpleList;
    RecyclerView.Adapter programAdapter;
    RecyclerView.LayoutManager layoutManager;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        simpleList = (RecyclerView) root.findViewById(R.id.resultList); //ties the simpleList RecyclerView to the resultList ID on the fragment

        YelpData yelpData = YelpData.getInstance();

        //gives a title to the Results page determined if it is just a Search or if its a Favorites
        final TextView searchText = (TextView) root.findViewById(R.id.resultTitle);
        if(yelpData.getFavoritesRun() == false){
            searchText.setText("Search Results");
        }else{
            searchText.setText("Favorites Results");
        }


        layoutManager = new LinearLayoutManager(getContext());
        simpleList.setLayoutManager(layoutManager);

        programAdapter = new ProgramAdapter(getContext());
        simpleList.setAdapter(programAdapter);

        return root;
    }
}