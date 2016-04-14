package Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import Adapter.ListFoodAdapter;
import paditech.com.fifood.DetailFoodActivity;
import paditech.com.fifood.R;
import Object.Food;

/**
 * Created by USER on 13/4/2016.
 */
public class NearFragment extends Fragment {

    private ListView lvFood;
    private ArrayList<Food> listFood;
    private ListFoodAdapter adapter;

    private View view;
    @Nullable
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

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init(View view) {

        lvFood = (ListView) view.findViewById(R.id.lvFood);
        listFood = new ArrayList<>();
        listFood.add(new Food("Bánh mỳ", "số 2 hai bà trưng, hà nội", 0, 0, null, null));
        listFood.add(new Food("Bánh mỳ", "số 2 hai bà trưng, hà nội", 0, 0, null, null));
        listFood.add(new Food("Bánh mỳ", "số 2 hai bà trưng, hà nội", 0, 0, null, null));
        listFood.add(new Food("Bánh mỳ", "số 2 hai bà trưng, hà nội", 0, 0, null, null));

        adapter = new ListFoodAdapter(getActivity(), listFood);
        lvFood.setAdapter(adapter);

        lvFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("YES", "clicked :" + position);
                Intent intent = new Intent(getActivity(), DetailFoodActivity.class);
                startActivity(intent);
            }
        });

    }

}
