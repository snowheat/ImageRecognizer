package id.mecha.imagerecognizer.logic;

import android.graphics.Bitmap;
import android.util.Log;

import id.mecha.imagerecognizer.Singleton;

/**
 * Created by insan on 10/21/2017.
 */

public class GrayscaleImage {

    String LOG = Singleton.getInstance().getLogString();
    private Bitmap resizedBitmap;
    private Bitmap bitmap;
    private int width,height;
    private int[] pixels;

    public GrayscaleImage(Bitmap resizedBitmap) {

        Log.i(LOG,"GrayscaleImage()");
        this.resizedBitmap = resizedBitmap;

        bitmap = getGrayscaleImageStyle2();

    }


    private Bitmap getGrayscaleImageStyle1(){
        Bitmap grayscaleTransformedBitmap = new GrayscaleTransformator(resizedBitmap).getBitmap();

        Bitmap imageConvolutionTransformedBitmap = new ImageConvolutionTransformator(grayscaleTransformedBitmap).getBitmap();
        Bitmap lookUpTableTransformedBitmap = new LookUpTableTransformator(imageConvolutionTransformedBitmap).getBitmap();
        Bitmap imageConvolutionTransformedBitmap2 = new ImageConvolutionTransformator(lookUpTableTransformedBitmap).getBitmap();
        Bitmap imageConvolutionTransformedBitmap3 = new ImageConvolutionTransformator(imageConvolutionTransformedBitmap2).getBitmap();
        Bitmap lookUpTableTransformedBitmap2 = new LookUpTableTransformator(imageConvolutionTransformedBitmap3).getBitmap();

        return lookUpTableTransformedBitmap2;
    }

    private Bitmap getGrayscaleImageStyle2(){
        Bitmap grayscaleTransformedBitmap = new GrayscaleTransformator(resizedBitmap).getBitmap();

        Bitmap lookUpTableTransformedBitmap = new LookUpTableTransformator(grayscaleTransformedBitmap).getBitmap();
        Bitmap imageConvolutionTransformedBitmap = new ImageConvolutionTransformator(lookUpTableTransformedBitmap).getBitmap();
        Bitmap imageConvolutionTransformedBitmap2 = new ImageConvolutionTransformator(imageConvolutionTransformedBitmap).getBitmap();

        return imageConvolutionTransformedBitmap2;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

}
