package u_bordeaux.etu.geese;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Class FragmentFilters, represents the fragment that handles the different filters buttons
 */

public class FragmentFilters extends Fragment implements Button.OnClickListener {

    private View view;


    private FragmentFiltersListener listener;

    /**
     * All the buttons of the fragment plus the seekbar
     */
    @BindView(R.id.gray)
    ImageButton gray;

    @BindView(R.id.sepia)
    ImageButton sepia;

    @BindView(R.id.egalization)
    ImageButton egalization;

    @BindView(R.id.linearExtention)
    ImageButton linearExtention;

    @BindView(R.id.negatif)
    ImageButton negatif;

    @BindView(R.id.sobel)
    ImageButton sobel;

    @BindView(R.id.laplacien)
    ImageButton laplacien;

    @BindView(R.id.sketch)
    ImageButton sketch;


    /**
     * The tag to know which button had been pressed
     * The tag is pass to the activity with the interface FragmentFiltersListener
     */
    private String Tag = "";




    /**
     * Setter of the listener of the Fragment
     * @param listener
     */
    public void setListener(FragmentFiltersListener listener) {
        this.listener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.filters_fragment, container, false);
        ButterKnife.bind(this,view);

        gray.setOnClickListener(this);
        sepia.setOnClickListener(this);
        linearExtention.setOnClickListener(this);
        egalization.setOnClickListener(this);
        negatif.setOnClickListener(this);
        laplacien.setOnClickListener(this);
        sobel.setOnClickListener(this);
        sketch.setOnClickListener(this);


        //Retrieves the image displayed in the Viewer of the EditingActivity to create the
        // thumbnails of the filters
        EditingActivity activity = (EditingActivity) getActivity();
        Uri pathImg = activity.getPathImg();

        Context applicationContext = activity.getApplicationContext();

        Bitmap bmp;
        Image img = null;

        try{
            final InputStream stream = applicationContext.getContentResolver().openInputStream(pathImg);
            final InputStream streamS = applicationContext.getContentResolver().openInputStream(pathImg);

            //Returns a bitmap smaller than the image retrieve from the EditingActivity
            bmp = decodeSampledBitmapFromStream(stream,streamS);

            Bitmap croppedBitmap;

            //Crops the bitmap width or the bitmap height so the thumbnails are square
            int diff = bmp.getWidth() - bmp.getHeight();
            if(diff < 0){
                croppedBitmap = Bitmap.createBitmap(bmp,0,Math.abs(diff)/2, bmp.getWidth(),bmp.getHeight()-Math.abs(diff));
            }else{
                croppedBitmap = Bitmap.createBitmap(bmp,Math.abs(diff)/2,0, bmp.getWidth()-Math.abs(diff),bmp.getHeight());
            }

            img = new Image(Bitmap.createScaledBitmap(croppedBitmap,200,200,false));

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        //The filters thumbnails
        if(img != null){

            //Each filter has a bitmap but they are small and don't take too much space on memory
            Bitmap bmpSepia = img.getBmp().copy(img.getBmp().getConfig(),true);
            Bitmap bmpNegatif = img.getBmp().copy(img.getBmp().getConfig(),true);
            Bitmap bmpEqualization = img.getBmp().copy(img.getBmp().getConfig(),true);
            Bitmap bmpLinearExtension = img.getBmp().copy(img.getBmp().getConfig(),true);
            Bitmap bmpSobel = img.getBmp().copy(img.getBmp().getConfig(),true);
            Bitmap bmpLaplacien = img.getBmp().copy(img.getBmp().getConfig(),true);
            Bitmap bmpSketch = img.getBmp().copy(img.getBmp().getConfig(),true);



            //Set the thumbnail of each filter
            Filters.toGray(img);
            gray.setImageBitmap(img.getBmp());

            img.setBmp(bmpSepia);
            Filters.sepia(img);
            sepia.setImageBitmap(img.getBmp());

            img.setBmp(bmpNegatif);
            Filters.negative(img);
            negatif.setImageBitmap(img.getBmp());

            img.setBmp(bmpEqualization);
            Histogram.equalization(img);
            egalization.setImageBitmap(img.getBmp());

            img.setBmp(bmpLinearExtension);
            Histogram.linearExtension(img);
            linearExtention.setImageBitmap(img.getBmp());

            img.setBmp(bmpSobel);
            Convolution.sobelRS(img, applicationContext);
            sobel.setImageBitmap(img.getBmp());

            img.setBmp(bmpLaplacien);
            Convolution.laplacien(img,applicationContext);
            laplacien.setImageBitmap(img.getBmp());

            img.setBmp(bmpSketch);
            Filters.sketch(img,applicationContext);
            sketch.setImageBitmap(img.getBmp());
        }


        return view;
    }






    /**
     * Method decodeSampleBitmapFromStream
     * Return a bitmap smaller than the one passed with the stream. The original bitmap is not
     * charged into the memory, so it doesn't overload it
     * @param stream the stream which "contains" the bitmap
     * @param streamS the same stream used to decode the smaller bitmap
     * @return a smaller version of the bitmap
     */
    public Bitmap decodeSampledBitmapFromStream(InputStream stream, InputStream streamS) {

        // First decode with inJustDecodeBounds=true to check dimensions
        //Not charged into memory
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream,null,options);

        //Compute the reduction factor with the width and the height given as parameters
        options.inSampleSize = calculateInSampleSize(options,200,200);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inScaled = true;
        options.inMutable = true;

        return BitmapFactory.decodeStream(streamS,null,options);
    }






    /**
     * Method calculateInSampleSize
     * Computes the reduction factor to apply to the bitmap to reduce it based on the width and the
     * height given in parameters
     * @param options the option of the Bitmap factory, to retrieve the width and height of the
     *                original bitmp
     * @param reqWidth the desired width of the resized bitmap
     * @param reqHeight the desired height of the resized bitmap
     * @return the reduction factor
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }






    @Override
    public void onClick(View v) {
        if (this.listener != null) {

            switch (v.getId()){
                case R.id.gray :
                    Tag ="gray";
                    break;

                case  R.id.sepia:
                    Tag = "sepia";
                    break;

                case  R.id.egalization:
                    Tag = "egalization";
                    break;

                case  R.id.linearExtention :
                    Tag = "linearExtention";
                    break;

                case R.id.negatif :
                    Tag ="negative";
                    break;

                case  R.id.laplacien :
                    Tag = "laplacien";
                    break;

                case  R.id.sobel :
                    Tag = "sobel";
                    break;

                case  R.id.sketch :
                    Tag = "sketch";
                    break;
            }

            listener.onFilterSelected(Tag);
        }
    }






    /**
     * Interface FragmentFiltersListener
     * Acts as a bond between the fragment and the EditingActivity
     */
    public interface FragmentFiltersListener {

        /**
         * Method onFilterSelected
         * Applies the filter on the original image when the modification is done
         * @param TAG a string which matches the button clicked
         */
        void onFilterSelected(String TAG);
    }
}
