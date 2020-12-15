package com.jasonette.seed.Component;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jasonette.seed.Helper.JasonHelper;
import org.json.JSONException;

import com.jasonette.seed.Core.JasonViewActivity;
import org.json.JSONObject;

public class JasonSpinnerComponent {
    public static View build(View view, final JSONObject component, final JSONObject parent, final Context context) {
        if(view == null) {
            return new Spinner(context);
        } else {
            try {
                String data = component.getString("data");
                final String[] spinnerData = data.split("\\|");
                view = JasonComponent.build(view, component, parent, context);

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                        (context, android.R.layout.simple_spinner_item,
                                spinnerData);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                ((Spinner)view).setAdapter(spinnerArrayAdapter);

                ((Spinner)view).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            JSONObject style = component.getJSONObject("style");
                            int colors = JasonHelper.parse_color(style.getString("color"));
                            ((TextView)parent.getChildAt(0)).setTextColor(colors);
                            ((JasonViewActivity) context).model.var.put(component.getString("name"), spinnerData[position]);
                        } catch (JSONException e) {
                            Log.e("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                ((Spinner)view).requestLayout();
                return view;
            } catch (Exception e){
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                return new View(context);
            }
        }
    }
}