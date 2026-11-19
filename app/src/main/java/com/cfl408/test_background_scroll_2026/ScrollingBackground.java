package com.cfl408.test_background_scroll_2026;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Fondo con scroll vertical infinito hacia abajo (estilo 1942).
 *
 * Uso por parte del equipo:
 *   1. Instanciar: new ScrollingBackground(context, R.drawable.fondo);
 *   2. En cada frame del game loop: background.update();
 *   3. En el draw del game loop:   background.draw(canvas);
 *
 * No gestiona su propio hilo. No dibuja nada más. No toca el resto del juego.
 */
public class ScrollingBackground {

    private Bitmap background;
    private int bgY = 0;          // offset actual del scroll
    private float speed;           // píxeles por frame
    private int screenWidth;
    private int screenHeight;
    private boolean initialized = false;

    /**
     * @param context     contexto para cargar el drawable
     * @param resId       R.drawable.tu_fondo
     * @param speedPx     velocidad en píxeles por frame (a 60fps). Ajusta a gusto.
     */
    public ScrollingBackground(Context context, int resId, float speedPx) {
        this.background = BitmapFactory.decodeResource(context.getResources(), resId);
        this.speed = speedPx;
    }

    /**
     * Llamar UNA vez cuando se conozca el tamaño de la superficie,
     * típicamente en surfaceChanged o al inicio del game loop.
     */
    public void init(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;

        // Escalar el fondo al ancho de pantalla manteniendo proporción
        int altoEscalado = (int) ((float) background.getHeight() * width / background.getWidth());
        if (background.getWidth() != width || background.getHeight() != altoEscalado) {
            background = Bitmap.createScaledBitmap(background, width, altoEscalado, true);
        }

        // Si el fondo escalado es más bajo que la pantalla, hay que repetirlo
        // más de una vez para cubrir toda la altura. Esto lo maneja draw().
        bgY = 0;
        initialized = true;
    }

    /** Llamar en cada frame del game loop. */
    public void update() {
        if (!initialized) return;

        // Scroll hacia ABAJO (el fondo baja, sensación de avanzar)
        bgY += speed;

        // Wrap: cuando la primera copia salió completamente, volvemos a 0.
        // Como draw() dibuja la imagen repetida, el salto es invisible.
        int altoFondo = background.getHeight();
        if (bgY >= altoFondo) {
            bgY -= altoFondo; // mantiene el desfase exacto sin salto
        }
    }

    /** Llamar en cada frame del game loop, ANTES de dibujar el resto del juego. */
    public void draw(Canvas canvas) {
        if (!initialized) return;

        int altoFondo = background.getHeight();
        int y = bgY - altoFondo; // empezamos una imagen "arriba" del viewport

        // Dibujamos tantas copias como hagan falta para cubrir la pantalla + 1
        while (y < screenHeight) {
            canvas.drawBitmap(background, 0, y, null);
            y += altoFondo;
        }
    }

    // --- Setters opcionales para tuning desde el resto del equipo ---

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void pause() {
        // Reservado por si el equipo quiere pausar el scroll
        // (podrían simplemente no llamar a update())
    }
}
