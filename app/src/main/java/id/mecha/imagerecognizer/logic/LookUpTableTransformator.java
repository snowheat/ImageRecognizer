package id.mecha.imagerecognizer.logic;

import android.graphics.Bitmap;
import android.util.Log;

import java.nio.IntBuffer;

import id.mecha.imagerecognizer.Singleton;
import id.mecha.imagerecognizer.Utils;

/**
 * Created by insan on 10/18/2017.
 */

public class LookUpTableTransformator {

    String LOG = Singleton.getInstance().getLogString();
    private Bitmap bitmap;
    private int[] colorFrequenciesCount = new int[256]
            ,accumulativeColorFreqCount = new int[256]
            ,lookUpTableArray = new int[256];
    private int width,height;

    public LookUpTableTransformator(Bitmap grayscaleTransformedBitmap) {

        Log.i(LOG,"LookUpTableTransformator()");

        int a,r,g,b,intFromRGBA;
        int[] rGBAFromInt;
        width = grayscaleTransformedBitmap.getWidth();
        height = grayscaleTransformedBitmap.getHeight();

        int[] lookUpTablePixels = new int[(width*height)];
        grayscaleTransformedBitmap.getPixels(lookUpTablePixels,0,width,0,0,width,height);


        for (int i = 0; i < lookUpTablePixels.length; i++) {

            rGBAFromInt = Utils.getColorFromInt(lookUpTablePixels[i]);

            r = rGBAFromInt[1];

            colorFrequenciesCount[r] ++;
        }

        for(int i = 0; i < accumulativeColorFreqCount.length; i++) {
            if(i==0){
                accumulativeColorFreqCount[i] = colorFrequenciesCount[i];
            }else{
                accumulativeColorFreqCount[i] = accumulativeColorFreqCount[i-1] + colorFrequenciesCount[i];
            }
        }

        for(int i = 0; i < lookUpTableArray.length; i++) {
            lookUpTableArray[i] = (int)((double)accumulativeColorFreqCount[i]
                    / (double)accumulativeColorFreqCount[255] * (double)255);
        }

        for (int i = 0; i < lookUpTablePixels.length; i++) {

            rGBAFromInt = Utils.getColorFromInt(lookUpTablePixels[i]);

            a = rGBAFromInt[0];
            r = rGBAFromInt[1];


            intFromRGBA = Utils.getIntFromRGBA(lookUpTableArray[r]
                ,lookUpTableArray[r]
                ,lookUpTableArray[r]
                ,a);

            lookUpTablePixels[i] = intFromRGBA;
        }


        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(lookUpTablePixels));

    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
