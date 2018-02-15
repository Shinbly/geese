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
    public void hue(Bitmap bmp, int value) { //value between 0 and 360
        int[] pixels = new int[this.size];
        bmp.getPixels(pixels, 0, width, 0,0, width, height);
        float[] hsv = new float[3];
        for (int i=0 ; i < (this.size) ; i++ ){
            Color.colorToHSV(pixels[i], hsv);
            hsv[0] = (hsv[0]+value)%360;
            pixels[i] = Color.HSVToColor(hsv);
        }
        bmp.setPixels(pixels,0,this.width,0,0,this.width,this.height);

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

    private void convolution(int [] pixels, double []filtre) {
        int f_size = (int) Math.sqrt(filtre.length);
        int side= f_size/2+1;
        int newRed;
        int newGreen;
        int newBlue;
        int position;
        double coeff = 0;

        for (int i= 0 ; i < filtre.length; i ++){
            coeff += filtre[i];
        }

        int []tmppixels = pixels.clone();






        for (int x = side; x < width-side ; x++ ) {
            for (int y = side; y < height - side; y++) {
                newRed = 0;
                newBlue= 0;
                newGreen= 0;
                for (int i= 0 ; i < filtre.length; i ++){
                    // la position est donc de x = ((x-taille/2)+(i%taille))     et y  = y -taille/2 + i/taille
                    position = ((x-f_size/2)+(i%f_size))+(width*(y -f_size/2 + i/f_size));
                    newRed += (Color.red(tmppixels[position]) * filtre[i]);
                    newGreen += (Color.green(tmppixels[position]) * filtre[i]);
                    newBlue += (Color.blue(tmppixels[position]) * filtre[i]);
                }
                pixels[x+width*y] = Color.rgb((int) (newRed/coeff),(int) (newGreen/coeff),(int) (newBlue/coeff));
            }
        }



    }

    @Override
    public void moyenneur(Bitmap bmp, int size_mask) {
        double mask[]= new double[size_mask*size_mask];
        for (int i = 0; i< size_mask*size_mask; i++){
            mask[i]= 1;
        }
        int[] pixels = new int[size];
        bmp.getPixels(pixels, 0, width, 0,0, width, height);
        convolution(pixels,mask);
        bmp.setPixels(pixels,0, width, 0,0, width, height);

    }

    @Override
    public void gaussien(Bitmap bmp, int size_mask) {
        double mask[]= new double[size_mask*size_mask];
        for (int x = 0; x< size_mask;x++){
            for(int y= 0; y < size_mask;y++){
                mask[x+y*size_mask]= Math.exp(-(Math.pow(x,2))-(Math.pow(y,2)));
            }
        }
        int[] pixels = new int[size];
        bmp.getPixels(pixels, 0, width, 0,0, width, height);
        convolution(pixels,mask);
        bmp.setPixels(pixels,0, width, 0,0, width, height);


    }

    @Override
    public void sobel(Bitmap bmp) {
        int newRed;
        int newGreen;
        int newBlue;
        double mask_v[]= {-1,0,1,-2,0,2,-1,0,1};
        double mask_h[]= {-1,-2,-1,0,0,0,1,2,1};
        int[] pixels = new int[size];
        int[] vertical = new int[size];
        int[] horizontal = new int[size];
        bmp.getPixels(vertical, 0, width, 0,0, width, height);
        horizontal = vertical.clone();
        convolution(vertical,mask_v);
        convolution(horizontal,mask_h);
        for (int i = 0; i < size; i++){
            newRed = (int) Math.sqrt(Math.pow(Color.red(vertical[i]),2)-Math.pow(Color.red(horizontal[i]),2));
            newGreen = (int) Math.sqrt(Math.pow(Color.green(vertical[i]),2)-Math.pow(Color.green(horizontal[i]),2));
            newBlue = (int) Math.sqrt(Math.pow(Color.blue(vertical[i]),2)-Math.pow(Color.blue(horizontal[i]),2));
            pixels[i] = Color.rgb(newRed,newGreen,newBlue);
        }
        bmp.setPixels(pixels,0, width, 0,0, width, height);




    }

    @Override
    public void laplacien() {

    }
}
