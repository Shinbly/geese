package u_bordeaux.etu.geese;

/**
 * Created by jfachan on 02/02/18.
 */

public interface Filters {

    /**
     *
     * @param
     */
    void brightness();

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
    void toGray();

    /**
     *
     * @param
     */
    void sepia();

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
