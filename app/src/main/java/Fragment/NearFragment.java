package Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import Adapter.ListFoodAdapter;
import Constant.Constant;
import cz.msebera.android.httpclient.Header;
import paditech.com.fifood_android.DetailFoodActivity;
import paditech.com.fifood_android.HomeActivity;
import paditech.com.fifood_android.LoginActivity;
import paditech.com.fifood_android.R;
import Object.Food;
import Constant.ImageLoaderConfig;
import paditech.com.fifood_android.ShowMapFoodActivity;

/**
 * Created by USER on 13/4/2016.
 */
public class NearFragment extends Fragment implements Constant {

    private ObservableListView lvFood;
    public ArrayList<Food> listFood;
    private ListFoodAdapter adapter;
    private MarkerOptions markerOptions;
    private GoogleMap googleMap;
    private View progressLayout;
    private Marker marker;
    private ArrayList<Marker> listMarker;
    private View view;
    private int index = 0;
    private boolean isLoading = false;
    private View maps;
    private SwipeRefreshLayout refreshLayout;
    public String detailResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_near, container, false);
            init(view);

        } catch (InflateException e) {
        }
        return view;
    }

    private void init(View view) {
        lvFood = (ObservableListView) view.findViewById(R.id.lvFood);
        maps = view.findViewById(R.id.mapLayout);
        progressLayout = view.findViewById(R.id.progressLayout);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        adapter = new ListFoodAdapter(getActivity(), listFood, this);
        lvFood.setAdapter(adapter);

        setMapLocation();
        setLvFoodChanged();
        setRefreshLayoutLoading();
    }

    private void setRefreshLayoutLoading(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                listFood = new ArrayList<Food>();
                getListFoodNear(LoginActivity.lang, 25, 0);
                index = 0;
            }
        });
    }

    private void setLvFoodChanged() {
        lvFood.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
            }

            @Override
            public void onDownMotionEvent() {
            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {
                if (maps == null) {
                    return;
                }
                if (scrollState == ScrollState.UP) {
                    hideMaps();
                } else if (scrollState == ScrollState.DOWN) {
                    showMaps();
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
                    getListFoodNear(LoginActivity.lang, 25, index);
                    isLoading = true;
                }
            }
        });

    }

    private void getListFoodNear(String lang, int offset, final int index) {
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
                    Log.e("SIZE", listFood.size() + "");
                    setMapLocation();
                    if(index == 0){
                        adapter = new ListFoodAdapter(getActivity(), listFood, NearFragment.this);
                        lvFood.setAdapter(adapter);
                        refreshLayout.setRefreshing(false);
                    }else {
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

    public void hideMaps() {
        if (maps.isShown()) {
            TranslateAnimation animate = new TranslateAnimation(0, 0, 0, -maps.getHeight());
            animate.setDuration(300);
            animate.setFillAfter(true);
            maps.startAnimation(animate);
            animate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    maps.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        }
    }

    public void showMaps() {
        if (!maps.isShown()) {
            TranslateAnimation animate = new TranslateAnimation(0, 0, -maps.getHeight(), 0);
            animate.setDuration(300);
            animate.setFillAfter(true);
            maps.startAnimation(animate);
            animate.setAnimationListener(new Animation.AnimationListener() {
                                             @Override
                                             public void onAnimationStart(Animation animation) {
                                                 maps.setVisibility(View.VISIBLE);
                                             }

                                             @Override
                                             public void onAnimationEnd(Animation animation) {}

                                             @Override
                                             public void onAnimationRepeat(Animation animation) {}
                                         }
            );
        }
    }

    public void showItemOnTopListview(int position) {
        lvFood.smoothScrollToPosition(position);
        if (!maps.isShown())
            lvFood.setSelection(position);
    }

    private void setMapLocation() {
        markerOptions = new MarkerOptions();
        googleMap = ((MapFragment) getActivity()
                .getFragmentManager().findFragmentById(R.id.map)).getMap();
        if (googleMap != null) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            listMarker = new ArrayList<>();
            for (int i = 0; i < listFood.size(); i++) {
                LatLng latLng = new LatLng(listFood.get(i).getLat(), listFood.get(i).getLongth());
                builder.include(latLng);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_map_app));
                markerOptions.position(latLng);
                marker = googleMap.addMarker(markerOptions);
                listMarker.add(marker);

            }
            LatLngBounds bounds = builder.build();
            final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 5);

            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    googleMap.moveCamera(cu);
                    googleMap.animateCamera(cu);
                }
            });

            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    int position = listMarker.indexOf(marker);
                    if (position != -1) {
                        Intent intent = new Intent(getActivity(), DetailFoodActivity.class);
                        intent.putExtra(ID, listFood.get(position).getShop_id());
                        intent.putExtra(NAME, listFood.get(position).getName());
                        getActivity().startActivity(intent);
                    }
                }
            });

            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    final int position = listMarker.indexOf(marker);
                    if (position != - 1 && listFood.size() > position) {
                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = inflater.inflate(R.layout.item_marker_maps, null);
                        TextView name = (TextView) v.findViewById(R.id.tvName);
                        TextView distance = (TextView) v.findViewById(R.id.tvDistance);
                        ImageView img = (ImageView) v.findViewById(R.id.img);
                        RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
                        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
                        name.setText(listFood.get(position).getName());
                        ImageLoader.getInstance().displayImage(listFood.get(position).getImgUrl(), img, ImageLoaderConfig.options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {
                                progressBar.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                        distance.setText(String.format("%.02f", (float) listFood.get(position).getDistance()) + " m");
                        ratingBar.setRating(listFood.get(position).getRating());
                        return v;
                    }
                    return null;
                }
            });

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    try {
                        int index= listMarker.indexOf(marker);
                        if(index > -1) {
                            JSONObject jsonObject = new JSONObject(detailResponse);
                            JSONObject response = jsonObject.getJSONObject(RESPONSE);
                            JSONArray shops = response.getJSONArray(SHOPS);
                            String rs = shops.getJSONObject(index).toString();
                            Intent intent = new Intent(getActivity(), ShowMapFoodActivity.class);
                            intent.putExtra(DETAIL_RESPONSE, rs );
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return false;
                }
            });
        }
    }
    public void showMarker(LatLng latLng, int position) {
        if(googleMap!=null) {
            CameraUpdate cu = CameraUpdateFactory.newLatLng(latLng);
            googleMap.moveCamera(cu);
            googleMap.animateCamera(cu);
            listMarker.get(position).showInfoWindow();
        }
    }

}
