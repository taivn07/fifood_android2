package paditech.com.fifood_android;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;
import GPSTracker.CheckConnectNetwork;

/**
 * Created by USER on 13/4/2016.
 */
public class HomeActivity extends FragmentActivity implements Constant {

    private final static int GPS_OPEN = 111;
    public ArrayList<Food> listFood, listNear, listPost;
    private RadioGroup rgMenu;
    private HomeFragment homeFragment;
    private NearFragment nearFragment;
    private AddFragment addFragment;
    private SearchFragment searchFragment;
    private AccountFragment accountFragment;
    private ActionBar actionBar;
    private View btnRefresh, progressBar;
    public static double currLat, currLongth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_home);
        actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBgMenu)));
        actionBar.setTitle(Html.fromHtml("<b>Home</b>"));

        CheckNetworkAsynTask checkNetworkAsynTask=new CheckNetworkAsynTask();
        checkNetworkAsynTask.execute();
        init();

    }

    private void init() {
        nearFragment = new NearFragment();
        addFragment = new AddFragment();
        searchFragment = new SearchFragment();
        accountFragment = new AccountFragment();

        btnRefresh = findViewById(R.id.btnRefresh);
        progressBar = findViewById(R.id.pbHeaderProgress);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListFoodNear(LoginActivity.lang, 25, 0);
            }
        });
        rgMenu = (RadioGroup) findViewById(R.id.rgMenu);
        for(int i=1; i<5; i++){
            rgMenu.getChildAt(i).setEnabled(false);
        }
        rgMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btnHome: {
                        actionBar.setTitle(Html.fromHtml("<b>Home</b>"));
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainLayout, homeFragment).commit();
                        break;
                    }
                    case R.id.btnAccount: {
                        accountFragment.listFood = listPost;
                        actionBar.setTitle(Html.fromHtml("<b>Các bài đã đăng</b>"));
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainLayout, accountFragment).commit();
                        break;
                    }
                    case R.id.btnAdd: {
                        actionBar.setTitle(Html.fromHtml("<b>Thêm quán ăn</b>"));
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainLayout, addFragment).commit();
                        break;
                    }
                    case R.id.btnSearch: {
                        actionBar.setTitle(Html.fromHtml("<b>Tìm kiếm</b>"));
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainLayout, searchFragment).commit();
                        break;
                    }
                    case R.id.btnNear: {
                        actionBar.setTitle(Html.fromHtml("<b>Gần đây</b>"));
                        nearFragment.listFood = listNear;
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainLayout, nearFragment).commit();
                        break;
                    }
                }
            }
        });
    }


    private void getListFoodNear(String lang, int offset, int index) {

        homeFragment = new HomeFragment();
        setProgressBarIndeterminateVisibility(true);
        btnRefresh.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        getCurrentLocation();
        listFood = new ArrayList<>();
        listNear = new ArrayList<>();
        AsyncHttpClient aClient = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put(LANG, lang);
        params.put(LAT, currLat);
        params.put(LONGTH, currLongth);
        params.put(OFFSET, offset);
        params.put(INDEX, index);

        if(currLat!=0.0 || currLongth!=0)
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
                        listNear.add(food);
                    }
                    homeFragment.listFood = listFood;
                    Log.e("SIZE", listFood.size() + "");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.mainLayout, homeFragment).commit();

                    for (int i = 1; i < 5; i++) {
                        rgMenu.getChildAt(i).setEnabled(true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setProgressBarIndeterminateVisibility(false);

            }
        });
    }
    private void getCurrentLocation() {

        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){ //if gps is disabled
            AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
            builder.setMessage("Bạn có muốn bật GPS để xác định vị trí của bạn?");
            builder.setPositiveButton("Bật GPS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_OPEN);
                }
            });
            builder.setNegativeButton("Bỏ qua", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }else {
            GPSTracker tracker = new GPSTracker(this);
            currLat = tracker.getLat();
            currLongth = tracker.getLng();
            Log.e("LAT + LNG", currLat + " and " + currLongth);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GPS_OPEN){
            btnRefresh.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

        }
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

    private  class CheckNetworkAsynTask extends AsyncTask{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!CheckConnectNetwork.isNetworkOnline(HomeActivity.this)){
                CheckConnectNetwork.showNotifyNetwork(HomeActivity.this);
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            while(true)
            {
                if(CheckConnectNetwork.isNetworkOnline(HomeActivity.this)) {
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            getListFoodNear(LoginActivity.lang, 25, 0);
        }
    }

}
