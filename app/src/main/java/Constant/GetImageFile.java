package Constant;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by USER on 19/4/2016.
 */
public class GetImageFile {

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static String getPath(Context context, Uri uri) {
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    public static File getImageFile(Context inContext, Bitmap inImage) {

        ContextWrapper cw = new ContextWrapper(inContext);
        File dir = cw.getDir("Equipment", Context.MODE_PRIVATE);
        Calendar c = Calendar.getInstance();
        File path = new File(dir, Integer.toString(c.get(Calendar.MILLISECOND))
                + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e(")))))))", path + "");
        return path;
    }

}
