package paditech.com.fifood;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Constant.Constant;
import Fragment.AccountFragment;
import Fragment.AddFragment;
import Fragment.HomeFragment;
import Fragment.NearFragment;
import Fragment.SearchFragment;
import GPSTracker.GPSTracker;
import Object.Food;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import Adapter.ListFoodAdapter;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import GPSTracker.CheckConnectNetwork;


/**
 * Created by USER on 13/4/2016.
 */
public class HomeActivity extends FragmentActivity implements Constant {

    public ArrayList<Food> listFood;

    private RadioGroup rgMenu;

    private HomeFragment homeFragment;
    private NearFragment nearFragment;
    private AddFragment addFragment;
    private SearchFragment searchFragment;
    private AccountFragment accountFragment;

    public static double currLat, currLongth;
    public static String userID = "4", token = "8K2MY6IVCCOZ", nickname="Paditech";
    public static String profileImageUrl = "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xat1/v/t1.0-1/s200x200/11204909_122874381407241_2799622312940915518_n.jpg?oh=138425eec69e4750b27f8d7d74079d6b&oe=57080315&__gda__=1463440946_3dabeb9fe56b383205d858007b46f2a3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);
        setContentView(R.layout.activity_home);

        getListFoodNear("vi", 25, 0);

        ActionBar actionBar = getActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBgMenu)));
        actionBar.setTitle(Html.fromHtml("<b>Home</b>"));


        init();


    }

    private void init() {
        homeFragment = new HomeFragment();
        nearFragment = new NearFragment();
        addFragment = new AddFragment();
        searchFragment = new SearchFragment();
        accountFragment = new AccountFragment();


        rgMenu = (RadioGroup) findViewById(R.id.rgMenu);

        rgMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                switch (checkedId) {

                    case R.id.btnHome: {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainLayout, homeFragment).commit();
                        break;

                    }
                    case R.id.btnAccount: {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainLayout, accountFragment).commit();
                        break;

                    }
                    case R.id.btnAdd: {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainLayout, addFragment).commit();
                        break;

                    }
                    case R.id.btnSearch: {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainLayout, searchFragment).commit();
                        break;

                    }
                    case R.id.btnNear: {
                        nearFragment.listFood = listFood;
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainLayout, nearFragment).commit();
                        break;

                    }
                }
            }
        });
    }


    private void getListFoodNear(String lang, int offset, int index) {

        getCurrentLocation();
        listFood = new ArrayList<>();
        AsyncHttpClient aClient = new AsyncHttpClient();
/*
        aClient.addHeader(KEY_X_API_TOKEN, API_TOCKEN);
        aClient.addHeader(KEY_X_APP_PLATFORM, PLATFORM);
        aClient.addHeader(KEY_X_APP_VERSION, VERSION);
        aClient.addHeader(KEY_X_LOCATE, LOCATE);
        aClient.addHeader(KEY_X_UID, UID);*/

        RequestParams params = new RequestParams();
        params.put(LANG, lang);
        params.put(LAT, currLat);
        params.put(LONGTH, currLongth);
        params.put(OFFSET, offset);
        params.put(INDEX, index);


        aClient.post(BASE_URL + RECOMMENT, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("JSON", "POST FAIL");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);

                    JSONObject response = jsonObject.getJSONObject(RESPONSE);

                    JSONArray shops = response.getJSONArray(SHOPS);

                    for (int i = 0; i < shops.length(); i++) {
                        Food food = new Food();
                        food.setAddress(shops.getJSONObject(i).getString(ADDRESS));
                        food.setDistance(shops.getJSONObject(i).getDouble(DISTANCE));
                        food.setLat(shops.getJSONObject(i).getDouble(LAT));
                        food.setLongth(shops.getJSONObject(i).getDouble(LONGTH));
                        food.setName(shops.getJSONObject(i).getString(NAME));
                        food.setRating(shops.getJSONObject(i).getInt(RATING));
                        food.setShop_id(shops.getJSONObject(i).getString(ID));
                        food.setImgUrl(shops.getJSONObject(i).getJSONObject(FILE).getString(URL));

                        listFood.add(food);
                    }
                    homeFragment.listFood = listFood;
                    Log.e("SIZE", listFood.size() + "");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.mainLayout, homeFragment).commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setProgressBarIndeterminateVisibility(false);


            }
        });


    }

    private void getCurrentLocation() {
        GPSTracker tracker = new GPSTracker(this);
        currLat = tracker.getLat();
        currLongth = tracker.getLng();

        Log.e("LAT + LNG", currLat + " and " + currLongth);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
