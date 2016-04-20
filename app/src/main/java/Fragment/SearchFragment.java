package Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import Constant.ExpandableHeightListView;
import cz.msebera.android.httpclient.Header;
import paditech.com.fifood.HomeActivity;
import paditech.com.fifood.R;
import Object.Food;
import Constant.HideKeyBoard;

/**
 * Created by USER on 13/4/2016.
 */
public class SearchFragment extends Fragment implements Constant {

    private EditText etName, etAddress;
    private View btnSearch;
    private ExpandableHeightListView lvFood;
    private ArrayList<Food> listFood;
    private ListFoodAdapter adapter;
    private View mainLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setProgressBarIndeterminateVisibility(false);

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        etAddress = (EditText) view.findViewById(R.id.etAddress);
        etName = (EditText) view.findViewById(R.id.etName);
        lvFood = (ExpandableHeightListView) view.findViewById(R.id.lvFood);
        btnSearch = view.findViewById(R.id.btnSearch);
        mainLayout = view.findViewById(R.id.mainLayout);

        listFood = new ArrayList<>();
        lvFood.setExpanded(true);

        setBtnSearchClicked();
        hideKeyBoard();


    }

    private void hideKeyBoard() {

        btnSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                HideKeyBoard.hideSoftKeyboard(getActivity());
                return false;
            }
        });
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                HideKeyBoard.hideSoftKeyboard(getActivity());

                return false;
            }
        });
    }

    private void setBtnSearchClicked() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setProgressBarIndeterminateVisibility(true);
                getListFood("vi", 25, 0);
            }
        });
    }

    private void getListFood(String lang, int offset, int index) {
        listFood = new ArrayList<>();
        AsyncHttpClient aClient = new AsyncHttpClient();


        RequestParams params = new RequestParams();
        params.put(LANG, lang);
        params.put(LAT, HomeActivity.currLat);
        params.put(LONGTH, HomeActivity.currLongth);
        params.put(OFFSET, offset);
        params.put(INDEX, index);
        params.put(NAME, etName.getText().toString().trim());
        params.put(ADDRESS, etAddress.getText().toString().trim());

        aClient.post(BASE_URL + SHOP_SEARCH, params, new TextHttpResponseHandler() {
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
                    Log.e("SIZE search", listFood.size() + "");

                    adapter = new ListFoodAdapter(getActivity(), listFood);
                    lvFood.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getActivity().setProgressBarIndeterminateVisibility(false);


            }
        });
    }

}
