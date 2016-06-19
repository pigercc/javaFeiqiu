package com.pigercc.feiqiu;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pigercc on 2016/6/17.
 */
public class FeiqUtil {

    private final static String DEFAULT_BROADCASE_IP = "226.81.9.8";
    private final static int DEFAULT_BROADCASE_PORT = 2425;
    private final static String DEFAULT_FONT = "{/font;-16 0 0 0 400 0 0 0 134 0 0 2 32 微软雅黑 8404992;}";
    private final static String DEFAULT_ENCODING = "GBK";

    private final static long MSG_TYPE_GROUP = 4194339L;


    /**
     * 向指定群，发送群消息
     *
     * @param groupNum
     * @param msg
     * @param userName
     * @param pcName
     * @param mac
     */
    public static void sendGroupMsg(long groupNum, String msg, String userName, String pcName, String mac) {
        try {
            InetAddress group = InetAddress.getByName(DEFAULT_BROADCASE_IP);
            MulticastSocket msr = new MulticastSocket(DEFAULT_BROADCASE_PORT);
            msr.joinGroup(group);
            byte[] sendData = getSendData(groupNum, msg, userName, pcName, mac);
            DatagramPacket dp = new DatagramPacket(sendData, sendData.length, group, DEFAULT_BROADCASE_PORT);
            msr.send(dp);
            msr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听整个组播地址内的群消息
     */
    public static void listenGroupMsg() {
        MulticastSocket msr = null;
        try {
            InetAddress group = InetAddress.getByName(DEFAULT_BROADCASE_IP);
            msr = new MulticastSocket(DEFAULT_BROADCASE_PORT);
            msr.joinGroup(group);
            byte[] buf = new byte[1024];
            while (true) {
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                msr.receive(dp);
                GroupMsg groupMsg = getGroupMsg(dp.getData());
                if (groupMsg != null) {
                    System.out.println(groupMsg.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msr != null) {
                msr.close();
            }
        }
    }

    private static GroupMsg getGroupMsg(byte[] data) throws Exception {
        if (!isGroupMsgType(data)) {
            return null;
        }
        String mac = getGroupMac(data);
        String userName = getUserName(data);
        String pcName = getPcName(data);
        byte[] encryData = getEncryData(data, getEncryDataLength(data));
        if (encryData == null) {
            return null;
        }

        byte[] decryData = decrypt(mac, encryData);
        //从消息体中获取群号和消息内容
        //QUNMSGMARK#3b199b07_24901708#/:bomb{/font;-16 0 0 0 400 0 0 0 134 0 0 2 32 微软雅黑 8404992;}
        String msgBody = new String(decryData, DEFAULT_ENCODING);
        GroupMsg groupMsg = new GroupMsg();
        groupMsg.setMac(mac);
        groupMsg.setUserName(userName);
        groupMsg.setPcName(pcName);
        groupMsg.setTime(DateUtil.getFormatTime(new Date()));

        if (msgBody.indexOf("#") > 0 && msgBody.indexOf("_") > 0) {
            groupMsg.setGroupNum(Long.valueOf(msgBody.substring(msgBody.indexOf("#") + 1, msgBody.indexOf("_")), 16));
            msgBody = msgBody.substring(msgBody.indexOf("_"));
        }
        if (msgBody.lastIndexOf("{") > 0 && msgBody.indexOf("#") > 0) {
            groupMsg.setMsg(msgBody.substring(msgBody.indexOf("#") + 1, msgBody.lastIndexOf("{")));
        }
        return groupMsg;
    }

    private static boolean isGroupMsgType(byte[] data) throws Exception {
        byte[] typeByte = getIndexValue(data, 4, ':');
        if (typeByte.length == 0) {
            return false;
        }
        return StringUtil.equalsIgnoreCase(new String(typeByte, DEFAULT_ENCODING), "" + MSG_TYPE_GROUP);
    }

    private static String getGroupMac(byte[] data) throws Exception {
        return new String(getIndexValue(data, 2, '#'), DEFAULT_ENCODING);
    }

    private static int getEncryDataLength(byte[] data) throws Exception {
        byte[] lengthByte = getIndexValue(data, 5, '#');
        return Integer.valueOf(new String(lengthByte, DEFAULT_ENCODING));
    }

    private static byte[] getEncryData(byte[] data, int length) {
        byte[] msgBodyData = getIndexValue(data, 5, ':');
        byte[] encryData = new byte[length];

        if (msgBodyData.length < encryData.length) {
            return null;
        }
        System.arraycopy(msgBodyData, 0, encryData, 0, encryData.length);
        return encryData;
    }

    private static String getUserName(byte[] data) throws Exception {
        return new String(getIndexValue(data, 2, ':'), DEFAULT_ENCODING);
    }

    private static String getPcName(byte[] data) throws Exception {
        return new String(getIndexValue(data, 3, ':'), DEFAULT_ENCODING);
    }

    private static byte[] getIndexValue(byte[] data, int startIndex, char charValue) {
        int count = 0;
        boolean findFlag = false;
        List<Byte> byteList = new ArrayList<Byte>();
        for (byte b : data) {
            if (b == charValue) {
                count++;
            }

            if (count == startIndex + 1) {
                break;
            }

            if (findFlag) {
                byteList.add(b);
            }
            if (count == startIndex) {
                findFlag = true;
            }

        }
        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = byteList.get(i);
        }
        return byteArray;
    }

    private static byte[] getSendData(long groupNum, String msg, String userName, String pcName, String mac) throws Exception {
        byte[] groupData = String.format("QUNMSGMARK#%s_%d#%s%s", Long.toHexString(groupNum), (System.currentTimeMillis() / 1000), msg, DEFAULT_FONT).getBytes(DEFAULT_ENCODING);
        byte[] groupEncryptData = encrypt(mac, groupData);
        byte[] headData = String.format("1_lbt4_0#128#%s#0#0#%d#4000#9:%d:%s:%s:%d:", mac, groupEncryptData.length, (System.currentTimeMillis() / 1000), pcName, userName, MSG_TYPE_GROUP).getBytes(DEFAULT_ENCODING);

        byte[] fullData = new byte[headData.length + groupEncryptData.length];
        System.arraycopy(headData, 0, fullData, 0, headData.length);
        System.arraycopy(groupEncryptData, 0, fullData, headData.length, groupEncryptData.length);
        return fullData;
    }

    //Blowfish加密
    private static byte[] encrypt(String keyValue, byte[] encryptData) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyValue.getBytes(), "Blowfish");
        IvParameterSpec iv = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
        Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(encryptData);
    }

    //Blowfish解密
    private static byte[] decrypt(String keyValue, byte[] decryptData) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyValue.getBytes(), "Blowfish");
        IvParameterSpec iv = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
        Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(decryptData);
    }


}
