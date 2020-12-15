package com.jasonette.seed.Component;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.view.View;

import com.jasonette.seed.Core.JasonViewActivity;
import com.jasonette.seed.Helper.JasonHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class JasonRadioComponent { public static View build(View view, final JSONObject component, final JSONObject parent, final Context context) {
    if(view == null) {
        try {
            JSONObject style = component.getJSONObject("style");
            RadioButton radioButton = new RadioButton(context);

            String hintText = component.getString("hint");
            radioButton.setText(hintText);

            if (style.has("background")) {
                int background = JasonHelper.parse_color(style.getString("background"));
                radioButton.setBackgroundColor(background);
            }

            if (style.has("color")) {
                int colors = JasonHelper.parse_color(style.getString("color"));
                ColorStateList colorStateList = new ColorStateList(
                        new int[][] {
                                new int[] { -android.R.attr.state_checked }, // unchecked
                                new int[] {  android.R.attr.state_checked }  // checked
                        },
                        new int[] {
                                colors,
                                colors
                        }
                );
                radioButton.setButtonTintList(colorStateList);
                radioButton.setTextColor(colors);
            }

            try {
                ((JasonViewActivity) context).model.var.put(component.getString("name"), "false");
            } catch (JSONException e) {
                Log.e("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }

            radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ((JasonViewActivity) context).model.var.put(component.getString("name"), "true");
                    } catch (JSONException e) {
                        Log.e("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                    }
                }
            });

            return radioButton;
        } catch (Exception e){
            return new View(context);
        }
    }
    return new View(context);
}
}