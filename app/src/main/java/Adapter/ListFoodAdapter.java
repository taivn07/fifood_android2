package Adapter;

import android.content.Context;
import android.content.Intent;

import Fragment.NearFragment;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import Constant.Constant;
import Object.Food;
import paditech.com.fifood.DetailFoodActivity;
import paditech.com.fifood.R;
import Constant.ImageLoaderConfig;

/**
 * Created by USER on 13/4/2016.
 */
public class ListFoodAdapter extends BaseAdapter implements Constant {
    private Context context;
    private ArrayList<Food> listFood = new ArrayList<>();
    private NearFragment fragment;

    public ListFoodAdapter(Context context, ArrayList<Food> listFood, NearFragment nearFragment) {
        this.context = context;
        this.listFood = listFood;
        fragment = nearFragment;

        ImageLoaderConfig.imageLoaderConfig();
    }

    public ListFoodAdapter(Context context, ArrayList<Food> listFood) {
        this.context = context;
        this.listFood = listFood;

        ImageLoaderConfig.imageLoaderConfig();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_menu_food, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvFoodName);
            viewHolder.addr = (TextView) convertView.findViewById(R.id.tvAddress);
            viewHolder.far = (TextView) convertView.findViewById(R.id.tvFar);
            viewHolder.rating = (RatingBar) convertView.findViewById(R.id.ratingBar);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.imgFood);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Food food = listFood.get(position);
        viewHolder.name.setText(food.getName());
        viewHolder.addr.setText(food.getAddress());
        viewHolder.far.setText(String.format("%.02f", (float) (food.getDistance())) + " km");
        int rateing = food.getRating();
        viewHolder.rating.setRating(rateing);


        ImageLoaderConfig.imageLoader.displayImage(food.getImgUrl(), viewHolder.img, ImageLoaderConfig.options);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment == null) {
                    Intent intent = new Intent(context, DetailFoodActivity.class);
                    intent.putExtra(ID, food.getShop_id());
                    intent.putExtra(NAME, food.getName());
                    context.startActivity(intent);
                } else {
                    fragment.showMarker(new LatLng(food.getLat(), food.getLongth()), position);
                    fragment.showItemOnTopListview(position);
                    fragment.showMaps();

                }
            }
        });
        return convertView;
    }

    private class ViewHolder {

        TextView name, addr, far;
        ImageView img;
        RatingBar rating;
    }
}
