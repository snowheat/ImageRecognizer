package id.mecha.imagerecognizer;

/**
 * Created by insan on 10/21/2017.
 */

public class Utils {
    public static int[] getColorFromInt(int pixels) {
        int[] colorFromInt = new int[4];
        colorFromInt[0] = (pixels >> 24) & 0xFF;
        colorFromInt[1] = (pixels >> 16) & 0xFF;
        colorFromInt[2] = (pixels >> 8) & 0xFF;
        colorFromInt[3] = pixels & 0xFF;

        return colorFromInt;
    }

    public static int getIntFromRGBA(int r, int g, int b, int a) {
        int intFromRGBA;
        intFromRGBA = (a << 24) | (r << 16) | (g << 8) | b;

        return intFromRGBA;
    }
}
