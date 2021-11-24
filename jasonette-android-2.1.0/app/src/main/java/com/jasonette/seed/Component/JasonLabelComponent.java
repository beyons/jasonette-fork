package com.jasonette.seed.Component;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import com.jasonette.seed.Helper.JasonHelper;
import org.json.JSONObject;

public class JasonLabelComponent {

    public static View build(View view, final JSONObject component, final JSONObject parent, final Context context) {
        if(view == null){
            return new TextView(context);
        } else {
            try {
                ((TextView)view).setText(component.getString("text"));
                JasonComponent.build(view, component, parent, context);

                String type;
                JSONObject style = JasonHelper.style(component, context);
                type = component.getString("type");

                if (style.has("color")) {
                    int color = JasonHelper.parse_color(style.getString("color"));
                    ((TextView)view).setTextColor(color);
                }

                JasonHelper.setTextViewFont(((TextView)view), style, context);

                int g = 0;
                if (style.has("align")) {
                    String align = style.getString("align");
                    if (align.equalsIgnoreCase("center")) {
                        g = g | Gravity.CENTER_HORIZONTAL;
                        ((TextView) view).setGravity(Gravity.CENTER_HORIZONTAL);
                    } else if (align.equalsIgnoreCase("right")) {
                        g = g | Gravity.RIGHT;
                        ((TextView) view).setGravity(Gravity.RIGHT);
                    } else if (align.equalsIgnoreCase("left")) {
                        g = g | Gravity.LEFT;
                    }

                    if (align.equalsIgnoreCase("top")) {
                        g = g | Gravity.TOP;
                    } else if (align.equalsIgnoreCase("bottom")) {
                        g = g | Gravity.BOTTOM;
                    } else {
                        g = g | Gravity.CENTER_VERTICAL;
                    }
                } else {
                    g = Gravity.CENTER_VERTICAL;
                }

                ((TextView)view).setGravity(g);

                if(style.has("animation")){
                    JSONObject animation = style.getJSONObject("animation");
                    if(animation.has("type")){
                        if(animation.getString("type").equals("fadeIn")){
                            final Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                            fadeIn.setDuration(animation.getInt("duration"));
                            fadeIn.setStartOffset(animation.getInt("start"));
                            ((TextView) view).startAnimation(fadeIn);
                            ((TextView) view).setVisibility(View.VISIBLE);
                        }
                        if(animation.getString("type").equals("fadeOut")){
                            final Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                            fadeOut.setStartOffset(100);
                            fadeOut.setDuration(1000);
                            ((TextView) view).startAnimation(fadeOut);
                            ((TextView) view).setVisibility(View.INVISIBLE);
                        }
                        if(animation.getString("type").equals("move")){
                            ObjectAnimator anim = ObjectAnimator.ofFloat(((TextView) view), animation.getString("translation"), animation.getInt("value"));
                            anim.setDuration(animation.getInt("duration"));
                            anim.start();
                        }
                        if(animation.getString("type").equals("hide")){
                            ((TextView) view).setVisibility(View.INVISIBLE);
                        }
                        if(animation.getString("type").equals("show")){
                            ((TextView) view).setVisibility(View.VISIBLE);
                        }
                    }
                }

                if (style.has("fontWeight")) {
                    String weight = style.getString("fontWeight");
                    if (weight.equalsIgnoreCase("bold")){
                        ((TextView) view).setTypeface(null, Typeface.BOLD);
                    } else if(weight.equalsIgnoreCase("italic")){
                        ((TextView) view).setTypeface(null, Typeface.ITALIC);
                    } else if(weight.equalsIgnoreCase("boldItalic")){
                        ((TextView) view).setTypeface(null, Typeface.BOLD_ITALIC);
                    } else {
                        ((TextView) view).setTypeface(null, Typeface.NORMAL);
                    }
                }

                if (style.has("size")) {
                    ((TextView)view).setTextSize(Float.parseFloat(style.getString("size")));
                }

                ((TextView)view).setHorizontallyScrolling(false);

                JasonComponent.addListener(view, context);

                view.requestLayout();
                return view;

            } catch (Exception e){
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                return new View(context);
            }
        }
    }
}
