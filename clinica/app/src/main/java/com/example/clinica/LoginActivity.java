package com.example.clinica;

import static android.service.controls.ControlsProviderService.TAG;

import static com.example.clinica.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.nullness.qual.NonNull;

public class LoginActivity extends AppCompatActivity {
    Intent registrar,menu;
    EditText incorreo ;
    EditText incontraseña ;
    String email1="",password1="";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Firebase Auth

        super.onCreate(savedInstanceState);
        setContentView(layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        incorreo = findViewById(id.inCorreo);
        incontraseña= findViewById(id.inContraseña);
        Button ingresarUsuario=findViewById(id.ingresarbtn);

        Button registrobtn = (Button) findViewById(id.registrobtn);

        registrobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(registrar);
            }
        });



        ingresarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//DEBE ESTAR DENTRO DEL EVENTO "iniciar sesion"
                email1 = incorreo.getText().toString();
                password1 = incontraseña.getText().toString();
                 if(password1.isEmpty() ||email1.isEmpty() ){
                    Toast.makeText( LoginActivity.this,  "CAMPOS INCOMPLETOS",Toast.LENGTH_SHORT).show();
                }else if(password1.length()<8 || password1.length()>10){
                    Toast.makeText( LoginActivity.this,  "CONTRASEÑA INVALIDA",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText( LoginActivity.this,  "PROCESANDO",Toast.LENGTH_SHORT).show();
                    signIn(email1,password1);
                }

            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            menu = new Intent(getApplicationContext(), MenuActivity.class);
                            startActivity(menu);
                        } else {

                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
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