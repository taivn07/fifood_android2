package Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import Fragment.AddFragment;
import paditech.com.fifood.R;

/**
 * Created by USER on 17/4/2016.
 */
public class GridPhotoAdapter extends BaseAdapter {

    private AddFragment addFragment;

    private ArrayList<Bitmap> listBitmap;
    private Context context;

    public GridPhotoAdapter(ArrayList<Bitmap> listBitmap, Context context, AddFragment addFragment) {
        this.listBitmap = listBitmap;
        this.context = context;
        this.addFragment = addFragment;
    }

    @Override
    public int getCount() {
        return listBitmap.size();
    }

    @Override
    public Object getItem(int position) {
        return listBitmap.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview_photo, parent, false);
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.img);
        View btnDel = convertView.findViewById(R.id.btnDel);

        img.setImageBitmap(listBitmap.get(position));

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment.deleteImage(position);
            }
        });
        return null;
    }
}
