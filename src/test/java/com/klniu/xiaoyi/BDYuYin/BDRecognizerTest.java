package com.klniu.xiaoyi.BDYuYin;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.Assert.*;

public class BDRecognizerTest {
    private final String filename = "src/test/resources/baidu.pcm";
    private BDRecognizer rec;
    private String apiKey = "zQGouzD3jmFq0wMob2G8MhQ5";
    private String secretKey = "1afbf5f91fe7c79134110ba89950f69c";
    private String cuid = "xiaoyi_robot";
    private String token;
    private LocalDateTime tokenTime;
    @Before
    public void setUp() throws Exception {
        rec = new BDRecognizer(apiKey, secretKey, cuid);
        // used to test token experied
        token = rec.getToken();
        tokenTime = rec.getTokenCreationTime();
    }

    @Test
    public void tokenTime() throws Exception {
        // not expired
        LocalDateTime oldTime = tokenTime.minusDays(28);
        BDRecognizer rec1 = new BDRecognizer(apiKey, secretKey, cuid,token, oldTime);
        assertEquals(rec1.getTokenCreationTime(), oldTime);

        // expired
        oldTime = tokenTime.minusDays(31);
        rec1 = new BDRecognizer(apiKey, secretKey, cuid,token, oldTime);
        assertNotEquals(rec1.getTokenCreationTime(), oldTime);
        // should be in 1 minutes.
       assertTrue(SECONDS.between(LocalDateTime.now(), rec1.getTokenCreationTime()) < 1);
    }

    @Test
    public void recognize() throws Exception {
        assertEquals("百度语音提供技术支持",rec.recognize(this.filename));

        // error
        try {
            rec.recognize("/usr/bin/java");
        } catch (BDResponseException e) {
            assertEquals("err_no: 3301, recognition error.", e.getMessage());
        }

    }
}