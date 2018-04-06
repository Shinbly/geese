package u_bordeaux.etu.geese;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FragmentFilters extends Fragment implements Button.OnClickListener {
    View view;
    String tag;
    private FragmentFiltersListener listener;

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
                croppedBitmap = Bitmap.createBitmap(bmp,0,Math.abs(diff)/2, bmp.getWidth(),bmp.getHeight()-Math.abs(diff)/2);
            }else{
                croppedBitmap = Bitmap.createBitmap(bmp,Math.abs(diff)/2,0, bmp.getWidth()-Math.abs(diff)/2,bmp.getHeight());
            }

            bmp.recycle();

            img = new Image(Bitmap.createScaledBitmap(croppedBitmap,200,200,false));

        }catch (FileNotFoundException e){

        }

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
            Filters.negatif(img);
            negatif.setImageBitmap(img.getBmp());

            img.setBmp(bmpEqualization);
            Histogram.equalization(img);
            egalization.setImageBitmap(img.getBmp());

            img.setBmp(bmpLinearExtension);
            Histogram.linearExtension(img);
            linearExtention.setImageBitmap(img.getBmp());

            img.setBmp(bmpSobel);
            Convolution.gaussien(img,3,applicationContext);
            Filters.contrast(img,300);
            Convolution.sobelRS(img,applicationContext);
            sobel.setImageBitmap(img.getBmp());

            img.setBmp(bmpLaplacien);
            laplacien.setImageBitmap(img.getBmp());
        }

        return view;

    }

    @Override
    public void onClick(View v) {
        if (this.listener != null) {
            switch (v.getId()){
                case R.id.gray :
                    tag ="gray";
                    break;
                case  R.id.sepia:
                    tag = "sepia";
                    break;
                case  R.id.egalization:
                    tag = "egalization";
                    break;
                case  R.id.linearExtention :
                    tag = "linearExtention";
                    break;
                case R.id.negatif :
                    tag ="negatif";
                    break;
                case  R.id.laplacien :
                    tag = "laplacien";
                    break;
                case  R.id.sobel :
                    tag = "sobel";
                    break;
            }
            Log.i(tag, "in onClick: ");
            listener.onFilterSelected(tag);
        }
    }


    public interface FragmentFiltersListener {
        void onFilterSelected(String TAG);
    }
}
