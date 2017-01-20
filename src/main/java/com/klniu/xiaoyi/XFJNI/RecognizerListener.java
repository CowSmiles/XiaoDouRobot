package com.klniu.xiaoyi.XFJNI;

public interface RecognizerListener {
    void onBeginOfSpeech();
    void onEndOfSpeech();
    void onResult(String result, boolean isLast);
}
