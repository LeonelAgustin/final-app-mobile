package com.example.clinica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class AtencionActivity extends AppCompatActivity {
    Intent cerrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atencion);

        ImageButton volver = findViewById(R.id.volverbtn4);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrar = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(cerrar);
            }
        });
    }
}