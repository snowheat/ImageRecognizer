package id.mecha.imagerecognizer.logic;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import id.mecha.imagerecognizer.Singleton;

/**
 * Created by insan on 10/18/2017.
 */

public class Matrix01ObjectsCounter {

    String LOG = Singleton.getInstance().getLogString();
    private int[][] matrix01Array;
    private List<Matrix01Object> matrix01ObjectList = new ArrayList<>();
    private List<Matrix01Object> selectedMatrix01ObjectList = new ArrayList<>();
    private int totalSelectedObjects = 0;


    public Matrix01ObjectsCounter(int[][] matrix01Array) {

        Log.i(LOG,"Matrix01ObjectsCounter()");

        this.matrix01Array = cleanMatrix01ArrayEdges(matrix01Array);

        collectMatrix01Objects();

        printTotalMatrix01Objects();
        //printMatrix01Array();
    }

    public List<Matrix01Object> getSelectedMatrix01ObjectList() {
        return selectedMatrix01ObjectList;
    }

    private void collectMatrix01Objects() {

        int maxLoop = 16,currentLoop = 0;
        int[] firstMatrix01ObjectPixel;

        //getRemainingBlackPixels();

        while( currentLoop < maxLoop ){

            firstMatrix01ObjectPixel = getFirstMatrix01ObjectPixel();

            if( firstMatrix01ObjectPixel[0]==0 ){
                break;
            }

            //System.out.println("********* OBJECT "+(currentLoop+1)+" BEGIN ***********");

            Matrix01Object matrix01Object = new Matrix01Object(matrix01Array,firstMatrix01ObjectPixel);

            matrix01ObjectList.add(matrix01Object);

            eraseObjectInMatrix01Array(matrix01Object);

            if(matrix01Object.getAllPixelsList().size()>150){
                selectedMatrix01ObjectList.add(matrix01Object);
                totalSelectedObjects ++;
            }

            //System.out.println("OBJECT "+(currentLoop+1)+" JUMLAH PIXEL : "+matrix01Object.getAllPixelsList().size());

            //getRemainingBlackPixels();

            //System.out.println("********* OBJECT "+(currentLoop+1)+" END ***********");

            currentLoop++;
        }

        System.out.println("TOTAL OBJEK BUKAN NOISE : "+totalSelectedObjects);
    }



    private void eraseObjectInMatrix01Array(Matrix01Object matrix01Object) {
        //System.out.println("eraseObjectInMatrix01Array");
        ArrayList<ArrayList<Integer>> matrix01allPixelsList = matrix01Object.getAllPixelsList();

        if(matrix01allPixelsList.size()>0){
            for(ArrayList<Integer> iJ : matrix01allPixelsList){
                matrix01Array[iJ.get(1)][iJ.get(0)] = 0;
            }
        }
    }

    private int[] getFirstMatrix01ObjectPixel(){
        int[] firstMatrix01ObjectPixel = {0,0,0};

        outerloop:
        for(int j = 0; j< matrix01Array.length; j++){
            for(int i = 0; i< matrix01Array[0].length; i++){
                if (matrix01Array[j][i] == 1) {
                    firstMatrix01ObjectPixel[0] = 1;
                    firstMatrix01ObjectPixel[1] = i;
                    firstMatrix01ObjectPixel[2] = j;

                    /*
                    Log.i(LOG, "firstMatrix01ObjectPixel() : "
                            + String.valueOf(firstMatrix01ObjectPixel[1])
                            + " " + String.valueOf(firstMatrix01ObjectPixel[2]));
                    */
                    break outerloop;
                }
            }
        }

        return firstMatrix01ObjectPixel;
    }

    public int[][] cleanMatrix01ArrayEdges(int[][] matrix01Array) {
        int[][] cleanedMatrix01ArrayEdges = matrix01Array;

        //cleanedMatrix01ArrayEdges[1][1] = 0;

        for(int j = 0; j< matrix01Array.length; j++){
            for(int i = 0; i< matrix01Array[0].length; i++){

                if(j==0 || j==(matrix01Array.length-1) || i==0 || i==(matrix01Array[0].length-1)){
                    cleanedMatrix01ArrayEdges[j][i] = 0; //White
                }

            }
        }

        return cleanedMatrix01ArrayEdges;

    }

    private int getRemainingBlackPixels() {

        int remainingBlackPixels = 0;
        for(int j = 0; j< matrix01Array.length; j++){
            for(int i = 0; i< matrix01Array[0].length; i++){
                if (matrix01Array[j][i] == 1) {
                    remainingBlackPixels++;
                }
            }
        }

        System.out.println("Remaining black pixels : "+remainingBlackPixels);

        return remainingBlackPixels;
    }

    private void printTotalMatrix01Objects() {
        System.out.println("Total matrix 0 1 objects : "+matrix01ObjectList.size());
    }

    private void printMatrix01Array() {
        for(int j = 0; j< matrix01Array.length; j++){
            for(int i = 0; i< matrix01Array[0].length; i++){
                System.out.print(matrix01Array[j][i]);
            }
            System.out.print("\n");
        }
    }

    public int getTotalSelectedObjects() {
        return totalSelectedObjects;
    }
}
