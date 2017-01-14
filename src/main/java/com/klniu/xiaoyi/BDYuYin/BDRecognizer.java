package com.klniu.xiaoyi.BDYuYin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDateTime;


/**
 * Created by klniu on 17-1-11.
 */
public class BDRecognizer extends BDAuth {
    private static final String serverURL = "http://vop.baidu.com/server_api";

    public BDRecognizer(String apiKey, String secretKey, String cuid) throws IOException {
        super(apiKey, secretKey, cuid);
    }

    public BDRecognizer(String apiKey, String secretKey, String cuid, String token, LocalDateTime tokenDate)
            throws IOException {
        super(apiKey, secretKey, cuid, token, tokenDate);
    }

    public String recognize(String filename) throws IOException, BDResponseException {
        File pcmFile = new File(filename);
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(serverURL
                    + "?cuid=" + cuid + "&token=" + token).openConnection();
            // add request header
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "audio/wav; rate=8000");

            conn.setDoInput(true);
            conn.setDoOutput(true);

            // send request
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(loadFile(pcmFile));
            wr.flush();
            wr.close();

            JSONObject json = getJSON(conn);
            int errNo = json.getInt("err_no");
            if (errNo > 0) {
                throw new BDResponseException("err_no: "+ errNo + ", " + json.getString("err_msg"));
            }
            JSONArray results = json.getJSONArray("result");
            String firstString = results.getString(0);
            if (firstString.matches(".*[,.，。]$")) {
                firstString = firstString.substring(0, firstString.length()-1);
            }
            return firstString;
        } catch (MalformedURLException|ProtocolException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            is.close();
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }
}
