package me.hkgumbs.just_write;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;

import me.hkgumbs.just_write.ContentFragment.ScreenSlidePagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.squareup.seismic.ShakeDetector;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ShakeDetector.Listener {

	private SharedPreferences sp;
	private ShakeDetector sd;

	private FrameLayout fl;
	private ScrollView sv;
	private EditText content;

	private AlertDialog ad;
	private LayoutInflater in;

	ScreenSlidePagerAdapter pagerAdapter;
	ViewPager pager;

	private final static int NOTIFICATION_ID = 88;
	private final static int MIN_FONT_SIZE = 12;
	private static String[] commands;
	private static int[] drawables;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);

		pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(pagerAdapter);

	}

	@Override
	public void onResume() {
		super.onResume();

		// INIT SHAKEDETECTOR
		sd = new ShakeDetector(this);
		sd.start((SensorManager) getSystemService(SENSOR_SERVICE));
	}

	@Override
	public void onPause() {
		super.onPause();
		sd.stop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) { // HARDWARE MENU BUTTON
			hearShake();
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	public void hearShake() {
		// shake detector implemented in square-seismic-1.0.0.jar

		if (!ad.isShowing()) {
			ad.show();
		}
	}

	// EDIT FONT SIZE
	private void showBar() {

		View layout = in.inflate(R.layout.dialog_font, (ViewGroup) findViewById(R.layout.activity_home));
		new AlertDialog.Builder(this).setView(layout).setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				sp.edit().putFloat("FONT_SIZE", toSP(content.getTextSize())).apply();
			}

		}).setNegativeButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				content.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp.getFloat("FONT_SIZE", 50));
			}

		}).create().show();

		SeekBar sb = (SeekBar) layout.findViewById(R.id.font);
		sb.setMax(100 - MIN_FONT_SIZE);
		sb.setProgress(toSP(content.getTextSize()) - MIN_FONT_SIZE);
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				content.setTextSize(TypedValue.COMPLEX_UNIT_SP, progress + MIN_FONT_SIZE);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

		});

	}

	// SAVE AS IMAGE
	private void capture() {

		// CREATE BITMAP FROM SCREEN CAPTURE
		content.setCursorVisible(false);
		fl.setDrawingCacheEnabled(true);
		Bitmap bitmap = Bitmap.createBitmap(fl.getDrawingCache());
		fl.setDrawingCacheEnabled(false);
		content.setCursorVisible(true);

		new AsyncTask<Bitmap, Void, Boolean>() {
			String name;
			String root;
			File file;

			@Override
			protected Boolean doInBackground(Bitmap... b) {

				// IMAGE NAMING
				name = new Timestamp(new java.util.Date().getTime()).toString() + ".jpg";
				root = Environment.getExternalStorageDirectory().toString() + "/Just Write/";
				File dir = new File(root);
				dir.mkdirs();
				file = new File(dir, name);
				if (file.exists())
					file.delete();

				// SAVE TO DEVICE
				try {
					FileOutputStream out = new FileOutputStream(file);
					b[0].compress(Bitmap.CompressFormat.JPEG, 90, out);
					out.flush();
					out.close();

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}

				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {

				// ERROR CASE
				if (!result) {
					Toast.makeText(MainActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
					return;
				}

				// REFRESH GALLERY
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
				values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
				getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

				// CREATE NOTIFICATION
				Intent i = new Intent();
				i.setAction(Intent.ACTION_VIEW);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.setDataAndType(Uri.parse("file://" + root + name), "image/*");

				NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this)
						.setContentTitle(getString(R.string.notification_title))
						.setContentText(getString(R.string.notification_desc))
						.setSmallIcon(R.drawable.notification_icon)
						.setAutoCancel(true)
						.setTicker(getString(R.string.notification_title))
						.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.camera_icon))
						.setContentIntent(
								PendingIntent.getActivity(MainActivity.this, 0, i, PendingIntent.FLAG_ONE_SHOT));

				NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				nm.cancel(NOTIFICATION_ID); // remove old notification
				nm.notify(NOTIFICATION_ID, builder.build());

			}

		}.execute(bitmap);

	}

	private int toSP(Float px) {
		float sd = getResources().getDisplayMetrics().scaledDensity;
		return (int) (px / sd);
	}

	private AlertDialog initMenu() {

		ListView lv = (ListView) in.inflate(R.layout.dialog_options, null);

		ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.item_options, commands) {

			@Override
			public View getView(int position, View v, ViewGroup root) {

				if (v == null)
					v = in.inflate(R.layout.item_options, null);

				Drawable d = getResources().getDrawable(drawables[position]);
				d.setBounds(0, 0, d.getIntrinsicWidth() / 2, d.getIntrinsicHeight() / 2);

				((TextView) v).setText(commands[position]);
				((TextView) v).setCompoundDrawables(d, null, null, null);

				return v;
			}

		};
		lv.setAdapter(aa);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				ad.dismiss();

				switch (position) {
				case 0:
					capture();
					break;
				case 1:
//					setTheme(true);
					break;
				case 2:
					showBar();
					break;
				}

			}
		});

		AlertDialog alert = new AlertDialog.Builder(MainActivity.this).setView(lv).create();

		alert.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(content.getWindowToken(), 0);

			}

		});

		return alert;
	}
}