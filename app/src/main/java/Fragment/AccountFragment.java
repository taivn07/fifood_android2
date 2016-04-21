package Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.ListPostAdapter;
import Constant.Constant;
import cz.msebera.android.httpclient.Header;
import paditech.com.fifood_android.R;
import Object.Food;

/**
 * Created by USER on 13/4/2016.
 */
public class AccountFragment extends Fragment implements Constant {

    private ListView lvFood;
    public ArrayList<Food> listFood;
    private ListPostAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setProgressBarIndeterminateVisibility(true);

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        lvFood = (ListView) view.findViewById(R.id.lvFood);
        getListPost("vi", 25, 0, "1", "8K2MY6IVCCOZ");


    }


    private void getListPost(String lang, int offset, int index, String userID, String token) {
        listFood = new ArrayList<>();
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

                        listFood.add(food);
                    }
                    Log.e("SIZE search", listFood.size() + "");

                    adapter = new ListPostAdapter(getActivity(), listFood);
                    lvFood.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getActivity().setProgressBarIndeterminateVisibility(false);


            }
        });
    }

}
