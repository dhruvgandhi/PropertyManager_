package com.wordpress.fruitything.propertymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddPropertyForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property_form);
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddProperty();
                //Change Back to Home Activity
                //This changes activity properly
                //Intent intent = new Intent(getBaseContext(), home.class);
                //startActivity(intent);
            }
        });

    }
    private void onAddProperty()
    {
        //Submit Form
        EditText short_locET = findViewById(R.id.short_location_card);
        EditText areaET = findViewById(R.id.area);
        EditText addressET = findViewById(R.id.address);
        EditText rentET = findViewById(R.id.rent);

        BackgroundWorker backgroundWorker = new BackgroundWorker(this,"ADD_PROPERTY");
        backgroundWorker.execute(short_locET.getText().toString(),areaET.getText().toString(),addressET.getText().toString(),rentET.getText().toString());

    }

}
