package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Object.Food;
import paditech.com.fifood.R;

/**
 * Created by USER on 13/4/2016.
 */
public class ListFoodAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Food> listFood = new ArrayList<>();

    public ListFoodAdapter(Context context, ArrayList<Food> listFood) {
        this.context = context;
        this.listFood = listFood;
    }

    @Override
    public int getCount() {
        return listFood.size();
    }

    @Override
    public Object getItem(int position) {
        return listFood.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_menu_food, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvFoodName);
            viewHolder.addr = (TextView) convertView.findViewById(R.id.tvAddress);
            viewHolder.far = (TextView) convertView.findViewById(R.id.tvFar);
            viewHolder.rate1 = (CheckBox) convertView.findViewById(R.id.btnRate1);
            viewHolder.rate2 = (CheckBox) convertView.findViewById(R.id.btnRate2);
            viewHolder.rate3 = (CheckBox) convertView.findViewById(R.id.btnRate3);
            viewHolder.rate4 = (CheckBox) convertView.findViewById(R.id.btnRate4);
            viewHolder.rate5 = (CheckBox) convertView.findViewById(R.id.btnRate5);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.imgFood);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Food food = listFood.get(position);
        viewHolder.name.setText(food.getName());
        viewHolder.addr.setText(food.getAddress());
        viewHolder.far.setText("Cach 12 km");
        viewHolder.rate1.setChecked(true);
        viewHolder.rate4.setChecked(true);
        return convertView;
    }

    private class ViewHolder {

        TextView name, addr, far;
        ImageView img;
        CheckBox rate1, rate2, rate3, rate4, rate5;
    }
}
