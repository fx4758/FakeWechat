package com.shawn.fakewechat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.shawn.fakewechat.adapter.ContactAdapter;
import com.shawn.fakewechat.adapter.SimpleMenuAdapter;
import com.shawn.fakewechat.component.PopupMenuWindows;
import com.shawn.fakewechat.bean.ContactShowInfo;
import com.shawn.fakewechat.utils.HelpUtils;

/**
 * Created by fengshawn on 2017/8/1.
 */

public class WechatActivity extends AppCompatActivity {

    public static final int TYPE_USER = 0x11;
    public static final int TYPE_SERVICE = 0X12;
    public static final int TYPE_SUBSCRIBE = 0x13;
    private int toolbarHeight, statusBarHeight;

    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_main);

        HelpUtils.transparentNav(this);

        Toolbar bar = findViewById(R.id.activity_wechat_toolbar);
        setSupportActionBar(bar);
        getSupportActionBar().setTitle("");
        initData();

        bar.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        toolbarHeight = bar.getMeasuredHeight();
        statusBarHeight = HelpUtils.getStatusBarHeight(WechatActivity.this);
    }

    private void initData() {
        ListView lv = findViewById(R.id.activity_wechat_lv);
        int[] headImgRes = {R.drawable.hdimg_3, R.drawable.group1, R.drawable.hdimg_2, R.drawable.user_2,
                R.drawable.user_3, R.drawable.user_4, R.drawable.user_5, R.drawable.hdimg_4,
                R.drawable.hdimg_5, R.drawable.hdimg_6};

        String[] usernames = {"Fiona", "  ...   ", "冯小", "深圳社保", "服务通知", "招商银行信用卡",
                "箫景、Fiona", "吴晓晓", "肖箫", "唐小晓"};
        //最新消息
        String[] lastMsgs = {"我看看", "吴晓晓：无人超市啊", "最近在忙些什么", "八月一号猛料，内地社保在这2...",
                "微信支付凭证", "#今日签到#你能到大的，比想象...", "箫景:准备去哪嗨", "[Video Call]", "什么东西？", "[微信红包]"};

        String[] lastMsgTimes = {"17:40", "10:56", "7月26日", "昨天", "7月27日", "09:46",
                "7月18日", "星期一", "7月26日", "4月23日"};

        int[] types = {TYPE_USER, TYPE_USER, TYPE_USER, TYPE_SUBSCRIBE, TYPE_SERVICE, TYPE_SUBSCRIBE,
                TYPE_USER, TYPE_USER, TYPE_USER, TYPE_USER};
        //静音&已读
        boolean[] isMutes = {false, true, false, false, false, false, true, false, false, false};
        boolean[] isReads = {true, true, true, true, true, true, true, true, true, true};

        List<ContactShowInfo> infos = new LinkedList<>();

        for (int i = 0; i < headImgRes.length; i++) {
            infos.add(i, new ContactShowInfo(headImgRes[i], usernames[i], lastMsgs[i], lastMsgTimes[i], isMutes[i], isReads[i], types[i]));
        }
        ContactAdapter adapter = new ContactAdapter(this, R.layout.item_wechat_main, infos);
        lv.setAdapter(adapter);


        lv.setOnTouchListener(new View.OnTouchListener() {
            int preX, preY;
            boolean isSlip = false, isLongClick = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        preX = (int) event.getX();
                        preY = (int) event.getY();
                        mHandler.postDelayed(() -> {
                            isLongClick = true;
                            int x = (int) event.getX();
                            int y = (int) event.getY();
                            //延时500ms后，其Y的坐标加入了Toolbar和statusBar高度
                            int position = lv.pointToPosition(x, y - toolbarHeight - statusBarHeight);
                            initPopupMenu(v, x, y, adapter, position, infos);

                        }, 500);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int nowX = (int) event.getX();
                        int nowY = (int) event.getY();

                        int movedX = Math.abs(nowX - preX);
                        int movedY = Math.abs(nowY - preY);
                        if (movedX > 50 || movedY > 50) {
                            isSlip = true;
                            mHandler.removeCallbacksAndMessages(null);
                            //处理滑动事件
                        }
                        break;


                    case MotionEvent.ACTION_UP:
                        mHandler.removeCallbacksAndMessages(null);
                        if (!isSlip && !isLongClick) {
                            //处理单击事件
                            int position = lv.pointToPosition(preX, preY);

                            Intent intent = new Intent(WechatActivity.this, ChatActivity.class);
                            intent.putExtra("name", usernames[position]);
                            intent.putExtra("profileId", headImgRes[position]);
                            startActivity(intent);
                        } else {
                            isSlip = false;
                            isLongClick = false;
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 设置已读还是未读
     *
     * @param isRead   true已读，false未读
     * @param position item position
     * @param adapter  数据源
     * @param datas
     */
    private void setIsRead(boolean isRead, int position, ContactAdapter adapter, List<ContactShowInfo> datas) {
        ContactShowInfo info = datas.get(position);
        info.setRead(isRead);
        adapter.notifyDataSetChanged();
    }

    /**
     * 删除指定位置item
     *
     * @param position 指定删除position
     * @param adapter  数据源
     * @param datas
     */
    private void deleteMsg(int position, ContactAdapter adapter, List<ContactShowInfo> datas) {
        datas.remove(position);
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化popup菜单
     */
    private void initPopupMenu(View anchorView, int posX, int posY, ContactAdapter adapter, int itemPos, List<ContactShowInfo> data) {
        List<String> list = new ArrayList<>();
        ContactShowInfo showInfo = data.get(itemPos);
        //初始化弹出菜单项
        switch (showInfo.getAccountType()) {
            case TYPE_SERVICE:
                list.clear();
                if (showInfo.isRead())
                    list.add("标为未读");
                else
                    list.add("标为已读");
                list.add("删除该聊天");
                break;

            case TYPE_SUBSCRIBE:
                list.clear();
                if (showInfo.isRead())
                    list.add("标为未读");
                else
                    list.add("标为已读");
                list.add("置顶公众号");
                list.add("取消关注");
                list.add("删除该聊天");
                break;

            case TYPE_USER:
                list.clear();
                if (showInfo.isRead())
                    list.add("标为未读");
                else
                    list.add("标为已读");
                list.add("置顶聊天");
                list.add("删除该聊天");
                break;
        }
        SimpleMenuAdapter<String> menuAdapter = new SimpleMenuAdapter<>(this, R.layout.item_menu, list);
        PopupMenuWindows ppm = new PopupMenuWindows(this, R.layout.popup_menu_general_layout, menuAdapter);
        int[] posArr = ppm.reckonPopWindowShowPos(posX, posY);
        ppm.setAutoFitStyle(true);
        ppm.setOnMenuItemClickListener((parent, view, position, id) -> {

            switch (list.get(position)) {
                case "标为未读":
                    setIsRead(false, itemPos, adapter, data);
                    break;

                case "标为已读":
                    setIsRead(true, itemPos, adapter, data);
                    break;

                case "置顶聊天":
                case "置顶公众号":
                    stickyTop(adapter, data, itemPos);
                    break;

                case "取消关注":
                case "删除该聊天":
                    deleteMsg(itemPos, adapter, data);
                    break;
            }
            ppm.dismiss();
        });
        ppm.showAtLocation(anchorView, Gravity.NO_GRAVITY, posArr[0], posArr[1]);
    }


    /**
     * 置顶item
     *
     * @param adapter
     * @param datas
     */
    private void stickyTop(ContactAdapter adapter, List<ContactShowInfo> datas, int position) {
        if (position > 0) {
            ContactShowInfo stickyTopItem = datas.get(position);
            datas.remove(position);
            datas.add(0, stickyTopItem);
        } else {
            return;
        }
        adapter.notifyDataSetChanged();
    }

}

