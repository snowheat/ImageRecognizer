package id.mecha.imagerecognizer.logic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileNotFoundException;

import id.mecha.imagerecognizer.Singleton;

/**
 * Created by insan on 10/21/2017.
 */

public class InitialImage {

    String LOG = Singleton.getInstance().getLogString();
    private Bitmap bitmap;
    private ImageView blackWhiteImageView;

    public InitialImage(Context context, ImageView blackWhiteImageView, Intent intentData) {

        Log.i(LOG,"InitialImage()");

        this.blackWhiteImageView = blackWhiteImageView;

        ParcelFileDescriptor fd;

        try {
            fd = context.getContentResolver().openFileDescriptor(intentData.getData(), "r");


            Log.i(LOG,"InitialImage() : Success getting bitmap");
        } catch (FileNotFoundException e) {
            Log.e(LOG,e.getMessage());
            return;
        }


        bitmap = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor());


    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Bitmap getResizedBitmap() {

        float ratio = Math.min(
                (float) blackWhiteImageView.getWidth() / bitmap.getWidth(),
                (float) blackWhiteImageView.getHeight() / bitmap.getHeight());


        int resizedWidth = Math.round((float) ratio * bitmap.getWidth());
        int resizedHeight = Math.round((float) ratio * bitmap.getHeight());

        /*
        System.out.println(bitmap.getWidth());
        System.out.println(bitmap.getHeight());
        System.out.println(blackWhiteImageView.getWidth());
        System.out.println(blackWhiteImageView.getHeight());
        System.out.println(ratio);
        System.out.println(resizedWidth);
        System.out.println(resizedHeight);
*/
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, resizedWidth,resizedHeight, true);

        return resizedBitmap;
    }
}
