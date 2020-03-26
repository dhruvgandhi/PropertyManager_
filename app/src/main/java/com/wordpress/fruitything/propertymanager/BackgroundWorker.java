package com.wordpress.fruitything.propertymanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundWorker extends AsyncTask<String,Void,String> {
    private Context context;
    private AlertDialog alertDialog;
    private String activity;
    private String home_url;
    String username;
    BackgroundWorker(Context context, String a)
    {
        this.context = context;
        home_url = context.getString(R.string.home_url);
        this.activity = a;
    }
    private String onLogin(String... voids)
    {
        //String type = voids[0];
        username = voids[0];
        String password = voids[1];
        String login_url = home_url+"/rent/login.php";
        //if(type.equals("login"))
        {
            try{
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //SEND REQUEST to SERVER
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8") + "=" + URLEncoder.encode(username,"UTF-8") +
                        "&"+URLEncoder.encode("passw","UTF-8") + "=" + URLEncoder.encode(password,"UTF-8");
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
                return "Some Error" + e;
                //e.printStackTrace();
            }

        }
        //return null;
    }

    private String onRegister(String... voids)
    {
        //String type = voids[0];
        String upiId = voids[0];
        String upiName = voids[1];
        username = voids[2];
        String password = voids[3];
        String reg_url = home_url+"/rent/register.php";
        //if(type.equals("login"))
        {
            try{
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //SEND REQUEST to SERVER
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("username","UTF-8") + "=" + URLEncoder.encode(username,"UTF-8") +
                        "&"+URLEncoder.encode("passw","UTF-8") + "=" + URLEncoder.encode(password,"UTF-8")
                        +"&"+URLEncoder.encode("upi_id","UTF-8") + "=" + URLEncoder.encode(upiId,"UTF-8")
                        +"&"+URLEncoder.encode("upi_name","UTF-8") + "=" + URLEncoder.encode(upiName,"UTF-8");
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
                return "Some Error" + e;
                //e.printStackTrace();
            }

        }
        //return null;
    }
    private String onPropertyAdd(String... voids)
    {
        String short_location = voids[0];
        String area = voids[1];
        String address = voids[2];
        String rent = voids[3];
        String property_url = home_url+"/rent/property.php";
        try
        {
            URL url = new URL(property_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.username), Context.MODE_PRIVATE);
            String user_name = sharedPref.getString(context.getString(R.string.username),"");
            //SEND REQUEST to SERVER
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String post_data = URLEncoder.encode("short_location","UTF-8") + "=" + URLEncoder.encode(short_location,"UTF-8") +
                    "&"+URLEncoder.encode("area","UTF-8") + "=" + URLEncoder.encode(area,"UTF-8")+
                    "&"+URLEncoder.encode("address","UTF-8") + "=" + URLEncoder.encode(address,"UTF-8")+
                    "&"+URLEncoder.encode("rent","UTF-8") + "=" + URLEncoder.encode(rent,"UTF-8")+
                    "&"+URLEncoder.encode("username","UTF-8") + "=" + URLEncoder.encode(user_name,"UTF-8");
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
        }catch (Exception e)
        {
            return "Some Error" + e;
            //e.printStackTrace();
        }
        //return "Some Error ";
    }


    @Override
    protected String doInBackground(String... voids) {
        switch(activity)
        {
            case "LOGIN":
                return onLogin(voids);
                //break;
            case "ADD_PROPERTY":
                return onPropertyAdd(voids);
            case "REGISTER":
                return onRegister(voids);

                //break;

        }
        return null;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
        //super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
        //START HOME ACTIVITY
        if(!result.contains("failed")){
            if(activity.matches("LOGIN") || activity.matches("REGISTER"))
            {
                SharedPreferences sharedPref = context.getSharedPreferences(
                        context.getString(R.string.username), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(context.getString(R.string.username),username);
                editor.commit();
            }
            Intent intent = new Intent(context, home.class);
            context.startActivity(intent);
        }
        //super.onPostExecute(result);
    }
}
