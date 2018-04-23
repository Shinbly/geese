package u_bordeaux.etu.geese;

import android.content.Context;
import android.graphics.Color;
import android.support.v8.renderscript.*;


/**
 * Class Convolution, manages all the method usefull for the convolution
 */

public class Convolution {

    /**
     * Method convolution
     * Does a convolution but not in renderscript (not use in the project)
     * @param img the image on which apply the convolution
     * @param pixels the pixels of the image
     * @param filtre the convolution's mask
     */
    private static void convolution(Image img, int[] pixels, double[] filtre) {
        int f_size = (int)Math.sqrt(filtre.length);
        int side = f_size/2;
        int newRed;
        int newGreen;
        int newBlue;
        int position;
        int pos_mask;
        double coeff = 0;

        for (int i = 0 ; i < filtre.length; i ++){
            coeff += filtre[i];
        }

        int[] tmppixels = pixels.clone();

        for (int x = side; x < img.getWidth()-side ; x++ ){
            for (int y = side; y < img.getHeight()-side; y++){
                newRed = 0;
                newBlue = 0;
                newGreen = 0;

                for (int i = -side ; i <= side; i ++){
                    for (int j = -side; j <= side; j++){
                        position = (x+i) + (img.getWidth() * (y+j));
                        pos_mask = i + side + (j+side)*f_size;

                        newRed += (Color.red(tmppixels[position]) * filtre[pos_mask]);
                        newGreen += (Color.green(tmppixels[position]) * filtre[pos_mask]);
                        newBlue += (Color.blue(tmppixels[position]) * filtre[pos_mask]);
                    }
                }

                pixels[x + img.getWidth()*y] = Color.rgb((int)(newRed/coeff), (int)(newGreen/coeff), (int)(newBlue/coeff));
            }
        }
    }


    /**
     * Method convolutionRs
     * Does a convolution on the image given as parameter but optimized with renderscript
     * @param img the image on which apply the convolution
     * @param pixels the pixels of the image
     * @param mask the convolution's mask
     * @param context the application's context
     */
    private static void convolutionRS(Image img, int[] pixels, double[] mask, Context context) {
        int width = img.getWidth();
        int height = img.getHeight();

        RenderScript script = RenderScript.create(context);
        Type.Builder typeBuilder = new Type.Builder(script, Element.U32(script));
        typeBuilder.setX(width);
        typeBuilder.setY(height);
        Allocation dataIn = Allocation.createTyped(script, typeBuilder.create());
        Allocation dataOut = Allocation.createTyped(script, typeBuilder.create());
        Allocation filter = Allocation.createSized(script, Element.F64(script), mask.length);

        ScriptC_Convolution convo = new ScriptC_Convolution(script);
        filter.copy1DRangeFrom(0, mask.length, mask);

        convo.set_picture(dataIn);
        convo.bind_filter(filter);
        convo.set_fside((int)(Math.sqrt(mask.length)/2));
        convo.set_imgWidth(img.getWidth());
        convo.set_imgHeight(img.getHeight());

        dataIn.copy2DRangeFrom(0, 0, width, height, pixels);

        convo.forEach_convolution(dataIn, dataOut);

        dataOut.copy2DRangeTo(0,0, width, height, pixels);
    }


    /**
     * Method moyenneur
     * Applies a moyenneur mask on the image given as parameter. Change each pixels of the image by
     * the average of the nearest pixels
     * @param img the image on which apply the moyenneur filter
     * @param size_mask the size of the mask
     * @param context the application's context
     */
    public static void moyenneur(Image img, int size_mask, Context context) {
        double[] mask = new double[size_mask * size_mask];

        for (int i = 0; i < size_mask*size_mask; i++){
            mask[i]= 1;
        }

        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);

        convolutionRS(img, pixels, mask, context);

