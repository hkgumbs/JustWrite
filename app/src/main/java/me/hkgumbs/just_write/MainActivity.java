package me.hkgumbs.just_write;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import me.hkgumbs.just_write.actions.Add;
import me.hkgumbs.just_write.actions.Capture;
import me.hkgumbs.just_write.actions.Delete;
import me.hkgumbs.just_write.actions.MyMenuAction;
import me.hkgumbs.just_write.actions.Theme;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    SharedPreferences sp;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sp = getPreferences(Context.MODE_PRIVATE);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyFragmentAdapter(this));

        ImageButton menu = (ImageButton) findViewById(R.id.menu);
        menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        PopupMenu pm = new PopupMenu(this, v);
        pm.setOnMenuItemClickListener(this);
        pm.inflate(R.menu.main);

        // change theme text
        String key = C.DARK_THEME + C.spKey(pager.getCurrentItem());
        boolean dark = sp.getBoolean(key, false);
        String title = getString(dark ? R.string.light_theme : R.string.dark_theme);
        pm.getMenu().findItem(R.id.theme).setTitle(title);

        pm.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        MyMenuAction action;
        int id = item.getItemId();

        if (id == R.id.capture)
            action = new Capture();
        else if (id == R.id.theme)
            action = new Theme();
        else if (id == R.id.add)
            action = new Add();
        else if (id == R.id.delete)
            action = new Delete();
        else
            return super.onOptionsItemSelected(item);

        action.execute(this, pager);
        return true;
    }

}
