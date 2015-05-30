package me.hkgumbs.just_write;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by hkgumbs on 1/30/15.
 */
public class MyFragmentAdapter extends FragmentStatePagerAdapter {

    SharedPreferences sp;

    public MyFragmentAdapter(FragmentActivity activity) {
        super(activity.getSupportFragmentManager());
        sp = activity.getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public ContentFragment getItem(int position) {
        ContentFragment fragment = new ContentFragment();
        Bundle arg = new Bundle();
        arg.putString(C.POSITION, C.spKey(position));
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public int getCount() {
        return sp.getInt(C.PAGES, 1);
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof ContentFragment)
            ((ContentFragment) object).update();
        return super.getItemPosition(object);
    }
}
