package me.hkgumbs.just_write.actions;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;

import me.hkgumbs.just_write.C;

public class Theme extends MyMenuAction {

    @Override
    public void execute(final Activity activity, final ViewPager pager) {

        final SharedPreferences sp = activity
                .getPreferences(Context.MODE_PRIVATE);
        String key = C.spKey(pager.getCurrentItem());
        boolean dark = sp.getBoolean(C.DARK_THEME + key, false);
        sp.edit().putBoolean(C.DARK_THEME + key, !dark).apply();

        pager.getAdapter().notifyDataSetChanged();
    }
}
