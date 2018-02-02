package u_bordeaux.etu.geese;

/**
 * Created by jfachan on 02/02/18.
 */

public interface Filters {
    /*égler la luminosité
Lorsqu’une image est affichée il doit être possible d’en régler la
luminosité
(b)
Régler le contraste
Lorsqu’une image est affichée il doit être possible d’en régler le
contraste
(c)
Égalisation d’histogramme
Le logiciel doit permettre d’égaliser l’histogramme de l’image
affichée
(d)
Filtrage couleur
Le logiciel doit permettre de mettre en oeuvre les traitements couleur vus
en TP, à savoir modification de la teinte (niveaux de gris, sépia, ...) ainsi que la sélection
d’une teinte à conserver lors du passage en niveaux de gris.
(e)
Convolution
Le logiciel doit permettre d’appliquer différents filtres basés sur une convolu-
tion sur l’image affichée. Les filtres moyenneur, Gaussien, Sobel et Laplacien seront implé-
mentés.

*/
    void brightness();
    void contrast();
    void histogram();

    void hue();
    void toGray();
    void sepia();



    void moyenneur();
    void gaussien();
    void sobel();
    void laplacien();



}
