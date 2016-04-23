package paditech.com.fifood_android;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.ViewPagerAdapter;
import Constant.Constant;

/**
 * Created by USER on 22/4/2016.
 */

public class PhotoViewPagerActivity extends Activity implements Constant{
    private ViewPager pager;
    private ArrayList<String> listImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewpager_photo);

        pager = (ViewPager) findViewById(R.id.pager);

        listImgUrl =new ArrayList<>();

        Bundle bundle= getIntent().getExtras();
        if(bundle.getString(URL)!=null){
            listImgUrl.add(bundle.getString(URL));
            setImageViewpager(listImgUrl);
        }else {
            int index = bundle.getInt(INDEX);
            String responseJSON = bundle.getString(RESPONSE);
            try {
                JSONArray imgs = new JSONArray(responseJSON);
                for(int i=0; i<imgs.length(); i++){
                    listImgUrl.add(imgs.getJSONObject(i).getString(URL));
                }
                setImageViewpager(listImgUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pager.setCurrentItem(index);
        }
    }

    private void setImageViewpager(ArrayList<String> a) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(a, this, false, this);
        pager.setAdapter(adapter);

    }

}
