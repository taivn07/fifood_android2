package Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.ListPostAdapter;
import Constant.Constant;
import cz.msebera.android.httpclient.Header;
import paditech.com.fifood_android.LoginActivity;
import paditech.com.fifood_android.R;
import Object.Food;
import Constant.ImageLoaderConfig;

/**
 * Created by USER on 13/4/2016.
 */
public class AccountFragment extends Fragment implements Constant {

    private ListView lvFood;
    public ArrayList<Food> listPost;
    private ListPostAdapter adapter;
    private Button btnLogout;
    private CircularImageView imgAvatar;
    private TextView tvNickName, tvPostTotal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        lvFood = (ListView) view.findViewById(R.id.lvFood);
        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        imgAvatar = (CircularImageView) view.findViewById(R.id.imgAvatar);
        tvNickName = (TextView) view.findViewById(R.id.tvNickname);
        tvPostTotal = (TextView) view.findViewById(R.id.tvPostTotal);
        adapter = new ListPostAdapter(getActivity(), listPost);
        lvFood.setAdapter(adapter);

        updateUser();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    LoginManager.getInstance().logOut();
                    SharedPreferences settings = getActivity().getSharedPreferences(PACKAGE_NAME, getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor prefEditor = settings.edit();
                    prefEditor.putString(ID, null);
                    prefEditor.putString(TOKEN, null);
                    prefEditor.commit();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();

            }
        });
    }

    public void updateList() {
        adapter.notifyDataSetChanged();
        tvPostTotal.setText("Tổng số bài đăng : "+listPost.size());
    }

    private void updateUser(){
        if(LoginActivity.user!=null) {
            btnLogout.setText("Đăng xuất");
            imgAvatar.setVisibility(View.VISIBLE);
            if (LoginActivity.user.getNickname() == null) {
                SharedPreferences settings = getActivity().getSharedPreferences(PACKAGE_NAME, getActivity().MODE_PRIVATE);
                getUserInfo(LoginActivity.lang, settings.getString(FB_TOKEN, null));
            } else {
                tvNickName.setText(LoginActivity.user.getNickname());
                ImageLoader.getInstance().displayImage(LoginActivity.user.getProfile_image(), imgAvatar, ImageLoaderConfig.options);
            }
        }
        else {
            btnLogout.setText("Đăng nhập");
            imgAvatar.setVisibility(View.GONE);
        }

    }

    private void getUserInfo(String lang, String token) {

        Log.e("TOKEN", token+"");
        AsyncHttpClient aClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(LANG, lang);
        params.put(FB_TOKEN, token);
        aClient.post(BASE_URL + AUTH, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("POST AUTH", "FAIL");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("GET INFO", responseString + "");
                    JSONObject response = new JSONObject(responseString).getJSONObject(RESPONSE);
                    if (responseString.contains("\"result\":")) {

                    } else {
                        JSONObject u = response.getJSONObject(USER);
                        tvNickName.setText(u.getString(NICKNAME));
                        ImageLoader.getInstance().displayImage(u.getString(PROFILE_IMAGE), imgAvatar, ImageLoaderConfig.options);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void getListPost(String lang, int offset, int index, String userID, String token) {
        AsyncHttpClient aClient = new AsyncHttpClient();


        RequestParams params = new RequestParams();
        params.put(LANG, lang);
        params.put(OFFSET, offset);
        params.put(INDEX, index);
        params.put(USER_ID, userID);
        params.put(TOKEN, token);

        aClient.post(BASE_URL + MY_SHOP, params, new TextHttpResponseHandler() {
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
                        food.setName(shops.getJSONObject(i).getString(NAME));
                        food.setRating(shops.getJSONObject(i).getInt(RATING));
                        food.setShop_id(shops.getJSONObject(i).getString(ID));
                        food.setImgUrl(shops.getJSONObject(i).getJSONObject(FILE).getString(URL));
                        food.setTotalComment(shops.getJSONObject(i).getInt(TOTAL_COMMENT));
                        food.setNotifyNum(shops.getJSONObject(i).getInt(NOTIFY_NUM));

                        listPost.add(food);
                    }
                    Log.e("SIZE search", listPost.size() + "");

                    adapter = new ListPostAdapter(getActivity(), listPost);
                    lvFood.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
