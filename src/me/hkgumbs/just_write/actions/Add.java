package me.hkgumbs.just_write.actions;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;

public class Add implements MyMenuAction {

    @Override
    public void execute(final Activity activity, final ViewPager pager) {
	SharedPreferences sp = activity.getPreferences(Context.MODE_PRIVATE);
	int pages = sp.getInt("pages", 1) + 1;
	sp.edit().putInt("pages", pages).apply();

	// redraw
	pager.getAdapter().notifyDataSetChanged();
	int count = pager.getAdapter().getCount();
	pager.setCurrentItem(count - 1);
    }

}
