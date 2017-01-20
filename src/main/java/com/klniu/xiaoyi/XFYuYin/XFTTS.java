package com.klniu.xiaoyi.XFYuYin;

import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SynthesizerListener;

/**
 * Created by klniu on 17-1-14.
 */
public class XFTTS extends XFAuth {
    // 语音合成对象
    private SpeechSynthesizer mTts;

    public XFTTS(String APPID) {
        super(APPID);
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer();
    }

    public void startSpeaking(String text) {
        mTts.startSpeaking(text, null);
    }

    public boolean isSpeaking() {
        return mTts.isSpeaking();
    }

    public void stopSpeaking(String text) {
        mTts.stopSpeaking();
    }

    public void setPerson(String name) {
        mTts.setParameter(SpeechConstant.VOICE_NAME, name);
    }
}
