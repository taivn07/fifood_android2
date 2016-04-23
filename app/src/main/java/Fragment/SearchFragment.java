package Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.EditText;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
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
import paditech.com.fifood_android.HomeActivity;
import paditech.com.fifood_android.LoginActivity;
import paditech.com.fifood_android.R;
import Object.Food;
import Constant.HideKeyBoard;

/**
 * Created by USER on 13/4/2016.
 */
public class SearchFragment extends Fragment implements Constant {

    private EditText etName, etAddress;
    private View btnSearch;
    private ObservableListView lvFood;
    private ArrayList<Food> listFood;
    private ListFoodAdapter adapter;
    private View mainLayout, progressLayout;
    private SwipeRefreshLayout refreshLayout;
    private int index = 0;
    private boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        init(view);
        getActivity().setProgressBarIndeterminateVisibility(false);
        return view;
    }

    private void init(View view) {
        listFood = new ArrayList<>();
        adapter = new ListFoodAdapter(getActivity(), listFood);
        etAddress = (EditText) view.findViewById(R.id.etAddress);
        etName = (EditText) view.findViewById(R.id.etName);
        lvFood = (ObservableListView) view.findViewById(R.id.lvFood);
        btnSearch = view.findViewById(R.id.btnSearch);
        mainLayout = view.findViewById(R.id.mainLayout);
        progressLayout = view.findViewById(R.id.progressLayout);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        listFood = new ArrayList<>();
        setBtnSearchClicked();
        hideKeyBoard();
        setLvFoodScrollChanged();
        setRefreshLayoutLoading();

        refreshLayout.setRefreshing(false);
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
                getListFood(LoginActivity.lang, 25, 0);
            }
        });
    }

    private void setLvFoodScrollChanged() {
        lvFood.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
            }

            @Override
            public void onDownMotionEvent() {
            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {

                if (scrollState == ScrollState.UP) {
                    hideMainLayout();
                } else if (scrollState == ScrollState.DOWN) {
                    showMainLayout();
                }
            }
        });

        lvFood.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (view.getLastVisiblePosition() >= totalItemCount - 1 && !isLoading) {
                    index++;
                    progressLayout.setVisibility(View.VISIBLE);
                    getListFood(LoginActivity.lang, 25, index);
                    isLoading = true;
                }
            }
        });

    }

    public void hideMainLayout() {
        if (mainLayout.isShown()) {
            TranslateAnimation animate = new TranslateAnimation(0, 0, 0, -mainLayout.getHeight());
            animate.setDuration(300);
            animate.setFillAfter(true);
            mainLayout.startAnimation(animate);
            animate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mainLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    public void showMainLayout() {
        if (!mainLayout.isShown()) {
            TranslateAnimation animate = new TranslateAnimation(0, 0, -mainLayout.getHeight(), 0);
            animate.setDuration(300);
            animate.setFillAfter(true);
            mainLayout.startAnimation(animate);
            animate.setAnimationListener(new Animation.AnimationListener() {
                                             @Override
                                             public void onAnimationStart(Animation animation) {
                                                 mainLayout.setVisibility(View.VISIBLE);
                                             }

                                             @Override
                                             public void onAnimationEnd(Animation animation) {
                                             }

                                             @Override
                                             public void onAnimationRepeat(Animation animation) {
                                             }
                                         }
            );
        }
    }

    private void setRefreshLayoutLoading() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                listFood = new ArrayList<>();
                getListFood(LoginActivity.lang, 25, 0);
                index = 0;
            }
        });
    }

    private void getListFood(String lang, int offset, final int index) {
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
                refreshLayout.setRefreshing(false);
                progressLayout.setVisibility(View.GONE);
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

                    if (index == 0) {
                        adapter = new ListFoodAdapter(getActivity(), listFood);
                        lvFood.setAdapter(adapter);
                        refreshLayout.setRefreshing(false);
                    } else {
                        if (shops.length() == 25)
                            isLoading = false;
                        adapter.notifyDataSetChanged();
                        progressLayout.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

}
