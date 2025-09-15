package main.managers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimerManager {
    private Timer timer;
    private int timeRemaining;
    private int baseTime;
    private TimerCallback callback;
    private boolean isRunning;

    public interface TimerCallback {
        void onTimeUpdate(int timeRemaining);
        void onTimeUp();
    }

    public TimerManager(int initialTime, TimerCallback callback) {
        this.baseTime = initialTime;
        this.timeRemaining = initialTime;
        this.callback = callback;
        this.isRunning = false;
        
        this.timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                callback.onTimeUpdate(timeRemaining);
                
                if (timeRemaining <= 0) {
                    stop();
                    callback.onTimeUp();
                }
            }
        });
    }

    public void start() {
        if (!isRunning) {
            timer.start();
            isRunning = true;
        }
    }

    public void stop() {
        if (isRunning) {
            timer.stop();
            isRunning = false;
        }
    }

    public void pause() {
        if (isRunning) {
            timer.stop();
            isRunning = false;
        }
    }

    public void resume() {
        if (!isRunning && timeRemaining > 0) {
            timer.start();
            isRunning = true;
        }
    }

    public void addTime(int seconds) {
        timeRemaining += seconds;
        callback.onTimeUpdate(timeRemaining);
    }

    public void adjustSpeedForRound(int round) {
        // Every 5 rounds, decrease base time by 2 seconds (minimum 5 seconds)
        int speedIncrements = (round - 1) / 5;
        int adjustedBaseTime = Math.max(5, baseTime - (speedIncrements * 2));
        
        // If we need to adjust the current timer
        if (adjustedBaseTime != baseTime) {
            boolean wasRunning = isRunning;
            stop();
            
            // Calculate ratio to maintain relative time
            double ratio = (double) timeRemaining / baseTime;
            baseTime = adjustedBaseTime;
            timeRemaining = Math.max(1, (int) (baseTime * ratio));
            
            if (wasRunning) {
                start();
            }
            
            callback.onTimeUpdate(timeRemaining);
        }
    }

    public void reset() {
        stop();
        timeRemaining = baseTime;
        callback.onTimeUpdate(timeRemaining);
    }

    public void resetToNewBase(int newBaseTime) {
        stop();
        this.baseTime = newBaseTime;
        this.timeRemaining = newBaseTime;
        callback.onTimeUpdate(timeRemaining);
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public boolean isRunning() {
        return isRunning;
    }
}