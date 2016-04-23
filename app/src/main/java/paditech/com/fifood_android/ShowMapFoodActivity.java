package paditech.com.fifood_android;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import Constant.ImageLoaderConfig;
import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import Constant.Constant;
import Constant.FormatValue;

/**
 * Created by USER on 14/4/2016.
 */
public class ShowMapFoodActivity extends Activity implements Constant {

    private ImageView imgMain;
    private TextView tvName, tvAddress, tvDistance;
    private RatingBar ratingBar;
    private String detailResponse;
    private GoogleMap googleMap;
    private ProgressBar progressBar;
    private Marker marker;
    private ActionBar actionBar;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_food);
        Bundle bundle = getIntent().getExtras();
        detailResponse = bundle.getString(DETAIL_RESPONSE);

        init();

    }

    private void init() {
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBgMenu)));

        imgMain = (ImageView) findViewById(R.id.imgMain);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        getShopDetail(detailResponse);
    }

    private void getShopDetail(String responseString) {
        JSONObject jsonObject = null;
        try {
            JSONObject response;
            if(responseString.contains("\"response\":")) {
                jsonObject = new JSONObject(responseString);
                response = jsonObject.getJSONObject(RESPONSE);
            }else {
                response = new JSONObject(responseString);
            }

            actionBar.setTitle(Html.fromHtml("<b>" + response.getString(NAME) + "</b>"));
            tvName.setText(response.getString(NAME));
            tvAddress.setText(response.getString(ADDRESS));
            tvDistance.setText(FormatValue.getDistance(response.getDouble(DISTANCE)));
            ratingBar.setRating((int) response.getDouble(RATING));

            ImageLoader.getInstance().displayImage(response.getJSONObject(FILE).getString(URL), imgMain, ImageLoaderConfig.options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    progressBar.setVisibility(View.GONE);
                }
            });

            double lat = response.getDouble(LAT);
            double lng = response.getDouble(LONGTH);
            setMapLocation(new LatLng(HomeActivity.currLat, HomeActivity.currLongth), new LatLng(lat, lng));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setMapLocation(final LatLng currLatLng, final LatLng foodLatLng) {

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
                            Log.e("e", "tes");
                            googleMap.addMarker(new MarkerOptions().position(foodLatLng).title(tvName.getText().toString().trim()));

                            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                            googleMap.addPolyline(DirectionConverter.createPolyline(ShowMapFoodActivity.this, directionPositionList, 5, Color.RED));
                        } else {
                            Log.e("e not ok", "tes");
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
