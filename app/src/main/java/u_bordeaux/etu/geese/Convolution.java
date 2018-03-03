package u_bordeaux.etu.geese;

import android.graphics.Color;

/**
 * Created by jfachan on 16/02/18.
 */

public class Convolution {


    private static void convolution(Image img, int [] pixels, double []filtre) {
        int f_size = (int) Math.sqrt(filtre.length);
        int side= f_size/2;
        int newRed;
        int newGreen;
        int newBlue;
        int position;
        int pos_mask;
        double coeff = 0;

        for (int i= 0 ; i < filtre.length; i ++){
            coeff += filtre[i];
        }
        System.out.println(coeff);

        int []tmppixels = pixels.clone();

        for (int x = side; x < img.getWidth()-side ; x++ ) {
            for (int y = side; y < img.getHeight() - side; y++) {
                newRed = 0;
                newBlue= 0;
                newGreen= 0;
                for (int i= -side ; i <= side; i ++) {
                    for (int j = -side; j <= side; j++) {
                        position = (x + i) + (img.getWidth() * (y + j));
                        pos_mask = i+side+(j+side)*f_size;
                        newRed += (Color.red(tmppixels[position]) * filtre[pos_mask]);
                        newGreen += (Color.green(tmppixels[position]) * filtre[pos_mask]);
                        newBlue += (Color.blue(tmppixels[position]) * filtre[pos_mask]);
                    }
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
        double coeff = 0;
        int sigma= size_mask/4;
        int rayon = size_mask/2;
        for (int y = -rayon; y<= rayon;y++){
            for(int x= -rayon ; x <= rayon ;x++){
                mask[(x+rayon)+(y+rayon)*size_mask]=(Math.exp(-((x*x+y*y)/(2*sigma*sigma)))) ;
            }
            System.out.println( "["+mask[0+(y+rayon)*size_mask]+" "+mask[1+(y+rayon)*size_mask]+" "+mask[2+(y+rayon)*size_mask]+" "+mask[3+(y+rayon)*size_mask]+" "+mask[4+(y+rayon)*size_mask]+"]");
        }
        coeff = (Math.exp(-((rayon*rayon*2)/(2*sigma*sigma))));
        for (int i = 0 ; i < size_mask*size_mask;i++){
            mask[i]/= coeff;
        }
        System.out.println(coeff);
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
