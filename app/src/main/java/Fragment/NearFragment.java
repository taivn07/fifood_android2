package Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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
    private ArrayList<Marker> listMarker;

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

        adapter = new ListFoodAdapter(getActivity(), listFood, this);
        lvFood.setAdapter(adapter);

        LatLng latLng = new LatLng(20.996309, 105.827309);
        setMapLocation();

        lvFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        lvFood.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void setMapLocation() {
        markerOptions = new MarkerOptions();
        googleMap = ((MapFragment) getActivity()
                .getFragmentManager().findFragmentById(R.id.map)).getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        googleMap.clear();

        listMarker = new ArrayList<>();
        for (int i = 0; i < listFood.size(); i++) {
            LatLng latLng = new LatLng(listFood.get(i).getLat(), listFood.get(i).getLongth());
            builder.include(latLng);
            switch (i % 4) {
                case 0: {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map1));
                    break;
                }
                case 1: {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map2));
                    break;
                }
                case 2: {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map3));
                    break;
                }
                case 3: {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map4));
                    break;
                }
            }

            markerOptions.position(latLng);
            markerOptions.title(listFood.get(i).getName());
            marker = googleMap.addMarker(markerOptions);
            marker.showInfoWindow();
            listMarker.add(marker);

        }
        LatLngBounds bounds = builder.build();
        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 5);

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                googleMap.moveCamera(cu);
                googleMap.animateCamera(cu);
            }
        });
    }

    public void showMarker(LatLng latLng, int position) {

        CameraUpdate cu = CameraUpdateFactory.newLatLng(latLng);
        googleMap.moveCamera(cu);
        googleMap.animateCamera(cu);

        listMarker.get(position).showInfoWindow();
    }

}
