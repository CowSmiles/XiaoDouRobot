package com.klniu.xiaoyi;


import com.klniu.xiaoyi.BDYuYin.BDRecognizer;
import com.klniu.xiaoyi.BDYuYin.BDText2Audio;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

import static java.lang.Thread.sleep;

/**
 * Created by klniu on 17-1-9.
 */
public class Main {
    private static Logger logger = LogManager.getLogger(Main.class);
    private static String projectDir = "src/main/resources/";

    public static void main(String[] args) {
        Configuration conf;
        BDRecognizer recog;
        BDText2Audio t2A;
        AudioPlayer audioPlayer = new AudioPlayer();
        AudioRecorder recorder = AudioRecorder.createRecorder(projectDir + "rec.wav");
        // read configuration
        try {
            conf = new Configuration(projectDir + "config");

            // initialize BDRecognizer and BDText2Audio
            String apiKey = conf.getProperty("BDAPIKey", "");
            String secretKey = conf.getProperty("BDSecretKey", "");
            String cuid = conf.getProperty("BDCUID", "");
            String token = conf.getProperty("BDToken", "");
            String tokenTime = conf.getProperty("BDTokenCreation", "2000-01-01T00:00:00");
            LocalDateTime t = LocalDateTime.parse(tokenTime);

            recog = new BDRecognizer(apiKey, secretKey, cuid, token, t);

            // use new token time if it is updated
            token = recog.getToken();
            t = recog.getTokenCreationTime();
            t2A = new BDText2Audio(apiKey, secretKey, cuid, token, t);

            // save token and token time
            conf.setProperty("BDToken", token);
            conf.setProperty("BDTokenCreation", t.toString());
            conf.save();

            String audioDir = conf.getProperty("AudioDir", ".");

            recorder.beginRecord();
            System.out.println("begin record...");
            sleep(4000);
            recorder.endRecord();
            System.out.println("end record...");
            recorder.save();
            audioPlayer.loadFile(projectDir + "rec.wav");
            audioPlayer.play();
            sleep(4000);
            String result = recog.recognize(projectDir+"rec.wav");
            logger.info(result);
        } catch (Exception e) {
            logger.fatal(e);
        }
    }
}
