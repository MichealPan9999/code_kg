package com.ktc.launcher.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ScreenParamsConfig {

    private static float cachedDpiDiv = 1.0f;
    private static float cachedResolutionDiv = 1.0f;
    public static boolean isInGuide = true;

    public static float getDpiDiv() {
        return cachedDpiDiv;
    }

    public static int getDpiValue(int i) {
        return (int) (i <= 0 ? (float) i : ((float) i) / cachedDpiDiv);
    }

    public static float getResolutionDiv() {
        return cachedResolutionDiv;
    }

    public static int getResolutionValue(int i) {
        return (int) (i <= 0 ? (float) i : ((float) i) / cachedResolutionDiv);
    }



    public static void setResolutionAndDpiDiv(Context context) {
        int displayWidth = getDisplayWidth(context);
        if (displayWidth == 3840) {
            cachedResolutionDiv = 0.5f;
        } else if (displayWidth == 1920) {
            cachedResolutionDiv = 1.0f;
        } else if (displayWidth == 1366) {
            cachedResolutionDiv = 1.4f;
        } else if (displayWidth == 1280) {
            cachedResolutionDiv = 1.5f;
        }
        cachedDpiDiv = cachedResolutionDiv * getDisplayDensity(context);
    }

    public static int getDisplayHeight(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
    }

    public static int getDisplayWidth(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return defaultDisplay == null ? 1920 : defaultDisplay.getWidth();
    }

    public static float getDisplayDensity(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        return context.getApplicationContext().getResources().getDisplayMetrics().density;
    }
}
