package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import Adapter.ListFoodAdapter;
import Constant.Constant;
import paditech.com.fifood.DetailFoodActivity;
import paditech.com.fifood.R;
import Object.Food;

/**
 * Created by USER on 13/4/2016.
 */
public class NearFragment extends Fragment implements Constant {

    private ListView lvFood;
    public ArrayList<Food> listFood;
    private ListFoodAdapter adapter;
    private MarkerOptions markerOptions;
    private GoogleMap googleMap;

    private Marker marker;

    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_near, container, false);
            init(view);

        } catch (InflateException e) {
        }
        return view;
    }


    private void init(View view) {

        lvFood = (ListView) view.findViewById(R.id.lvFood);

        adapter = new ListFoodAdapter(getActivity(), listFood, NEAR_FRAGMENT);
        lvFood.setAdapter(adapter);

        LatLng latLng = new LatLng(20.996309, 105.827309);
        setMapLocation(latLng);


    }

    private void setMapLocation(LatLng latLng) {
        markerOptions = new MarkerOptions();
        googleMap = ((MapFragment) getActivity()
                .getFragmentManager().findFragmentById(R.id.map)).getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMyLocationEnabled(true);

        // set fit bound marker
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(latLng);
        LatLngBounds bound = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bound, 20, 20, 3);

        if (googleMap != null) {
            markerOptions.position(latLng);
            String name = "nha";
            if (name.isEmpty()) {
                name = "Current Location";
            }
            markerOptions.title(name);
            marker = googleMap.addMarker(markerOptions);
            marker.showInfoWindow();
            googleMap.moveCamera(cu);
            googleMap.animateCamera(cu);
        }
    }

}
