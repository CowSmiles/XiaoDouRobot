package com.klniu.xiaoyi;

import com.klniu.xiaoyi.XFYuYin.XFArs;
import com.klniu.xiaoyi.XFYuYin.XFTTS;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.Thread.sleep;

/**
 * 命令队列是一个阻塞命令队列管理器，可以通过设置命令的制造者来填充命令，通过方法来获取命令。
 */
public class CommandQueue {
    private ArrayBlockingQueue<CommandResult> commands;
    private List<CommandPutter> putters;

    public void setPutters(List<CommandPutter> putters) {
        this.putters = putters;
        for (CommandPutter putter : putters) {
            putter.put(this);
        }
    }

    public CommandQueue(int capacity) {
        commands = new ArrayBlockingQueue<>(capacity);
    }

    public CommandResult getCommand() {
        return commands.poll();
    }

    public void putCommand(CommandResult result) throws InterruptedException {
        commands.put(result);
    }

    public boolean isEmpty() {
        return commands.isEmpty();
    }

}

class CommandResult {
    private String command;
    private CommandPutter putter;

    public CommandResult(String command, CommandPutter putter) {
        this.command = command;
        this.putter = putter;
    }

    public String getCommand() {
        return command;
    }

    public CommandPutter getPutter() {
        return putter;
    }
}

/**
 * a commander putter according xunfei ars.
 */
class XFCommanderPutter implements CommandPutter {
    private CommandQueue queue;
    private XFArs ars;
    private XFTTS tts;
    private String grammarID;
    private SoundChecker soundChecker;

    public XFCommanderPutter(XFArs ars, XFTTS tts, SoundChecker soundChecker) {
        this.ars = ars;
        this.tts = tts;
        this.soundChecker = soundChecker;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(300);
                if (soundChecker.hasSound()) {
                    ars.startListening(grammarID);
                    while (true) {
                        sleep(300);
                        if (!soundChecker.hasSound()) {
                            String command = ars.stopListening();
                            queue.putCommand(new CommandResult(command, this));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        }

    }

    @Override
    public void put(CommandQueue commandQueue) {
        this.queue = commandQueue;
        new Thread(this).start();
    }

    @Override
    public void output(String response) {
        tts.startSpeaking(response);
    }

    public String buildCommandsGrammer(String id, String filename) throws Exception {
        String contents, grammarId = null;
        URL input = getClass().getClassLoader().getResource(filename);
        contents = new String(Files.readAllBytes(Paths.get(input.toURI())));
        if (ars.buildGrammar(contents)) {
            while (ars.getmGrammarId() == null) {
                sleep(1000);
            }
            grammarId = ars.getmGrammarId();
        }
        return grammarId;
    }

    public void setGrammarID(String grammarID) {
        this.grammarID = grammarID;
    }
}

/**
 * a commander putter accoring stardard input.
 */
@Component
class StdInCommanderPutter implements CommandPutter {
    private CommandQueue queue;

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        String line;
        System.out.print("Insert Command:");
        while (sc.hasNextLine()) {
            try {
                line = sc.nextLine();
                System.out.print("Insert Command:");
                queue.putCommand(new CommandResult(line, this));
            } catch (Exception e) {
                System.out.print(e);
            }
        }
    }

    @Override
    public void output(String response) {
        System.out.println(response);
    }

    @Override
    public void put(CommandQueue commandQueue) {
        this.queue = commandQueue;
        new Thread(this).start();
    }
}
