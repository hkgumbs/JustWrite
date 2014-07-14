package me.hkgumbs.just_write.actions;

import android.app.Activity;
import android.support.v4.view.ViewPager;

public abstract interface MyMenuAction {

    /*
     * Execute the command implemented by the interface
     */
    public void execute(final Activity activity, final ViewPager pager);

}
