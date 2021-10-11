package com.jasonette.seed.Component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.core.graphics.drawable.DrawableCompat;

import com.jasonette.seed.Core.JasonViewActivity;
import com.jasonette.seed.Helper.JasonHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class JasonRatingBarComponent {
    public static View build(View view, final JSONObject component, final JSONObject parent, final Context context) {
        if(view == null) {
            try {

                JSONObject style = component.getJSONObject("style");


                //parent layout in which you want to add rating bar
                RatingBar r = new RatingBar(context);
                r.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                r.setStepSize(Float.parseFloat(component.getString("step")));
                r.setNumStars(Integer.parseInt(component.getString("number")));
                r.setIsIndicator(false);
                r.setRating(Float.parseFloat(component.getString("rating")));
                r.setScaleX(Float.parseFloat(component.getString("scale")));
                r.setScaleY(Float.parseFloat(component.getString("scale")));

                if (style.has("background")) {
                    int background = JasonHelper.parse_color(style.getString("background"));
                    r.setBackgroundColor(background);
                    r.setDrawingCacheBackgroundColor(background);
                }

                LayerDrawable stars = (LayerDrawable) r.getProgressDrawable();

                if (style.has("firstColor")) {
                    int firstbackground = JasonHelper.parse_color(style.getString("firstColor"));
                    stars.getDrawable(2).setTint( firstbackground ); // Empty star
                }

                if (style.has("secondColor")) {
                    int secondbackground = JasonHelper.parse_color(style.getString("secondColor"));
                    stars.getDrawable(1).setTint( secondbackground ); // Partial star
                    stars.getDrawable(0).setTint( secondbackground ); // Partial star
                }

                r.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating,
                                                boolean fromUser) {
                        try {
                            ((JasonViewActivity) context).model.var.put(component.getString("name"), ratingBar.getRating());
                        } catch (JSONException e) {
                            Log.e("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                        }
                    }
                });

                return r;
            } catch (Exception e){
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }
        }
        return new View(context);
    }
}