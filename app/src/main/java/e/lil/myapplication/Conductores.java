package e.lil.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Conductores extends AppCompatActivity {

    private DatabaseReference mDatabase;
    String Usuarios[]  = new String[50];
    int pos, Lenght;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductores);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Usuarios Activos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Obteniendo todos los usuarios activos
                String Activos = dataSnapshot.getValue().toString();

                //Eliminando llaves
                pos = Activos.indexOf("}");
                Activos = Activos.substring(0,pos);
                Activos = Activos.replace("{"," " );
                Lenght = Activos.length();

                //Guardando los usuarios activos en el vector Usuarios
                int i=0;
                int pi, pf;

                while(Lenght>6){
                    pi = Activos.indexOf(" ");
                    pf = Activos.indexOf("=");
                    Usuarios[i] = Activos.substring(pi+1,pf);
                    Activos = Activos.substring(pf+1);

                    Toast.makeText(Conductores.this, "Usuario " + i + ": "+ Usuarios[i], Toast.LENGTH_SHORT).show();
                    i = i+1;
                    Lenght = Activos.length();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
