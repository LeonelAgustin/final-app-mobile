package com.example.clinica;

import static android.content.ContentValues.TAG;
import static com.example.clinica.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;

public class PerfilActivity extends AppCompatActivity {
    Intent cerrar;
    String providerId,uid,name,email;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_perfil);
        ImageButton Atras = (ImageButton) findViewById(R.id.volverbtn3);
        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cerrar = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(cerrar);
                Toast.makeText( PerfilActivity.this,  "NO SE CAMBIARON LOS DATOS",Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                providerId = profile.getProviderId();
                uid = profile.getUid();
                name = profile.getDisplayName();
                email = profile.getEmail();
            }
        }
        TextView usuario = findViewById(R.id.textUsuario);
        usuario.setText(name);

        TextView correo = findViewById(R.id.textEmail);
        correo.setText(email);

        Button actualizar = findViewById(R.id.btnAct);
        EditText nuevoUsuario = findViewById(R.id.inName);
        EditText nuevoCorreo = findViewById(R.id.inEmail);
        EditText nuevaContra = findViewById(R.id.inContra);
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombrenuevo = nuevoUsuario.getText().toString();
                String correonuevo = nuevoCorreo.getText().toString();
                String contranueva = nuevaContra.getText().toString();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nombrenuevo)
                        .setPhotoUri(Uri.parse("https://images.app.goo.gl/3vwszoxNWnNZjdPS7"))
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                }
                            }
                        });
                user.updateEmail(correonuevo)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User email address updated.");
                                }
                            }
                        });


                user.updatePassword(contranueva)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User password updated.");
                                }
                            }
                        });
                cerrar = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(cerrar);
                Toast.makeText( PerfilActivity.this,  "DATOS ACTUALIZADOS",Toast.LENGTH_SHORT).show();
            }
        });
        Button eliminar = findViewById(R.id.btnEliminar);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    cerrar = new Intent(getApplicationContext(),MenuActivity.class);
                                    startActivity(cerrar);
                                    Toast.makeText( PerfilActivity.this,  "CUENTA BORRADA",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}