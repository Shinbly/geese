package u_bordeaux.etu.geese;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

/**
 * Created by Lalie on 06/02/2018.
 */

public class Image extends BitmapFactory implements Filters {

    /**
     *
     */
    private int height;

    /**
     *
     */
    private int width;

    /**
     *
     */
    private int size;

    /**
     *
     */
    private int[] backup;


    /**
     *
     */
    public Image(Bitmap bmp){
        this.height = bmp.getHeight();
        this.width = bmp.getWidth();
        this.size = height*width;

        backup = new int[size];
        bmp.getPixels(backup, 0, width, 0,0, width, height);
    }


    private int truncate(int value){
        if(value < 0){
            value = 0;
        }else if(value > 255){
            value = 255;
        }

        return value;
    }


    @Override
    public void brightness(Bitmap bmp, int value) {
        int[] pixels = new int[this.size];

        bmp.getPixels(pixels, 0, width, 0,0, width, height);

        int newRed;
        int newGreen;
        int newBlue;

        for(int i=0; i<size; i++){
            newRed = truncate(Color.red(pixels[i]) + value);
            newGreen = truncate(Color.green(pixels[i]) + value);
            newBlue = truncate(Color.blue(pixels[i]) + value);

            pixels[i] = Color.rgb(newRed,newGreen,newBlue);
        }

        bmp.setPixels(pixels,0, width, 0,0, width, height);
    }

    @Override
    public void contrast() {

    }

    @Override
    public void histogram() {

    }

    @Override
    public void hue() {

    }

    @Override
    public void toGray(Bitmap bmp) {
        int[] pixels = new int[size];
        bmp.getPixels(pixels, 0, width, 0,0, width, height);

        for (int i = 0; i < size; i++) {
            int newColor = (int)( 0.3 * Color.red(pixels[i]) + 0.59 * Color.green(pixels[i]) + 0.11 * Color.blue(pixels[i]));
            pixels[i] = Color.rgb(newColor, newColor, newColor);
        }

        bmp.setPixels(pixels,0, width, 0,0, width, height);
    }

    @Override
    public void sepia(Bitmap bmp) {
        int[] pixels = new int[size];
        bmp.getPixels(pixels, 0, width, 0,0, width, height);

        int newRed;
        int newGreen;
        int newBlue;

        for (int i = 0; i < size; i++) {
            newRed = truncate((int)(Color.red(pixels[i])*.393) + (int)(Color.green(pixels[i])*.769) + (int)(Color.blue(pixels[i])*.189));
            newGreen = truncate((int)(Color.red(pixels[i])*.349) + (int)(Color.green(pixels[i])*.686) + (int)(Color.blue(pixels[i])*.168));
            newBlue = truncate((int)(Color.red(pixels[i])*.272) + (int)(Color.green(pixels[i])*.534) + (int)(Color.blue(pixels[i])*.131));
            pixels[i] = Color.rgb(newRed, newGreen, newBlue);
        }

        bmp.setPixels(pixels,0, width, 0,0, width, height);
    }

    @Override
    public void moyenneur() {

    }

    @Override
    public void gaussien() {

    }

    @Override
    public void sobel() {

    }

    @Override
    public void laplacien() {

    }
}
