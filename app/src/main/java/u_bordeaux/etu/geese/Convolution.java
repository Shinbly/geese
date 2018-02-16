package u_bordeaux.etu.geese;

import android.graphics.Color;

/**
 * Created by jfachan on 16/02/18.
 */

public class Convolution {


    private static void convolution(Image img, int [] pixels, double []filtre) {
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

        for (int x = side; x < img.getWidth()-side ; x++ ) {
            for (int y = side; y < img.getHeight() - side; y++) {
                newRed = 0;
                newBlue= 0;
                newGreen= 0;
                for (int i= 0 ; i < filtre.length; i ++){
                    // la position est donc de x = ((x-taille/2)+(i%taille))     et y  = y -taille/2 + i/taille
                    position = ((x-f_size/2)+(i%f_size))+(img.getWidth()*(y -f_size/2 + i/f_size));
                    newRed += (Color.red(tmppixels[position]) * filtre[i]);
                    newGreen += (Color.green(tmppixels[position]) * filtre[i]);
                    newBlue += (Color.blue(tmppixels[position]) * filtre[i]);
                }
                pixels[x+img.getWidth()*y] = Color.rgb((int) (newRed/coeff),(int) (newGreen/coeff),(int) (newBlue/coeff));
            }
        }



    }


    public static void moyenneur(Image img, int size_mask) {
        double mask[]= new double[size_mask*size_mask];
        for (int i = 0; i< size_mask*size_mask; i++){
            mask[i]= 1;
        }
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);
        convolution(img,pixels,mask);
        img.setPixels(pixels);

    }

    public static void gaussien(Image img, int size_mask) {
        double mask[]= new double[size_mask*size_mask];
        for (int x = 0; x< size_mask;x++){
            for(int y= 0; y < size_mask;y++){
                mask[x+y*size_mask]= Math.exp(-(Math.pow(x,2))-(Math.pow(y,2)));
            }
        }
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);
        convolution(img,pixels,mask);
        img.setPixels(pixels);


    }


    public static void sobel(Image img) {
        int newRed;
        int newGreen;
        int newBlue;
        double mask_v[]= {-1,0,1,-2,0,2,-1,0,1};
        double mask_h[]= {-1,-2,-1,0,0,0,1,2,1};
        int[] pixels = new int[img.getNbPixels()];
        int[] vertical = new int[img.getNbPixels()];
        int[] horizontal;
        img.getPixels(vertical);
        horizontal = vertical.clone();
        convolution(img,vertical,mask_v);
        convolution(img,horizontal,mask_h);
        for (int i = 0; i < img.getNbPixels(); i++){
            newRed = (int) Math.sqrt(Math.pow(Color.red(vertical[i]),2)+Math.pow(Color.red(horizontal[i]),2));
            newGreen = (int) Math.sqrt(Math.pow(Color.green(vertical[i]),2)+Math.pow(Color.green(horizontal[i]),2));
            newBlue = (int) Math.sqrt(Math.pow(Color.blue(vertical[i]),2)+Math.pow(Color.blue(horizontal[i]),2));
            pixels[i] = Color.rgb(newRed,newGreen,newBlue);
        }
        img.setPixels(pixels);




    }

    public void laplacien() {

    }
}
