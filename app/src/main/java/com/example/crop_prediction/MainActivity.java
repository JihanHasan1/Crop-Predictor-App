package com.example.crop_prediction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;

public class MainActivity extends AppCompatActivity {

    private EditText etNitrogen, etPhosphorus, etPotassium, etPH, etTemperature, etHumidity, etConductivity;
    private Button btnPredict, btnRefresh;
    private DatabaseReference soilDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        soilDataRef = database.getReference().child("soil_data");

        etNitrogen = findViewById(R.id.etNitrogen);
        etPhosphorus = findViewById(R.id.etPhosphorus);
        etPotassium = findViewById(R.id.etPotassium);
        etPH = findViewById(R.id.etPH);
        etTemperature = findViewById(R.id.etTemperature);
        etHumidity = findViewById(R.id.etHumidity);
        etConductivity = findViewById(R.id.etConductivity);
        btnPredict = findViewById(R.id.btnPredict);
        btnRefresh = findViewById(R.id.btnRefresh);


        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDataFromFirebase();
            }
        });

        btnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Nitrogen = etNitrogen.getText().toString();
                String Phosphorus = etPhosphorus.getText().toString();
                String Potassium = etPotassium.getText().toString();
                String PH = etPH.getText().toString();
                String Temperature = etTemperature.getText().toString();
                String Humidity = etHumidity.getText().toString();
                String Conductivity = etConductivity.getText().toString();

                if(Nitrogen.equals("-")||Phosphorus.equals("-")||Potassium.equals("-")||PH.equals("-")||Temperature.equals("-")||Humidity.equals("-")||Conductivity.equals("-")){
                    Toast.makeText(MainActivity.this, "Invalid Data !", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, PredictionActivity.class);
                    intent.putExtra("NitrogenData", Nitrogen);
                    intent.putExtra("PhosphorusData", Phosphorus);
                    intent.putExtra("PotassiumData", Potassium);
                    intent.putExtra("PHData", PH);
                    intent.putExtra("TemperatureData", Temperature);
                    intent.putExtra("HumidityData", Humidity);
                    intent.putExtra("ConductivityData", Conductivity);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    // Method to fetch data from Firebase
    private void fetchDataFromFirebase() {
        soilDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String conductivity = dataSnapshot.child("Conductivity").getValue(String.class);
                    String humidity = dataSnapshot.child("Humidity").getValue(String.class);
                    String nitrogen = dataSnapshot.child("Nitrogen").getValue(String.class);
                    String phosphorus = dataSnapshot.child("Phosphorus").getValue(String.class);
                    String potassium = dataSnapshot.child("Potassium").getValue(String.class);
                    String temperature = dataSnapshot.child("Temperature").getValue(String.class);
                    String pH = dataSnapshot.child("pH").getValue(String.class);

                    // Set fetched values to EditText fields
                    etConductivity.setText(conductivity);
                    etHumidity.setText(humidity);
                    etNitrogen.setText(nitrogen);
                    etPhosphorus.setText(phosphorus);
                    etPotassium.setText(potassium);
                    etTemperature.setText(temperature);
                    etPH.setText(pH);

                    Toast.makeText(MainActivity.this, "Data Refreshed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(MainActivity.this, "Failed to retrieve data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}