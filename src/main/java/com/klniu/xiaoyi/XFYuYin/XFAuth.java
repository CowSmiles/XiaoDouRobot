package com.klniu.xiaoyi.XFYuYin;

import com.iflytek.cloud.speech.Setting;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechUtility;

/**
 * Created by klniu on 17-1-14.
 */
public class XFAuth {
    public XFAuth(String APPID) {
        //在应用发布版本中，请勿显示日志，详情见此函数说明。
        Setting.setShowLog( true );
        SpeechUtility.createUtility(SpeechConstant.APPID +"="+APPID);
    }
}
