package u_bordeaux.etu.geese;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.*;

/**
 * Created by Lalie on 06/02/2018.
 */

public class Image {

    /**
     *
     */
    private Bitmap bmp;

    /**
     *
     */
    final private int[] backup;
    /**
     *
     */
    final private Context mainContext;


    /**
     *
     */
    public Image(Bitmap bmp, Context context) {
        this.bmp = bmp;
        this.mainContext=context;
        this.backup = new int[bmp.getHeight()*bmp.getWidth()];
        bmp.getPixels(backup, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public int getWidth(){
        return bmp.getWidth();
    }

    public int getHeight(){
        return bmp.getHeight();
    }

    public int getNbPixels(){
        return bmp.getHeight()*bmp.getWidth();
    }

    public void restore() {
        bmp.setPixels(backup, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }

    public void getPixels(int []pixels){
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0,0, bmp.getWidth(), bmp.getHeight());
    }

    public void setPixels(int []pixels){
        bmp.setPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }

    public void getHsv(float[][]hsv){
        int[] pixels = new int[this.getWidth()*this.getHeight()];
        this.getPixels(pixels);
        for (int i = 0; i < (this.getWidth()*this.getHeight()); i++) {
            Color.colorToHSV(pixels[i], hsv[i]);
        }
    }

    public void setHsv(float[][]hsv){
        int[] pixels = new int[this.getWidth()*this.getHeight()];
        for (int i = 0; i < (this.getWidth()*this.getHeight()); i++) {
            pixels[i] = Color.HSVToColor(hsv[i]);
        }
        this.setPixels(pixels);
    }


}
