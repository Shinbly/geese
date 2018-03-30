package u_bordeaux.etu.geese;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.*;

/**
 * Created by jfachan on 16/02/18.
 */

public class Filters {

    private static int truncate(int value) {
        if (value < 0) {
            value = 0;
        } else if (value > 255) {
            value = 255;
        }

        return value;
    }

    public static void brightness(Image img, int value) {
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);

        int newRed;
        int newGreen;
        int newBlue;

        for (int i = 0; i < img.getNbPixels(); i++) {
            newRed = truncate(Color.red(pixels[i]) + value);
            newGreen = truncate(Color.green(pixels[i]) + value);
            newBlue = truncate(Color.blue(pixels[i]) + value);

            pixels[i] = Color.rgb(newRed, newGreen, newBlue);
        }
        img.setPixels(pixels);
    }

    public static void hue(Image img, int value) { //value between 0 and 360

        float[][] hsv = new float[img.getNbPixels()][3];
        img.getHsv(hsv);
        for (int i = 0; i < (img.getNbPixels()); i++) {
            hsv[i][0] = (hsv[i][0] + value) % 360;
        }
        img.setHsv(hsv);
    }

    public static void hueRs(Image img, int value,Context mainContext) { //value between -180 and 180

        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);
        int width = img.getWidth();
        int height = img.getHeight();

        RenderScript script = RenderScript.create(mainContext);
        Type.Builder typeBuilder = new Type.Builder(script, Element.U32(script));
        typeBuilder.setX(width);
        typeBuilder.setY(height);
        Allocation dataIn = Allocation.createTyped(script,typeBuilder.create());
        Allocation dataOut = Allocation.createTyped(script,typeBuilder.create());

        ScriptC_Hue hue = new ScriptC_Hue(script);
        hue.set_hueValue((float)value);

        dataIn.copy2DRangeFrom(0,0,width,height,pixels);

        hue.forEach_Hue(dataIn,dataOut);

        dataOut.copy2DRangeTo(0,0,width,height,pixels);



        img.setPixels(pixels);
    }


    public static void toGray(Image img) {
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);
        int newColor;

        for (int i = 0; i < img.getNbPixels(); i++) {
            newColor =(30 * Color.red(pixels[i]) + 59 * Color.green(pixels[i]) + 11 * Color.blue(pixels[i]))/100;
            pixels[i] = Color.rgb(newColor, newColor, newColor);
        }

        img.setPixels(pixels);
    }

    public static void sepia(Image img) {
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);

        int newRed;
        int newGreen;
        int newBlue;

        for (int i = 0; i < img.getNbPixels(); i++) {
            newRed = truncate((int) (Color.red(pixels[i]) * .393) + (int) (Color.green(pixels[i]) * .769) + (int) (Color.blue(pixels[i]) * .189));
            newGreen = truncate((int) (Color.red(pixels[i]) * .349) + (int) (Color.green(pixels[i]) * .686) + (int) (Color.blue(pixels[i]) * .168));
            newBlue = truncate((int) (Color.red(pixels[i]) * .272) + (int) (Color.green(pixels[i]) * .534) + (int) (Color.blue(pixels[i]) * .131));
            pixels[i] = Color.rgb(newRed, newGreen, newBlue);
        }

        img.setPixels(pixels);
    }

    public static void contrast(Image img, int value){//value between -255 and 255
        float coeff= (float)(259*(value+255)) / (float)((255 * (259-value)));
        int[] pixels = new int[img.getNbPixels()];
        img.getPixels(pixels);
        int newRed;
        int newGreen;
        int newBlue;
        for (int i = 0; i < img.getNbPixels(); i++) {
            newRed = (int)(coeff*(Color.red(pixels[i])-128) +128);
            newGreen = (int)(coeff*(Color.green(pixels[i])-128) +128);
            newBlue = (int)(coeff*(Color.blue(pixels[i])-128) +128);
            pixels[i] = Color.rgb(truncate(newRed), truncate(newGreen), truncate(newBlue));
        }
        img.setPixels(pixels);


    }

    public static void saturationRs(Image img, int value, Context context) {
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

        ScriptC_Saturation sat = new ScriptC_Saturation(script);
        sat.set_saturationValue((float)value);

        dataIn.copy2DRangeFrom(0,0,width,height,pixels);

        sat.forEach_Saturation(dataIn,dataOut);

        dataOut.copy2DRangeTo(0,0,width,height,pixels);



        img.setPixels(pixels);
    }
}
