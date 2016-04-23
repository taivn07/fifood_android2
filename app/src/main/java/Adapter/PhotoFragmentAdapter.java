package Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 23/4/2016.
 */
public class PhotoFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    public PhotoFragmentAdapter(FragmentManager fm, ArrayList<Fragment> f) {
        super(fm);
        fragments=f;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
