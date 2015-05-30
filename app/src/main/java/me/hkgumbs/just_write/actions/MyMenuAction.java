package me.hkgumbs.just_write.actions;

import android.app.Activity;
import android.support.v4.view.ViewPager;

public abstract class MyMenuAction {

    /**
     * @param activity
     * @param pager
     */
    public abstract void execute(final Activity activity, final ViewPager pager);

}
