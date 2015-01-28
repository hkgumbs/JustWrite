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

    View frame;
    View scroll;
    EditText content;

    SharedPreferences sp;
    String position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        frame = inflater.inflate(R.layout.fragment_content, container, false);
        scroll = frame.findViewById(R.id.scroll);
        content = (EditText) frame.findViewById(R.id.text);
        sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        position = getArguments().getString("position");
        update();

        content.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                sp.edit()
                        .putString("CONTENT" + position,
                                content.getText().toString()).apply();
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

        frame.setTag("TAG" + position);
        return frame;
    }

    /*
     * setup look and content from preferences
     */
    public void update() {
        String text = sp.getString("CONTENT" + position, "");
        float size = sp.getFloat("FONT_SIZE" + position, 50);
        String font = sp.getString("FONT" + position, C.FONT_SLAB);
        boolean dark = sp.getBoolean("DARK_THEME" + position, false);

        content.setText(text);
        content.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        content.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),
                font));

        int textColor;
        int viewColor;
        if (dark) {
            textColor = getResources().getColor(android.R.color.white);
            viewColor = getResources().getColor(android.R.color.black);
        } else {
            textColor = getResources().getColor(android.R.color.black);
            viewColor = getResources().getColor(android.R.color.white);
        }
        content.setTextColor(textColor);
        frame.setBackgroundColor(viewColor);
        scroll.setBackgroundColor(viewColor);
    }
}
