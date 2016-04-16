package paditech.com.fifood;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import Constant.ImageLoaderConfig;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Adapter.ListCommentAdapter;
import Adapter.ViewPagerAdapter;
import Constant.Constant;
import cz.msebera.android.httpclient.Header;
import Constant.ExpanableListView;

/**
 * Created by USER on 14/4/2016.
 */
public class ShowMapFoodActivity extends Activity implements Constant {

    private double lat, longth;
    private ImageView imgMain;
    private TextView tvName, tvAddress, tvDistance;
    private RatingBar ratingBar;
    private String detailResponse;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);
        setContentView(R.layout.activity_map_food);

        Bundle bundle = getIntent().getExtras();
        detailResponse = bundle.getString(DETAIL_RESPONSE);

        init();

    }

    private void init() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getShopDetail(detailResponse);
        imgMain = (ImageView) findViewById(R.id.imgMain);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);


    }

    private void getShopDetail(String responseString) {
        /*JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseString);
            JSONObject response = jsonObject.getJSONObject(RESPONSE);

            tvName.setText(response.getString(NAME));
            tvAddress.setText(response.getString(ADDRESS));
            tvDistance.setText((int) response.getDouble(DISTANCE) + " km");
            ratingBar.setRating((int) response.getDouble(RATING));

            ImageLoaderConfig.imageLoader.displayImage(response.getJSONObject(FILE).getString(URL), imgMain, ImageLoaderConfig.options);


            JSONArray images = response.getJSONArray(IMAGES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
