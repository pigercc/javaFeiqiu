package com.pigercc.feiqiu;

/**
 * Created by pigercc on 2016/6/19.
 */
public class GroupMsg {
    private String userName;

    private String pcName;

    private String mac;

    private String msg;

    private String time;

    private long groupNum;

    @Override
    public String toString() {
        return userName + "(" + mac + ") " + time + System.lineSeparator() + msg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(long groupNum) {
        this.groupNum = groupNum;
    }

}
