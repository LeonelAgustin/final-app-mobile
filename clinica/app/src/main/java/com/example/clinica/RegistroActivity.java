package com.example.clinica;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroActivity extends AppCompatActivity {
    Intent volver;
    EditText CorreoET,ContraseñaET;
    Button RegistrarUsuario;

    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    String email ="",password ="";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(RegistroActivity.this);
        progressDialog.setTitle("espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        CorreoET = findViewById(R.id.inCorreo);

        ContraseñaET = findViewById(R.id.inContraseña);
        RegistrarUsuario = findViewById(R.id.btnregistrar);


        RegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarDatos();
            }
        });
        ImageButton Atras = (ImageButton) findViewById(R.id.cerrarbtn);
        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(volver);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void ValidarDatos() {
        email = CorreoET.getText().toString();
        password = ContraseñaET.getText().toString();


        if(password.isEmpty()  ||email.isEmpty()){
            Toast.makeText( RegistroActivity.this,  "CAMPOS INCOMPLETOS",Toast.LENGTH_SHORT).show();

        }else if(password.length()<8 || password.length()>10){
            Toast.makeText( RegistroActivity.this,  "CONTRASEÑA INVALIDA",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText( RegistroActivity.this,  "PROCESANDO",Toast.LENGTH_SHORT).show();
            CrearCuenta();
        }
    }

    private void CrearCuenta() {
        progressDialog.setMessage("creando su cuenta...");
        progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password)

                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();

                                Log.d(TAG, "CREACION DE CUENTA EXITOSA");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);

                            } else {
                                progressDialog.dismiss();
                                Log.w(TAG, "CREACION DE CUENTA FALLIDA", task.getException());
                                Toast.makeText(RegistroActivity.this, "AUTENTICACION FALLIDA",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);

                            }
                        }
                    });
    }

    private void updateUI(FirebaseUser user) {

    }
    private void reload() { }
}