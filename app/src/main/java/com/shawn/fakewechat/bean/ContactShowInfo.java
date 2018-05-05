package com.shawn.fakewechat.bean;

/**
 * Created by fengshawn on 2017/8/2.
 */

public class ContactShowInfo {

    private int headImage;
    private String username;
    private String lastMsg;
    private String lastMsgTime;
    private boolean isMute;
    private boolean isRead;
    private int accountType;

    public ContactShowInfo(int headImage, String username, String lastMsg, String lastMsgTime, boolean isMute, boolean isRead, int accountType) {
        this.headImage = headImage;
        this.username = username;
        this.lastMsg = lastMsg;
        this.lastMsgTime = lastMsgTime;
        this.isMute = isMute;
        this.isRead = isRead;
        this.accountType = accountType;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }


    public int getHeadImage() {
        return headImage;
    }

    public void setHeadImage(int headImage) {
        this.headImage = headImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(String lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }
}
