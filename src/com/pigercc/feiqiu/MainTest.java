package com.pigercc.feiqiu;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by pigercc on 2016/6/17.
 */
public class MainTest {

    private final static long MSG_TYPE_GROUP = 0L;
    public static void main(String[] args) {
        //FeiqUtil.listenGroupMsg();
        FeiqUtil.sendGroupMsg(991533831L,"myMsg","myUserName","myPcName","myMac");
    }
}
