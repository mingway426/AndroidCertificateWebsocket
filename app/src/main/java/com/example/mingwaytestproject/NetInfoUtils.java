package com.tcl.ipq.websocket.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.tcl.ipq.IpqLogic;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * file name: NetInfoUtils
 *
 * @author： lumingwei
 * @date: 2020/4/3 10:42
 * e-mail: mingwei1.lu@tcl.com
 */
public class NetInfoUtils {

    public static InetSocketAddress getInetAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                //得到IPV4地址
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
                InetSocketAddress address = new InetSocketAddress(ipAddress, WebsocketConstant.DEFAULT_PORT);
                return address;
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                String localIp = getLocalIp();
                IpqLogic.logd("NetInfo", "get local ip = " + localIp);
                return new InetSocketAddress(localIp, WebsocketConstant.DEFAULT_PORT);
            }
        } else {
            //不在 wifi 网络中。
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    // 获取有限网IP
    private static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return "0.0.0.0";

    }

}
