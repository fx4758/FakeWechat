package com.shawn.fakewechat.bean;

/**
 * Created by fengshawn on 2017/8/10.
 */

public class MsgData {
    private String msg;
    private long timeStamp;
    private int profile_res;
    private int msgType;

    public MsgData(String msg, long timeStamp, int hd_img_res, int msgType) {
        this.msg = msg;
        this.timeStamp = timeStamp;
        this.profile_res = hd_img_res;
        this.msgType = msgType;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getProfile_res() {
        return profile_res;
    }

    public void setProfile_res(int profile_res) {
        this.profile_res = profile_res;
    }
}
