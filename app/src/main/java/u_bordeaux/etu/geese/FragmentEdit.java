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
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lalie on 03/03/2018.
 */

public class FragmentEdit extends Fragment implements ImageButton.OnClickListener, SeekBar.OnSeekBarChangeListener {
    View view;
    FragmentEditListener listener;

    @BindView(R.id.brightness)
    ImageButton brightness;
    @BindView(R.id.contrast)
    ImageButton contrast;
    @BindView(R.id.blurring)
    Button blur;
    @BindView(R.id.hue)
    Button hue;
    @BindView(R.id.seekBarEdit)
    SeekBar seekBarControl;

    String Tag ="";
    int progress;

    public FragmentEdit() {
    }

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


        return view;
    }

    @Override
    public void onClick(View v) {
        Log.i("listener", "on click :  "+this.listener);
        if (this.listener != null) {
            if (v.getId() == R.id.brightness) {
                seekBarControl.setMax(200);
                seekBarControl.setProgress(100);
                Tag = "brightness";
            }
            if (v.getId() == R.id.contrast) {
                seekBarControl.setMax(200);
                seekBarControl.setProgress(100);
                Tag = "contrast";
            }
            if (v.getId() == R.id.blurring) {
                seekBarControl.setMax(3);
                seekBarControl.setProgress(0);
                Tag = "blur";
            }
            if (v.getId() == R.id.hue) {
                seekBarControl.setMax(360);
                seekBarControl.setProgress(180);
                Tag = "hue";
            }
            seekBarControl.setVisibility(View.VISIBLE);
            seekBarControl.setOnSeekBarChangeListener(this);
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (this.listener != null) {
            switch (Tag) {
                case "brightness":
                    this.progress = progress - 100;
                    break;
                case "blur":
                    this.progress = progress*2+1;
                    break;
                case "hue":
                    this.progress = progress-180;
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
        if (this.listener != null) {
            listener.onPreviewStart();
        }
        else
            Log.i("listener", "onStartTrackingTouch: listener null");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (this.listener != null) {
            seekBar.setVisibility(View.INVISIBLE);
            listener.onFilterSelected(Tag,progress);
        }

        else
            Log.i("listener", "onStopTrackingTouch: listener null");


    }

    public interface FragmentEditListener {
        void onPreviewSelected(String TAG,int progress);
        void onPreviewStart();
        void onFilterSelected(String TAG,int progress);

    }
}
