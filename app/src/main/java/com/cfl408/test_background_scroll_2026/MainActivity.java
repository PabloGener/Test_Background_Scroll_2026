package com.cfl408.test_background_scroll_2026;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BackgroundView fondo = findViewById(R.id.backgroundView);
        Button boton = findViewById(R.id.btnToggle);

        boton.setOnClickListener(v -> {
            boolean nowScrolling = !fondo.isScrolling();
            fondo.setScrolling(nowScrolling);
            boton.setText(nowScrolling ? "STOP" : "START");

        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void scrollear(View vista) {



    }


}