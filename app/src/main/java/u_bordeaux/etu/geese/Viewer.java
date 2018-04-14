package u_bordeaux.etu.geese;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Class Viewer
 */

class Viewer extends AppCompatImageView {

    private int maxWidth;
    private int maxHeight;

    public Viewer(Context context) {
        super(context);
    }

    public Viewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Viewer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setMaxHeight(int maxHeight) {
        super.setMaxHeight(maxHeight);
        this.maxHeight = maxHeight;
    }

    @Override
    public void setMaxWidth(int maxWidth) {

        this.maxWidth = maxWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void zoomIn(){}

    public void zoomOut(){}

    public void scroll(){}
}
