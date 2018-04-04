package u_bordeaux.etu.geese;

import android.graphics.Color;


/**
 * Class Histogram, represents the histogram of an image
 */

public class Histogram {

    /**
     * Method histogram
     * Return the RVB histogram of the image given in parameter and put all the values
     * in the array given in parameter
     * @param img the image from which we wants to extract the histogram
     * @param histo the array which will be filled with the values of the histogram
     */
    private static void histogram(Image img, int[] histo) {
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);

        int value;

        for (int i = 0; i < img.getNbPixels(); i++) {
            value = (30*Color.red(pixels[i]) + 59*Color.green(pixels[i]) + 11*Color.blue(pixels[i]))/100;
            histo[value]+=1;
        }
    }


    /**
     * Method histogramRed
     * Return the Red histogram of the image given in parameter and put all the values
     * in the array given in parameter
     * @param img the image from which we wants to extract the histogram
     * @param histo the array which will be filled with the values of the histogram
     */
    private void histogramRed(Image img, int[] histo) {
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);

        for (int i = 0; i < img.getNbPixels(); i++) {
            histo[Color.red(pixels[i])] += 1;
        }
    }


    /**
     * Method histogramGreen
     * Return the Green histogram of the image given in parameter and put all the values
     * in the array given in parameter
     * @param img the image from which we wants to extract the histogram
     * @param histo the array which will be filled with the values of the histogram
     */
    private void histogramGreen(Image img, int[] histo) {
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);

        for (int i = 0; i < img.getNbPixels(); i++) {
            histo[Color.green(pixels[i])] += 1;
        }
    }


    /**
     * Method histogramBlue
     * Return the Blue histogram of the image given in parameter and put all the values
     * in the array given in parameter
     * @param img the image from which we wants to extract the histogram
     * @param histo the array which will be filled with the values of the histogram
     */
    private void histogramBlue(Image img, int[] histo) {
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);

        for (int i = 0; i < img.getNbPixels(); i++) {
            histo[Color.blue(pixels[i])] += 1;
        }
    }


    /**
     * Method getMin
     * Return the minimum value of the histogram given in parameter
     * @param histo the histogram from which we wants to extract the minimum value
     * @return the minimum value of the array
     */
    private static int getMin(int[] histo) {
        int min = 0;

        while (histo[min] == 0) {
            min++;
        }

        return min;
    }


    /**
     * Method getMax
     * Return the maximum value of the histogram given in parameter
     * @param histo the histogram from which we wants to extract the maximum value
     * @return the maximum value of the array
     */
    private static int getMax(int[] histo) {
        int max = 255;

        while (histo[max] == 0) {
            max--;
        }

        return max;
    }


    /**
     * Method linearExtension
     * Retrieves the histogram of the image given in parameter, get the minimum and the maximum
     * values of the histogram and apply the method linearExtensionByPart
     * @param img the image on which the linearExtension will be apply
     */
    public static void linearExtension(Image img) {
        int[] histo = new int[256];
        histogram(img, histo);

        int min = getMin(histo);
        int max = getMax(histo);

        linearExtensionByParts(img, min, max);
    }


    /**
     * Method linearExtensionByParts
     * Create the lookup table and fills it, calls the method applyLut on the image given
     * in parameter
     * @param img the image on which the linearExtensions will be apply
     * @param min the minimum of the histogram of the image
     * @param max the maximum of the histogram of the image
     */
    private static void linearExtensionByParts(Image img, int min, int max) {
        int[] Lut = new int[256];

        for (int i = 0; i < 256; i++) {
            Lut[i] = (255*(i-min))/(max-min);
            Lut[i] = Math.max(0,(Math.min(255,Lut[i])));
        }

        applyLut(img, Lut);
    }


    /**
     * Method applyLut
     * Apply the values of a Lookup table, given in parameter, to the pixels of an image also
     * given in parameter
     * @param img the image which will be edited
     * @param Lut the lookup table to modify the pixels of the image
     */
    private static void applyLut(Image img, int[] Lut){
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);

        for (int i = 0; i < img.getNbPixels(); i++) {
            pixels[i] = Color.rgb(Lut[Color.red(pixels[i])], Lut[Color.green(pixels[i])], Lut[Color.blue(pixels[i])]);
        }

        img.setPixels(pixels);
    }


    /**
     * Method equalization
     * Works on the cumulative histogram of the image given in parameter to "spread" it.
     * @param img the image on which the equalization will be apply
     */
    public static void equalization(Image img){
        int idealnb = img.getNbPixels()/255;

        int counter = 0;

        int[] histo = new int[256];

        histogram(img, histo);

        int[] Lut = new int[256];

        for (int i = 0; i < 256; i++) {
            counter += histo[i];
            Lut[i] = counter/idealnb;
        }

        applyLut(img, Lut);
    }
}
