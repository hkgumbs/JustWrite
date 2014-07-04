package me.hkgumbs.just_write;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ContentFragment extends Fragment {

    private SharedPreferences sp;
    private EditText content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	super.onCreateView(inflater, container, savedInstanceState);

	final View frame = inflater.inflate(R.layout.fragment_content,
		container, false);

	// BOOTSTRAP
	sp = getActivity().getPreferences(Context.MODE_PRIVATE);
	content = (EditText) frame.findViewById(R.id.text);

	// INIT CONTENT
	Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(),
		sp.getString("FONT", "fonts/RobotoSlab-Thin.ttf"));
	content.setTypeface(typeFace);
	content.setText(sp.getString("CONTENT", ""));
	content.setTextSize(TypedValue.COMPLEX_UNIT_SP,
		sp.getFloat("FONT_SIZE", 50));
	content.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void afterTextChanged(Editable arg0) {
		sp.edit().putString("CONTENT", content.getText().toString())
			.apply();
	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {
	    }

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before,
		    int count) {
	    }
	});

	return frame;
    }

}
