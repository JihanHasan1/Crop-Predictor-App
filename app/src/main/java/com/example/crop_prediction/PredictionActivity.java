package com.example.crop_prediction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class PredictionActivity extends AppCompatActivity {

    private TextView tvDecision, tvSoybean, tvSesame, tvSunflower, tvMustard;
    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        tvSoybean = findViewById(R.id.tvSoybean);
        tvSesame = findViewById(R.id.tvSesame);
        tvSunflower = findViewById(R.id.tvSunflower);
        tvMustard = findViewById(R.id.tvMustard);
        tvDecision = findViewById(R.id.tvDecision);

        // Get all data stored in Intent
        Intent intent = getIntent();
        String receivedNitrogen = intent.getStringExtra("NitrogenData");
        String receivedPhosphorus = intent.getStringExtra("PhosphorusData");
        String receivedPotassium = intent.getStringExtra("PotassiumData");
        String receivedPH = intent.getStringExtra("PHData");
        String receivedTemperature = intent.getStringExtra("TemperatureData");
        String receivedHumidity = intent.getStringExtra("HumidityData");
        String receivedConductivity = intent.getStringExtra("ConductivityData");

        String selectedCrop;

        // Convert String to Decimal
        try {
            double Nitrogen = Double.parseDouble(receivedNitrogen);
            double Phosphorus = Double.parseDouble(receivedPhosphorus);
            double Potassium = Double.parseDouble(receivedPotassium);
            double PH = Double.parseDouble(receivedPH);
            double Temperature = Double.parseDouble(receivedTemperature);
            double Humidity = Double.parseDouble(receivedHumidity);
            double Conductivity = Double.parseDouble(receivedConductivity);

            if(Nitrogen < 10 || Phosphorus < 15 || Potassium < 15 || PH < 5.5 || Temperature < 15){
                selectedCrop = "You can't cultivate any of the four crops";
                tvDecision.setText(selectedCrop);
            }
            else{
                // Determine Suitability Score
                double Soybean_score = Nitrogen * 0.603 + Phosphorus * 0.131 + Potassium * 0.203 + PH * 0.0562;
                //System.out.println(Soybean_score);
                double Sesame_score = Nitrogen * 0.592 + Phosphorus * 0.131 + Potassium * 0.220 + PH * 0.0502;
                //System.out.println(Sesame_score);
                double Sunflower_score = Nitrogen * 0.598 + Phosphorus * 0.113 + Potassium * 0.241 + PH * 0.046;
                //System.out.println(Sunflower_score);
                double Mustard_score = Nitrogen * 0.492 + Phosphorus * 0.122 + Potassium * 0.338 + PH * 0.045;
                //System.out.println(Mustard_score);


                // Format Decimal Values
                DecimalFormat decimalFormat = new DecimalFormat("#.###");
                Soybean_score = Double.parseDouble(decimalFormat.format(Soybean_score));
                Sesame_score = Double.parseDouble(decimalFormat.format(Sesame_score));
                Sunflower_score = Double.parseDouble(decimalFormat.format(Sunflower_score));
                Mustard_score = Double.parseDouble(decimalFormat.format(Mustard_score));


                // Make Decision

                if (Soybean_score > Sesame_score && Soybean_score > Sunflower_score && Soybean_score > Mustard_score) {
                    selectedCrop = "You Should Cultivate Soybean";
                } else if (Sesame_score > Soybean_score && Sesame_score > Sunflower_score && Sesame_score > Mustard_score) {
                    selectedCrop = "You Should Cultivate Sesame";
                } else if (Sunflower_score > Soybean_score && Sunflower_score > Sesame_score && Sunflower_score > Mustard_score) {
                    selectedCrop = "You Should Cultivate Sunflower";
                } else if (Mustard_score > Soybean_score && Mustard_score > Sesame_score && Mustard_score > Sunflower_score) {
                    selectedCrop = "You Should Cultivate Mustard";
                } else {
                    selectedCrop = "Scores are equal or invalid";
                }

                tvDecision.setText(selectedCrop);

                // Set scores to Prediction Page
                tvSoybean.setText(String.valueOf(Soybean_score));
                tvSesame.setText(String.valueOf(Sesame_score));
                tvSunflower.setText(String.valueOf(Sunflower_score));
                tvMustard.setText(String.valueOf(Mustard_score));
            }
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PredictionActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}