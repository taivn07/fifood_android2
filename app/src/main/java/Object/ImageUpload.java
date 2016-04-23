package Object;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by USER on 23/4/2016.
 */
public class ImageUpload {

    private Bitmap bitmap;
    private int progress;
    private File file;

    public ImageUpload(Bitmap bitmap, int progress, File file) {
        this.bitmap = bitmap;
        this.progress = progress;
        this.file = file;
    }

    public ImageUpload() {
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
