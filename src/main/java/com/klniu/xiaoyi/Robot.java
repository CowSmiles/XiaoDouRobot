package com.klniu.xiaoyi;

import com.klniu.xiaoyi.XFYuYin.XFArs;
import com.klniu.xiaoyi.XFYuYin.XFRecognizer;
import com.klniu.xiaoyi.XFYuYin.XFTTS;
import ddf.minim.spi.AudioOut;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static java.lang.Thread.sleep;

public class Robot {
    private static Logger logger = LogManager.getLogger(Robot.class);
    private Configuration conf;
    private XFRecognizer recog;
    private XFTTS tts;
    private XFArs ars;
    private AudioPlayer audioPlayer = new AudioPlayer();
    private IndexFiles audioIdx;
    private String mainGrammarID;
    private String musicGrammarID;
    private final static String robotName = "小豆";

    public Robot() {
        try {
            conf = new Configuration("config.properties");

            // initialize ifly apiID
            String apiID = conf.getProperty("XFAPIID", "");
            if (apiID.equals("")) {
                throw new Exception("invalid xunfei api id");
            }

            recog = new XFRecognizer(apiID);
            tts = new XFTTS(apiID);
            tts.setPerson("vinn");
            ars = new XFArs(apiID);

            indexAudioFiles();
            mainGrammarID = buildCommandsGrammer("MainGrammarID", "grammars/main_grammar.txt");
            musicGrammarID = buildCommandsGrammer("MusicGrammarID", "grammars/music_grammar.txt");
        } catch (Exception e) {
            logger.fatal(e);
        }
    }

    private void indexAudioFiles() throws Exception {
        String audioDir = conf.getProperty("AudioDir", "");
        if (audioDir.equals("")) {
            throw new Exception("no audio directory found");
        }
        String audioFormat = conf.getProperty("AudioFormat", ".mp3");
        audioIdx = new IndexFiles(audioDir, Arrays.asList(audioFormat.split(",")));
    }

    private String buildCommandsGrammer(String id, String filename) throws Exception {
        String contents;
        String grammarId = conf.getProperty(id, "");
        if (grammarId.equals("")) {
            URL input = getClass().getClassLoader().getResource(filename);
            contents = new String(Files.readAllBytes(Paths.get(input.toURI())));
            if (ars.buildGrammar(contents)) {
                while (ars.getmGrammarId() == null) {
                    sleep(1000);
                }
                grammarId = ars.getmGrammarId();
                conf.setProperty(id, grammarId);
                conf.save();
            }
        }
        return grammarId;
    }

    void loop() {
        try {
            while (true) {
                sleep(300);
                if (hasSound() && isRobotQuiet()) {
                    mainAction(listenArs(mainGrammarID));
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void mainAction(String command) throws Exception {
        if (command.contains(robotName+"你会干什么")) {
            // help
            tts.startSpeaking(String.format("我可以做好多事情呢，比如，您说：%1$s，哼个歌，我就可以唱流行歌曲了；" +
                    "您说，%1$s,唱个儿歌，我就可以给小宝宝唱歌了呢；" +
                    "您说，%1$s，朗诵一首古文，我就可以读好多种古文了；" +
                    "您说，%1$s，读诵佛经，我就会很虔诚的读经给您听；", robotName));
        } else if (command.contains(robotName+"哼个歌")) {
            tts.startSpeaking("哼什么呢");
            waitUntilRobotQuiet();
            musicAction(listenArs(musicGrammarID));
        } else if (command.contains(robotName+"唱个儿歌")) {
            tts.startSpeaking("唱什么呢");
            waitUntilRobotQuiet();
            musicAction(listenArs(musicGrammarID));
        } else if (command.contains(robotName+"朗诵一首古文")) {
            tts.startSpeaking("朗诵什么呢");
            waitUntilRobotQuiet();
            musicAction(listenArs(musicGrammarID));
        } else if (command.contains(robotName+"读诵佛经")) {
            tts.startSpeaking("读诵什么呢");
            waitUntilRobotQuiet();
            musicAction(listenArs(musicGrammarID));
        } else {
            tts.startSpeaking("无效命令: " + command);
        }
    }

    private void musicAction(String command) {
        String path = audioIdx.getFilePath(command);
        if (path != null) {
            audioPlayer.loadFile(path);
            audioPlayer.play();
        }

    }

    private String listenArs(String grammarID) throws Exception {
        String command;
        logger.debug("开始识别");
        ars.startListening(mainGrammarID);
        while (true) {
            sleep(3000);
            if (!hasSound()) {
                logger.debug("结束识别");
                command = ars.stopListening();
                logger.debug("Command: " + command);
                return command;
            }
        }
    }

    private boolean hasSound() {
        return true;
    }

    // check if the audioplayer is stopped, is not listening and speaking.
    private boolean isRobotQuiet() {
        return !(ars.isListening() || tts.isSpeaking() || audioPlayer.isPlaying());
    }

    private void waitUntilRobotQuiet() throws Exception {
        while (tts.isSpeaking()) {
            sleep(300);
        }
    }
}
