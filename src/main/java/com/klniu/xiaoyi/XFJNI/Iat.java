package com.klniu.xiaoyi.XFJNI;

import com.iflytek.cloud.speech.GrammarListener;

/**
 * Ifly Auto Transform jni.
 */
public class Iat extends SpeechRecognizer {
    public Iat(String appID) throws IllegalArgumentException {
        super(appID);
    }

    public native int uploadHotWords(String content);
}
