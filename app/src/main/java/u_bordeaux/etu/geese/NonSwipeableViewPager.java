package u_bordeaux.etu.geese;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;


/**
 * Class NonSwipeableViewPager, disallow to swipe between fragments
 */

public class NonSwipeableViewPager extends ViewPager {

    /**
     * Constructor
     * Summons the constructor of the superclass and set the scroller
     * @param context
     */
    public NonSwipeableViewPager(Context context) {
        super(context);
        setMyScroller();
    }


    /**
     * Constructor
     * Summons the constructor of the superclass and set the scroller
     * @param context
     * @param attrs
     */
    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMyScroller();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //Disable swiping between pages
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //idem
        return false;
    }


    /**
     * Method setMyScroller
     * set the scroller of the ViewPager
     */
    private void setMyScroller(){
        try{
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Inner class Myscroller
     */

    public class MyScroller extends Scroller {

        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350 /*1 sec*/);
        }
    }
}
