package paditech.com.fifood;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by USER on 17/4/2016.
 */
public class PickMultiPhotoActivity extends Activity {

    private GridView gridPhoto;
    private View btnSelect;

    private ArrayList<Bitmap> listBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_pick_multi_photo);


    }

    private void init() {
        gridPhoto = (GridView) findViewById(R.id.gridPhoto);
        btnSelect = findViewById(R.id.btnSelect);

        listBitmap = new ArrayList<>();

    }

    private void getListBitmap() {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;

        Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
        int columnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
        int count = imageCursor.getCount();

        for (int i = 0; i < count; i++) {
            int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            String path = imageCursor.getString(dataColumnIndex);

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
            listBitmap.add(bitmap);
        }
    }
}
