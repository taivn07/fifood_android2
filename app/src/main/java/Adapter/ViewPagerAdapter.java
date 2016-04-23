package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;

import Constant.Constant;
import Constant.ImageLoaderConfig;
import paditech.com.fifood_android.DetailFoodActivity;
import paditech.com.fifood_android.PhotoViewPagerActivity;

/**
 * Created by USER on 15/4/2016.
 */
public class ViewPagerAdapter extends PagerAdapter implements Constant{

    private JSONArray imgUrls;
    private Context context;
    private boolean isClick;

    public ViewPagerAdapter(JSONArray imgUrls, Context context) {
        this.imgUrls = imgUrls;
        this.context = context;
        isClick = true;
    }
    public ViewPagerAdapter(JSONArray imgUrls, Context context, boolean isClick) {
        this.imgUrls = imgUrls;
        this.context = context;
        this.isClick = isClick;
    }

    @Override
    public int getCount() {
        return imgUrls.length();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ImageView img = new ImageView(context);
        try {
            ImageLoader.getInstance().displayImage(imgUrls.getJSONObject(position).getString(URL), img, ImageLoaderConfig.options);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(isClick) {
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PhotoViewPagerActivity.class);
                    intent.putExtra(INDEX, position);
                    intent.putExtra(RESPONSE, String.valueOf(imgUrls));
                    context.startActivity(intent);
                }
            });
        }
        container.addView(img);
        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
