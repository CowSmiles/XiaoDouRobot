package com.klniu.xiaoyi;

public interface CommandPutter extends Runnable {
    void put(CommandQueue commandQueue);
    // response to user
    void output(String response);
}
