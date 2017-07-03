package com.guaju.videodemo;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by root on 17-7-3.
 */

public class Dp2px {
    public static int Dp2px(Activity activity, int dp) {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display dd = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        dd.getMetrics(dm);
        float density = dm.density;
        float pxf = density * dp + 0.5f;
        int px = (int) pxf;
        return px;
    }

}
