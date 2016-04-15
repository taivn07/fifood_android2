package Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import Adapter.ListFoodAdapter;
import Adapter.ListPostAdapter;
import paditech.com.fifood.R;
import Object.Food;

/**
 * Created by USER on 13/4/2016.
 */
public class AccountFragment extends Fragment {

    private ListView lvFood;
    private ArrayList<Food> listFood;
    private ListPostAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        lvFood = (ListView) view.findViewById(R.id.lvFood);
        listFood = new ArrayList<>();
        listFood.add(new Food("Bánh mỳ", "số 2 hai bà trưng, hà nội", 0, 0, null));
        listFood.add(new Food("Bánh mỳ", "số 2 hai bà trưng, hà nội", 0, 0, null));
        listFood.add(new Food("Bánh mỳ", "số 2 hai bà trưng, hà nội", 0, 0, null));
        listFood.add(new Food("Bánh mỳ", "số 2 hai bà trưng, hà nội", 0, 0, null));

        adapter = new ListPostAdapter(getActivity(), listFood);
        lvFood.setAdapter(adapter);

    }

}
