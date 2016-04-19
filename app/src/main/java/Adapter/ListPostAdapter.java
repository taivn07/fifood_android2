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

import Constant.ImageLoaderConfig;
import Object.Food;
import paditech.com.fifood.R;

/**
 * Created by USER on 13/4/2016.
 */
public class ListPostAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Food> listFood = new ArrayList<>();

    public ListPostAdapter(Context context, ArrayList<Food> listFood) {
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
            convertView = inflater.inflate(R.layout.item_list_post_food, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvFoodName);
            viewHolder.addr = (TextView) convertView.findViewById(R.id.tvAddress);
            viewHolder.sum = (TextView) convertView.findViewById(R.id.tvSum);
            viewHolder.numb = (TextView) convertView.findViewById(R.id.tvNumb);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.imgFood);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Food food = listFood.get(position);
        viewHolder.name.setText(food.getName());
        viewHolder.addr.setText(food.getAddress());
        viewHolder.sum.setText("Phản hồi: " + food.getTotalComment());
        viewHolder.numb.setText(food.getNotifyNum() + "");

        ImageLoaderConfig.imageLoader.displayImage(food.getImgUrl(), viewHolder.img, ImageLoaderConfig.options);

        return convertView;
    }

    private class ViewHolder {

        TextView name, addr, numb, sum;
        ImageView img;

    }
}
