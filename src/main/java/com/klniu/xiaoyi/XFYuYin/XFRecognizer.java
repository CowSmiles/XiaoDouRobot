package com.klniu.xiaoyi.XFYuYin;

import com.iflytek.cloud.speech.*;
import com.iflytek.util.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by klniu on 17-1-14.
 */
public class XFRecognizer extends XFAuth {
    protected static Logger logger = LogManager.getLogger(XFRecognizer.class);
    SpeechRecognizer mIat;
    String resultText;
    int errorCode;
    String errorDesc;

    private RecognizerListener recognizerListener = new RecognizerListener() {
        public void onBeginOfSpeech() {}
        public void onEndOfSpeech() {}
        public void onResult(RecognizerResult results, boolean islast) {
            //如果要解析json结果，请考本项目示例的 com.iflytek.com.iflytek.util.JsonParser类
            resultText += JsonParser.parseIatResult(results.getResultString());
        }
        public void onVolumeChanged(int volume) {}
        public void onError(SpeechError error) {
            if(null != error) {
                errorCode = error.getErrorCode();
                errorDesc = error.getErrorDescription(true);
                logger.error(errorDesc);
            }
        }
        public void onEvent(int eventType, int arg1, int agr2, String msg) {}
    };

    public XFRecognizer(String APPID) {
        super(APPID);
        mIat = SpeechRecognizer.createRecognizer();
    }

    public void startListening() {
        mIat.startListening(recognizerListener);
    }

    public String endListening() {
        mIat.stopListening();
        return resultText;
    }

    private void setting() {
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
    }
}
