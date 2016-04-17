package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import Adapter.ListFoodAdapter;
import Constant.Constant;
import paditech.com.fifood.DetailFoodActivity;
import paditech.com.fifood.R;
import Object.Food;

/**
 * Created by USER on 13/4/2016.
 */
public class HomeFragment extends Fragment implements Constant {

    private ListView lvFood;
    public ArrayList<Food> listFood;
    private ListFoodAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);


        return view;
    }

    private void init(View view) {

        lvFood = (ListView) view.findViewById(R.id.lvFood);
        adapter = new ListFoodAdapter(getActivity(), listFood);
        lvFood.setAdapter(adapter);


    }


}
