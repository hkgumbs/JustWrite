package me.hkgumbs.just_write.actions;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import me.hkgumbs.just_write.C;

public class Delete implements MyMenuAction {

    @Override
    public void execute(Activity activity, ViewPager pager) {
        // check that not the last page
        SharedPreferences sp = activity.getPreferences(Context.MODE_PRIVATE);
        int pages = sp.getInt(C.PAGES, 1);
        if (pages == 1) {
            Toast.makeText(activity, "Cannot delete the only page!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // move all saved stuff over
        int p = pager.getCurrentItem();
        int original = p;
        SharedPreferences.Editor editor = sp.edit();
        String position = p == 0 ? "" : Integer.toString(p);
        String next = Integer.toString(p + 1);

        while (p < pages - 1) {
            String text = sp.getString(C.CONTENT + next, "");
            editor.putString(C.CONTENT + position, text);

            float size = sp.getFloat(C.FONT_SIZE + next, 50);
            editor.putFloat(C.FONT_SIZE + position, size);

            String font = sp.getString(C.FONT + next, C.FONT_SLAB);
            editor.putString(C.FONT + position, font);

            boolean dark = sp.getBoolean(C.DARK_THEME + next, false);
            editor.putBoolean(C.DARK_THEME + position, dark);

            p++;
            position = next; // Integer.toString(p)
            next = Integer.toString(p + 1);
        }

        // remove extra preferences
        pages--;
        editor.putInt(C.PAGES, pages);
        editor.remove(C.CONTENT + position);
        editor.remove(C.FONT_SIZE + position);
        editor.remove(C.FONT + position);
        editor.remove(C.DARK_THEME + position);
        editor.apply();

        // redraw
        PagerAdapter adapter = pager.getAdapter();
        adapter.notifyDataSetChanged();
        pager.setAdapter(adapter);

        // if last page adjust original index
        if (original == pages)
            original--;
        pager.setCurrentItem(original, false);

    }
}
