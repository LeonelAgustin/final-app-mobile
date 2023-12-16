package com.example.clinica;

import static android.content.ContentValues.TAG;

import static com.example.clinica.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class AnularActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    Intent volver;

    EditText fechaBorrar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anular);

        String uid = user.getUid();

        RecyclerView recyclerViewCitas = findViewById(R.id.view_citas);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewCitas.setLayoutManager(layoutManager);

        List<Cita> citas = new ArrayList<>();

        db.collection("areas")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String doctor = document.getString("doctor");
                                String especialidad = document.getString("especialidad");
                                String costo = document.getString("costo");
                                String horario = document.getString("horario");
                                String fecha = document.getString("fecha");

                                Cita cita = new Cita(doctor, especialidad, horario, fecha,costo);
                                citas.add(cita);
                            }
                            CitaAdapter adapter = new CitaAdapter(citas);
                            recyclerViewCitas.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        Button borrar = findViewById(id.btnBorrar);
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechaBorrar = findViewById(id.inFechaAnular);
                String fecha = fechaBorrar.getText().toString();
                db.collection("areas").document(fecha)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                volver = new Intent(getApplicationContext(),MenuActivity.class);
                                startActivity(volver);
                                Toast.makeText( AnularActivity.this,  "CITA ANULADA",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                                Toast.makeText( AnularActivity.this,  "ERROR NO SE PUDO ANULAR CITA",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        ImageButton back = findViewById(id.btnback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(volver);
            }
        });
    }

    public class Cita {
        private String doctor;
        private String especialidad;

        private String horario;
        private String fecha;
        private String costo;

        public Cita() {}

        public Cita(String doctor, String especialidad, String horario, String fecha, String costo) {
            this.doctor = doctor;
            this.especialidad = especialidad;

            this.horario = horario;
            this.fecha = fecha;
            this.costo = costo;
        }

        public String getDoctor() {
            return "Doctor: "+doctor;
        }

        public String getEspecialidad() {
            return "Especialidad: "+especialidad;
        }


        public String getHorario() {
            return "Hora de consulta: "+horario;
        }

        public String getFecha() {
            return "Fecha: "+fecha;
        }
        public String getCosto() {
            return "Costo : "+costo+" USSD";
        }
    }

    public class CitaAdapter extends RecyclerView.Adapter<CitaAdapter.CitaViewHolder> {
        private List<Cita> citaList;

        public CitaAdapter(List<Cita> citaList) {
            this.citaList = citaList;
        }

        @NonNull
        @Override
        public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cita, parent, false);
            return new CitaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {
            Cita cita = citaList.get(position);
            holder.bind(cita);
        }

        @Override
        public int getItemCount() {
            return citaList.size();
        }

        public class CitaViewHolder extends RecyclerView.ViewHolder {
            private TextView doctorTextView;
            private TextView especialidadTextView;
            private TextView nombreTextView;
            private TextView horarioTextView;
            private TextView costoTextView;

            public CitaViewHolder(@NonNull View itemView) {
                super(itemView);
                doctorTextView = itemView.findViewById(R.id.text_view_doctor);
                especialidadTextView = itemView.findViewById(R.id.text_view_especialidad);
                nombreTextView = itemView.findViewById(R.id.text_view_nombre);
                horarioTextView = itemView.findViewById(R.id.text_view_horario);
                costoTextView = itemView.findViewById(id.text_view_costo);
            }

            public void bind(Cita cita) {
                doctorTextView.setText(cita.getDoctor());
                especialidadTextView.setText(cita.getEspecialidad());
                nombreTextView.setText(cita.getFecha());
                horarioTextView.setText(cita.getHorario());
                costoTextView.setText(cita.getCosto());
            }
        }
    }
}


