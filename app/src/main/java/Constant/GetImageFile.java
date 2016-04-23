package Constant;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
