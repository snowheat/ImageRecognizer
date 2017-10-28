package id.mecha.imagerecognizer.logic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

import id.mecha.imagerecognizer.Utils;

/**
 * Created by insan on 10/21/2017.
 */

public class HistogramGenerator{

    private int[] colorFrequenciesCountArray = new int[256];
    private List<Integer> valleysList;

    public HistogramGenerator(Bitmap grayscaleBitmap) {
        int a ,r ,g ,b ,intFromRGBA ,width ,height;
        int[] rGBAFromInt;

        width = grayscaleBitmap.getWidth();
        height = grayscaleBitmap.getHeight();

        int[] histogramPixels = new int[(width*height)];
        grayscaleBitmap.getPixels(histogramPixels,0,width,0,0,width,height);


        for (int i = 0; i < histogramPixels.length; i++) {
            rGBAFromInt = Utils.getColorFromInt(histogramPixels[i]);
            r = rGBAFromInt[1];
            colorFrequenciesCountArray[r] ++;
        }

        valleysList = new HistogramValleysFinder(colorFrequenciesCountArray).getValleysList();
    }

    public int[] getColorFrequenciesCountArray() {
        return colorFrequenciesCountArray;
    }

    public List<Integer> getValleysList(){
        return valleysList;
    }

    public Bitmap getHistogramBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint p = new Paint();
        p.setColor(Color.rgb(30,30,30));

        int maxColorFrequencyCount = 0;
        int barWidth = (width/256);
        int currentLeft,currentTop,currentRight;

        for(int colorFrequencyCount : colorFrequenciesCountArray)
            if(colorFrequencyCount > maxColorFrequencyCount)
                maxColorFrequencyCount = colorFrequencyCount;


        for( int i=0;i<colorFrequenciesCountArray.length;i++ ){

            currentLeft = i*barWidth;
            currentRight = currentLeft + barWidth;
            currentTop = height - (int)((double)colorFrequenciesCountArray[i]/(double)maxColorFrequencyCount*(double)height);

            canvas.drawRect(currentLeft,currentTop,currentRight,height,p);
        }

        /*
        System.out.println("MAXNYA = "+max);
        System.out.println("width = "+width);
        System.out.println("height = "+height);
        System.out.println("maxColorFrequencyCount/height = "+(maxColorFrequencyCount/height));
        System.out.println("width/256 = "+barWidth);
        */

        return bitmap;
    }
}
