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


        EditingActivity activity = (EditingActivity) getActivity();
        Uri pathImg = activity.getPathImg();

        Context applicationContext = activity.getApplicationContext();

        Bitmap bmp;
        Image img = null;

        try{
            final InputStream stream = applicationContext.getContentResolver().openInputStream(pathImg);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = true;
            options.inMutable = true;
            bmp = BitmapFactory.decodeStream(stream, null, options);

            Bitmap croppedBitmap;

            int diff = bmp.getWidth() - bmp.getHeight();
            if(diff < 0){
                croppedBitmap = Bitmap.createBitmap(bmp,0,Math.abs(diff)/2, bmp.getWidth(),bmp.getHeight()-Math.abs(diff));
            }else{
                croppedBitmap = Bitmap.createBitmap(bmp,Math.abs(diff)/2,0, bmp.getWidth()-Math.abs(diff),bmp.getHeight());
            }

            bmp.recycle();

            img = new Image(Bitmap.createScaledBitmap(croppedBitmap,200,200,false));

        }catch (FileNotFoundException e){

        }

        //The filters thumbnails
        if(img != null){

            Bitmap bmpSepia = img.getBmp().copy(img.getBmp().getConfig(),true);
            Bitmap bmpNegatif = img.getBmp().copy(img.getBmp().getConfig(),true);
            Bitmap bmpEqualization = img.getBmp().copy(img.getBmp().getConfig(),true);
            Bitmap bmpLinearExtension = img.getBmp().copy(img.getBmp().getConfig(),true);
            Bitmap bmpSobel = img.getBmp().copy(img.getBmp().getConfig(),true);
            Bitmap bmpLaplacien = img.getBmp().copy(img.getBmp().getConfig(),true);


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
        }


        return view;
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
            }

            listener.onFilterSelected(Tag);
        }
    }


    /**
     * Interface FragmentFiltersListener
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
