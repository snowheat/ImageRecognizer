package id.mecha.imagerecognizer.logic;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import id.mecha.imagerecognizer.Singleton;

/**
 * Created by insan on 10/18/2017.
 */

public class Matrix01Object {

    String LOG = Singleton.getInstance().getLogString();
    private int[][] matrix01Array;
    private int[] firstMatrix01ObjectPixel;
    private ArrayList<ArrayList<Integer>> allPixelsList = new ArrayList<ArrayList<Integer>>();
    private ArrayList<ArrayList<Integer>> edgePixelsList = new ArrayList<ArrayList<Integer>>();
    private String chainCode = "";
    private int[] centroidIJ;


    public Matrix01Object(int[][] matrix01Array, int[] firstMatrix01ObjectPixel) {
        this.matrix01Array = matrix01Array;
        this.firstMatrix01ObjectPixel = firstMatrix01ObjectPixel;

        buildEdgePixelsList();
        buildAllPixelsList();

    }

    private void buildAllPixelsList() {

        //Log.i(LOG,"Matrix01ObjectsCounter()");
        ArrayList<Integer> contentPixel;
        ArrayList<Integer> integerToTest1;
        ArrayList<Integer> integerToTest0;
        boolean isEdgeWithBlankRightPixel;
        int nextRightPixelI;

        for(ArrayList<Integer> edgePixel: edgePixelsList){

            contentPixel = new ArrayList<Integer>();
            contentPixel.add(edgePixel.get(0));
            contentPixel.add(edgePixel.get(1));

            if(!allPixelsList.contains(contentPixel)){
                allPixelsList.add(contentPixel);
            }

            if(edgePixel.get(2)==1){

                isEdgeWithBlankRightPixel = false;
                nextRightPixelI = edgePixel.get(0) + 1;
                //System.out.println("nextRightPixelI "+nextRightPixelI);

                blankRightPictureLoop:
                while(isEdgeWithBlankRightPixel==false&&nextRightPixelI<matrix01Array[0].length-1){

                    //System.out.println(nextRightPixelI+" "+edgePixel.get(1)+" "+1);
                    //System.out.println(nextRightPixelI+" "+edgePixel.get(1)+" "+0);

                    integerToTest1 = new ArrayList<Integer>();
                    integerToTest1.add(nextRightPixelI);
                    integerToTest1.add(edgePixel.get(1));
                    integerToTest1.add(1);

                    integerToTest0 = new ArrayList<Integer>();
                    integerToTest0.add(nextRightPixelI);
                    integerToTest0.add(edgePixel.get(1));
                    integerToTest0.add(0);


                    if(edgePixelsList.contains(integerToTest1)
                            ||edgePixelsList.contains(integerToTest0)){
                        //System.out.println("Touch an edge pixel, break the loop");

                        break blankRightPictureLoop;
                    }

                    contentPixel = new ArrayList<Integer>();
                    contentPixel.add(nextRightPixelI);
                    contentPixel.add(edgePixel.get(1));

                    if(!allPixelsList.contains(contentPixel)){
                        allPixelsList.add(contentPixel);
                    }

                    nextRightPixelI++;
                }

            }

        }
    }

    public ArrayList<ArrayList<Integer>> getAllPixelsList() {
        return allPixelsList;
    }

    public void buildEdgePixelsList() {

        boolean isChainCodeComplete = false;
        ArrayList<Integer> edgePixel;

        int nextDirection,initNextDirectionAfter,initNextDirection
                ,currentI,currentJ,iToCheck,jToCheck,nextRightPixel;

        int found = 0,pixelToCheck = 0;

        initNextDirection = 4;

        int startI = firstMatrix01ObjectPixel[1];
        int startJ = firstMatrix01ObjectPixel[2];

        currentI = firstMatrix01ObjectPixel[1];
        currentJ = firstMatrix01ObjectPixel[2];


        mainwhile:
        while(isChainCodeComplete==false){

            nextRightPixel = matrix01Array[currentJ][currentI+1];
            edgePixel = new ArrayList<Integer>();
            edgePixel.add(currentI);
            edgePixel.add(currentJ);
            edgePixel.add(nextRightPixel);

            //System.out.println("edgePixel to add "+currentI+" "+currentJ+" "+nextRightPixel);

            if(!edgePixelsList.contains(edgePixel)){
                edgePixelsList.add(edgePixel);
            }

            for(int i=initNextDirection;i<initNextDirection+8;i++){

                if(i>9){
                    nextDirection = i - 8;
                }else{
                    nextDirection = i;
                }

                switch(nextDirection){
                    case 2:
                        iToCheck = currentI;
                        jToCheck = currentJ-1;
                        initNextDirectionAfter = 9;
                        break;
                    case 3:
                        iToCheck = currentI+1;
                        jToCheck = currentJ-1;
                        initNextDirectionAfter = 9;
                        break;
                    case 4:
                        iToCheck = currentI+1;
                        jToCheck = currentJ;
                        initNextDirectionAfter = 3;
                        break;
                    case 5:
                        iToCheck = currentI+1;
                        jToCheck = currentJ+1;
                        initNextDirectionAfter = 3;
                        break;
                    case 6:
                        iToCheck = currentI;
                        jToCheck = currentJ+1;
                        initNextDirectionAfter = 5;
                        break;
                    case 7:
                        iToCheck = currentI-1;
                        jToCheck = currentJ+1;
                        initNextDirectionAfter = 5;
                        break;
                    case 8:
                        iToCheck = currentI-1;
                        jToCheck = currentJ;
                        initNextDirectionAfter = 7;
                        break;
                    case 9:
                        iToCheck = currentI-1;
                        jToCheck = currentJ-1;
                        initNextDirectionAfter = 7;
                        break;
                    default:
                        //not find neighbour
                        iToCheck = currentI;
                        jToCheck = currentJ;
                        initNextDirectionAfter = 1;
                        //break mainwhile;
                }


                pixelToCheck = matrix01Array[jToCheck][iToCheck];

                if(pixelToCheck==1){
                    chainCode += String.valueOf(nextDirection);
                    found=1;




                    if(iToCheck!=startI||jToCheck!=startJ){
                        //Log.i(LOG,"beda ama titik awal");
                        currentI = iToCheck;
                        currentJ = jToCheck;

                        initNextDirection = initNextDirectionAfter;
                        continue mainwhile;
                    }

                    if(iToCheck==startI&&jToCheck==startJ){
                        //Log.i(LOG,"Terkelilingi. Kembali ke titik awal");
                        isChainCodeComplete = true;
                    }

                    break;
                }else{
                    //Log.i("Track "+LOG,String.valueOf(xToCheck)+", "+String.valueOf(yToCheck)+" found : "+found);
                }

            }

            if(edgePixelsList.size()==1 && found==0){
                Log.e(LOG,"LONELY PIXEL");
                isChainCodeComplete = true;
            }

        }

        //System.out.println("Chain code : "+chainCode);

    }


    public int[] getCentroidIJ() {

        int[] centroidIJ;
        int iMean = 0,jMean = 0;

        for(ArrayList<Integer> pixel : allPixelsList){
            iMean += pixel.get(0);
            jMean += pixel.get(1);
        }

        iMean = iMean / allPixelsList.size();
        jMean = jMean / allPixelsList.size();

        centroidIJ = new int[]{iMean,jMean,allPixelsList.size()};

        return centroidIJ;
    }


}
