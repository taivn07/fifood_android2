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
public class DetailFoodActivity extends Activity implements Constant {

    private String shopID;
    private double lat, longth;
    private View mainLayout;
    private View btnShowMap;
    private ImageView imgMain;
    private TextView tvName, tvAddress, tvDistance, tvShopID, tvGoodNumb, tvBadNumb;
    private RatingBar ratingBar;
    private ViewPager pager;
    private ExpanableListView lvComment;

    private String detailResponse, imagesResponse;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);
        setContentView(R.layout.activity_detail_food);

        Bundle bundle = getIntent().getExtras();
        shopID = bundle.getString(ID);

        init();

    }

    private void init() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getShopDetail("vi");
        getListImageFood("vi");
        mainLayout = findViewById(R.id.mainLayout);
        imgMain = (ImageView) findViewById(R.id.imgMain);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvShopID = (TextView) findViewById(R.id.tvShopID);
        tvGoodNumb = (TextView) findViewById(R.id.tvGoodCount);
        tvBadNumb = (TextView) findViewById(R.id.tvBadCount);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        pager = (ViewPager) findViewById(R.id.pager);
        lvComment = (ExpanableListView) findViewById(R.id.lvComment);
        btnShowMap = findViewById(R.id.btnShowMap);


        setBtnShowMapClicked();
    }

    private void getShopDetail(String lang) {
        AsyncHttpClient aClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(LANG, lang);
        params.put(LAT, HomeActivity.currLat);
        params.put(SHOP_ID, shopID);
        params.put(LONGTH, HomeActivity.currLongth);

        aClient.post(BASE_URL + SHOP_DETAIL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("JSON", "POST FAIL");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                detailResponse = responseString;
                Log.e("JSON", responseString + "");
                try {
                    JSONObject jsonObject = new JSONObject(responseString);

                    JSONObject response = jsonObject.getJSONObject(RESPONSE);

                    tvShopID.setText(response.getString(ID));
                    tvName.setText(response.getString(NAME));
                    tvAddress.setText(response.getString(ADDRESS));
                    tvDistance.setText((int) response.getDouble(DISTANCE) + " km");
                    tvGoodNumb.setText(response.getString(LIKE_NUMB));
                    tvBadNumb.setText(response.getString(DISLIKE_NUMB));
                    ratingBar.setRating((int) response.getDouble(RATING));

                    ImageLoaderConfig.imageLoader.displayImage(response.getJSONObject(FILE).getString(URL), imgMain, ImageLoaderConfig.options);


                    JSONArray comments = response.getJSONArray(COMMENTS);

                    ListCommentAdapter adapter = new ListCommentAdapter(comments, DetailFoodActivity.this);
                    lvComment.setAdapter(adapter);
                    lvComment.setExpanded(true);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setProgressBarIndeterminateVisibility(false);
                mainLayout.setVisibility(View.GONE);

            }
        });
    }

    private void setBtnShowMapClicked() {
        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailFoodActivity.this, ShowMapFoodActivity.class);
                intent.putExtra(DETAIL_RESPONSE, detailResponse);
                startActivity(intent);
            }
        });
    }

    private void getListImageFood(String lang) {

        AsyncHttpClient aClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(LANG, lang);
        params.put(SHOP_ID, shopID);

        aClient.post(BASE_URL + SHOP_IMAGE, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("JSON", "POST FAIL");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                imagesResponse = responseString;
                Log.e("JSON", responseString + "");
                try {
                    JSONObject jsonObject = new JSONObject(responseString);

                    JSONObject response = jsonObject.getJSONObject(RESPONSE);

                    JSONArray imgs = response.getJSONArray(IMAGES);

                    setImageViewpager(imgs);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private void setImageViewpager(JSONArray a) {


        ViewPagerAdapter adapter = new ViewPagerAdapter(a, this);

        pager.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
