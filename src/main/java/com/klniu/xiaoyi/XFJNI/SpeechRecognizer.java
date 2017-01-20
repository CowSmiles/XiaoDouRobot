package com.klniu.xiaoyi.XFJNI;

import java.io.File;

public abstract class SpeechRecognizer {
    static {
        System.loadLibrary("jnimsc");
    }

    public SpeechRecognizer(String appID) throws IllegalArgumentException {
        if (appID.trim().length() <= 5) {
            throw new IllegalArgumentException("invalid xunfei appid");
        }
        init(appID);
    }

    /**
     * login the msc
     * @param appID appID for xunfei sdk
     * @return error code according the xunfei reference
     */
    private native int init(String appID);

    public native void startListening(RecognizerListener listener);

    public native void stopListening();

    public native boolean isListening();

    private native boolean destroy();

    protected void finalize( )
    {
        destroy();
    }
}

