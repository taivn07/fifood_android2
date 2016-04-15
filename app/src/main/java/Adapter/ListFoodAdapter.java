package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import Constant.Constant;
import Object.Food;
import paditech.com.fifood.DetailFoodActivity;
import paditech.com.fifood.R;

/**
 * Created by USER on 13/4/2016.
 */
public class ListFoodAdapter extends BaseAdapter implements Constant {
    private Context context;
    private ArrayList<Food> listFood = new ArrayList<>();

    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    private String key;

    public ListFoodAdapter(Context context, ArrayList<Food> listFood, String key) {
        this.context = context;
        this.listFood = listFood;
        this.key = key;

        configOptionImage();
    }

    public void configOptionImage() {
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .build();
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
            viewHolder.rates[0] = (CheckBox) convertView.findViewById(R.id.btnRate1);
            viewHolder.rates[1] = (CheckBox) convertView.findViewById(R.id.btnRate2);
            viewHolder.rates[2] = (CheckBox) convertView.findViewById(R.id.btnRate3);
            viewHolder.rates[3] = (CheckBox) convertView.findViewById(R.id.btnRate4);
            viewHolder.rates[4] = (CheckBox) convertView.findViewById(R.id.btnRate5);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.imgFood);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Food food = listFood.get(position);
        viewHolder.name.setText(food.getName());
        viewHolder.addr.setText(food.getAddress());
        viewHolder.far.setText(Double.toString(food.getDistance()).substring(0, 3) + " km");
        int rateing = food.getRating();

        for (int i = 0; i < 5; i++) {
            if (i < rateing)
                viewHolder.rates[i].setChecked(true);
            else viewHolder.rates[i].setChecked(false);
        }

        imageLoader.displayImage(food.getImgUrl(), viewHolder.img, options);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.equals(HOME_FRAGMENT)) {
                    Intent intent = new Intent(context, DetailFoodActivity.class);
                    intent.putExtra(ID, food.getShop_id());
                    intent.putExtra(LAT, food.getLat());
                    intent.putExtra(LONGTH, food.getLongth());
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {

        TextView name, addr, far;
        ImageView img;
        CheckBox rates[] = new CheckBox[5];
    }
}
