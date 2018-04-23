package u_bordeaux.etu.geese;

import android.graphics.Bitmap;
import android.graphics.Color;


/**
 * Class Image, represents the editing image on which all the modifications will be apply
 */

public class Image {

    /**
     * the bitmap field which contains the image
     */
    private Bitmap bmp;


    /**
     * an array which contains all the initials pixels of the image for the function restore
     */
    private int[] backup;




    /**
     * Constructor of the class Image
     * Constructs an image with a bitmap and an array which contains the originals pixels of
     * the image
     * @param bmp the bitmap of the image
     */
    public Image(Bitmap bmp) {
        this.bmp = bmp;

        this.backup = new int[bmp.getHeight()*bmp.getWidth()];
        bmp.getPixels(backup, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }


    /**
     * Setter for the bitmap
     * @return the new value for the bitmap and the backup array which are the ones given as parameter
     */
    public void setBmp(Bitmap bmp){
        this.bmp = bmp;
        this.backup = new int[bmp.getHeight()*bmp.getWidth()];
        bmp.getPixels(backup, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }


    /**
     * Getter for the bitmap
     * @return the field bmp, the bitmap of the image
     */
    public Bitmap getBmp() {
        return bmp;
    }


    /**
     * Getter for the width of the image
     * @return the width of the bitmap
     */
    public int getWidth(){
        return bmp.getWidth();
    }


    /**
     * Getter for the height of the image
     * @return the height of the bitmap
     */
    public int getHeight() {
        return bmp.getHeight();
    }


    /**
     * Getter for the resolution of the image
     * @return the number of pixels of the bitmap
     */
    public int getNbPixels() {
        return bmp.getHeight()*bmp.getWidth();
    }


    /**
     * Getter for the pixels of the image
     * @param pixels an array which will be filled with the pixels of the bitmap
     */
    public void getPixels(int[] pixels) {
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }


    /**
     * Setter for the pixels of the image
     * @param pixels the new set of pixels of the image
     */
    public void setPixels(int[] pixels) {
        bmp.setPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }


    /**
     * Method getPreview
     * Return a bitmap scaled from the image's bitmap using the ratio of the width and the height
     * given in parameters
     * @param width the width of the ImageView on which the preview will be displayed
     * @param height the height of the ImageView on which the preview will be displayed
     * @return a bitmap scaled from the image's bitmap
     */
    public Bitmap getPreview(int width, int height) {
        float ratio = (float)(getWidth())/(float)getHeight();

        if(ratio < 1) {
            return Bitmap.createScaledBitmap(bmp, (int)(height*ratio), height, true);
        }else{
            return Bitmap.createScaledBitmap(bmp, width, (int)(width/ratio), true);
        }
    }


    /**
     * Method restore
     * Set the currents pixels of the image with the originals pixels of the image using
     * the field backup
     */
    public void restore() {
        bmp.setPixels(backup, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }


    /**
     * Method original
     * Return a bitmap with all the pixels of the original image
     * @return a bitmap with the same pixels than the original image
     */
    public Bitmap original() {
        Bitmap original = bmp.copy(bmp.getConfig(), true);
        original.setPixels(backup, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        return original;
    }


    /**
     * Method getHsv
     * Fill the array given in parameter with the value of each pixels of the image in HSV
     * @param hsv a two dimensions array of float
     */
    public void getHsv(float[][] hsv) {
        int[] pixels = new int[this.getWidth()*this.getHeight()];
        this.getPixels(pixels);

        for (int i = 0; i < (this.getWidth()*this.getHeight()); i++) {
            Color.colorToHSV(pixels[i], hsv[i]);
        }
    }


    /**
     * Method setHsv
     * Set the pixels of the images with the pixels contained in the array given in parameter
     * The pixels of the array are in HSV and convert into RGB before being set into the image
     * @param hsv a two dimensions array of float
     */
    public void setHsv(float[][] hsv) {
        int[] pixels = new int[this.getWidth()*this.getHeight()];

        for (int i = 0; i < (this.getWidth()*this.getHeight()); i++) {
            pixels[i] = Color.HSVToColor(hsv[i]);
        }

        this.setPixels(pixels);
    }
}
