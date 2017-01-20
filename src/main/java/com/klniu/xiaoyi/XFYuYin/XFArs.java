package com.klniu.xiaoyi.XFYuYin;

import com.iflytek.cloud.speech.*;
import com.iflytek.util.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XFArs extends XFAuth {
    protected static Logger logger = LogManager.getLogger(XFArs.class);
    // 语音合成对象
    private SpeechRecognizer mAsr;
    private String mGrammarId = null;
    private String result = null;

    private GrammarListener grammarListener = (id, error) -> {
        if (error == null) {
            mGrammarId = id;
        } else {
            logger.error("语法构建失败,错误码：" + error.getErrorDescription(true));
        }
    };

    private RecognizerListener recognizerListener = new RecognizerListener() {
        @Override
        public void onResult(RecognizerResult results, boolean islast) {
            // 结果返回为默认json格式,用JsonParser工具类解析
            result += JsonParser.parseGrammarResult(results
                    .getResultString());
        }
        @Override
        public void onVolumeChanged(int volume) {
            if (volume == 0)
                volume = 1;
            else if (volume >= 6)
                volume = 6;
        }

        @Override
        public void onError(SpeechError error) {
            if (null != error) {
                logger.error(error.getErrorDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int agr2, String msg) {}

        @Override
        public void onBeginOfSpeech() {}

        @Override
        public void onEndOfSpeech() {}
    };

    public XFArs(String APPID) {
        super(APPID);
        // 初始化合成对象
        mAsr = SpeechRecognizer.createRecognizer();
        mAsr.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");
        mAsr.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
    }

    /**
     * build grammar for commands.
     * @param grammar abnf format string
     * @return true if build successfully.
     */
    public boolean buildGrammar(String grammar) {
        // clear mGrammarID
        mGrammarId = null;
        int ret = mAsr.buildGrammar("abnf", grammar, grammarListener);
        if (ret != ErrorCode.SUCCESS) return false;
        return true;
    }

    public void startListening(String grammarID) {
        result = null;
        mAsr.setParameter(SpeechConstant.CLOUD_GRAMMAR, grammarID);
        mAsr.startListening(recognizerListener);
    }

    public boolean isListening() {
        return mAsr.isListening();
    }

    public String stopListening() {
        mAsr.stopListening();
        return result;
    }

    public String getmGrammarId() {
        return mGrammarId;
    }
}
