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
import android.widget.TableLayout;

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

    @BindView(R.id.hue)
    ImageButton hue;

    @BindView(R.id.saturation)
    ImageButton saturation;

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
        hue.setOnClickListener(this);
        saturation.setOnClickListener(this);
        cancel.setOnClickListener(this);
        validate.setOnClickListener(this);

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
                    seekBarControl.setMax(pourcentMax*2);
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
            }

            if (v.getId() == R.id.validate) {
                listener.onFilterSelected(Tag,progress);
            }

            if (v.getId() == R.id.cancel) {
                listener.onFilterSelected("cancel",progress);
            }

            if (v.getId() == R.id.cancel || v.getId() == R.id.validate) {

                seekBarLayout.setVisibility(View.INVISIBLE);
                filtersLayout.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);

            }else{
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
                case "brightness":
                    this.progress = progress - pourcentMax;
                    break;

                case "saturation":
                    this.progress = progress - pourcentMax;
                    break;

                case "contrast":
                    this.progress = progress - colorMax;
                    break;

                case "blur":
                    this.progress = progress*2+1; // to be a impair number
                    break;

                case "hue":
                    this.progress = progress-(degresMax/2);
                    break;

                default:
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
