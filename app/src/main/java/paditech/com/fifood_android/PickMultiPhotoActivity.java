package paditech.com.fifood_android;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
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

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBgMenu)));
        actionBar.setTitle(Html.fromHtml("<b>Chọn ảnh</b>"));

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

                ImageLoader.getInstance().stop();
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_pick_multi_photos,
                        parent, false);
            }

            final CheckBox mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.cbImage);
            final ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.img);
            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);

            ImageLoader.getInstance().displayImage("file://" + imageUrls.get(position),
                    imageView, options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {
                            progressBar.setVisibility(View.GONE);
                        }
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
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCheckBox.setChecked(true);
                }
            });
            return convertView;
        }

    }
}
