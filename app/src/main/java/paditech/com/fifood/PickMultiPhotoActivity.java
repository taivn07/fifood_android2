package paditech.com.fifood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import Constant.ImageLoaderConfig;

/**
 * Created by USER on 17/4/2016.
 */
public class PickMultiPhotoActivity extends Activity {

    private ArrayList<String> imageUrls;
    private DisplayImageOptions options;
    private ImageAdapter imageAdapter;
    public static ArrayList<String> selectedItems;

    private Button btnSelectPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_multi_photo);


        final String[] columns = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");

        this.imageUrls = new ArrayList<String>();

        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));

            System.out.println("=====> Array path => " + imageUrls.get(i));
        }

        options = new DisplayImageOptions.Builder().cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY).cacheOnDisk(true)
                .cacheInMemory(true)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher).build();

        imageAdapter = new ImageAdapter(this, imageUrls);

        GridView gridView = (GridView) findViewById(R.id.gridPhoto);
        gridView.setAdapter(imageAdapter);

        btnSelectPhoto = (Button) findViewById(R.id.btnSelect);
        btnChoosePhotosClick(btnSelectPhoto);
    }

    @Override
    protected void onStop() {
        ImageLoaderConfig.imageLoader.stop();
        super.onStop();
    }

    public void btnChoosePhotosClick(View v) {

        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectedItems = new ArrayList<String>();

                selectedItems = imageAdapter.getCheckedItems();
                Toast.makeText(PickMultiPhotoActivity.this,
                        "Total photos selected: " + selectedItems.size(),
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra("data", selectedItems);
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });

    }


    public class ImageAdapter extends BaseAdapter {

        ArrayList<String> mList;
        LayoutInflater mInflater;
        Context mContext;
        SparseBooleanArray mSparseBooleanArray;

        public ImageAdapter(Context context, ArrayList<String> imageList) {
            // TODO Auto-generated constructor stub
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mSparseBooleanArray = new SparseBooleanArray();
            mList = new ArrayList<String>();
            this.mList = imageList;

        }

        public ArrayList<String> getCheckedItems() {
            ArrayList<String> mTempArry = new ArrayList<String>();

            for (int i = 0; i < mList.size(); i++) {
                if (mSparseBooleanArray.get(i)) {
                    mTempArry.add(mList.get(i));
                }
            }

            return mTempArry;
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_pick_multi_photos,
                        parent, false);
            }

            CheckBox mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.cbImage);
            final ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.img);

            ImageLoaderConfig.imageLoader.displayImage("file://" + imageUrls.get(position),
                    imageView, options, new SimpleImageLoadingListener() {

                    });

            mCheckBox.setTag(position);
            mCheckBox.setChecked(mSparseBooleanArray.get(position));
            mCheckBox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            mSparseBooleanArray.put(
                                    (Integer) buttonView.getTag(), isChecked);
                        }
                    });
            return convertView;
        }

    }
}
