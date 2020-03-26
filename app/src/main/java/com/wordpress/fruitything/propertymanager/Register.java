package com.wordpress.fruitything.propertymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerButton = findViewById(R.id.button2);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterClick();
            }
        });
    }
    void onRegisterClick()
    {
        TextView upiID = findViewById(R.id.upiIDTV);
        TextView upiName = findViewById(R.id.upiNameTV);
        TextView aadhar = findViewById(R.id.aadharcardnoTV);
        TextView passw = findViewById(R.id.passwordTV);
        BackgroundWorker backgroundWorker = new BackgroundWorker(this,"REGISTER");
        String type="login";
        if(upiID.getText().toString().matches("")|| upiName.getText().toString().matches("")||
                aadhar.getText().toString().matches("")||
                passw.getText().toString().matches(""))
        {
            Toast.makeText(this,"Cant be blank",Toast.LENGTH_LONG).show();
        }
        else{
            backgroundWorker.execute(upiID.getText().toString(),
                    upiName.getText().toString(),
                    aadhar.getText().toString(),
                    passw.getText().toString());
        }

    }
}
