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

    ViewGroup frame;
    EditText text;

    SharedPreferences sp;
    String key;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        frame = (ViewGroup) inflater.inflate(R.layout.fragment_content, container, false);
        text = (EditText) frame.findViewById(R.id.text);
        sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        key = getArguments().getString(C.POSITION);
        update();

        // setup pinch to change font
        FontScaler fs = new FontScaler(getActivity(), text, key);
        frame.setOnTouchListener(fs);

        // save text to SharedPreferences as user is typing
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                sp.edit().putString(C.CONTENT + key, text.getText().toString()).apply();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        // helps finds frame in Capture
        // ideally would not need to use findViewWithTag
        frame.setTag(key);

        return frame;
    }

    /**
     * Setup appearance and text from preferences. This method is '''public''' so that it can be
     * called in MyFragmentAdapter.getItemPosition instead of return POSITION_NONE.
     */
    public void update() {
        String content = sp.getString(C.CONTENT + key, "");
        float size = sp.getFloat(C.FONT_SIZE + key, 50);
        boolean dark = sp.getBoolean(C.DARK_THEME + key, false);

        text.setText(content);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        text.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), C.FONT_SLAB));

        int textColor;
        int viewColor;
        if (dark) {
            textColor = getResources().getColor(android.R.color.white);
            viewColor = getResources().getColor(android.R.color.black);
        } else {
            textColor = getResources().getColor(android.R.color.black);
            viewColor = getResources().getColor(android.R.color.white);
        }
        text.setTextColor(textColor);
        frame.setBackgroundColor(viewColor);
    }
}
