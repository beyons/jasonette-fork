package com.jasonette.seed.Component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.gms.vision.text.Line;
import com.jasonette.seed.Helper.JasonHelper;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

public class JasonCardComponent {
    public static View build(View view, final JSONObject component, final JSONObject parent, final Context context) {
        if(view == null) {
            try {
                JSONObject style = component.getJSONObject("style");
                JSONObject image = component.getJSONObject("image");
                JSONObject title = component.getJSONObject("title");
                JSONObject description = component.getJSONObject("description");
                JSONObject date = component.getJSONObject("date");
                CardView card = new CardView(context);

                if (style.has("background")) {
                    int background = JasonHelper.parse_color(style.getString("background"));
                    card.setCardBackgroundColor(background);
                }

                if (style.has("min-height")) {
                    int minHeight = style.getInt("min-height");
                    card.setMinimumHeight(minHeight);
                }

                if (style.has("min-width")) {
                    int minWidth = style.getInt("min-width");
                    card.setMinimumWidth(minWidth);
                }
                //Create ImageView
                ImageView myImage = new ImageView(context);
                InputStream stream = null;
                stream = context.getAssets().open(image.getString("url"));
                Drawable d = Drawable.createFromStream(stream, null);
                myImage.setImageDrawable(d);
                myImage.setPadding(0,0,0,0);
                myImage.setAdjustViewBounds(true);
                //Create textView
                TextView titles = new TextView(context);
                titles.setText(title.getString("text"));
                titles.measure(0, 0);
                //Define the size
                if (title.has("size")) {
                    titles.setTextSize(Float.parseFloat(title.getString("size")));
                }
                //Define the color
                if (title.has("color")) {
                    int colorD = JasonHelper.parse_color(title.getString("color"));
                    titles.setTextColor(colorD);
                }
                //Define the fontWeight
                if (title.has("fontWeight")) {
                    String weight = description.getString("fontWeight");
                    if (weight.equalsIgnoreCase("bold")){
                        titles.setTypeface(null, Typeface.BOLD);
                    } else if(weight.equalsIgnoreCase("italic")){
                        titles.setTypeface(null, Typeface.ITALIC);
                    } else if(weight.equalsIgnoreCase("boldItalic")){
                        titles.setTypeface(null, Typeface.BOLD_ITALIC);
                    } else {
                        titles.setTypeface(null, Typeface.NORMAL);
                    }
                }
                //Define the alignement
                if (title.has("align")) {
                    int g1 = 0;
                    String align = description.getString("align");
                    if (align.equalsIgnoreCase("center")) {
                        g1 = g1 | Gravity.CENTER_HORIZONTAL;
                        titles.setGravity(Gravity.CENTER_HORIZONTAL);
                    } else if (align.equalsIgnoreCase("right")) {
                        g1 = g1 | Gravity.RIGHT;
                        titles.setGravity(Gravity.RIGHT);
                    } else if (align.equalsIgnoreCase("left")) {
                        g1 = g1 | Gravity.LEFT;
                        titles.setGravity(Gravity.LEFT);
                    }
                }
                //Define the margin
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setLayoutDirection(LinearLayout.VERTICAL);
                params.setMargins(title.getInt("margin-left"), title.getInt("margin-top"), title.getInt("margin-right"), title.getInt("margin-bottom"));
                titles.setLayoutParams(params);
                //Create the textView
                TextView descriptions = new TextView(context);
                descriptions.setText(description.getString("text"));
                //Define the size
                if (description.has("size")) {
                    descriptions.setTextSize(Float.parseFloat(description.getString("size")));
                }
                //Define the color
                if (description.has("color")) {
                    int colorD = JasonHelper.parse_color(description.getString("color"));
                    descriptions.setTextColor(colorD);
                }
                //Define the fontWeight
                if (description.has("fontWeight")) {
                    String weight = description.getString("fontWeight");
                    if (weight.equalsIgnoreCase("bold")){
                        descriptions.setTypeface(null, Typeface.BOLD);
                    } else if(weight.equalsIgnoreCase("italic")){
                        descriptions.setTypeface(null, Typeface.ITALIC);
                    } else if(weight.equalsIgnoreCase("boldItalic")){
                        descriptions.setTypeface(null, Typeface.BOLD_ITALIC);
                    } else {
                        descriptions.setTypeface(null, Typeface.NORMAL);
                    }
                }
                //Define the alignement
                if (description.has("align")) {
                    int g = 0;
                    String align = description.getString("align");
                    if (align.equalsIgnoreCase("center")) {
                        g = g | Gravity.CENTER_HORIZONTAL;
                        descriptions.setGravity(Gravity.CENTER_HORIZONTAL);
                    } else if (align.equalsIgnoreCase("right")) {
                        g = g | Gravity.RIGHT;
                        descriptions.setGravity(Gravity.RIGHT);
                    } else if (align.equalsIgnoreCase("left")) {
                        g = g | Gravity.LEFT;
                        descriptions.setGravity(Gravity.LEFT);
                    }
                }
                //Define the description margin
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params1.setLayoutDirection(LinearLayout.VERTICAL);
                params1.setMargins(description.getInt("margin-left"), description.getInt("margin-top"), description.getInt("margin-right"), description.getInt("margin-bottom"));
                descriptions.setLayoutParams(params1);
                //Create the textView
                TextView dates = new TextView(context);
                dates.setText(date.getString("text"));
                //Define the size
                if (date.has("size")) {
                    dates.setTextSize(Float.parseFloat(date.getString("size")));
                }
                //Define the color
                if (date.has("color")) {
                    int colorD = JasonHelper.parse_color(date.getString("color"));
                    dates.setTextColor(colorD);
                }
                //Define the fontWeight
                if (date.has("fontWeight")) {
                    String weight = date.getString("fontWeight");
                    if (weight.equalsIgnoreCase("bold")){
                        dates.setTypeface(null, Typeface.BOLD);
                    } else if(weight.equalsIgnoreCase("italic")){
                        dates.setTypeface(null, Typeface.ITALIC);
                    } else if(weight.equalsIgnoreCase("boldItalic")){
                        dates.setTypeface(null, Typeface.BOLD_ITALIC);
                    } else {
                        dates.setTypeface(null, Typeface.NORMAL);
                    }
                }
                //Define the alignement

                if (date.has("align")) {
                    int g = 0;
                    String align = date.getString("align");
                    if (align.equalsIgnoreCase("center")) {
                        g = g | Gravity.CENTER_HORIZONTAL;
                        dates.setGravity(Gravity.CENTER_HORIZONTAL);
                    } else if (align.equalsIgnoreCase("right")) {
                        g = g | Gravity.RIGHT;
                        dates.setGravity(Gravity.RIGHT);
                    } else if (align.equalsIgnoreCase("left")) {
                        g = g | Gravity.LEFT;
                        dates.setGravity(Gravity.LEFT);
                    }
                }
                //Define the description margin
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params2.setLayoutDirection(LinearLayout.HORIZONTAL);
                params2.setMargins(description.getInt("margin-left"), description.getInt("margin-top"), description.getInt("margin-right"), description.getInt("margin-bottom"));
                dates.setLayoutParams(params2);




                //Define LinearLayout
                LinearLayout lay =new LinearLayout(context);
                lay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                lay.setOrientation(LinearLayout.VERTICAL);
                //Get the actual position
                int positionImg = image.getInt("position");
                int positionTit = title.getInt("position");
                if(positionImg == 1 && positionTit == 2) {
                    lay.addView(myImage);
                    lay.addView(titles);
                    lay.addView(descriptions);
                    lay.addView(dates);
                }
                else {
                    if (positionImg == 2 && positionTit == 1) {
                        lay.addView(titles);
                        lay.addView(myImage);
                        lay.addView(descriptions);
                        lay.addView(dates);
                    }
                }
                card.addView(lay);

                //Return the card
                return card;
            } catch (Exception e){
                return new View(context);
            }
        }
        return new View(context);
    }
}
