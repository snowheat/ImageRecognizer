package id.mecha.imagerecognizer.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by insan on 10/22/2017.
 */

public class HistogramValleysFinder {
    private final int[] colorFreqCountArray;
    private List<Integer> valleysList = new ArrayList<Integer>();

    public HistogramValleysFinder(int[] colorFreqCountArray) {

        this.colorFreqCountArray = colorFreqCountArray;

        findValleys1();
    }

    private void findValleys1() {
        boolean valleyCandidate,isValley;
        int totalValleyCandidate = 0;



        int i=4;
        int s,t;

        histogramValleyFinderIteration:
        while(i<(colorFreqCountArray.length-4)){

            valleyCandidate = false;
            isValley = false;

            if(colorFreqCountArray[i-1]!=0){

                int h = i-2;

                hLoop:
                while(h>0){

                    if(colorFreqCountArray[h]!=0){

                        break hLoop;
                    }

                    h--;
                }

                if(colorFreqCountArray[h]>colorFreqCountArray[i-1]&&colorFreqCountArray[i-1]>colorFreqCountArray[i]){
                    //valleyCandidate = true;
                }

                if(colorFreqCountArray[i-1]>colorFreqCountArray[i]
                    &&(double)colorFreqCountArray[i-1]/(double)colorFreqCountArray[i]>=1.4f){
                    valleyCandidate = true;
                }

            }

            //System.out.println("****************");
            //System.out.println(i+": "+colorFreqCountArray[i]);

            if(valleyCandidate){

            //ITERATING SAME FREQUENCY COUNT

                s = 1; //Step to next different frequencies count
                while(colorFreqCountArray[i+s]==colorFreqCountArray[i]
                        &&colorFreqCountArray[i+s]<(colorFreqCountArray.length-4)){
                    s++;
                }

                if(colorFreqCountArray[i+s]>colorFreqCountArray[i]){

                    //i+s+1 = 0
                    if(colorFreqCountArray[i+s+1]==0){

                        t = 1; //Step to next different frequencies count, from i+s+1 = 0
                        while(colorFreqCountArray[i+s+1+t]==0
                                &&colorFreqCountArray[i+s+1+t]<(colorFreqCountArray.length-4)){
                            t++;
                        }

                        if(colorFreqCountArray[i+s+1+t]>colorFreqCountArray[i+s]){
                            isValley = true;
                            valleysList.add(i);
                            //System.out.println("valleysList.add("+i+")");
                        }

                    }

                    if(colorFreqCountArray[i+s+1]>colorFreqCountArray[i+s]){
                        isValley = true;
                        valleysList.add(i);
                        //System.out.println("valleysList.add("+i+")");
                    }

                }

                totalValleyCandidate++;
                //System.out.println("VALLEY CANDIDATE "+i+": "+colorFreqCountArray[i]+" IS VALLEY : "+isValley);

                i += s;

            }else{

                //System.out.println("j = "+s+" , i until "+i);
                i += 1;
            }


        }

        valleysList.add(16);
        valleysList.add(32);

        //System.out.println("TOTAL VALLEY CANDIDATE "+totalValleyCandidate);
        //System.out.println("TOTAL VALLEY "+valleysList.size());

        for(Integer j: valleysList){
            System.out.println("valleysList : "+j);
        }
    }

    private void findValleys2() {
        boolean valleyCandidate,isValley;
        int totalValleyCandidate = 0;

        valleysList.add(32);
        valleysList.add(64);
        valleysList.add(128);

        int i=4;

        histogramValleyFinderIteration:
        while(i<(colorFreqCountArray.length-4)){

            valleyCandidate = false;
            isValley = false;

            if(colorFreqCountArray[i-1]!=0){

                if(colorFreqCountArray[i-2]!=0){

                    if(colorFreqCountArray[i-2]>colorFreqCountArray[i-1]&&
                            colorFreqCountArray[i-1]>colorFreqCountArray[i]){
                        valleyCandidate = true;
                    }

                }else if(colorFreqCountArray[i-3]!=0){

                    if(colorFreqCountArray[i-3]>colorFreqCountArray[i-1]&&
                            colorFreqCountArray[i-1]>colorFreqCountArray[i]){
                        valleyCandidate = true;
                    }


                }

            }else if(colorFreqCountArray[i-2]!=0){

                if(colorFreqCountArray[i-3]!=0){

                    if(colorFreqCountArray[i-3]>colorFreqCountArray[i-2]&&
                            colorFreqCountArray[i-2]>colorFreqCountArray[i]){
                        valleyCandidate = true;
                    }

                }else if(colorFreqCountArray[i-4]!=0){

                    if(colorFreqCountArray[i-4]>colorFreqCountArray[i-2]&&
                            colorFreqCountArray[i-2]>colorFreqCountArray[i]){
                        valleyCandidate = true;
                    }

                }

            }

            System.out.println("****************");
            System.out.println(i+": "+colorFreqCountArray[i]);


            //ITERATING SAME FREQUENCY COUNT

            int s = 1; //Step to next different frequencies count
            while(colorFreqCountArray[i+s]==colorFreqCountArray[i]){
                s++;
            }

            if(valleyCandidate){

                totalValleyCandidate++;
                System.out.println("VALLEY CANDIDATE "+i+": "+colorFreqCountArray[i]+" IS VALLEY : "+isValley);

                if(colorFreqCountArray[i+s]!=0){

                    if(colorFreqCountArray[i+s+1]!=0){

                        if(colorFreqCountArray[i+s+1]>colorFreqCountArray[i+s]&&
                                colorFreqCountArray[i+s]>colorFreqCountArray[i]){
                            isValley = true;
                            valleysList.add(i);
                            i += s;
                            continue histogramValleyFinderIteration;
                        }

                    }else if(colorFreqCountArray[i+s+2]!=0){

                        if(colorFreqCountArray[i+s+2]>colorFreqCountArray[i+s]&&
                                colorFreqCountArray[i+s]>colorFreqCountArray[i]){
                            isValley = true;
                            valleysList.add(i);
                            i += s;
                            continue histogramValleyFinderIteration;
                        }

                    }

                }else if(colorFreqCountArray[i+s+1]!=0){

                    if(colorFreqCountArray[i+s+2]!=0){

                        if(colorFreqCountArray[i+s+2]>colorFreqCountArray[i+s+1]&&
                                colorFreqCountArray[i+s+1]>colorFreqCountArray[i]){
                            isValley = true;
                            valleysList.add(i);
                            i += s;
                            continue histogramValleyFinderIteration;
                        }

                    }else if(colorFreqCountArray[i+s+3]!=0){

                        if(colorFreqCountArray[i+s+3]>colorFreqCountArray[i+s+1]&&
                                colorFreqCountArray[i+s+1]>colorFreqCountArray[i]){
                            isValley = true;
                            valleysList.add(i);
                            i += s;
                            continue histogramValleyFinderIteration;
                        }

                    }

                }
            }



            //System.out.println("j = "+s+" , i until "+i);
            i += s;
        }

        System.out.println("TOTAL VALLEY CANDIDATE "+totalValleyCandidate);
        System.out.println("TOTAL VALLEY "+valleysList.size());

        for(Integer j: valleysList){
            System.out.println("valleysList : "+j);
        }
    }

    public List<Integer> getValleysList() {
        return valleysList;
    }
}
