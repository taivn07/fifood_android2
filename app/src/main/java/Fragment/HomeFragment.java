package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.ListFoodAdapter;
import Constant.Constant;
import cz.msebera.android.httpclient.Header;
import paditech.com.fifood.DetailFoodActivity;
import paditech.com.fifood.HomeActivity;
import paditech.com.fifood.R;
import Object.Food;

/**
 * Created by USER on 13/4/2016.
 */
public class HomeFragment extends Fragment implements Constant {

    private ListView lvFood;
    public ArrayList<Food> listFood;
    private ListFoodAdapter adapter;
    private View progressLayout;
    private int index = 0;
    private boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);


        return view;
    }

    private void init(View view) {

        lvFood = (ListView) view.findViewById(R.id.lvFood);
        progressLayout = view.findViewById(R.id.progressLayout);
        adapter = new ListFoodAdapter(getActivity(), listFood);

        lvFood.setAdapter(adapter);


        lvFood.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (view.getLastVisiblePosition() >= totalItemCount - 1 && !isLoading) {
                    index++;
                    getListFoodNear("vi", 25, index);

                    isLoading = true;

                }
            }
        });

    }

    private void getListFoodNear(String lang, int offset, int index) {
        progressLayout.setVisibility(View.VISIBLE);
        AsyncHttpClient aClient = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put(LANG, lang);
        params.put(LAT, HomeActivity.currLat);
        params.put(LONGTH, HomeActivity.currLongth);
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
                    Log.e("SIZE", listFood.size() + "");
                    adapter.notifyDataSetChanged();

                    if (shops.length() == 25)
                        isLoading = false;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressLayout.setVisibility(View.GONE);


            }
        });


    }


}
