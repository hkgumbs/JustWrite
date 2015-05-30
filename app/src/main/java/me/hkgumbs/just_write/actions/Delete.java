package me.hkgumbs.just_write.actions;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import me.hkgumbs.just_write.C;

public class Delete extends MyMenuAction {

    @Override
    public void execute(Activity activity, ViewPager pager) {
        SharedPreferences sp = activity.getPreferences(Context.MODE_PRIVATE);
        int pages = sp.getInt(C.PAGES, 1);

        if (pages == 1) {
            // if this is the only page, clear contents
            sp.edit().clear().apply();
            pager.getAdapter().notifyDataSetChanged();
            return;

        } else {
            // move all saved stuff over
            SharedPreferences.Editor editor = sp.edit();
            int original = pager.getCurrentItem();
            for (int i = original; i < pages - 1; i++) {
                String current = C.spKey(i);
                String next = C.spKey(i + 1);

                String text = sp.getString(C.CONTENT + next, "");
                editor.putString(C.CONTENT + current, text);

                float size = sp.getFloat(C.FONT_SIZE + next, 50);
                editor.putFloat(C.FONT_SIZE + current, size);

                String font = sp.getString(C.FONT + next, C.FONT_SLAB);
                editor.putString(C.FONT + current, font);

                boolean dark = sp.getBoolean(C.DARK_THEME + next, false);
                editor.putBoolean(C.DARK_THEME + current, dark);
            }

            // remove extra preferences
            pages--;
            String key = C.spKey(pages);
            editor.putInt(C.PAGES, pages);
            editor.remove(C.CONTENT + key);
            editor.remove(C.FONT_SIZE + key);
            editor.remove(C.FONT + key);
            editor.remove(C.DARK_THEME + key);
            editor.commit();

            // workaround to avoid residual pages
            PagerAdapter adapter = pager.getAdapter();
            adapter.notifyDataSetChanged();
            pager.setAdapter(null);
            pager.invalidate();
            pager.setAdapter(adapter);

            // if last page, adjust original index
            if (original == pages)
                original--;
            pager.setCurrentItem(original, true);
        }
    }
}
