package id.mecha.imagerecognizer.logic;

import android.graphics.Bitmap;
import android.util.Log;

import java.nio.IntBuffer;

import id.mecha.imagerecognizer.Singleton;
import id.mecha.imagerecognizer.Utils;

import static android.R.attr.width;

/**
 * Created by insan on 10/22/2017.
 */

public class TresholdTransformator {

    String LOG = Singleton.getInstance().getLogString();
    private Bitmap bitmap;
    private int width,height;

    public TresholdTransformator(Bitmap grayscaleBitmap, int treshold) {

        Log.i(LOG,"TresholdTransformator()");

        int a,r,g,b,intFromRGBA,grayscaleColorFrequency, imgArrayColumn, imgArrayRow;
        int[] rGBAFromInt;
        width = grayscaleBitmap.getWidth();
        height = grayscaleBitmap.getHeight();

        int[] tresholdPixels = new int[(width*height)];
        grayscaleBitmap.getPixels(tresholdPixels,0,width,0,0,width,height);


            for (int i = 0; i < tresholdPixels.length; i++) {
                imgArrayRow = i/width;
                imgArrayColumn = (i+width) % width;

                rGBAFromInt = Utils.getColorFromInt(tresholdPixels[i]);

                a = rGBAFromInt[0];
                r = rGBAFromInt[1];
                g = rGBAFromInt[2];
                b = rGBAFromInt[3];

                if(r > treshold){
                    intFromRGBA = Utils.getIntFromRGBA(255
                            ,255
                            ,255
                            ,a);
                }else{
                    intFromRGBA = Utils.getIntFromRGBA(0
                            ,0
                            ,0
                            ,a);
                }


                tresholdPixels[i] = intFromRGBA;

            }


        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(tresholdPixels));
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
