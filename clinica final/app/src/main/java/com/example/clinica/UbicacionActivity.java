package com.example.clinica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class UbicacionActivity extends AppCompatActivity {
    Intent cerrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);
        ImageButton volver = findViewById(R.id.cerrarbtn);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrar = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(cerrar);
            }
        });
    }
}