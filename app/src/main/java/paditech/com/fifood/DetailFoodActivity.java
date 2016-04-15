package paditech.com.fifood;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import Constant.Constant;
import cz.msebera.android.httpclient.Header;

/**
 * Created by USER on 14/4/2016.
 */
public class DetailFoodActivity extends Activity implements Constant {

    private String shopID;
    private double lat, longth;
    private View mainLayout;
    private ImageView imgMain;
    private TextView tvName, tvAddress, tvDistance, tvShopID, tvGoodNumb, tvBadNumb;
    private CheckBox rates[] = new CheckBox[5];
    private ViewPager pager;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);
        setContentView(R.layout.activity_detail_food);

        Bundle bundle = getIntent().getExtras();
        shopID = bundle.getString(ID);
        lat = bundle.getDouble(LAT);
        longth = bundle.getDouble(LONGTH);

        init();

    }

    private void init() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getShopDetail("vi");
        mainLayout = findViewById(R.id.mainLayout);
        imgMain = (ImageView) findViewById(R.id.imgMain);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvShopID = (TextView) findViewById(R.id.tvShopID);
        tvGoodNumb = (TextView) findViewById(R.id.tvGoodCount);
        tvBadNumb = (TextView) findViewById(R.id.tvBadCount);
        rates[0] = (CheckBox) findViewById(R.id.btnRate1);
        rates[1] = (CheckBox) findViewById(R.id.btnRate2);
        rates[2] = (CheckBox) findViewById(R.id.btnRate3);
        rates[3] = (CheckBox) findViewById(R.id.btnRate4);
        rates[4] = (CheckBox) findViewById(R.id.btnRate5);
        pager = (ViewPager) findViewById(R.id.pager);
    }

    private void getShopDetail(String lang) {
        AsyncHttpClient aClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(LANG, lang);
        params.put(LAT, lat);
        params.put(SHOP_ID, shopID);
        params.put(LONGTH, longth);

        aClient.post(BASE_URL + SHOP_DETAIL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("JSON", "POST FAIL");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e("JSON", responseString + "");
                try {
                    JSONObject jsonObject = new JSONObject(responseString);

                    JSONObject response = jsonObject.getJSONObject(RESPONSE);

                    tvShopID.setText(response.getString(ID));
                    tvName.setText(response.getString(NAME));
                    tvAddress.setText(response.getString(ADDRESS));
                    tvDistance.setText(response.getString(DISTANCE) + " km");
                    tvGoodNumb.setText(response.getString(LIKE_NUMB));
                    tvBadNumb.setText(response.getString(DISLIKE_NUMB));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setProgressBarIndeterminateVisibility(false);
                mainLayout.setVisibility(View.GONE);

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
