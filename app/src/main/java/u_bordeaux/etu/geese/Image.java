package u_bordeaux.etu.geese;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

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
    public Image(Bitmap bmp) {
        this.bmp = bmp;
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
