package u_bordeaux.etu.geese;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private Button gray;

    @BindView(R.id.sepia)
    private Button sepia;

    @BindView(R.id.egalization)
    private Button egalization;

    @BindView(R.id.linearExtention)
    private Button linearExtention;

    @BindView(R.id.negatif)
    private Button negatif;

    @BindView(R.id.sobel)
    private Button sobel;

    @BindView(R.id.laplacien)
    private Button laplacien;

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
