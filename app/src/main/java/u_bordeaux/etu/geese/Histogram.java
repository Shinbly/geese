package u_bordeaux.etu.geese;

import android.graphics.Color;

/**
 * Created by jfachan on 16/02/18.
 */

public class Histogram {


    private static void histogram(Image img,int []histo) {
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);
        int value;
        for (int i = 0; i < img.getNbPixels(); i++) {
            value =(30 * Color.red(pixels[i]) + 59 * Color.green(pixels[i]) + 11 * Color.blue(pixels[i]))/100;
            histo[value]+=1;
        }
    }

    private void histogramRed(Image img,int []histo) {
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);
        for (int i = 0; i < img.getNbPixels(); i++) {
            histo[Color.red(pixels[i])]+=1;
        }
    }

    private void histogramGreen(Image img,int []histo) {
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);
        for (int i = 0; i < img.getNbPixels(); i++) {
            histo[Color.green(pixels[i])]+=1;
        }
    }
    private void histogramBlue(Image img,int []histo) {
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);
        for (int i = 0; i < img.getNbPixels(); i++) {
            histo[Color.blue(pixels[i])]+=1;
        }
    }

    private static int getMin(int[]histo) {
        int min = 0;
        while (histo[min] == 0) {
            min++;
        }
        return min;
    }
    private static int getMax(int[]histo) {
        int max = 255;
        while (histo[max] == 0) {
            max--;
        }
        return max;
    }

    public static void linearExtension(Image img){
        int[] histo= new int[256];
        histogram(img,histo);
        int min = getMin(histo);
        int max = getMax(histo);
        linearExtensionByParts(img,min,max);
    }



    public static void linearExtensionByParts(Image img,int min, int max) {
        int[]Lut  = new int[256];
        for (int i= 0;i< 256;i++)
            Lut[i]= (255*(i-min))/(max-min);
        applyLut(img,Lut);
    }

    public static void equalization(Image img){
        int idealnb = img.getNbPixels()/255;
        int counter = 0;
        int[] histo= new int[256];
        histogram(img,histo);
        int[]Lut  = new int[256];
        for (int i= 0;i< 256;i++){
            counter+= histo[i];
            Lut[i]=counter/idealnb;
        }
        applyLut(img,Lut);
    }


    public static void applyLut(Image img,int[] Lut){
        int newColor;
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);
        for (int i = 0; i < img.getNbPixels(); i++) {
            pixels[i] = Color.rgb(Lut[Color.red(pixels[i])], Lut[Color.green(pixels[i])], Lut[Color.blue(pixels[i])]);
        }
        img.setPixels(pixels);
    }




}