        img.setPixels(pixels);
    }


    /**
     * Method gaussien
     * Create a mask and compute a convolution on each pixel of the mask based on a gaussien function
     * @param img the image on which apply the gaussien filter
     * @param size_mask the size of the mask
     * @param context the application's context
     */
    public static void gaussien(Image img, int size_mask, Context context) {
        if (size_mask%2 != 0 && size_mask != 1) {
            int size = size_mask;
            double[] mask = new double[size*size];
            double sigma = 0.3;
            int rayon = size/2;
            double pos;

            //Value of all mask's corner
            double norm = (Math.exp(-((rayon*rayon*2) / (2*(sigma*sigma)))));

            for (float y = -rayon; y <= rayon; y++) {
                for (float x = -rayon; x <= rayon; x++) {
                    pos = (int)((Math.exp(-((x*x + y*y) / (2*(sigma*sigma))))) / norm);
                    mask[(int)((x+rayon) + (y+rayon) * size)] = pos;
                }
            }

            int[] pixels = new int[img.getNbPixels()];
            img.getPixels(pixels);

            convolutionRS(img, pixels, mask, context);

            img.setPixels(pixels);
        }
    }


    /**
     * Method sobel
     * Applies an horizontal and vertical mask on the image given as paramater
     * @param img the image on which apply the sobel filter
     * @param context the application's context
     */
    public static void sobel(Image img, Context context) {
        int newRed;
        int newGreen;
        int newBlue;

        double[] mask_v = {-1,0,1,-2,0,2,-1,0,1};
        double[] mask_h = {-1,-2,-1,0,0,0,1,2,1};

        int[] pixels = new int[img.getNbPixels()];
        int[] vertical = new int[img.getNbPixels()];
        int[] horizontal;
        img.getPixels(vertical);

        horizontal = vertical.clone();

        convolutionRS(img,vertical, mask_v,context);

        convolutionRS(img,horizontal, mask_h,context);

        for (int i = 0; i < img.getNbPixels(); i++){
            newRed = (int)Math.sqrt(Math.pow(Color.red(vertical[i]), 2) + Math.pow(Color.red(horizontal[i]), 2));
            newGreen = (int)Math.sqrt(Math.pow(Color.green(vertical[i]), 2) + Math.pow(Color.green(horizontal[i]), 2));
            newBlue = (int)Math.sqrt(Math.pow(Color.blue(vertical[i]), 2) + Math.pow(Color.blue(horizontal[i]), 2));

            pixels[i] = Color.rgb(newRed,newGreen,newBlue);
        }

        img.setPixels(pixels);
    }


    /**
     * Method sobelRs
     * Same as previous but optimized with renderscript
     * @param img the image on which apply the sobel filter
     * @param context the application's context
     */
    public static void sobelRS(Image img, Context context) {
        Filters.toGray(img);

        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);

        int width = img.getWidth();
        int height = img.getHeight();

        RenderScript script = RenderScript.create(context);
        Type.Builder typeBuilder = new Type.Builder(script, Element.U32(script));
        typeBuilder.setX(width);
        typeBuilder.setY(height);
        Allocation dataIn = Allocation.createTyped(script, typeBuilder.create());
        Allocation dataOut = Allocation.createTyped(script, typeBuilder.create());

        ScriptC_Sobel sobel = new ScriptC_Sobel(script);
        sobel.set_picture(dataIn);
        sobel.set_imgWidth(img.getWidth());
        sobel.set_imgHeight(img.getHeight());

        dataIn.copy2DRangeFrom(0, 0 ,width, height, pixels);
        sobel.forEach_sobel(dataIn, dataOut);

        dataOut.copy2DRangeTo(0,0, width, height, pixels);

        img.setPixels(pixels);
    }


    /**
     * Method laplacien
     * @param img the image on which apply the laplacien filter
     * @param context the application's context
     */
    public static void laplacien(Image img, Context context) {
        double mask_8cx[]= {1,1,1,1,-8,1,1,1,1};

        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);

        convolutionRS(img, pixels ,mask_8cx, context);

        img.setPixels(pixels);
    }
}
