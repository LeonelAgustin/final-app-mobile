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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AreasActivity extends AppCompatActivity {
    Intent exito,back;
    Button confirmar;
    EditText especialidadET ;
    EditText horaET ;
    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String doctor = "",especialidad = "",horario = "",costo = "";

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areas);

        Button regresar = findViewById(R.id.btnRegresar);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(back);
                //Toast.makeText( AreasActivity.this,  "CITA CONFIRMADA",Toast.LENGTH_SHORT).show();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();

        confirmar = findViewById(R.id.btnConfirmar);
        especialidadET = findViewById(R.id.inEsp);


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
                                 doctor = document.getString("medico");
                                 especialidad = document.getString("especialidad");

                                 horario = document.getString("horario");
                                 costo = document.getString("costo");

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

//FALTA AGREGAR TURNO A LA BASE: Especialidad, fecha, doctor, hora y uid del usuario
            }
        });




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



