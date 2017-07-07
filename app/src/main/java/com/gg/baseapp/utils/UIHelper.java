package com.gg.baseapp.utils;

import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gg.baseapp.R;
import com.gg.baseapp.customui.flatui.FlatToggleButton;


/**
 * Created by anfengyi on 9/6/16.
 */
public class UIHelper {

    public static void setAccessibleRowData(View view, String text, String subtext, String description, View.OnClickListener onClickListener) {
        setTextRowData(view, text, description, onClickListener);
        TextView textView = (TextView) view.findViewById(R.id.subtext);
        textView.setText(subtext);
    }

    public static void setTextRowData(View view, String text, String description, View.OnClickListener onClickListener) {
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(text);

        TextView descriptionView = (TextView) view.findViewById(R.id.description);
        descriptionView.setText(description);

        view.setOnClickListener(onClickListener);
    }

    public static void setSwitchRowData(View view, String text, boolean isChecked, View.OnClickListener onClickListener) {
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(text);

        FlatToggleButton button = (FlatToggleButton) view.findViewById(R.id.switch_button);
        button.setChecked(isChecked);

        view.setOnClickListener(onClickListener);
    }


    public static void setIconText(View view, String text, int resourceid, View.OnClickListener clickListener) {
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(text);

        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        imageView.setImageResource(resourceid);
        view.setOnClickListener(clickListener);
    }

    public static void setEditTextRowData(View view, String text, String hint, TextWatcher textWatcher) {
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(text);

        EditText inputView = (EditText) view.findViewById(R.id.input);
        inputView.setHint(hint);
        inputView.addTextChangedListener(textWatcher);
    }

    public static void setEditableAccessibleRowData(View view, String text, String hint, String description,
                                                    TextWatcher textWatcher, View.OnClickListener onClickListener) {
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(text);

        EditText inputView = (EditText) view.findViewById(R.id.input);
        inputView.setHint(hint);
        inputView.addTextChangedListener(textWatcher);

        TextView descriptionView = (TextView) view.findViewById(R.id.description);
        descriptionView.setText(description);
        descriptionView.setOnClickListener(onClickListener);
    }
}
