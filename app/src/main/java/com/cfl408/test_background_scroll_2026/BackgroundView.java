package com.cfl408.test_background_scroll_2026;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BackgroundView extends SurfaceView implements SurfaceHolder.Callback {

    // --- State that must survive across frames (class fields!) ---
    private Bitmap background;      // the image
    private int bgY = 0;            // current scroll offset
    private int screenW, screenH;   // size of the surface
    private boolean scrolling = true;
    private float speed = 3f;       // pixels per frame

    // --- The thread that runs the loop ---
    private RenderThread thread;

    public BackgroundView(Context context) {
        super(context);
        getHolder().addCallback(this);   // so we get surfaceCreated/Changed
    }

    public BackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    // ← AND optionally this (used if you ever style it from a theme)
    public BackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    // ============================================================
    // STEP 1: surface exists. Surface size is NOT known yet here.
    // ============================================================
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // nothing to do
    }

    // ============================================================
    // STEP 2: surface size is known. This is where everything
    //         gets initialized and the loop starts.
    // ============================================================
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        screenW = width;
        screenH = height;

        // Load the image and scale it to screen width
        background = BitmapFactory.decodeResource(getResources(), R.drawable.fondo);
        int scaledH = (int) ((float) background.getHeight() * width / background.getWidth());
        background = Bitmap.createScaledBitmap(background, width, scaledH, true);

        // Create and start the loop
        thread = new RenderThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    // ============================================================
    // STEP 3: surface is going away. Stop the loop cleanly.
    // ============================================================
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.setRunning(false);
        try { thread.join(); } catch (InterruptedException ignored) {}
    }

    // ============================================================
    // Called by the thread ~60 times per second
    // ============================================================
    void update() {
        if (!scrolling) return;
        bgY += (int) speed;
        if (bgY >= background.getHeight()) {
            bgY -= background.getHeight();
        }
    }

    // ============================================================
    // Called by the thread ~60 times per second, with the Canvas
    // ============================================================
    void drawFrame(Canvas canvas) {
        canvas.drawColor(0xFF000000);
        int h = background.getHeight();
        int y = bgY - h;
        while (y < screenH) {
            canvas.drawBitmap(background, 0, y, null);
            y += h;
        }
    }

    public boolean isScrolling() { return scrolling; }
    public void setScrolling(boolean s) { scrolling = s; }
}