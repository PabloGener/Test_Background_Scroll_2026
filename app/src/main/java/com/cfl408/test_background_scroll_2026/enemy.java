package com.cfl408.test_background_scroll_2026;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class enemy {

    private static final int STATE_FLYING_IN = 0;
    private static final int STATE_FLYING_HOVER = 1;
    private static final int STATE_FLYING_OUT = 2;

    private int state = STATE_FLYING_IN;

    private float x, y;
    private float vx, vy;

    private float hx, hy;

    private int hoverFramesLeft;

    private Bitmap sprite;

    private int width, height;

    private static final float FLY_IN_SPEED = 4f;
    private static final float FLY_OUT_SPEED = 6f;

    private static final int HOVER_FRAMES = 90;


    public enemy(Context context, int resId, int scnWidth, int scnHeight){
        sprite = BitmapFactory.decodeResource(context.getResources(), resId);
        width = sprite.getWidth();
        height = sprite.getHeight();

        x = (scnWidth - width) / 2f;
        y = -height;

        hx = (scnWidth - width) / 2f;
        hy = (scnHeight - height) / 2f;

        vx = FLY_OUT_SPEED;
        vy = 0;

    }

    public void update(){
        switch(state){
            case STATE_FLYING_IN:
            {
                float dx = hx - x;
                float dy = hy - y;
                float dist = (float) Math.hypot(dx, dy);
                if(dist <= FLY_IN_SPEED){
                    x = hx;
                    y = hy;
                    state = STATE_FLYING_HOVER;
                    hoverFramesLeft = HOVER_FRAMES;
                } else {
                    x += dx / dist * FLY_IN_SPEED;
                    y += dy / dist * FLY_IN_SPEED;
                }
            }
            break;

            case STATE_FLYING_HOVER:
            {
                hoverFramesLeft--;
                if(hoverFramesLeft <= 0){
                    state = STATE_FLYING_OUT;
                }
            }
            break;

            case STATE_FLYING_OUT: {
                x += vx;
                y += vy;

                break;
            }



        }
        public void draw(){}

        public boolean isOffScreen(){
            return x > scn || x + width < 0 || y > screenH || y + height < 0;    }

    }
    }


