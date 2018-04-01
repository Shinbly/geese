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

        return view;

    }

    @Override
    public void onClick(View v) {
        if (this.listener != null) {
            if (v.getId() == R.id.gray) {
                listener.onFilterSelected("gray");
            }
            if (v.getId() == R.id.sepia) {
                listener.onFilterSelected("sepia");
            }
            if (v.getId() == R.id.egalization) {
                listener.onFilterSelected("egalization");
            }
            if (v.getId() == R.id.linearExtention) {
                listener.onFilterSelected("linearExtention");
            }
            if (v.getId() == R.id.negatif) {
                listener.onFilterSelected("negatif");
            }
        }

    }


    public interface FragmentFiltersListener {
        void onFilterSelected(String TAG);
    }
}
