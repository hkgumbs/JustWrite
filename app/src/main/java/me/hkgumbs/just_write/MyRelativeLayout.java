package me.hkgumbs.just_write;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Intercepts touch events so that pinch-to-change-font works
 */
public class MyRelativeLayout extends RelativeLayout {

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

}
