package com.klniu.xiaoyi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.Thread.sleep;

public class Robot {
    private static Logger logger = LogManager.getLogger(Robot.class);
    private CommandQueue commandQueue;

    private AudioPlayer audioPlayer;

    private IndexFiles audioIdx;
    private String robotName;
    private Mode context = Mode.MAIN;

    // Mode is to indicate what context robot talk with you
    private static enum Mode {
        MAIN, FINDMUSIC, FINDCHILDMUSIC, FINDGUWEN, FINDBUDDHA
    }

    public void setAudioIdx(IndexFiles audioIdx) {
        this.audioIdx = audioIdx;
    }

    public void setCommandQueue(CommandQueue commandQueue) {
        this.commandQueue = commandQueue;
    }

    @Autowired
    public void setAudioPlayer(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public Robot(String robotName) {
        this.robotName = robotName;
        try {
        } catch (Exception e) {
            logger.fatal(e);
        }
    }

    void loop() {
        CommandResult commandResult;
        try {
            while (true) {
                sleep(300);
                if (!commandQueue.isEmpty()) {
                    commandResult = commandQueue.getCommand();
                    if (commandResult != null) {
                        logger.debug("command: " + commandResult.getCommand());
                        actionPerformed(commandResult);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void actionPerformed(CommandResult command) throws Exception {
        switch (context) {
            case MAIN:
                mainAction(command);
                break;
            case FINDMUSIC:
                musicAction(command);
                break;
        }
    }

    private void mainAction(CommandResult commandResult) throws Exception {
        String command = commandResult.getCommand();
        CommandPutter putter = commandResult.getPutter();
        if (command.contains("你会干什么") || command.equals("h")) {
            // help
            System.out.println(robotName);
            putter.output(String.format("我可以做好多事情呢，比如：\n" +
                    "0. 您说：%1$s，哼个歌，我就可以唱流行歌曲了；\n" +
                    "1. 您说，%1$s，唱个儿歌，我就可以给小宝宝唱歌了呢；\n" +
                    "2. 您说，%1$s，朗诵一首古文，我就可以读好多种古文了；\n" +
                    "3. 您说，%1$s，读诵佛经，我就会很虔诚的读经给您听；", robotName));
        } else if (command.contains("哼个歌") || command.equals()) {
            putter.output("哼什么呢");
            context = Mode.FINDMUSIC;
        } else if (command.contains("唱个儿歌")) {
            putter.output("唱什么呢");
            context = Mode.FINDMUSIC;
        } else if (command.contains("朗诵一首古文")) {
            putter.output("朗诵什么呢");
            context = Mode.FINDGUWEN;
        } else if (command.contains("读诵佛经")) {
            putter.output("读诵什么呢");
            context = Mode.FINDBUDDHA;
        } else {
            putter.output("无效命令: " + command);
        }
    }

    private void musicAction(CommandResult commandResult) {
        String path = audioIdx.getFilePath(commandResult.getCommand());
        if (path != null) {
            audioPlayer.loadFile(path);
            audioPlayer.play();
        } else {
            commandResult.getPutter().output("找不到您要的音频");
        }
        // return to main mode
        context = Mode.MAIN;
    }
}

