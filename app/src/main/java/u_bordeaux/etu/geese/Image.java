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
        getHsvRs(hsv);
        /*
        int[] pixels = new int[this.getWidth()*this.getHeight()];
        this.getPixels(pixels);
        for (int i = 0; i < (this.getWidth()*this.getHeight()); i++) {
            Color.colorToHSV(pixels[i], hsv[i]);
        }*/
    }

    public void setHsv(float[][]hsv){
        int[] pixels = new int[this.getWidth()*this.getHeight()];
        for (int i = 0; i < (this.getWidth()*this.getHeight()); i++) {
            pixels[i] = Color.HSVToColor(hsv[i]);
        }
        this.setPixels(pixels);
    }

    public void getHsvRs(float[][]hsv){
        int width = this.getWidth();
        int height = this.getHeight();
        int nbPixels= this.getNbPixels();
        int[] pixels = new int[this.getWidth()*this.getHeight()];
        this.getPixels(pixels);

        RenderScript script = RenderScript.create(mainContext);
        Type.Builder typeBuilder = new Type.Builder(script, Element.U32(script));
        typeBuilder.setX(width);
        typeBuilder.setY(height);
        Allocation dataIn = Allocation.createTyped(script,typeBuilder.create());
        Allocation dataOut = Allocation.createTyped(script,typeBuilder.create());
        ScriptC_HSVConverter hsvToRvb = new ScriptC_HSVConverter(script);
        dataIn.copy2DRangeFrom(0,0,width,height,pixels);
        hsvToRvb.forEach_rgbToHsv(dataIn,dataOut);
        dataOut.copy2DRangeTo(0,0,width,height,pixels);
        for (int i= 0 ; i < nbPixels; i++){
            System.out.println(pixels[i]);
            hsv[i][0] =pixels[i]/1000000;//(float) ((pixels[i] >> 16) & 0x1FF);
            hsv[i][1] =pixels[i]/1000;// (float)((pixels[i] >>  8) & 0x7F);
            hsv[i][2] =pixels[i];//(float)((pixels[i]      ) & 0x7F);
        }
    }

}
