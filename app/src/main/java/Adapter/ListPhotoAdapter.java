package Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;

import Constant.*;

/**
 * Created by USER on 16/4/2016.
 */
public class ListPhotoAdapter extends BaseAdapter implements Constant {
    private JSONArray imgs;
    private Context context;

    public ListPhotoAdapter(JSONArray imgs, Context context) {
        this.imgs = imgs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imgs.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return imgs.getJSONObject(position).getString(URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = new ImageView(context);
        imageView.setMaxHeight(300);
        imageView.setMaxWidth(300);
        try {
            ImageLoaderConfig.imageLoader.displayImage(imgs.getJSONObject(position).getString(URL), imageView, ImageLoaderConfig.options);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imageView;
    }
}
