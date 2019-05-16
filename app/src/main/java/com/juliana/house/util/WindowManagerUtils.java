package com.juliana.house.util;

import android.content.Context;
import android.view.WindowManager;

public class WindowManagerUtils {
    /**
     * 获取屏幕的宽度
     * @return
     */
    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager)context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    /**
     * 获取屏幕的高度
     * @return
     */
    public static int getScreenHight(Context context){
        WindowManager wm = (WindowManager)context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    /**
     * 获取屏幕的高度的一半
     * @return
     */
    public static int getScreenPartHight(Context context){
        WindowManager wm = (WindowManager)context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height/2;
    }

}
