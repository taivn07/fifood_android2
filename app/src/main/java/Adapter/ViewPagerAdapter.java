package Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;

import Constant.Constant;
import Constant.ImageLoaderConfig;
import paditech.com.fifood.R;

/**
 * Created by USER on 15/4/2016.
 */
public class ViewPagerAdapter extends PagerAdapter implements Constant{

    private JSONArray imgUrls;
    private Context context;


    public ViewPagerAdapter(JSONArray imgUrls, Context context) {
        this.imgUrls = imgUrls;
        this.context = context;
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
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView img = new ImageView(context);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        try {
            ImageLoaderConfig.imageLoader.displayImage(imgUrls.getJSONObject(position).getString(URL), img, ImageLoaderConfig.options);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        container.addView(img);

        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
