package com.jasonette.seed.Component;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import com.jasonette.seed.Helper.JasonHelper;
import com.jasonette.seed.R;

import org.json.JSONObject;

public class JasonButtonComponent{
    public static View build(View view, final JSONObject component, final JSONObject parent, final Context context) {
        if(component.has("url")){
            // image button
            view = JasonImageComponent.build(view, component, parent, context);

            /*******
             * Create a move animation for image button
             ******/
            try {
                JSONObject style = component.getJSONObject("style");
                if(style.has("animation")){
                    JSONObject animation = style.getJSONObject("animation");
                    if(animation.has("type")){
                        if(animation.getString("type").equals("fadeIn")){
                            final Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                            fadeIn.setDuration(1000);
                            fadeIn.setStartOffset(100);
                            view.startAnimation(fadeIn);
                            view.setVisibility(View.VISIBLE);
                        }
                        if(animation.getString("type").equals("fadeOut")){
                            final Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                            fadeOut.setStartOffset(100);
                            fadeOut.setDuration(1000);
                            view.startAnimation(fadeOut);
                            view.setVisibility(View.INVISIBLE);
                        }
                        if(animation.getString("type").equals("move")){
                            ObjectAnimator anim = ObjectAnimator.ofFloat(view, animation.getString("translation"), animation.getInt("value"));
                            anim.setDuration(animation.getInt("duration"));
                            anim.start();
                        }
                        if(animation.getString("type").equals("hide")){
                            view.setVisibility(View.INVISIBLE);
                        }
                        if(animation.getString("type").equals("show")){
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
            catch (Exception e) {
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }
        } else if(component.has("text")){
            // label button

            view = JasonLabelComponent.build(view, component, parent, context);
            try {
                JSONObject style = component.getJSONObject("style");

                /*******
                 * ALIGN : By default align center
                 ******/

                // Default is center
                int g = Gravity.CENTER;

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
                }
                ((TextView)view).setGravity(g);

                /*******
                 * Create a fadeIn / fadeout / move animation for textview button
                 ******/


                if(style.has("animation")){

                    JSONObject animation = style.getJSONObject("animation");
                    if(animation.has("type")){
                        if(animation.getString("type").equals("fadeIn")){
                            final Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                            fadeIn.setDuration(1000);
                            fadeIn.setStartOffset(100);
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

                /*******
                 * Drawable: Add drawable icon
                 ******/
                if (style.has("drawable")) {

                    int drawable = style.getInt("drawable");
                    int drawableColor = JasonHelper.parse_color(style.getString("drawableColor"));
                    //System.out.println("--"+ R.drawable.ic_baseline_list_24);

                    Drawable image = context.getResources().getDrawable(drawable);
                    image.setColorFilter(new
                            PorterDuffColorFilter(drawableColor, PorterDuff.Mode.MULTIPLY));
                    int h = image.getIntrinsicHeight();
                    int w = image.getIntrinsicWidth();
                    image.setBounds( 0, 0, w, h );

                    ((TextView) view).setCompoundDrawables(image, null, null, null);
                }

                /*******
                 * Padding: By default padding is 15
                 ******/
                // override each padding value only if it's not specified

                int padding_top = -1;
                int padding_left = -1;
                int padding_bottom = -1;
                int padding_right = -1;
                if(style.has("padding")){
                    padding_top = (int)JasonHelper.pixels(context, style.getString("padding_top"), "horizontal");
                    padding_left = padding_top;
                    padding_right = padding_top;
                    padding_bottom = padding_top;
                }
                if(style.has("padding_top")){
                    padding_top = (int)JasonHelper.pixels(context, style.getString("padding_top"), "vertical");
                }
                if(style.has("padding_left")){
                    padding_left = (int)JasonHelper.pixels(context, style.getString("padding_left"), "horizontal");
                }
                if(style.has("padding_bottom")){
                    padding_bottom = (int)JasonHelper.pixels(context, style.getString("padding_bottom"), "vertical");
                }
                if(style.has("padding_right")){
                    padding_right = (int)JasonHelper.pixels(context, style.getString("padding_right"), "horizontal");
                }

                // if not specified, default is 15
                if(padding_top < 0){
                    padding_top = 15;
                }
                if(padding_left < 0){
                    padding_left = 15;
                }
                if(padding_bottom < 0){
                    padding_bottom = 15;
                }
                if(padding_right < 0){
                    padding_right = 15;
                }

                view.setPadding(padding_left, padding_top, padding_right, padding_bottom);
            } catch (Exception e) {
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }

        } else {
            // shouldn't happen
            if (view == null) {
                return new View(context);
            } else {

                return view;
            }
        }
        JasonComponent.addListener(view, context);
        try {
            JSONObject style = component.getJSONObject("style");
            if(style.has("elevation")){
                view.setElevation(style.getInt("elevation"));
            }
        }catch(Exception e)
        {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
        // Add ripple effect to button
        JSONObject style = JasonHelper.style(component, context);
        try {
            if(style.has("rippleColor")){
                int bgColor = JasonHelper.parse_color(style.getString("rippleColor"));
                RippleDrawable rippleDrawable = new RippleDrawable(ColorStateList.valueOf(bgColor), view.getBackground(), null);
                view.setBackground(rippleDrawable);
            }
            else{
                RippleDrawable rippleDrawable = new RippleDrawable(ColorStateList.valueOf(Color.WHITE), view.getBackground(), null);
                view.setBackground(rippleDrawable);
            }
        }
        catch(Exception e){System.out.println(""+e);}

        return view;
    }
}
