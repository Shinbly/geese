package u_bordeaux.etu.geese;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Class FragmentEdit, represents the fragment which handle the different editing buttons
 */

public class FragmentEdit extends Fragment implements ImageButton.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private View view;


    private FragmentEditListener listener;


    /**
     * Constants for the seekbars
     */
    private int colorMax = 255;
    private int pourcentMax = 100;
    private int degresMax = 360;


    /**
     * All the buttons of the fragment plus the seekbar
     */
    @BindView(R.id.brightness)
    ImageButton brightness;

    @BindView(R.id.contrast)
    ImageButton contrast;

    @BindView(R.id.blurring)
    ImageButton blur;

    @BindView(R.id.averager)
    ImageButton averager;

    @BindView(R.id.hue)
    ImageButton hue;

    @BindView(R.id.saturation)
    ImageButton saturation;

    @BindView(R.id.colorization)
    ImageButton colorization;

    @BindView(R.id.seekBarEdit)
    SeekBar seekBarControl;

    @BindView(R.id.cancel)
    Button cancel;

    @BindView(R.id.validate)
    Button validate;

    @BindView(R.id.seekBarLayout)
    RelativeLayout seekBarLayout;

    @BindView(R.id.filtersLayout)
    LinearLayout filtersLayout;

    @BindView(R.id.layout_colorization)
    RelativeLayout ColorizationLayout;



    @BindView(R.id.colo_none)
    ImageButton none;

    @BindView(R.id.colo_jaune)
    ImageButton jaune;

    @BindView(R.id.colo_orange)
    ImageButton orange;

    @BindView(R.id.colo_rouge)
    ImageButton rouge;

    @BindView(R.id.colo_rose)
    ImageButton rose;

    @BindView(R.id.colo_violet)
    ImageButton violet;

    @BindView(R.id.colo_bleu)
    ImageButton bleu;

    @BindView(R.id.colo_turquoise)
    ImageButton turquoise;

    @BindView(R.id.colo_vert)
    ImageButton vert;

    @BindView(R.id.validate_colo)
    Button validate_colo;

    @BindView(R.id.cancel_colo)
    Button cancel_colo;


    TabLayout tabLayout;


    /**
     * The tag to know which button had been pressed
     * The tag is pass to the activity with the interface FragmentEditListener
     */
    private String Tag = "";


    /**
     * The value which will be given to the method of Filters based on the value set on the seekbar
     */
    private int progress;




    /**
     * Setter of the listener of the Fragment
     * @param listener
     */
    public void setListener(FragmentEditListener listener) {
        this.listener = listener;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_fragment, container, false);
        ButterKnife.bind(this,view);

        brightness.setOnClickListener(this);
        contrast.setOnClickListener(this);
        blur.setOnClickListener(this);
        averager.setOnClickListener(this);
        hue.setOnClickListener(this);
        saturation.setOnClickListener(this);
        colorization.setOnClickListener(this);
        cancel.setOnClickListener(this);
        validate.setOnClickListener(this);

        none.setOnClickListener(this);
        jaune.setOnClickListener(this);
        orange.setOnClickListener(this);
        rouge.setOnClickListener(this);
        rose.setOnClickListener(this);
        violet.setOnClickListener(this);
        bleu.setOnClickListener(this);
        turquoise.setOnClickListener(this);
        vert.setOnClickListener(this);
        validate_colo.setOnClickListener(this);
        cancel_colo.setOnClickListener(this);

        return view;
    }




    @Override
    public void onClick(View v) {
        EditingActivity activity = (EditingActivity) getActivity();
        tabLayout = activity.getTabLayout();
        if (this.listener != null) {

            switch (v.getId()){
                case R.id.brightness :
                    seekBarControl.setMax(pourcentMax*2);
                    seekBarControl.setProgress(pourcentMax);
                    Tag = "brightness";
                    break;

                case R.id.saturation :
                    seekBarControl.setMax(pourcentMax*3);
                    seekBarControl.setProgress(pourcentMax);
                    Tag = "saturation";
                    break;

                case R.id.contrast :
                    seekBarControl.setMax(colorMax*2);
                    seekBarControl.setProgress(colorMax);
                    Tag = "contrast";
                    break;

                case R.id.blurring :
                    seekBarControl.setMax(4);
                    seekBarControl.setProgress(0);
                    Tag = "blur";
                    break;

                case R.id.averager :
                    seekBarControl.setMax(4);
                    seekBarControl.setProgress(0);
                    Tag = "averager";
                    break;

                case R.id.sobel :
                    seekBarControl.setMax(colorMax*2);
                    seekBarControl.setProgress(colorMax);
                    Tag = "sobel";
                    break;

                case R.id.hue :
                    seekBarControl.setMax(degresMax);
                    seekBarControl.setProgress(degresMax/2);
                    Tag = "hue";
                    break;

                case R.id.colo_jaune :
                    Tag = "colorization";
                    progress = 48;
                    listener.onPreviewSelected(Tag, (this.progress));
                    break;

                case R.id.colo_orange :
                    Tag = "colorization";
                    progress = 22;
                    listener.onPreviewSelected(Tag, (this.progress));
                    break;

                case R.id.colo_rouge :
                    Tag = "colorization";
                    progress = 360;
                    listener.onPreviewSelected(Tag, (this.progress));
                    break;

                case R.id.colo_rose :
                    Tag = "colorization";
                    progress = 330;
                    listener.onPreviewSelected(Tag, (this.progress));
                    break;

                case R.id.colo_violet :
                    Tag = "colorization";
                    progress = 283;
                    listener.onPreviewSelected(Tag, (this.progress));
                    break;

                case R.id.colo_bleu :
                    Tag = "colorization";
                    progress = 241;
                    listener.onPreviewSelected(Tag, (this.progress));
                    break;

                case R.id.colo_turquoise :
                    Tag = "colorization";
                    progress = 180;
                    listener.onPreviewSelected(Tag, (this.progress));
                    break;

                case R.id.colo_vert :
                    Tag = "colorization";
                    progress = 103;
                    listener.onPreviewSelected(Tag, (this.progress));
                    break;

                case R.id.colo_none :
                    listener.onFilterSelected("cancel",progress);
                    break;
            }

            if (v.getId() == R.id.validate || v.getId() == R.id.validate_colo) {
                listener.onFilterSelected(Tag,progress);
            }

            if (v.getId() == R.id.cancel || v.getId() == R.id.cancel_colo) {
                listener.onFilterSelected("cancel",progress);
            }

            if (v.getId() == R.id.cancel || v.getId() == R.id.validate) {

                //Hides the seekbar and shows the tabLayout and the filters
                seekBarLayout.setVisibility(View.INVISIBLE);
                filtersLayout.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);

            }else if(v.getId() == R.id.cancel_colo || v.getId() == R.id.validate_colo){
                //Hides the colorization buttons and shows filters
                ColorizationLayout.setVisibility(View.INVISIBLE);
                filtersLayout.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);

            }else if(v.getId() == R.id.colorization || v.getId() == R.id.colo_jaune || v.getId() == R.id.colo_bleu ||
                    v.getId() == R.id.colo_orange || v.getId() == R.id.colo_rouge || v.getId() == R.id.colo_rose ||
                    v.getId() == R.id.colo_violet || v.getId() == R.id.colo_turquoise || v.getId() == R.id.colo_vert || v.getId() == R.id.colo_none){
                //Hides the filters and shows all the smalls buttons
                seekBarLayout.setVisibility(View.INVISIBLE);
                filtersLayout.setVisibility(View.INVISIBLE);
                tabLayout.setVisibility(View.INVISIBLE);
                ColorizationLayout.setVisibility(View.VISIBLE);
            }else{
                //Hides the filters and the tablayout and shows the seekbar
                seekBarLayout.setVisibility(View.VISIBLE);
                filtersLayout.setVisibility(View.INVISIBLE);
                tabLayout.setVisibility(View.INVISIBLE);
            }

            seekBarControl.setOnSeekBarChangeListener(this);
            listener.onPreviewStart();
        }
    }




    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (this.listener != null) {

            switch (Tag) {
                case "brightness" :
                    this.progress = progress - pourcentMax; //To be between -100 and 100
                    break;

                case "saturation" :
                    this.progress = progress - pourcentMax; //To be between -100 and 100
                    break;

                case "contrast" :
                    this.progress = progress - colorMax;
                    break;

                case "averager" :
                    this.progress = progress*2+1; //The progress needs to be odd
                    break;

                case "blur" :
                    this.progress = progress*2+1; //The progress needs to be odd
                    break;

                case "hue" :
                    this.progress = progress-(degresMax/2); //So the hue can be between -180 and 180
                    break;

                default :
                    this.progress = progress;
                    break;
            }
            listener.onPreviewSelected(Tag, (this.progress));
        }
    }




    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }




    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }






    /**
     * Interface FragmentEditListener
     * Acts as a bond between the fragment and the EditingActivity
     */
    public interface FragmentEditListener {

        /**
         * Method onPreviewStart
         * Creates a preview
         */
        void onPreviewStart();


        /**
         * Method onPreviewSelected
         * Applies the method of Filters corresponding to the TAG on the preview according to the
         * changes of the seekbar (progress)
         * @param TAG a string which matches the button clicked
         * @param progress the value to pass to the methods of Filters
         */
        void onPreviewSelected(String TAG,int progress);


        /**
         * Method onFilter
         * Applies the filter on the original image when the modification is done
         * @param TAG a string which matches the button clicked
         * @param progress the value to pass to the methods of Filters
         */
        void onFilterSelected(String TAG,int progress);
    }
}
