package com.guaju.videodemo;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by root on 17-7-3.
 */

public class ToastUtils {
    private static Toast toast;

    public static void show(Activity activity, String str) {
        if (toast == null) {
            toast = Toast.makeText(activity, str, Toast.LENGTH_SHORT);
        }
        toast.setText(str);
        toast.show();
    }

}
