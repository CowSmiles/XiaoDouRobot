package com.klniu.xiaoyi.BDYuYin;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by klniu on 17-1-12.
 */
public class BDText2Audio extends BDAuth {
    private static final String serverURL = "http://tsn.baidu.com/text2audio";

    public BDText2Audio(String apiKey, String secretKey, String cuid) throws IOException {
        super(apiKey, secretKey, cuid);
    }

    public BDText2Audio(String apiKey, String secretKey, String cuid, String token, LocalDateTime tokenDate) throws IOException {
        super(apiKey, secretKey, cuid, token, tokenDate);
    }

    // public String text2Audio(String text) throws Exception {
    //     // twice
    //     String encodedStr = URLEncoder.encode(text, "UTF-8");
    //     encodedStr = URLEncoder.encode(encodedStr, "UTF-8");
    //     HttpURLConnection conn = (HttpURLConnection) new URL(serverURL
    //             + "?tex=" + encodedStr + "&lan=zh&cuid" + cuid + "&ctp=1&token=" + token).openConnection();

    //     // add request header
    //     conn.setRequestMethod("POST");

    //     conn.setDoInput(true);
    //     conn.setDoOutput(true);

    //     DataInputStream in = new DataInputStream(conn.getInputStream());
    //     // send request
    //     DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
    //     wr.write(loadFile(pcmFile));
    //     wr.flush();
    //    wr.close();

    //    return getJSON(conn);
    //}

}
