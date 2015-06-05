package com.tealium.collect.testutil;

public final class Timeout {

    private volatile boolean isTimedout;
    public static final long THRESHOLD = 500;

    private Timeout() {
        this.isTimedout = false;
    }

    public boolean isTimedout() {
        return isTimedout;
    }

    public static Timeout start() {
        final Timeout t = new Timeout();
        t.startNewTimeoutThread();
        return t;
    }

    public void rerun() {
        this.isTimedout = false;
        this.startNewTimeoutThread();
    }

    private void startNewTimeoutThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(THRESHOLD);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                isTimedout = true;
            }
        }).start();
    }

}
