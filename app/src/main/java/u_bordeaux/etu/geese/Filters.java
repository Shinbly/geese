package u_bordeaux.etu.geese;

import android.graphics.Bitmap;

/**
 * Created by jfachan on 02/02/18.
 */

public interface Filters {

    /**
     *
     * @param bmp
     * @param value
     */
    void brightness(Bitmap bmp, int value);

    /**
     *
     * @param
     */
    void contrast();

    /**
     *
     * @param
     */
    void histogram();

    /**
     *
     * @param
     */
    void hue(Bitmap bmp, int value);

    /**
     *
     * @param
     */
    void toGray(Bitmap bmp);

    /**
     *
     * @param
     */
    void sepia(Bitmap bmp);

    /**
     *
     * @param
     */
    void moyenneur(Bitmap bmp, int size);

    /**
     *
     * @param
     */
    void gaussien(Bitmap bmp, int size);

    /**
     *
     * @param
     */
    void sobel(Bitmap bmp);

    /**
     *
     * @param
     */
    void laplacien();

}
