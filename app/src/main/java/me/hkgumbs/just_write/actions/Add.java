package me.hkgumbs.just_write.actions;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;

import me.hkgumbs.just_write.C;

public class Add extends MyMenuAction {

    @Override
    public void execute(final Activity activity, final ViewPager pager) {
        SharedPreferences sp = activity.getPreferences(Context.MODE_PRIVATE);
        int pages = sp.getInt(C.PAGES, 1) + 1;
        sp.edit().putInt(C.PAGES, pages).apply();

        pager.getAdapter().notifyDataSetChanged();
        int count = pager.getAdapter().getCount();
        pager.setCurrentItem(count - 1, true);
    }

}
