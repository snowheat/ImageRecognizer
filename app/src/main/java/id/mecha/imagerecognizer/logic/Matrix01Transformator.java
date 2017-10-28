package id.mecha.imagerecognizer.logic;

import android.graphics.Bitmap;
import android.util.Log;

import id.mecha.imagerecognizer.Singleton;
import id.mecha.imagerecognizer.Utils;

/**
 * Created by insan on 10/18/2017.
 */

public class Matrix01Transformator {

    String LOG = Singleton.getInstance().getLogString();
    private int[][] matrix01Array;

    public Matrix01Transformator(Bitmap tresholdTransformedBitmap) {

        Log.i(LOG,"Matrix01Transformator()");

        int width, height, imgArrayColumn, imgArrayRow;
        int[] rGBAFromInt;
        int a,r,g,b,intFromRGBA;
        int[] colorFromInt;

        height = tresholdTransformedBitmap.getHeight();
        width = tresholdTransformedBitmap.getWidth();

        matrix01Array = new int[tresholdTransformedBitmap.getHeight()][tresholdTransformedBitmap.getWidth()];

        int[] tresholdPixels = new int[(width*height)];
        tresholdTransformedBitmap.getPixels(tresholdPixels, 0, width,0,0,width,height);

        Log.i(LOG,"tresholdTransformedBitmap.getPixels");

        for (int i = 0; i < tresholdPixels.length; i++) {
            imgArrayRow = i/width;
            imgArrayColumn = (i+width) % width;

            rGBAFromInt = Utils.getColorFromInt(tresholdPixels[i]);

            a = rGBAFromInt[0];
            r = rGBAFromInt[1];
            g = rGBAFromInt[2];
            b = rGBAFromInt[3];



            if(i%19==0){
                //System.out.println("r ++ : "+r);
            }

            if(r == 255){
                //1
                matrix01Array[imgArrayRow][imgArrayColumn] = 0;

                if(i%19==0){
                    //System.out.println("r putih ["+imgArrayRow+"]["+imgArrayColumn+"] : "+r);
                }
            }

            if(r == 0){
                //0
                matrix01Array[imgArrayRow][imgArrayColumn] = 1;

                if(i%19==0){
                    //System.out.println("r hitam : "+r);
                }
            }

        }

        for(int j = 0; j< matrix01Array.length; j++){
            for(int i = 0; i< matrix01Array[0].length; i++){
                //System.out.print(matrix01Array[j][i]);
            }
            //System.out.print("\n");
        }

    }

    public int[][] getMatrix01Array() {
        return matrix01Array;
    }
}
