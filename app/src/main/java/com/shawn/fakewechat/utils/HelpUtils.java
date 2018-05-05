package com.shawn.fakewechat.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.shawn.fakewechat.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fengshawn on 2017/8/8.
 */

public class HelpUtils {

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getStatusBarHeight(Context context) {
        Resources res = context.getResources();
        int resId = res.getIdentifier("status_bar_height", "dimen", "android");
        return res.getDimensionPixelSize(resId);

        //也可以通过反射
        /**
         Class<?> c = null;
         Object obj = null;
         Field field = null;
         int x = 0, statusBarHeight = 0;
         try {
         c = Class.forName("com.android.internal.R$dimen");
         obj = c.newInstance();
         field = c.getField("status_bar_height");
         x = Integer.parseInt(field.get(obj).toString());
         statusBarHeight = context.getResources().getDimensionPixelSize(x);
         } catch (Exception e1) {
         e1.printStackTrace();
         }
         return statusBarHeight;
         */
    }

    /**
     * 用户友好时间显示
     *
     * @param nowTime 现在时间毫秒
     * @param preTime 之前时间毫秒
     * @return 符合用户习惯的时间显示
     */
    public static String calculateShowTime(long nowTime, long preTime) {
        if (nowTime <= 0 || preTime <= 0)
            return null;
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd-HH-mm-E");
        String now = format.format(new Date(nowTime));
        String pre = format.format(new Date(preTime));
        String[] nowTimeArr = now.split("-");
        String[] preTimeArr = pre.split("-");
        //当天以内,年月日相同，超过一分钟显示
        if (nowTimeArr[0].equals(preTimeArr[0]) && nowTimeArr[1].equals(preTimeArr[1]) && nowTimeArr[2].equals(preTimeArr[2]) && nowTime - preTime > 60000) {
            return preTimeArr[3] + ":" + preTimeArr[4];
        }
        //一周以内
        else if (Integer.valueOf(nowTimeArr[2]) - Integer.valueOf(preTimeArr[2]) > 0 && nowTime - preTime < 7 * 24 * 60 * 60 * 1000) {

            if (Integer.valueOf(nowTimeArr[2]) - Integer.valueOf(preTimeArr[2]) == 1)
                return "昨天 " + preTimeArr[3] + ":" + preTimeArr[4];
            else
                return preTimeArr[5] + " " + preTimeArr[3] + ":" + preTimeArr[4];
        }
        //一周以上
        else if (nowTime - preTime > 7 * 24 * 60 * 60 * 1000) {
            return preTimeArr[0] + "年" + preTimeArr[1] + "月" + preTimeArr[2] + "日" + " " + preTimeArr[3] + ":" + preTimeArr[4];
        }
        return null;
    }


    public static long getCurrentMillisTime() {
        return System.currentTimeMillis();
    }


    public static void transparentNav(Activity activity) {
        setTransparentStatus(true, activity);
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorDark);
    }

    @TargetApi(19)
    private static void setTransparentStatus(boolean on, Activity activity) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on)
            params.flags |= bits;
        else
            params.flags &= ~bits;

        win.setAttributes(params);
    }
}
