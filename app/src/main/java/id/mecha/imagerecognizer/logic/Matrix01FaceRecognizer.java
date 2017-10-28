package id.mecha.imagerecognizer.logic;

import java.util.HashMap;

/**
 * Created by insan on 10/18/2017.
 */

public class Matrix01FaceRecognizer {
    private final Matrix01ObjectsCounter matrix01ObjectsCounter;
    private String faceProportionText;
    private String personName;

    public Matrix01FaceRecognizer(Matrix01ObjectsCounter matrix01ObjectsCounter) {

        System.out.println("Matrix01FaceRecognizer");

        this.matrix01ObjectsCounter =  matrix01ObjectsCounter;
        System.out.println("margahayu raya "+matrix01ObjectsCounter.getTotalSelectedObjects());

        faceProportionText = "Mata - Mata : \n Mata - Hidung : ";
        personName = "Tidak diketahui";


        System.out.println("margahayu raya barat " +
                ""+this.matrix01ObjectsCounter.getTotalSelectedObjects());



        for(Matrix01Object matrix01Object : this.matrix01ObjectsCounter.getSelectedMatrix01ObjectList()){

            System.out.println("Koordinat : "+matrix01Object.getCentroidIJ()[0]
                    +" "+matrix01Object.getCentroidIJ()[1]
                    +" Jumlah pixel : "+matrix01Object.getCentroidIJ()[2]);
        }

    }

    public String getFaceProportionText() {
        return faceProportionText;
    }

    public String getPersonName() {
        return personName;
    }
}
