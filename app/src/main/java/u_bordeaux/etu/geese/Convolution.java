package u_bordeaux.etu.geese;

import android.content.Context;
import android.graphics.Color;
import android.support.v8.renderscript.*;
import android.util.Log;

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
    private static void convolutionRS(Image img, int [] pixels, double []mask,Context context){
        int width = img.getWidth();
        int height = img.getHeight();

        RenderScript script = RenderScript.create(context);
        Type.Builder typeBuilder = new Type.Builder(script, Element.U32(script));
        typeBuilder.setX(width);
        typeBuilder.setY(height);
        Allocation dataIn = Allocation.createTyped(script,typeBuilder.create());
        Allocation dataOut = Allocation.createTyped(script,typeBuilder.create());
        Allocation filter = Allocation.createSized(script,Element.F64(script),mask.length);

        ScriptC_Convolution convo = new ScriptC_Convolution(script);
        filter.copy1DRangeFrom(0,mask.length,mask);
        convo.set_picture(dataIn);
        convo.bind_filter(filter);
        convo.set_fSize((int)(Math.sqrt(mask.length)));
        convo.set_imgWidth(img.getWidth());
        convo.set_imgHeight(img.getHeight());

        dataIn.copy2DRangeFrom(0,0,width,height,pixels);

        convo.forEach_convolution(dataIn,dataOut);

        dataOut.copy2DRangeTo(0,0,width,height,pixels);


    }


    public static void moyenneur(Image img, int size_mask,Context context) {
        double mask[]= new double[size_mask*size_mask];
        for (int i = 0; i< size_mask*size_mask; i++){
            mask[i]= 1;
        }
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);
        convolutionRS(img,pixels,mask,context);
        img.setPixels(pixels);

    }

    public static void gaussien(Image img, int size_mask,Context context) {
        double mask[]= new double[size_mask*size_mask];
        double coeff = 0;
        double sigma= 1+size_mask/4;
        int rayon = size_mask/2;
        for (int y = -rayon; y<= rayon;y++){
            for(int x= -rayon ; x <= rayon ;x++){
                mask[(x+rayon)+(y+rayon)*size_mask]=(Math.exp(-((x*x+y*y)/(2*(sigma*sigma))))) ;
            }
        }
        coeff = (Math.exp(-((rayon*rayon*2)/(2*sigma*sigma))));
        for (int i = 0 ; i < size_mask*size_mask;i++){
            mask[i]/= coeff;
        }
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);
        if (context != null) {
            convolutionRS(img,pixels,mask,context);
        }
        else {
            convolution(img, pixels, mask);
        }
        img.setPixels(pixels);
    }


    public static void sobel(Image img,Context context) {
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
        convolutionRS(img,vertical,mask_v,context);
        convolutionRS(img,horizontal,mask_h,context);
        for (int i = 0; i < img.getNbPixels(); i++){
            newRed = (int) Math.sqrt(Math.pow(Color.red(vertical[i]),2)+Math.pow(Color.red(horizontal[i]),2));
            newGreen = (int) Math.sqrt(Math.pow(Color.green(vertical[i]),2)+Math.pow(Color.green(horizontal[i]),2));
            newBlue = (int) Math.sqrt(Math.pow(Color.blue(vertical[i]),2)+Math.pow(Color.blue(horizontal[i]),2));
            pixels[i] = Color.rgb(newRed,newGreen,newBlue);
        }
        img.setPixels(pixels);




    }
    public static void sobelRS(Image img, Context context){

        Filters.toGray(img);
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);
        int width = img.getWidth();
        int height = img.getHeight();

        RenderScript script = RenderScript.create(context);
        Type.Builder typeBuilder = new Type.Builder(script, Element.U32(script));
        typeBuilder.setX(width);
        typeBuilder.setY(height);
        Allocation dataIn = Allocation.createTyped(script,typeBuilder.create());
        Allocation dataOut = Allocation.createTyped(script,typeBuilder.create());

        ScriptC_Sobel sobel = new ScriptC_Sobel(script);
        sobel.set_picture(dataIn);
        sobel.set_imgWidth(img.getWidth());
        sobel.set_imgHeight(img.getHeight());

        dataIn.copy2DRangeFrom(0,0,width,height,pixels);
        sobel.forEach_sobel(dataIn,dataOut);

        dataOut.copy2DRangeTo(0,0,width,height,pixels);

        img.setPixels(pixels);


    }

    public static void laplacien(Image img,Context context) {

        double mask_4cx[]= {0,1,0,1,-4,1,0,1,0};
        //double mask_8cx[]= {1,1,1,1,-8,1,1,1,1};

        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);
        convolutionRS(img,pixels,mask_4cx,context);
        //convolutionRS(img,pixels,mask_8cx,context);
        img.setPixels(pixels);

    }
}
