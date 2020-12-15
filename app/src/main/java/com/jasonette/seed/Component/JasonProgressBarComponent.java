package com.jasonette.seed.Component;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import org.json.JSONObject;

public class JasonProgressBarComponent {
    public static View build(View view, final JSONObject component, final JSONObject parent, final Context context) {
        if(view == null) {
            return new ProgressBar(context);
        }
        else{
            try {

                RelativeLayout layout = new RelativeLayout(context);
                ProgressBar progressBar = new ProgressBar(context,null,android.R.attr.progressBarStyleLarge);
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.SRC_ATOP);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                layout.addView(progressBar,params);

                return view;
            } catch (Exception e){
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            return new View(context);
            }
        }
    }
}
