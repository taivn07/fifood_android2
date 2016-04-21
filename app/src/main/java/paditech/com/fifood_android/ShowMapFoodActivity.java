package paditech.com.fifood_android;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import Constant.ImageLoaderConfig;


import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import Constant.Constant;

/**
 * Created by USER on 14/4/2016.
 */
public class ShowMapFoodActivity extends Activity implements Constant {

    private double lat, longth;
    private ImageView imgMain;
    private TextView tvName, tvAddress, tvDistance;
    private RatingBar ratingBar;
    private String detailResponse;
    private MarkerOptions markerOptions;
    private GoogleMap googleMap;

    private Marker marker;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map_food);

        Bundle bundle = getIntent().getExtras();
        detailResponse = bundle.getString(DETAIL_RESPONSE);

        init();

    }

    private void init() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        imgMain = (ImageView) findViewById(R.id.imgMain);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        getShopDetail(detailResponse);


    }

    private void getShopDetail(String responseString) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseString);
            JSONObject response = jsonObject.getJSONObject(RESPONSE);

            tvName.setText(response.getString(NAME));
            tvAddress.setText(response.getString(ADDRESS));
            tvDistance.setText((int) response.getDouble(DISTANCE) + " km");
            ratingBar.setRating((int) response.getDouble(RATING));

            ImageLoaderConfig.imageLoader.displayImage(response.getJSONObject(FILE).getString(URL), imgMain, ImageLoaderConfig.options);

            double lat = response.getDouble(LAT);
            double lng = response.getDouble(LONGTH);
            setMapLocation(new LatLng(HomeActivity.currLat, HomeActivity.currLongth), new LatLng(lat, lng));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setMapLocation(LatLng currLatLng, LatLng foodLatLng) {

        googleMap = ((MapFragment) this
                .getFragmentManager().findFragmentById(R.id.maps)).getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, 13));

        GoogleDirection.withServerKey(getResources().getString(R.string.google_maps_server_key))
                .from(currLatLng)
                .to(foodLatLng)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            // Do something
                        } else {
                            // Do something
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                    }
                });

        /*markerOptions = new MarkerOptions();
        googleMap = ((MapFragment) this
                .getFragmentManager().findFragmentById(R.id.map)).getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

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
        }*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
