package id.mecha.imagerecognizer.logic;

import android.graphics.Bitmap;
import android.util.Log;

import java.nio.IntBuffer;
import java.util.Arrays;

import id.mecha.imagerecognizer.Singleton;
import id.mecha.imagerecognizer.Utils;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by insan on 10/18/2017.
 */

public class ImageConvolutionTransformator {

    String LOG = Singleton.getInstance().getLogString();
    private Bitmap bitmap;
    private int width,height;

    public ImageConvolutionTransformator(Bitmap lookUpTableTransformedBitmap) {

        Log.i(LOG,"ImageConvolutionTransformator()");

        int width, height, imgArrayColumn, imgArrayRow;
        int a,r,g,b,intFromRGBA;
        int[] rGBAFromInt,convolutionArray;
        width = lookUpTableTransformedBitmap.getWidth();
        height = lookUpTableTransformedBitmap.getHeight();

        int[] lookUpTablePixels = new int[(width*height)];
        lookUpTableTransformedBitmap.getPixels(lookUpTablePixels,0,width,0,0,width,height);

        int[] imageConvolutionPixels = new int[(width*height)];

        for (int i = 0; i < lookUpTablePixels.length; i++) {
            imgArrayRow = i/width;
            imgArrayColumn = (i+width) % width;

            rGBAFromInt = Utils.getColorFromInt(lookUpTablePixels[i]);

            a = rGBAFromInt[0];
            r = rGBAFromInt[1];

            if(imgArrayRow == 0
                    || imgArrayRow == height-1
                    || imgArrayColumn == 0
                    || imgArrayColumn == width-1){

            }else{
                //Convolution algorithm for new pixel value
                //1. T – (width + 1)
                //2. T – width
                //3. T – (width – 1)
                //4. T – 1
                //5. T
                //6. T + 1
                //7. T + (width – 1)
                //8. T + width
                //9. T + (width + 1)

                convolutionArray = new int[]{
                        Utils.getColorFromInt(lookUpTablePixels[ i - (width+1)  ])[1],
                        Utils.getColorFromInt(lookUpTablePixels[ i - (width)    ])[1],
                        Utils.getColorFromInt(lookUpTablePixels[ i - (width-1)  ])[1],
                        Utils.getColorFromInt(lookUpTablePixels[ i - 1          ])[1],
                        Utils.getColorFromInt(lookUpTablePixels[ i              ])[1],
                        Utils.getColorFromInt(lookUpTablePixels[ i + 1          ])[1],
                        Utils.getColorFromInt(lookUpTablePixels[ i + (width-1)  ])[1],
                        Utils.getColorFromInt(lookUpTablePixels[ i + (width)    ])[1],
                        Utils.getColorFromInt(lookUpTablePixels[ i + (width+1)  ])[1]
                };
                Arrays.sort(convolutionArray);

                if(i == (width + 100)||i == (width + 200)||i == (width + 400)){
                    /*
                    Log.e(LOG,String.valueOf(convolutionArray[0]));
                    Log.e(LOG,String.valueOf(convolutionArray[1]));
                    Log.e(LOG,String.valueOf(convolutionArray[2]));
                    Log.e(LOG,String.valueOf(convolutionArray[3]));
                    Log.e(LOG,String.valueOf(convolutionArray[4]));
                    Log.e(LOG,String.valueOf(convolutionArray[5]));
                    Log.e(LOG,String.valueOf(convolutionArray[6]));
                    Log.e(LOG,String.valueOf(convolutionArray[7]));
                    Log.e(LOG,String.valueOf(convolutionArray[8]));
                    Log.e(LOG,String.valueOf("PANGRIPUR"));
                    Log.e(LOG,String.valueOf(convolutionArray[4]));
                    Log.e(LOG,String.valueOf("***"));
                    */
                }


                intFromRGBA = Utils.getIntFromRGBA(convolutionArray[4]
                        ,convolutionArray[4]
                        ,convolutionArray[4]
                        ,a);

                imageConvolutionPixels[i] = intFromRGBA;

            }

        }

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(imageConvolutionPixels));

    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
