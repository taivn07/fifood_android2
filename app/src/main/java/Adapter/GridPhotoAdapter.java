package Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;

import Fragment.AddFragment;
import paditech.com.fifood_android.LoginActivity;
import paditech.com.fifood_android.R;
import Object.ImageUpload;

/**
 * Created by USER on 17/4/2016.
 */
public class GridPhotoAdapter extends BaseAdapter {

    private AddFragment addFragment;
    private ArrayList<ImageUpload> listBitmap;
    private Context context;
    public GridPhotoAdapter(ArrayList<ImageUpload> listBitmap, Context context, AddFragment addFragment) {
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

        convertView = inflater.inflate(R.layout.item_gridview_photo, parent, false);

        View btnDel = convertView.findViewById(R.id.btnDel);
        DonutProgress circleProgress = (DonutProgress) convertView.findViewById(R.id.pbUploadImg);
        ImageView img = (ImageView) convertView.findViewById(R.id.img);

        circleProgress.setProgress(listBitmap.get(position).getProgress());
        img.setImageBitmap(listBitmap.get(position).getBitmap());
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment.deleteImage(position);
            }
        });

        if (listBitmap.get(position).getProgress() == 0) {
            circleProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //addFragment.listBitmap.remove(position);
                    addFragment.uploadImage(listBitmap.get(position).getFile(), position, LoginActivity.user.getUserID()
                            , LoginActivity.user.getToken(), LoginActivity.lang);

                }
            });
        }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addFragment.isSelector) {
                        addFragment.setImageAvatarID(position);
                        addFragment.gridPhoto.setSelection(position);
                    }
                }
            });

        return convertView;
    }


}
