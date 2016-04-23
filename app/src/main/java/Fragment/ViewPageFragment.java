package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;

import Constant.ImageLoaderConfig;
import paditech.com.fifood_android.PhotoViewPagerActivity;
import paditech.com.fifood_android.R;

/**
 * Created by USER on 23/4/2016.
 */
public class ViewPageFragment extends Fragment {

    public String imgURL;

    public ViewPageFragment(String imgURL) {
        this.imgURL = imgURL;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ImageView img = new ImageView(getActivity());
        ImageLoader.getInstance().displayImage(imgURL, img, ImageLoaderConfig.options);

        container.addView(img);
        return img;



    }
}
