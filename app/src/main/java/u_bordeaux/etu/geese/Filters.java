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
    void hue();

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
    void moyenneur();

    /**
     *
     * @param
     */
    void gaussien();

    /**
     *
     * @param
     */
    void sobel();

    /**
     *
     * @param
     */
    void laplacien();

}
