package com.cfl408.test_background_scroll_2026;


import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class RenderThread extends Thread {

    private final SurfaceHolder holder;
    private final BackgroundView view;
    private volatile boolean running;

    public RenderThread(SurfaceHolder holder, BackgroundView view) {
        this.holder = holder;
        this.view = view;
    }

    public void setRunning(boolean r) { this.running = r; }

    @Override
    public void run() {
        while (running) {
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();   // get a Canvas
                if (canvas != null) {
                    synchronized (holder) {
                        view.update();          // move the background
                        view.drawFrame(canvas); // paint it
                    }
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas); // show it
                }
            }

            // ~60 FPS
            try { Thread.sleep(16); } catch (InterruptedException ignored) {}
        }
    }
}
