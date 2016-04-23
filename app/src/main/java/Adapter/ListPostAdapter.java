package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import Constant.Constant;
import Constant.ImageLoaderConfig;
import Object.Food;
import paditech.com.fifood_android.DetailFoodActivity;
import paditech.com.fifood_android.R;

/**
 * Created by USER on 13/4/2016.
 */
public class ListPostAdapter extends BaseAdapter implements Constant {
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

        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_post_food, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvFoodName);
            viewHolder.addr = (TextView) convertView.findViewById(R.id.tvAddress);
            viewHolder.sum = (TextView) convertView.findViewById(R.id.tvSum);
            viewHolder.numb = (TextView) convertView.findViewById(R.id.tvNumb);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.imgFood);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Food food = listFood.get(position);
        viewHolder.name.setText(food.getName());
        viewHolder.addr.setText(food.getAddress());
        viewHolder.sum.setText("Phản hồi: " + food.getTotalComment());
        viewHolder.numb.setText(food.getNotifyNum() + "");

        ImageLoader.getInstance().displayImage(food.getImgUrl(), viewHolder.img, ImageLoaderConfig.options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                viewHolder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                viewHolder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                viewHolder.progressBar.setVisibility(View.GONE);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailFoodActivity.class);
                intent.putExtra(ID, food.getShop_id());
                intent.putExtra(NAME, food.getName());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {

        TextView name, addr, numb, sum;
        ImageView img;
        ProgressBar progressBar;
    }
}
