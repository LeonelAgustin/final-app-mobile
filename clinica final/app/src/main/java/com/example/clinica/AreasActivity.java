package com.example.clinica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AreasActivity extends AppCompatActivity {
    Intent exito;
    EditText especialidadET ;
    EditText fechaET ;
    EditText horaET ;
    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String especialidad = "",horario = "";
    String doctorB,costoB;
//SOLO TOMO:doctor, costo
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areas);

        ImageButton regresar = findViewById(R.id.btnRegresar);//NO CONFUNDIR ImageButton con Button
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exito = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(exito);

            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();

        Button confirmar = findViewById(R.id.btnConfirmar);
        //LAS DATOS  QUE INGRESO
        especialidadET = findViewById(R.id.inEsp);
        fechaET = findViewById(R.id.inFecha);
        horaET = findViewById(R.id.inHora);

        RecyclerView recyclerViewAreas = findViewById(R.id.view_areas);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewAreas.setLayoutManager(layoutManager);

        List<AreasActivity.Area> areas = new ArrayList<>();

        db.collection("areas")
                .whereEqualTo("disponible", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                 String doctor = document.getString("medico");
                                 especialidad = document.getString("especialidad");

                                 horario = document.getString("horario");
                                 String costo = document.getString("costo");

                                Area area = new AreasActivity.Area(doctor, especialidad, horario, costo);
                                areas.add(area);
                            }
                            AreasActivity.AreaAdapter adapter = new AreasActivity.AreaAdapter(areas);
                            recyclerViewAreas.setAdapter(adapter);
                        } else {
                            Log.d(ContentValues.TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String esp = especialidadET.getText().toString();

                List<AreasActivity.Area> areas1 = new ArrayList<>();

                db.collection("areas")
                        .whereEqualTo("especialidad", esp)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                         doctorB = document.getString("medico");
                                         costoB = document.getString("costo");
                                    }
                                    cargarDoctorCosto(doctorB,costoB,esp);

                                } else {
                                    Log.d(ContentValues.TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

    }

    private void cargarDoctorCosto(String doctorB, String costoB, String esp) {
        String uid = user.getUid();
        String fe = fechaET.getText().toString();
        String hor = horaET.getText().toString();
        Cita cargar = new Cita(doctorB, esp, hor,fe,costoB,uid);
        db.collection("areas").document(fe).set(cargar);

        exito = new Intent(getApplicationContext(),MenuActivity.class);
        startActivity(exito);
        Toast.makeText( AreasActivity.this,  "CITA CONFIRMADA",Toast.LENGTH_SHORT).show();
    }

    public class Cita {
        private String doctor;
        private String especialidad;
        private String horario;
        private String fecha;
        private String costo;
        private String uid;
        public Cita(String doctor, String especialidad, String horario, String fecha, String costo,String uid) {
            this.doctor = doctor;
            this.especialidad = especialidad;

            this.horario = horario;
            this.fecha = fecha;
            this.costo = costo;
            this.uid = uid;
        }

        public String getDoctor() {
            return doctor;
        }

        public String getEspecialidad() {
            return especialidad;
        }


        public String getHorario() {
            return horario;
        }

        public String getFecha() {
            return fecha;
        }
        public String getCosto() {
            return costo;
        }
        public String getUid() {
            return uid;
        }
    }

    public class Area {
        private String especialidad;
        private String doctor;

        private String costo;
        private String horario;

        public Area() {}

        public Area(String doctor, String especialidad, String horario, String costo) {
            this.doctor = doctor;
            this.especialidad = especialidad;
            this.horario = horario;
            this.costo = costo;
        }

        public String getDoctor() {
            return "Doctor: "+doctor;
        }

        public String getEspecialidad() {
            return "Especialidad: "+especialidad;
        }


        public String getHorario() {
            return "Horario de consulta:"+horario;
        }

        public String getCosto() {
            return "Costo: "+costo;
        }
    }

    public class AreaAdapter extends RecyclerView.Adapter<AreasActivity.AreaAdapter.AreaViewholder> {
        private List<AreasActivity.Area> areaList;

        public AreaAdapter(List<Area> areaList) {
            this.areaList = areaList;
        }

        @NonNull
        @Override
        public AreasActivity.AreaAdapter.AreaViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);
            return new AreasActivity.AreaAdapter.AreaViewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AreasActivity.AreaAdapter.AreaViewholder holder, int position) {
            Area area = areaList.get(position);
            holder.bind(area);
        }

        @Override
        public int getItemCount() {
            return areaList.size();
        }

        public class AreaViewholder extends RecyclerView.ViewHolder {
            private TextView doctorTextView;
            private TextView especialidadTextView;
            private TextView costoTextView;
            private TextView horarioTextView;

            public AreaViewholder(@NonNull View itemView) {
                super(itemView);
                doctorTextView = itemView.findViewById(R.id.text_doctor);
                especialidadTextView = itemView.findViewById(R.id.text_especialidad);
                costoTextView = itemView.findViewById(R.id.text_costo);
                horarioTextView = itemView.findViewById(R.id.text_horario);
            }

            public void bind(Area area) {
                doctorTextView.setText(area.getDoctor());
                especialidadTextView.setText(area.getEspecialidad());
                costoTextView.setText(area.getCosto());
                horarioTextView.setText(area.getHorario());
            }
        }
    }
}



