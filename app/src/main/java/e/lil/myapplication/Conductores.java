package e.lil.myapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Conductores extends AppCompatActivity implements OnMapReadyCallback {

    private DatabaseReference mDatabase;
    private GoogleMap mMap;
    String Usuarios[]  = new String[50];
    int pos, Lenght, i=0;
    Button button;
    String Usuario;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductores);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

        ///--------------------------------------------------------------------------------///

        Database();

        button = (Button) findViewById(R.id.boton);
        dialog = new Dialog(this);
    }

    private void Database() {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Usuarios Activos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                i =0;

                //Obteniendo todos los usuarios activos
                String Activos = dataSnapshot.getValue().toString();
                Lenght = Activos.length();

                if(Lenght<7){
                    Toast.makeText(Conductores.this, "No hay usuarios activos.", Toast.LENGTH_SHORT).show();
                }

                else {
                    //Eliminando llaves
                    Activos = Activos.replace("}","," );
                    Activos = Activos.replace("{"," " );
                    Lenght = Activos.length();

                    //Guardando los usuarios activos en el vector Usuarios
                    int pi=0, pf, cm;

                    while (Lenght>0) {

                        pi = Activos.indexOf(" ");
                        pf = Activos.indexOf("=");
                        cm = Activos.indexOf(",");

                        String Estado = Activos.substring(pf + 1, cm);

                        if (Estado.length() == 6) {
                            Usuarios[i] = Activos.substring(pi + 1, pf);
                            Toast.makeText(Conductores.this, "Usuario Activo: " + Usuarios[i], Toast.LENGTH_SHORT).show();
                            i = i + 1;
                        }

                        Activos = Activos.substring(cm + 1);
                        Lenght = Activos.length();

                    }

                    LocationUsers();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void LocationUsers() {
        mDatabase.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mMap.clear();

                LatLng UD = new LatLng(4.5792601, -74.1579414);
                mMap.addMarker(new MarkerOptions().position(UD).title("Universidad Distrital").
                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                int ii=0;

                while (ii < i){


                    String Latitud = dataSnapshot.child(Usuarios[ii]).child("Ubicación").child("Latitud").getValue().toString();
                    String Longitud = dataSnapshot.child(Usuarios[ii]).child("Ubicación").child("Longitud").getValue().toString();

                    double Lat = Double.parseDouble(Latitud);
                    double Lng = Double.parseDouble(Longitud);

                    LatLng user = new LatLng(Lat, Lng);
                    mMap.addMarker(new MarkerOptions().position(user).title(Usuarios[ii]));

                    ii = ii+1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng UD = new LatLng(4.5792601, -74.1579414);
        mMap.addMarker(new MarkerOptions().position(UD).title("Universidad Distrital"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UD,13));

        mMap.setMyLocationEnabled(true);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Usuario = marker.getTitle();
                button.setVisibility(View.VISIBLE);
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                button.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void Aceptar(View view) {

        mDatabase.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMap.clear();

                String Latitud = dataSnapshot.child(Usuario).child("Ubicación").child("Latitud").getValue().toString();
                String Longitud = dataSnapshot.child(Usuario).child("Ubicación").child("Longitud").getValue().toString();
                String Celular = dataSnapshot.child(Usuario).child("Celular").getValue().toString();
                String Nombre = dataSnapshot.child(Usuario).child("Nombre").getValue().toString();

                double Lat = Double.parseDouble(Latitud);
                double Lng = Double.parseDouble(Longitud);

                LatLng user = new LatLng(Lat, Lng);
                mMap.addMarker(new MarkerOptions().position(user).title(Usuario));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user,13));

                button.setText("Información");
                //button.setTextColor(Color.WHITE);
                //button.setBackgroundColor(Color.CYAN);

                TextView textView, textView1, textView2, textView3, textView4;
                Button button;

                dialog.setContentView(R.layout.window);

                textView = (TextView) dialog.findViewById(R.id.text);
                textView1 = (TextView) dialog.findViewById(R.id.text1);
                textView2 = (TextView) dialog.findViewById(R.id.text2);
                textView3 = (TextView) dialog.findViewById(R.id.text3);
                textView4 = (TextView) dialog.findViewById(R.id.text4);

                button = (Button) dialog.findViewById(R.id.close);

                textView.setText(R.string.Mensaje_2);
                textView1.setText(R.string.Tx1);
                textView2.setText(Nombre);
                textView3.setText(R.string.Tx2);
                textView4.setText(Celular);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}