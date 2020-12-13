package com.example.yelpfinalproject.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.yelpfinalproject.YelpData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import com.example.yelpfinalproject.R;

import java.net.MalformedURLException;
import java.net.URL;

public class NotificationsFragment extends Fragment {

    private GoogleMap mMap;

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                TileProvider tileProvider = new UrlTileProvider(256, 256) {
                    @Override
                    public URL getTileUrl(int x, int y, int zoom) {            /* Define the URL pattern for the tile images */
                        String s = String.format("https://tile.openweathermap.org/map/temp_new/%d/%d/%d.png?appid=886fe0f06bc7abce6ea11e881327ebd3", zoom, x, y);
                        if (!checkTileExists(x, y, zoom)) {
                            return null;
                        }            try {
                            return new URL(s);
                        } catch (MalformedURLException e) {
                            throw new AssertionError(e);
                        }
                    }        /*
                     * Check that the tile server supports the requested x, y and zoom.
                     * Complete this stub according to the tile range you support.
                     * If you support a limited range of tiles at different zoom levels, then you
                     * need to define the supported x, y range at each zoom level.
                     */
                    private boolean checkTileExists(int x, int y, int zoom) {
                        int minZoom = 12;
                        int maxZoom = 16;            return (zoom >= minZoom && zoom <= maxZoom);
                    }
                };
                YelpData y = YelpData.getInstance();

                //sets the Lat and Lon of the Map to the Current Lat and Current Lon determined by the GPS button on the search results
                LatLng loc = new LatLng(y.getCurrentLat(),y.getCurrentLon());
                //sets the tag based on the Lat and Lon & makes the tag the location of the current business determined by the GPS button on the search results
                googleMap.addMarker(new MarkerOptions().position(loc).title(y.getCurrentLocationName()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(12));
                TileOverlay tileOverlay = googleMap.addTileOverlay(new TileOverlayOptions()
                        .tileProvider(tileProvider));
            }
        });
        return root;
    }
}