package com.jasonette.seed.Component;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.jasonette.seed.Core.JasonViewActivity;
import com.jasonette.seed.Helper.JasonHelper;
import com.jasonette.seed.R;

public class JasonCheckboxComponent {
    public static View build(View view, final JSONObject component, final JSONObject parent, final Context context) {
        if(view == null) {
            try {
                // Create Checkbox Dynamically
                CheckBox checkBox = new CheckBox(context);
                checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                JSONObject style = component.getJSONObject("style");
                if (style.has("background")) {
                    int background = JasonHelper.parse_color(style.getString("background"));
                    checkBox.setBackgroundColor(background);
                }

                checkBox.setTextSize(Float.parseFloat(style.getString("size")));

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
                    checkBox.setButtonTintList(colorStateList);
                    checkBox.setTextColor(colors);
                }

                ((JasonViewActivity) context).model.var.put(component.getString("name"), false);

                String hintText = component.getString("hint");
                checkBox.setText(hintText);

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        try {
                            ((JasonViewActivity) context).model.var.put(component.getString("name"), isChecked);
                        } catch (JSONException e) {
                            Log.e("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                        }
                    }
                });
                return checkBox;
            } catch (Exception e){
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }
        }
        return new View(context);
    }
}