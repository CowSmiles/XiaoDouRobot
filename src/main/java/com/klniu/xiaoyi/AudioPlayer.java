package com.klniu.xiaoyi;

import ddf.minim.Minim;

/**
 * Created by klniu on 17-1-12.
 */
public class AudioPlayer {
    Minim minim = new Minim(new MinimInput());
    ddf.minim.AudioPlayer player;

    public void loadFile(String filename) {
        player = minim.loadFile(filename);
    }

    public void play() {
        if (player == null || player.isPlaying()) return;
        player.play();
    }

    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    public void pause() {
        if (player != null && player.isPlaying()) player.pause();
    }

    public void close() {
        if (player != null) player.close();
    }

    public void rewind() {
        if (player != null) player.rewind();
    }
    public void skip(int millis) {
        if (player != null) player.skip(millis);
    }
    public void loop(int num) {
        if (player != null) player.loop(num);
    }
}

