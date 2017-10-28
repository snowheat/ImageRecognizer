package id.mecha.imagerecognizer.logic;

import android.graphics.Bitmap;
import android.util.Log;

import java.nio.IntBuffer;

import id.mecha.imagerecognizer.Singleton;
import id.mecha.imagerecognizer.Utils;

/**
 * Created by insan on 10/18/2017.
 */

public class GrayscaleTransformator {

    String LOG = Singleton.getInstance().getLogString();
    private Bitmap bitmap;
    private int width,height;

    public GrayscaleTransformator(Bitmap resizedBitmap) {

        Log.i(LOG,"GrayscaleTransformator()");

        int a,r,g,b,intFromRGBA,grayscaleColorFrequency, imgArrayColumn, imgArrayRow;
        int[] rGBAFromInt;
        width = resizedBitmap.getWidth();
        height = resizedBitmap.getHeight();

        int[] grayscalePixels = new int[(width*height)];
        resizedBitmap.getPixels(grayscalePixels,0,width,0,0,width,height);

        try {
            for (int i = 0; i < grayscalePixels.length; i++) {
                imgArrayRow = i/width;
                imgArrayColumn = (i+width) % width;

                rGBAFromInt = Utils.getColorFromInt(grayscalePixels[i]);

                a = rGBAFromInt[0];
                r = rGBAFromInt[1];
                g = rGBAFromInt[2];
                b = rGBAFromInt[3];

                grayscaleColorFrequency = (int)((double)r + (double)b + (double)g)/3;

                if(imgArrayRow == 0
                        || imgArrayRow == height-1
                        || imgArrayColumn == 0
                        || imgArrayColumn == width-1){
                    intFromRGBA = Utils.getIntFromRGBA(grayscaleColorFrequency
                            ,grayscaleColorFrequency
                            ,grayscaleColorFrequency
                            ,a);
                }else{
                    intFromRGBA = Utils.getIntFromRGBA(grayscaleColorFrequency
                            ,grayscaleColorFrequency
                            ,grayscaleColorFrequency
                            ,a);
                }


                grayscalePixels[i] = intFromRGBA;

            }
        }catch(Exception e){

        }

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(grayscalePixels));
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
