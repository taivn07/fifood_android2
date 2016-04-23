package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import Constant.Constant;
import Constant.ImageLoaderConfig;
import paditech.com.fifood_android.DetailFoodActivity;
import paditech.com.fifood_android.PhotoViewPagerActivity;

/**
 * Created by USER on 15/4/2016.
 */
public class ViewPagerAdapter extends PagerAdapter implements Constant {

    private ArrayList<String> imgUrls;
    private Context context;
    private boolean isClick;
    private PhotoViewPagerActivity activity;
    private JSONArray jsonArray;

    public ViewPagerAdapter(ArrayList<String> imgUrls, Context context, JSONArray jsonArray) {
        this.imgUrls = imgUrls;
        this.context = context;
        this.jsonArray = jsonArray;
        isClick = true;
    }

    public ViewPagerAdapter(ArrayList<String> imgUrls, Context context, boolean isClick, PhotoViewPagerActivity activity) {
        this.imgUrls = imgUrls;
        this.context = context;
        this.isClick = isClick;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return imgUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        RelativeLayout params = new RelativeLayout(context);
        params.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ImageView img = new ImageView(context);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final ProgressBar progressBar = new ProgressBar(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressBar.setLayoutParams(layoutParams);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.GONE);
        ImageLoader.getInstance().displayImage(imgUrls.get(position), img, ImageLoaderConfig.options, new ImageLoadingListener() {
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
        img.setBackgroundColor(Color.BLACK);
        if (isClick) {
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PhotoViewPagerActivity.class);
                    intent.putExtra(INDEX, position);
                    intent.putExtra(RESPONSE, String.valueOf(jsonArray));
                    context.startActivity(intent);
                }
            });
        } else {
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }
        params.addView(img);
        params.addView(progressBar);
        container.addView(params);
        return params;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
