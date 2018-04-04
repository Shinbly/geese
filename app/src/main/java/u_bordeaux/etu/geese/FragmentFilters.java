package u_bordeaux.etu.geese;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lalie on 03/03/2018.
 */

public class FragmentFilters extends Fragment implements Button.OnClickListener {
    View view;
    String tag;
    private FragmentFiltersListener listener;

    @BindView(R.id.gray)
    Button gray;

    @BindView(R.id.sepia)
    Button sepia;

    @BindView(R.id.egalization)
    Button egalization;

    @BindView(R.id.linearExtention)
    Button linearExtention;

    @BindView(R.id.negatif)
    Button negatif;

    @BindView(R.id.sobel)
    Button sobel;

    @BindView(R.id.laplacien)
    Button laplacien;




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
