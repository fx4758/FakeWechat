package com.shawn.fakewechat;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shawn.fakewechat.adapter.ChatAdapter;
import com.shawn.fakewechat.bean.MsgData;
import com.shawn.fakewechat.utils.HelpUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @author shawn 小烦 2017/8/9.
 */

public class ChatActivity extends AppCompatActivity {

    public final static int TYPE_RECEIVER_MSG = 0x21;
    public final static int TYPE_SENDER_MSG = 0x22;
    public final static int TYPE_TIME_STAMP = 0x23;

    private int profileId = R.drawable.hdimg_3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_chat);

        HelpUtils.transparentNav(this);
        Toolbar bar = findViewById(R.id.activity_wechat_chat_toolbar);
        setSupportActionBar(bar);
        getSupportActionBar().setTitle("");
        TextView tv = findViewById(R.id.activity_wechat_chat_tv_name);
        String name = getIntent().getStringExtra("name");
        profileId = getIntent().getIntExtra("profileId", R.drawable.hdimg_3);
        if (name != null) {
            tv.setText(name);
        }
        initComponents();
    }

    private void initComponents() {
        ImageView iv_back = findViewById(R.id.activity_wechat_chat_back);
        TextView tv_user = findViewById(R.id.activity_wechat_chat_tv_name);
        ImageView iv_voice = findViewById(R.id.activity_wechat_chat_iv_voice);
        EditText et_msg = findViewById(R.id.activity_wechat_chat_et_msg);
        ImageView iv_emoji = findViewById(R.id.activity_wechat_chat_iv_emoji);
        ImageView iv_add = findViewById(R.id.activity_wechat_chat_iv_add);
        Button btn_send = findViewById(R.id.activity_wechat_chat_btn_send);
        RecyclerView rv = findViewById(R.id.activity_wechat_chat_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));


        iv_back.setOnClickListener((v) -> finish());
        btn_send.startAnimation(getVisibleAnim(false, btn_send));
        btn_send.setVisibility(View.GONE);


        et_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("tag", "onTextChanged --- start -> " + start + " , count ->" + count + "，before ->" + before);
                if (start == 0 && count > 0) {
                    btn_send.startAnimation(getVisibleAnim(true, btn_send));
                    btn_send.setVisibility(View.VISIBLE);
                    iv_add.setVisibility(View.GONE);
                }

                if (start == 0 && count == 0) {
                    //btn_send.startAnimation(getVisibleAnim(false, btn_send));
                    btn_send.setVisibility(View.GONE);
                    iv_add.startAnimation(getVisibleAnim(true, iv_add));
                    iv_add.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        String[] msgs = {"在吗", "不在", "不在怎么回消息的", "我是天才机器人", "机器人这么智能啊", "是啊，时代在发展", "那你唱歌看看"
                , "你叫唱就唱啊，多没面子", "那你能干嘛", "啥都不能", "那你有啥用？", "没啥用你和我聊啥", "我想看下有到底有啥用",
                "你这智商看不出来的", "你智商能看出来啥？", "你智商不足", "不带你这么聊天的...", "那你说应该怎么聊天才对？",
                "没啥对不对就是瞎聊", "看到一条新闻\"43岁男友不回家带饭 27岁女友放火点房子涉刑罪\" ， 好逗！！！", "哈哈哈哈~~~"};
        List<MsgData> data = new ArrayList<>();

        for (int i = 0; i < msgs.length; i++) {
            MsgData msgData = new MsgData(msgs[i], HelpUtils.getCurrentMillisTime(), i % 2 == 0 ? profileId : R.drawable.hdimg_1
                    , i % 2 == 0 ? TYPE_RECEIVER_MSG : TYPE_SENDER_MSG);
            data.add(i, msgData);
        }

        ChatAdapter adapter = new ChatAdapter(this, data);
        rv.setAdapter(adapter);
        Handler smartReplyMsgHandler = new Handler();


        btn_send.setOnClickListener((v) -> {
            String sendMsg = et_msg.getText().toString();
            MsgData msgData = new MsgData(sendMsg, HelpUtils.getCurrentMillisTime(), R.drawable.hdimg_1, TYPE_SENDER_MSG);
            data.add(data.size(), msgData);
            adapter.notifyDataSetChanged();
            rv.scrollToPosition(data.size() - 1);
            et_msg.setText("");

            smartReplyMsgHandler.postDelayed(() -> {
                String receiveMsg = getRandomMessage();
                MsgData msgData1 = new MsgData(receiveMsg, HelpUtils.getCurrentMillisTime(), profileId, TYPE_RECEIVER_MSG);
                data.add(data.size(), msgData1);
                adapter.notifyDataSetChanged();
                rv.scrollToPosition(data.size() - 1);
                smartReplyMsgHandler.removeCallbacksAndMessages(null);
            }, 500);


        });
    }


    private String getRandomMessage() {
        String[] randomMsgs = {"我是机器人", "你发再多我主人也看不到", "不要回了", "你好无聊啊", "其实你发的没点用，我会全部把它过滤掉"
                , "因为我是机器人", "不管你信不信，反正我是不信", "再发我就神经错乱了", "我无法正常跟你沟通", "你说的我懂，我就是不做", "哈哈哈~~~"
                , "嘻嘻", "呵呵", "你可以走了", "我没逻辑的不要奢求多了", "我就呵呵了", "什么鬼", "我不懂", "啥意思？", "好了，你可以退下了", "本机器宝宝累了"};
        Random random = new Random();
        int pos = random.nextInt(randomMsgs.length);
        if (pos >= 0 && pos < randomMsgs.length - 1)
            return randomMsgs[pos];
        else
            return randomMsgs[0];
    }


    private Animation getVisibleAnim(boolean show, View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int y = view.getMeasuredHeight() / 4;
        int x = view.getMeasuredWidth() / 4;
        if (show) {
            ScaleAnimation showAnim = new ScaleAnimation(0.01f, 1f, 0.01f, 1f, x, y);
            showAnim.setDuration(200);
            return showAnim;

        } else {

            ScaleAnimation hiddenAnim = new ScaleAnimation(1f, 0.01f, 1f, 0.01f, x, y);
            hiddenAnim.setDuration(200);
            return hiddenAnim;
        }
    }

}
