package me.hkgumbs.just_write.actions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import me.hkgumbs.just_write.C;
import me.hkgumbs.just_write.R;

public class Font implements MyMenuAction {

    @Override
    public void execute(final Activity activity, final ViewPager pager) {

        // find view
        int p = pager.getCurrentItem();
        String position = p == 0 ? "" : Integer.toString(p);
        View current = pager.findViewWithTag(position);

        final TextView content = (TextView) current.findViewById(R.id.text);
        final SharedPreferences sp = activity
                .getPreferences(Context.MODE_PRIVATE);

        // setup dialog
        LayoutInflater in = LayoutInflater.from(activity);
        View layout = in.inflate(R.layout.dialog_font, null);
        final String SIZE = C.FONT_SIZE + position;

        new AlertDialog.Builder(activity)
                .setView(layout)
                .setPositiveButton(android.R.string.ok, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        float size = toSP(activity, content.getTextSize());
                        sp.edit().putFloat(SIZE, size).apply();
                    }

                })
                .setNegativeButton(android.R.string.cancel,
                        new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                float size = sp.getFloat(SIZE, 50);
                                content.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                                        size);
                            }

                        }).create().show();

        SeekBar sb = (SeekBar) layout.findViewById(R.id.font);
        sb.setMax(100 - C.MIN_FONT_SIZE);
        sb.setProgress(toSP(activity, content.getTextSize()) - C.MIN_FONT_SIZE);
        sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                float size = progress + C.MIN_FONT_SIZE;
                content.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });
    }

    /*
     * Helper to convert pixels to SP units
     */
    private int toSP(Activity activity, Float px) {
        float sd = activity.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / sd);
    }

}
