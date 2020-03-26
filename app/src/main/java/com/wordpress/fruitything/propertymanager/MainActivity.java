package com.wordpress.fruitything.propertymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /*
    * THIS IS LOGIN ACTIVITY
    *
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Saved Login
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.username), Context.MODE_PRIVATE);

        String user_name = sharedPref.getString(getString(R.string.username),"");
        Toast.makeText(this,"Hi "+user_name,Toast.LENGTH_LONG).show();
        if(!user_name.matches(""))
        {
            Intent intent = new Intent(this, home.class);
            this.startActivity(intent);
        }


        //Login Process
        final Button loginButton = findViewById(R.id.loginButton);

        //new FetchExploreProperty("EXPLORE_FETCH",this,null).execute();
        //new FetchMyProperty("EXPLORE_FETCH",this,null).execute("dhruv");
        TextView username = findViewById(R.id.username_card);
        TextView password = findViewById(R.id.password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This doesn't change activity properly
                //setContentView(R.layout.activity_home);
                onLogin();
                //v.setEnabled(false);
                //This changes activity properly
                //Intent intent = new Intent(getBaseContext(), home.class);
                //startActivity(intent);
            }
        });
        Button registerButton = findViewById(R.id.registerbutton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Register.class);
                startActivity(intent);
            }
        });

    }
    private void onLogin()
    {
        TextView username = findViewById(R.id.username_card);
        TextView password = findViewById(R.id.password);
        BackgroundWorker backgroundWorker = new BackgroundWorker(this,"LOGIN");
        String type="login";
        backgroundWorker.execute(username.getText().toString(),password.getText().toString());

    }
}
