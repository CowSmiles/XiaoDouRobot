package com.klniu.xiaoyi;

import com.klniu.xiaoyi.XFYuYin.XFArs;
import com.klniu.xiaoyi.XFYuYin.XFTTS;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.util.DefaultPropertiesPersister;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.klniu")
public class RobotConfig {
    private String confFile = "config.properties";
    private Properties props;
    private String robotName;
    private String audioDir;
    private String audioFormat;
    private String XFApiId;
    private String mainGrammarID;
    private String musicGrammarID;
    private String mainGrammarFile;
    private String musicGrammarFile;
    private String ttsPerson;

    @PostConstruct
    public void init() throws Exception {
        props = new Properties();
        InputStreamReader input = null;
        try {
            input = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(confFile), "UTF-8");
            // load a properties file
            props.load(input);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("can not find config file " + confFile);
        } catch (IOException e) {
            throw new IOException("can not load configuration for " + confFile);
        } finally {
            if (input != null) {
                input.close();
            }
        }
        robotName = props.getProperty("RobotName");
        audioDir = props.getProperty("AudioDir");
        audioFormat = props.getProperty("AudioFormat");
        XFApiId = props.getProperty("XFAPIID");
        mainGrammarID = props.getProperty("MainGrammarID");
        musicGrammarID = props.getProperty("MusicGrammarID");
        mainGrammarFile = props.getProperty("MainGrammarFile");
        musicGrammarFile = props.getProperty("MusicGrammarFile");
        ttsPerson = props.getProperty("TTSPerson");
    }

    @Bean
    public Robot robot() throws Exception {
        Robot robot = new Robot(robotName);
        robot.setCommandQueue(commandQueue());
        robot.setAudioIdx(indexFiles());
        return robot;
    }

    @Bean
    public com.klniu.xiaoyi.Configuration configuration() throws IOException {
        return new com.klniu.xiaoyi.Configuration("config.properties");
    }

    @Bean
    public CommandQueue commandQueue() throws Exception{
        CommandQueue queue = new CommandQueue(10);
        ArrayList<CommandPutter> putters = new ArrayList<>();
        //putters.add(xfCommanderPutter());
        putters.add(new StdInCommanderPutter());
        queue.setPutters(putters);
        return queue;
    }

    @Bean
    public XFCommanderPutter xfCommanderPutter() throws Exception {
        XFCommanderPutter putter = new XFCommanderPutter(xfArs(), xftts(), new SoundChecker());
        String grammarID = null;
        if (mainGrammarID == null || mainGrammarID.equals("")) {
            grammarID = putter.buildCommandsGrammer("MainGrammarID", "grammars/main_grammar.txt");
            props.setProperty("MainGrammarID", grammarID);
        }
        if (musicGrammarID == null || musicGrammarID.equals("")) {
            grammarID = putter.buildCommandsGrammer("MusicGrammarID", "grammars/music_grammar.txt");
            props.setProperty("MusicGrammarID", grammarID);
        }
        if (grammarID != null) {
            saveProps();
        }
        return putter;
    }

    @Bean
    public IndexFiles indexFiles() {
        return new IndexFiles(audioDir, new ArrayList<>(Arrays.asList(audioFormat.split(","))));
    }

    @Bean
    public XFArs xfArs() {
        return new XFArs(XFApiId);
    }

    @Bean
    public XFTTS xftts() {
        XFTTS tts = new XFTTS(XFApiId, ttsPerson);
        tts.setPerson(ttsPerson);
        return tts;
    }

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    private void saveProps() {
        try {
            // get or create the file
            File f = new File(confFile);
            OutputStream out = new FileOutputStream(f);
            // write into it
            DefaultPropertiesPersister p = new DefaultPropertiesPersister();
            p.store(props, out, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
