package id.mecha.imagerecognizer.logic;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import id.mecha.imagerecognizer.Singleton;

/**
 * Created by insan on 10/18/2017.
 */

public class AutomaticTresholdTransformator {

    String LOG = Singleton.getInstance().getLogString();

    private Bitmap tresholdTransformedBitmap;
    private int[][] matrix01Array;
    private Matrix01ObjectsCounter matrix01ObjectsCounter;
    private ArrayList<Integer> bestValleysList = new ArrayList<Integer>();


    public AutomaticTresholdTransformator(HistogramGenerator histogram, GrayscaleImage grayscaleImage) {

        Log.i(LOG,"AutomaticTresholdTransformator()");
        int totalSelectedObjects;
        int maxCandidate = 3;
        int currentCandidate = 0;

        loop:
        for(int freq : histogram.getValleysList()){
            tresholdTransformedBitmap = new TresholdTransformator(grayscaleImage.getBitmap(),freq).getBitmap();
            matrix01Array = new Matrix01Transformator(tresholdTransformedBitmap).getMatrix01Array();

            ///*
            matrix01ObjectsCounter = new Matrix01ObjectsCounter(matrix01Array);

            //totalSelectedObjects = matrix01ObjectsCounter.getSelectedMatrix01ObjectList().size();
            totalSelectedObjects = matrix01ObjectsCounter.getTotalSelectedObjects();

            //System.out.println("Best freq : "+freq+" "+totalSelectedObjects);

            if( totalSelectedObjects > 6 && totalSelectedObjects < 16 ){
                System.out.println("Best freq : "+freq+" "+totalSelectedObjects);
                bestValleysList.add(freq);

            }

            if(bestValleysList.size() >= 1){
                break loop;
            }

            System.out.println("bestValleysList.size() = "+bestValleysList.size());
            System.out.println("currentCandidate = "+currentCandidate);
            //*/

            if(currentCandidate>maxCandidate){
                break;
            }

            currentCandidate++;

        }

        System.out.println("YANG TERPILIH : matrix01ObjectsCounter" + matrix01ObjectsCounter.getTotalSelectedObjects());



        //35



        //Get array of Color Frequencies
        //Get array of Valleys
    }

    public Bitmap getTresholdTransformedBitmap() {
        return tresholdTransformedBitmap;
    }

    public Matrix01ObjectsCounter getMatrix01ObjectsCounter() {
        return matrix01ObjectsCounter;
    }
}
