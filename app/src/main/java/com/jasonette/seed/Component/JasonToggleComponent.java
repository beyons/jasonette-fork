package com.jasonette.seed.Component;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.jasonette.seed.Core.JasonViewActivity;
import com.jasonette.seed.Helper.JasonHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class JasonToggleComponent {
    public static View build(View view, final JSONObject component, final JSONObject parent, final Context context) {
        if(view == null) {
            try {
                JSONObject style = component.getJSONObject("style");
                ToggleButton toggle = new ToggleButton(context);
                boolean checked = (boolean) component.getBoolean("checked");

                if(checked == true) toggle.setChecked(true);
                else toggle.setChecked(false);

                toggle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                if (style.has("background")) {
                    int background = JasonHelper.parse_color(style.getString("background"));
                    toggle.setBackgroundColor(background);
                }
                if (style.has("color")) {
                    int colors = JasonHelper.parse_color(style.getString("color"));
                    toggle.setTextColor(colors);
                }
                toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            // The toggle is enabled
                            try {
                                ((JasonViewActivity) context).model.var.put(component.getString("name"), true);
                            } catch (JSONException e) {
                                Log.e("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                            }
                        } else {
                            // The toggle is disabled
                            try {
                                ((JasonViewActivity) context).model.var.put(component.getString("name"), false);
                            } catch (JSONException e) {
                                Log.e("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                            }
                        }
                    }
                });
                return toggle;
            } catch (Exception e){
                return new View(context);
            }
        }
        return new View(context);
    }
}