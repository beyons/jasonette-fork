package com.jasonette.seed.Component;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jasonette.seed.Core.JasonViewActivity;
import com.jasonette.seed.Helper.JasonHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class JasonRadioGroupComponent { public static View build(View view, final JSONObject component, final JSONObject parent, final Context context) {
    if(view == null) {
        try {
            JSONObject style = component.getJSONObject("style");
            String data = component.getString("data");
            final String[] radioData = data.split("\\|");
            final String[] websitesArray = radioData;
            final RadioGroup radioGrp =  new RadioGroup(context);
            //create radio buttons
            for (int i = 0; i < websitesArray.length; i++) {
                final RadioButton radioButton = new RadioButton(context);
                radioButton.setText(websitesArray[i]);
                radioButton.setId(i);
                radioButton.setTextSize(Float.parseFloat(style.getString("size")));
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
                    final int finalI = i;
                    radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (radioButton.isChecked()){
                                try {
                                    ((JasonViewActivity) context).model.var.put(component.getString("name"), websitesArray[finalI]);
                                } catch (JSONException e) {
                                    Log.e("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                                }

                            }
                        }
                    });
                }
                radioGrp.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                radioGrp.addView(radioButton);
            }

            int checked = Integer.parseInt(component.getString("checked"));
            if(radioGrp.getChildCount() > 0)
                radioGrp.check(radioGrp.getChildAt(checked).getId());



            return radioGrp;
        } catch (Exception e){
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
    return new View(context);
    }
}
