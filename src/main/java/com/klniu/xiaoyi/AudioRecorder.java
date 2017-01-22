package com.klniu.xiaoyi;

import ddf.minim.Minim;
import org.springframework.stereotype.Component;

@Component
public class AudioRecorder {
    Minim minim = new Minim(new MinimInput());
    ddf.minim.AudioRecorder recorder;

    private AudioRecorder() {}

    private AudioRecorder(String filename){
        // make rate is 8000, bitDepth is 16
        recorder = minim.createRecorder(minim.getLineIn(minim.MONO, 2048,
                8000f, 16), filename);
    }

    public static AudioRecorder createRecorder(String filename) {
        return new AudioRecorder(filename);
    }

    public void beginRecord() {
        if (recorder==null || recorder.isRecording()) return;
        recorder.beginRecord();
    }

    public void endRecord() {
        if (recorder!=null && recorder.isRecording()) recorder.endRecord();
    }

    public void save() {
        if (recorder!=null) recorder.save();
    }
}
