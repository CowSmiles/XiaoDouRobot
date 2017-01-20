package com.klniu.xiaoyi;


import com.iflytek.cloud.speech.GrammarListener;
import com.iflytek.cloud.speech.SpeechError;
import com.klniu.xiaoyi.XFJNI.Iat;
import com.klniu.xiaoyi.XFJNI.RecognizerListener;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws Exception {
        // read configuration
        Robot robot = new Robot();
        robot.loop();
        Iat iat = new Iat("58736af3");
        //RecognizerListener recognizerListener = new RecognizerListener() {
        //    @Override
        //    public void onBeginOfSpeech() {
        //        System.out.println("begin");
        //    }

        //    @Override
        //    public void onEndOfSpeech() {
        //        System.out.println("end");
        //    }

        //    @Override
        //    public void onResult(String result, boolean isLast) {
        //        System.out.println("result" + isLast);
        //    }
        //};
        //iat.startListening(recognizerListener);
        //System.out.print(iat.isListening());
        //sleep(5000);
        //iat.stopListening();
    }
}
