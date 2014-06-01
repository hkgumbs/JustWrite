package me.hkgumbs.just_write;

import com.squareup.seismic.ShakeDetector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;

public class ContentFragment extends Fragment {

	private int position;

	private SharedPreferences sp;
	private Boolean dark;
	private ShakeDetector sd;

	private FrameLayout fl;
	private ScrollView sv;
	private EditText content;

	private AlertDialog ad;
	private LayoutInflater in;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		final View frame = inflater.inflate(R.layout.fragment_content, container, false);
		position = getArguments().getInt("position");

		// BOOTSTRAP
		sp = getActivity().getPreferences(Context.MODE_PRIVATE);
		fl = (FrameLayout) frame.findViewById(R.id.root);
		sv = (ScrollView) frame.findViewById(R.id.containter);
		content = (EditText) frame.findViewById(R.id.text);
		in = getActivity().getLayoutInflater();

		// INIT CONTENT
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoSlab-Thin.ttf");
		content.setTypeface(typeFace);
		content.setText(sp.getString("CONTENT", ""));
		content.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp.getFloat("FONT_SIZE", 50));
		content.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				sp.edit().putString("CONTENT", content.getText().toString()).apply();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});

		return frame;
	}

	static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new ContentFragment();
			Bundle arg = new Bundle();
			arg.putInt("position", position);
			fragment.setArguments(arg);
			return fragment;
		}

		@Override
		public int getCount() {
			return 5;
		}

	}

}
