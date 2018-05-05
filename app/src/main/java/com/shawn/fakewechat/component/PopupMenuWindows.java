package com.shawn.fakewechat.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;

import com.shawn.fakewechat.R;
import com.shawn.fakewechat.utils.HelpUtils;

/**
 * Created by fengshawn on 2017/8/3.
 */

public class PopupMenuWindows extends PopupWindow {

    private final static int SHOW_ON_LEFT = 0X11;
    private final static int SHOW_ON_RIGHT = 0X12;
    private final static int SHOW_ON_UP = 0X13;
    private final static int SHOW_ON_DOWN = 0X14;

    private View contentView;
    private Context context;
    private int showAtVertical, showAtOrientation;
    private OnMenuItemClickListener listener;
    private ListAdapter adapter;
    private boolean isSetAutoFitStyle = false;

    public interface OnMenuItemClickListener {
        void onMenuItemClickListener(AdapterView<?> parent, View view, int position, long id);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    public PopupMenuWindows(Context context, int resId, ListAdapter adapter) {
        super(context);
        this.context = context;
        this.adapter = adapter;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(resId, null);
        setContentView(contentView);
        initPopWindow();
    }

    private void initPopWindow() {
        Drawable dw;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(35);
        } else {
            dw = ContextCompat.getDrawable(context, R.drawable.bg);
        }
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        dw = ContextCompat.getDrawable(context, R.color.colorWhite);
        setBackgroundDrawable(dw);
        ListViewSelfAdapt listViewSelfAdapt = (ListViewSelfAdapt) contentView.findViewById(R.id.popup_menu_general_layout_lv);
        listViewSelfAdapt.setAdapter(adapter);
        listViewSelfAdapt.setOnItemClickListener((parent, view, position, id) -> {
            listener.onMenuItemClickListener(parent, view, position, id);
        });
    }


    public View getContentView() {
        return contentView;
    }

    /**
     * 计算弹出位置，弹出位置空间不足的向反方向弹出
     *
     * @param posX 触摸的X坐标
     * @param posY 触摸的Y坐标
     * @return 长度为2的坐标数组, 为重新计算过的弹出位置，0为x坐标,1为y坐标
     */
    public int[] reckonPopWindowShowPos(int posX, int posY) {
        int screenH = HelpUtils.getScreenHeight(context);
        int screenW = HelpUtils.getScreenWidth(context);
        //popupWindow显示区域View
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int windowsHeight = contentView.getMeasuredHeight();
        int windowsWidth = contentView.getMeasuredWidth();
        int x = posX, y = posY;    //窗口弹出坐标

        //向上弹出
        if (screenH - posY < windowsHeight) {
            y = posY - windowsHeight;
            showAtVertical = SHOW_ON_UP;
        } else {  //向下弹出
            showAtVertical = SHOW_ON_DOWN;
        }

        //左弹出
        if (screenW - posX < windowsWidth) {
            x = posX - windowsWidth;
            showAtOrientation = SHOW_ON_LEFT;
        } else {   //右弹出
            showAtOrientation = SHOW_ON_RIGHT;
        }
        int[] posArr = new int[2];
        posArr[0] = x;
        posArr[1] = y;
        //防止设置自适应动画方法在此方法之前调用
        if (isSetAutoFitStyle) {
            setAutoFitStyle(true);
        }
        return posArr;
    }

    /**
     * 自适应触摸位置弹出动画
     *
     * @param isSet
     */
    public void setAutoFitStyle(boolean isSet) {
        isSetAutoFitStyle = isSet;
        if (isSet) {

            if (showAtOrientation == SHOW_ON_RIGHT && showAtVertical == SHOW_ON_UP) {
                setAnimationStyle(R.style.PopupWindowAnimationLB);
            }

            if (showAtOrientation == SHOW_ON_RIGHT && showAtVertical == SHOW_ON_DOWN) {
                setAnimationStyle(R.style.PopupWindowAnimationLT);
            }

            if (showAtOrientation == SHOW_ON_LEFT && showAtVertical == SHOW_ON_UP) {
                setAnimationStyle(R.style.PopupWindowAnimationRB);
            }

            if (showAtOrientation == SHOW_ON_LEFT && showAtVertical == SHOW_ON_DOWN) {
                setAnimationStyle(R.style.PopupWindowAnimationRT);
            }
        }
    }
}
