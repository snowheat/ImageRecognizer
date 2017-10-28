package id.mecha.imagerecognizer;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import id.mecha.imagerecognizer.logic.InitialImage;

/**
 * Created by insan on 10/21/2017.
 */

public class Singleton {
    public static Singleton singletonInstance;

    private Singleton(){}

    public static Singleton getInstance(){
        if(singletonInstance == null){
            singletonInstance = new Singleton();
        }

        return singletonInstance;
    }

    public String getLogString() {
        return "LOG";
    }

}
