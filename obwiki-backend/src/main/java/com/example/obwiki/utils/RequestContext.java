package com.example.obwiki.utils;

import java.io.Serializable;

public class RequestContext implements Serializable {

    private static ThreadLocal<String> remoteAddr = new ThreadLocal<>();
	//获取ip
    public static String getRemoteAddr() {
        return remoteAddr.get();
    }
	//存储远程ip字符串
    public static void setRemoteAddr(String remoteAddr) {
        RequestContext.remoteAddr.set(remoteAddr);
    }

}
