package me.hkgumbs.just_write;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.EditText;

public class FontScaler extends ScaleGestureDetector.SimpleOnScaleGestureListener
        implements View.OnTouchListener {

    final private Activity activity;
    final private EditText text;
    final private ScaleGestureDetector detector;
    final private SharedPreferences sp;
    final private String key;

    int count;

    public FontScaler(Activity activity, EditText text, String key) {
        this.activity = activity;
        this.text = text;
        this.key = key;

        sp = activity.getPreferences(Context.MODE_PRIVATE);
        detector = new ScaleGestureDetector(activity, this);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float size = text.getTextSize();
        float factor = detector.getScaleFactor();
        float product = size * factor;

        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, product);
        sp.edit().putFloat(C.FONT_SIZE + key, product).apply();

        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        detector.onTouchEvent(event);

        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN)
            count = 1;
        else if (action == MotionEvent.ACTION_POINTER_DOWN)
            count += 1;
        else if (action == MotionEvent.ACTION_POINTER_UP)
            count -= 1;
        else if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_CANCEL)
            count = 0;

        if (count > 1)
            return true;
        else
            return text.dispatchTouchEvent(event);
    }

}
