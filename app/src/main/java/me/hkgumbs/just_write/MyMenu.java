package me.hkgumbs.just_write;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import me.hkgumbs.just_write.actions.Add;
import me.hkgumbs.just_write.actions.Capture;
import me.hkgumbs.just_write.actions.Delete;
import me.hkgumbs.just_write.actions.Font;
import me.hkgumbs.just_write.actions.MyMenuAction;
import me.hkgumbs.just_write.actions.Theme;

public class MyMenu implements OnItemClickListener {

    private static final int LAYOUT = R.layout.item_options;

    AlertDialog alert;
    Activity activity;
    ViewPager pager;

    public MyMenu(final Activity activity, final ViewPager pager) {

        final Typeface typeface = Typeface.createFromAsset(
                activity.getAssets(), C.FONT_AWESOME);
        final String[] commands = activity.getResources().getStringArray(
                R.array.commands);
        final String[] icons = activity.getResources().getStringArray(
                R.array.icons);
        ListView lv = new ListView(activity);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(activity, LAYOUT,
                commands) {
            @Override
            public View getView(int position, View view, ViewGroup root) {

                if (view == null)
                    view = LayoutInflater.from(activity).inflate(LAYOUT, null);

                TextView command = (TextView) view.findViewById(R.id.command);
                command.setText(commands[position]);
                TextView icon = (TextView) view.findViewById(R.id.icon);
                icon.setText(icons[position]);
                icon.setTypeface(typeface);

                return view;
            }
        };

        lv.setAdapter(aa);
        lv.setOnItemClickListener(this);

        alert = new AlertDialog.Builder(activity).setView(lv).create();
        this.activity = activity;
        this.pager = pager;
    }

    /*
     * open menu unless not already showing
     */
    public boolean open(int position, View root) {
        if (!alert.isShowing()) {
            alert.show();
            return true;
        } else
            return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long item) {
        alert.dismiss();

        MyMenuAction action;
        if (position == 0)
            action = new Capture();

        else if (position == 1)
            action = new Theme();

        else if (position == 2)
            action = new Font();

        else if (position == 3)
            action = new Add();

        else
            // if (position == 4)
            action = new Delete();

        action.execute(activity, pager);
    }
}
