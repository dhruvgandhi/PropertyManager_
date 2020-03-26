package com.wordpress.fruitything.propertymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PropertyDescription extends AppCompatActivity {
    int propertyid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_description);
        Intent intent = getIntent();
        propertyid = intent.getIntExtra("com.wordpress.fruitything.PROPERTYID",0);
        FetchProperty fetchProperty = new FetchProperty();
        fetchProperty.execute(propertyid);

        Button rentButton = findViewById(R.id.button);
        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRentClick();
            }
        });
    }
    void onRentClick(){
        //Send Rent Request
        /*
        * Send Rent Req -> Accept ->
        * */
        TextView ownerTV = findViewById(R.id.ownerTV);
        new SendRequest().execute(ownerTV.getText().toString(),propertyid+"");
    }
    class SendRequest extends AsyncTask<String, Void, String>{
        private String home_url=getString(R.string.home_url);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(), s,Toast.LENGTH_LONG).show();
            super.onPostExecute(s);
        }
        @Override
        protected String doInBackground(String... strings) {
            String SEND_REQ_URL = home_url+"/rent/sendrequest.php";
            try{
                URL url = new URL(SEND_REQ_URL);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //SEND REQUEST to SERVER
                String owner = strings[0];
                String propertyid = strings[1];
                SharedPreferences sharedPref = PropertyDescription.this.getSharedPreferences(
                        getString(R.string.username), Context.MODE_PRIVATE);
                String user_name = sharedPref.getString(getString(R.string.username),"");

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("requester","UTF-8") + "=" + URLEncoder.encode(user_name,"UTF-8")
                        +"&"+URLEncoder.encode("propertyid","UTF-8")+"="+URLEncoder.encode(propertyid,"UTF-8")
                        +"&"+URLEncoder.encode("owner","UTF-8")+"="+URLEncoder.encode(owner,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                //RECEIVE RESPONSE
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line;
                while((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;


            }catch(Exception e){
                return "Cannot Send Request "+e;
            }
        }
    }
    //Fetch Description
    class FetchProperty extends AsyncTask<Integer, Void, String> {
        private String home_url=getString(R.string.home_url);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        private String onPropertyFetch(Integer[] voids) {
            String FETCH_URL = home_url+"/rent/individual_property.php";
            try{
                URL url = new URL(FETCH_URL);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //SEND REQUEST to SERVER

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("propertyid","UTF-8") + "=" + URLEncoder.encode(""+voids[0],"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //RECEIVE RESPONSE
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line;
                while((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
                //return result;
            }catch(Exception e)
            {
                return "failed " + e;
            }
        }
        @Override
        protected String doInBackground(Integer... strings) {
            return onPropertyFetch(strings);
        }

        void afterPropertyFetch(String s)
        {
            TextView rentTV = findViewById(R.id.rentTV);
            TextView rentTV2 = findViewById(R.id.rentTV2);
            TextView usernameTV = findViewById(R.id.ownerTV);
            TextView areaTV = findViewById(R.id.areaTV);
            TextView shortLocationTV = findViewById(R.id.shortLocationTV);
            TextView beds = findViewById(R.id.bathTV);
            //TextView depositTV = findViewById(R.id.depositTV);
            TextView addressTV = findViewById(R.id.addressTV);

            try{
                Log.e("JSON",s);
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                rentTV.setText("₹"+jsonObject.getInt("rent"));
                rentTV2.setText("₹"+jsonObject.getInt("rent"));
                areaTV.setText("Area: "+jsonObject.getInt("area")+" sq.ft.");
                usernameTV.setText("Owner: "+jsonObject.getString("username"));
                shortLocationTV.setText(jsonObject.getString("short_location"));
                addressTV.setText(jsonObject.getString("address"));
                beds.setText(jsonObject.getString("beds")+" beds");

            }catch(Exception e)
            {
                Toast.makeText(PropertyDescription.this,e+"",Toast.LENGTH_LONG).show();
            }
        }
        @Override
        protected void onPostExecute(String s) {
            afterPropertyFetch(s);
            super.onPostExecute(s);
        }
    }
}
