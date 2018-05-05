package com.shawn.fakewechat.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by fengshawn on 2017/8/7.
 */

public class ListViewSelfAdapt extends ListView {


    public ListViewSelfAdapt(Context context) {
        super(context);
    }

    public ListViewSelfAdapt(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewSelfAdapt(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getMaxChildWidth() + getPaddingLeft() + getPaddingRight();
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), expandSpec);
    }

    private final int getMaxChildWidth() {
        int max = 0;
        ListAdapter adapter = getAdapter();
        int childCount = adapter == null ? 0 : adapter.getCount();
        View childView = null;
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                childView = adapter.getView(i, childView, this);
                childView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                max = max > childView.getMeasuredWidth() ? max : childView.getMeasuredWidth();
            }
        }
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = max;
        setLayoutParams(params);

        return max;
    }

    /**
     * 计算自适应高度，但是子布局必须LinearLayout，且在数据更新完成后，
     * 例如：
     * mAdapter.notifyDataSetChanged();
     * Function.getTotalHeightofListView(mListView);
     * @return
     */
    private int getSelfAdapterHeight() {
        int totalHeight = 0;

        //计算所有item高度
        ListAdapter adapter = getAdapter();
        int childCount = adapter == null ? 0 : adapter.getCount();
        View childView = null;
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                childView = adapter.getView(i, null, this);
                childView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                totalHeight += childView.getMeasuredHeight();
            }
            //计算所有分割线高度
            totalHeight += getDividerHeight() * (childCount - 1);
        }
        ViewGroup.LayoutParams params = getLayoutParams();

        params.height = totalHeight;

        // listView.getDividerHeight()获取子项间分隔符占用的高度

        // params.height最后得到整个ListView完整显示需要的高度

        setLayoutParams(params);

        return totalHeight;
    }

}
