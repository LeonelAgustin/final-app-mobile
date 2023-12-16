package com.example.clinica;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {
    Intent citas,anular,cerrar;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        user = FirebaseAuth.getInstance().getCurrentUser();

        String email = user.getEmail();

        TextView titulousuario = findViewById(R.id.saludo);

        titulousuario.setText("Bienvenido "+email);

        Button btnanular = (Button) findViewById(R.id.btnAnular);
        btnanular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anular = new Intent(getApplicationContext(), AnularActivity.class);
                startActivity(anular);
            }
        });

        Button btncitas = (Button) findViewById(R.id.btnCitas);
        btncitas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    citas = new Intent(getApplicationContext(), AreasActivity.class);
                    startActivity(citas);

                }
            });

        ImageButton Atras = (ImageButton) findViewById(R.id.cerrarbtn);
        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                cerrar = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(cerrar);
                Toast.makeText( MenuActivity.this,  "SESION CERRADA",Toast.LENGTH_SHORT).show();
            }
        });

        Button atencion = (Button) findViewById(R.id.btnAtencion);
        btncitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                citas = new Intent(getApplicationContext(), AreasActivity.class);
                startActivity(citas);

            }
        });

    }
}