package Constant;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import paditech.com.fifood_android.R;

/**
 * Created by USER on 15/4/2016.
 */
public class ImageLoaderConfig {

    public static ImageLoader imageLoader;
    public static DisplayImageOptions options;

    public  static void imageLoaderConfig(){

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .build();

    }
}
