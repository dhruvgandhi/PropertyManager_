package com.wordpress.fruitything.propertymanager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class home extends AppCompatActivity {
    public ConstraintLayout loadingScreen;
    private FusedLocationProviderClient fusedLocationClient;
    private String USER_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadingScreen = findViewById(R.id.loadingScreen);
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.username), Context.MODE_PRIVATE);
        USER_NAME = sharedPref.getString(getString(R.string.username),"");



        //FOR LOCATION
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //if(myPropertyList!=null)
        //    implementMyRecyclerView();
        new FetchExploreProperty("EXPLORE_FETCH",home.this,loadingScreen).execute();

        new FetchMyProperty("EXPLORE_FETCH",home.this,loadingScreen).execute(USER_NAME);
        new FetchReqProperty("EXPLORE_FETCH",home.this,loadingScreen).execute(USER_NAME);

        new FetchPayments("EXPLORE_FETCH",home.this,loadingScreen).execute(USER_NAME);
        final SwipeRefreshLayout myProperty = findViewById(R.id.myPropertiesSRL);
        final SwipeRefreshLayout exploreProperty = findViewById(R.id.exploreSRL);
        myProperty.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new FetchMyProperty("EXPLORE_FETCH",home.this,loadingScreen).execute(USER_NAME);
                myProperty.setRefreshing(false);
            }
        });
        exploreProperty.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchExploreProperty("EXPLORE_FETCH",home.this,loadingScreen).execute();
                exploreProperty.setRefreshing(false);
            }
        });
        TabLayout tabLayout = findViewById(R.id.tabLayout2);

        //TAB CHANGE LISTENER
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition())
                {
                    case 0: //My Properties Tab
                        findViewById(R.id.myPropertoesCL).setVisibility(View.VISIBLE);
                        findViewById(R.id.requestCL).setVisibility(View.GONE);
                        findViewById(R.id.exploreSRL).setVisibility(View.GONE);
                        Log.d("DEMO","INSIDE "+tab.getText());

                        if(myPropertyList!=null)
                            //implementMyRecyclerView();
                        new FetchMyProperty("EXPLORE_FETCH",home.this,loadingScreen).execute(USER_NAME);

                        //Fetch Data
                        //new FetchProperty("EXPLORE_FETCH").execute();
                        //FetchProperty fetchProperty = new FetchProperty();
                        //fetchProperty.execute();
                        new FetchExploreProperty("EXPLORE_FETCH",home.this,loadingScreen).execute();

                        new FetchPayments("EXPLORE_FETCH",home.this,loadingScreen).execute(USER_NAME);
                        new FetchReqProperty("EXPLORE_FETCH",home.this,loadingScreen).execute(USER_NAME);

                        break;
                    case 1:
                        findViewById(R.id.myPropertoesCL).setVisibility(View.GONE);
                        findViewById(R.id.requestCL).setVisibility(View.VISIBLE);
                        findViewById(R.id.exploreSRL).setVisibility(View.GONE);
                        //Fetch Data
                        new FetchExploreProperty("EXPLORE_FETCH",home.this,loadingScreen).execute();
                        new FetchMyProperty("EXPLORE_FETCH",home.this,loadingScreen).execute(USER_NAME);

                        new FetchReqProperty("EXPLORE_FETCH",home.this,loadingScreen).execute(USER_NAME);

                        new FetchPayments("EXPLORE_FETCH",home.this,loadingScreen).execute(USER_NAME);

                        //new FetchProperty("EXPLORE_FETCH").execute();
                        //Tenant Tab
                        break;
                    case 2:
                        findViewById(R.id.myPropertoesCL).setVisibility(View.GONE);
                        findViewById(R.id.requestCL).setVisibility(View.GONE);
                        findViewById(R.id.exploreSRL).setVisibility(View.VISIBLE);
                        if(propertyList!=null)
                        {
                            //implementRecyclerView();
                        }
                        //Fetch Data
                        new FetchExploreProperty("EXPLORE_FETCH",home.this,loadingScreen).execute();
                        new FetchMyProperty("EXPLORE_FETCH",home.this,loadingScreen).execute(USER_NAME);

                        new FetchPayments("EXPLORE_FETCH",home.this,loadingScreen).execute(USER_NAME);
                        new FetchReqProperty("EXPLORE_FETCH",home.this,loadingScreen).execute(USER_NAME);

                        //new FetchProperty("EXPLORE_FETCH").execute();
                        //fetchProperty.execute();
                        //Explore Tab
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        FloatingActionButton addPropertyButton = findViewById(R.id.addPropertyButton);

        addPropertyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show Add Property Form
                //This changes activity properly
                Intent intent = new Intent(getBaseContext(), AddPropertyForm.class);
                startActivity(intent);
            }
        });


        /*
        addPropertyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                        .with(home.this)
                        .setPayeeVpa("dhruvgandhi331@okhdfcbank")
                        .setPayeeName("Dhruv Gandhi")
                        .setTransactionId("1")
                        .setTransactionRefId("1")
                        .setDescription("Demo")
                        .setAmount("20.20")
                        .build();
                easyUpiPayment.startPayment();
                easyUpiPayment.setPaymentStatusListener(new PaymentStatusListener() {
                    @Override
                    public void onTransactionCompleted(TransactionDetails transactionDetails) {
                        Toast.makeText(home.this,"Completed Tran",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionSuccess() {
                        Toast.makeText(home.this,"success",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionSubmitted() {

                    }

                    @Override
                    public void onTransactionFailed() {
                        Toast.makeText(home.this,"Failed",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionCancelled() {
                        Toast.makeText(home.this,"canceled",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onAppNotFound() {
                        Toast.makeText(home.this,"app not found",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });*/
        TabLayout tabLayout1 = findViewById(R.id.tabLayout);
        tabLayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition())
                {
                    case 0:
                        findViewById(R.id.requestsRV).setVisibility(View.VISIBLE);
                        findViewById(R.id.paymentsRV).setVisibility(View.GONE);
                        break;
                    case 1:
                        findViewById(R.id.requestsRV).setVisibility(View.GONE);
                        findViewById(R.id.paymentsRV).setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public static List<Property> propertyList;
    void implementRecyclerView()
    {

        recyclerView = findViewById(R.id.exploreRV);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true);
        adapter = new PropertyAdapter(propertyList);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)
        //String[] a={"hELLO","mELLO","TELLO"};
        recyclerView.setAdapter(adapter);
    }

    public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.MyViewHolder>{
        //Explore Property adapter
        private List<Property> propertyList;
        int i=0;
        public class MyViewHolder extends RecyclerView.ViewHolder{

            public TextView rent, short_location, username, propertyidTV;
            public Button viewButton;
            public MyViewHolder(View view){
                super(view);
                rent = view.findViewById(R.id.rent_card);
                short_location = view.findViewById(R.id.short_location_card);
                username = view.findViewById(R.id.username_card);
                viewButton = view.findViewById(R.id.viewButton);
                propertyidTV = view.findViewById(R.id.propertyIDTV);

            }

        }
        public PropertyAdapter(List<Property> propertyList)
        {
            this.propertyList = propertyList;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.property_card, parent, false);
            Log.e("Create",""+i);
            i++;
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            try{
                final Property property = propertyList.get(position);
                Log.e("POSITION",position+" "+ property.display()+" "+
                        holder.getLayoutPosition());
                holder.rent.setText("₹"+property.rent+"");
                holder.short_location.setText("\uD83D\uDCCD"+property.short_location);
                holder.username.setText("Owner: "+property.username);
                holder.propertyidTV.setText("ID:"+property.propertyid);
                holder.viewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Change Intent
                        Intent intent = new Intent(getBaseContext(), PropertyDescription.class);
                        intent.putExtra("com.wordpress.fruitything.PROPERTYID",property.propertyid);
                        startActivity(intent);
                    }
                });
                //Thread.sleep(2000);
            }
            catch(Exception e)
            {
                Log.e("Error",e+"");
            }


        }

        @Override
        public int getItemCount() {
            Log.e("SIZE",""+propertyList.size());
            return propertyList.size();
        }


    }

    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    public static List<Property> myPropertyList;
    void implementMyRecyclerView()
    {

        myRecyclerView = findViewById(R.id.myPropertiesRV);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true);
        myAdapter = new MyPropertyAdapter(myPropertyList);
        // use a linear layout manager
        myLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myLayoutManager);
        // specify an adapter (see also next example)
        //String[] a={"hELLO","mELLO","TELLO"};
        myRecyclerView.setAdapter(myAdapter);
    }

    public class MyPropertyAdapter extends RecyclerView.Adapter<MyPropertyAdapter.MyViewHolder>{
        //Explore Property adapter
        private List<Property> propertyList;
        int i=0;
        public class MyViewHolder extends RecyclerView.ViewHolder{

            public TextView rent, short_location, username,propertyidTV;
            public Button viewButton;
            public MyViewHolder(View view){
                super(view);
                rent = view.findViewById(R.id.rent_card);
                short_location = view.findViewById(R.id.short_location_card);
                username = view.findViewById(R.id.username_card);
                viewButton = view.findViewById(R.id.viewButton);
                propertyidTV = view.findViewById(R.id.propertyIDTV);

            }

        }
        public MyPropertyAdapter(List<Property> propertyList)
        {
            this.propertyList = propertyList;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.property_card, parent, false);
            Log.e("Create",""+i);
            i++;
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            try{
                final Property property = propertyList.get(position);
                Log.e("POSITION",position+" "+ property.display()+" "+
                        holder.getLayoutPosition());
                holder.rent.setText("₹"+property.rent+"");
                holder.short_location.setText("\uD83D\uDCCD"+property.short_location);
                holder.username.setText("Tenant: "+property.tenant);
                holder.viewButton.setText("Delete");
                holder.propertyidTV.setText("ID:"+property.propertyid);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.viewButton.setBackground(getDrawable(R.drawable.reject_button));
                }
                holder.viewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Change Intent
                        //Intent intent = new Intent(getBaseContext(), PropertyDescription.class);
                        //intent.putExtra("com.wordpress.fruitything.PROPERTYID",property.propertyid);
                        //startActivity(intent);
                        new DelPropertyAsync().execute(property.propertyid);
                    }
                });
                //Thread.sleep(2000);
            }
            catch(Exception e)
            {
                Log.e("Error",e+"");
            }


        }

        @Override
        public int getItemCount() {
            Log.e("SIZE",""+propertyList.size());
            return propertyList.size();
        }




    }

    //Fetch My Property
    public class FetchMyProperty extends AsyncTask<String, Void, String> {
        String activity;
        Context context;
        ConstraintLayout loadingScreen;
        public FetchMyProperty(String activity, Context context, ConstraintLayout loadingScreen)
        {
            this.activity = activity;
            this.context = context;
            this.loadingScreen = loadingScreen;
        }
        //private String home_url="http://192.168.0.3";
        private String home_url=getString(R.string.home_url);
        @Override
        protected void onPreExecute() {
            switch(activity)
            {
                case "EXPLORE_FETCH":
                    if(loadingScreen!=null)
                        loadingScreen.setVisibility(View.VISIBLE);
                    break;
            }
            super.onPreExecute();
        }
        private String onPropertyFetch(String[] voids) {
            String username = voids[0];
            String FETCH_URL = home_url+"/rent/user_property.php";
            try{
                URL url = new URL(FETCH_URL);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //SEND REQUEST to SERVER
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8") + "=" + URLEncoder.encode(username,"UTF-8");
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
        protected String doInBackground(String... strings) {
            switch (activity)
            {
                case "EXPLORE_FETCH":
                    return onPropertyFetch(strings);
            }
            return null;
        }

        void afterPropertyFetch(String s)
        {
            try{

                home.myPropertyList = new ArrayList<Property>();
                if(loadingScreen!=null)
                    loadingScreen.setVisibility(View.GONE);
                JSONArray jsonArray = new JSONArray(s);
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    home.myPropertyList.add(new Property(
                            jsonObject.getString("username"),
                            jsonObject.getString("short_location"),
                            jsonObject.getString("address"),
                            jsonObject.getInt("productid"),
                            jsonObject.getInt("rent"),
                            jsonObject.getInt("area"),
                            jsonObject.getString("tenant"),
                            jsonObject.getInt("beds")));


                }

            }catch(Exception e)
            {
                Toast.makeText(context,e+"",Toast.LENGTH_LONG).show();
            }
            //Hide Loading Bar
            //implementRecyclerView();

        }
        @Override
        protected void onPostExecute(String s) {
            switch (activity)
            {
                case "EXPLORE_FETCH":
                    afterPropertyFetch(s);
                    implementMyRecyclerView();
                    break;
            }

            super.onPostExecute(s);
        }
    }

    static String locationS="%";
    //FETCH EXPLORE PROPERTY
    public class FetchExploreProperty extends AsyncTask<String, Void, String> {
        String activity;
        Context context;
        ConstraintLayout loadingScreen;
        public FetchExploreProperty(String activity, final Context context, ConstraintLayout loadingScreen)
        {
            this.activity = activity;
            this.context = context;
            this.loadingScreen = loadingScreen;

            //Get Location
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Geocoder gcd = new Geocoder(context, Locale.getDefault());
                    try{
                        List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses.size() > 0) {
                            Log.e("Location",addresses.get(0).getLocality());
                            if(addresses.get(0).getLocality()!=null && !locationS.matches(addresses.get(0).getLocality()))
                                locationS = addresses.get(0).getLocality();

                        }
                        else {
                            // do your stuff
                        }
                    }catch(Exception e){

                    }

                }
            });

        }
        //private String home_url="http://192.168.0.3";
        private String home_url=getString(R.string.home_url);
        @Override
        protected void onPreExecute() {
            switch(activity)
            {
                case "EXPLORE_FETCH":
                    if(loadingScreen!=null)
                        loadingScreen.setVisibility(View.VISIBLE);
                    break;
            }
            super.onPreExecute();
        }
        private String onPropertyFetch(String[] voids) {
            String FETCH_URL = home_url+"/rent/api.php";
            try{
                URL url = new URL(FETCH_URL);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                SharedPreferences sharedPref = home.this.getSharedPreferences(
                        getString(R.string.username), Context.MODE_PRIVATE);
                String user_name = sharedPref.getString(getString(R.string.username),"");
                //SEND REQUEST to SERVER
                OutputStream outputStream = httpURLConnection.getOutputStream();
                if(locationS==null)
                {
                    locationS = "%";
                }
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("location","UTF-8") + "=" + URLEncoder.encode(locationS.toLowerCase(),"UTF-8")
                        +"&"+URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8");
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
                Log.e("Some Error",e+"");
                return "failed " + e;
            }
        }
        @Override
        protected String doInBackground(String... strings) {
            switch (activity)
            {
                case "EXPLORE_FETCH":
                    return onPropertyFetch(strings);
            }
            return null;
        }

        private void afterPropertyFetch(String s)
        {
            try{
                home.propertyList = new ArrayList<Property>();
                if(loadingScreen!=null)
                    loadingScreen.setVisibility(View.GONE);
                JSONArray jsonArray = new JSONArray(s);
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    home.propertyList.add(new Property(
                            jsonObject.getString("username"),
                            jsonObject.getString("short_location"),
                            jsonObject.getString("address"),
                            jsonObject.getInt("productid"),
                            jsonObject.getInt("rent"),
                            jsonObject.getInt("area"),
                            jsonObject.getString("tenant"),
                            jsonObject.getInt("beds")));


                }

            }catch(Exception e)
            {
                Toast.makeText(context,e+"",Toast.LENGTH_LONG).show();
            }
            //Hide Loading Bar
            //implementRecyclerView();

        }
        @Override
        protected void onPostExecute(String s) {
            switch (activity)
            {
                case "EXPLORE_FETCH":
                    afterPropertyFetch(s);
                    implementRecyclerView();
                    break;
            }

            super.onPostExecute(s);
        }
    }



    //FOR REQUESTS ACCEPT AND REJECT

    private RecyclerView reqRecyclerView;
    private RecyclerView.Adapter reqAdapter;
    private RecyclerView.LayoutManager reqLayoutManager;
    public static List<Request> reqList;
    void implementRequestsRecyclerView()
    {

        reqRecyclerView = findViewById(R.id.requestsRV);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true);
        reqAdapter = new ReqPropertyAdapter(reqList);
        // use a linear layout manager
        reqLayoutManager = new LinearLayoutManager(this);
        reqRecyclerView.setLayoutManager(reqLayoutManager);
        // specify an adapter (see also next example)
        //String[] a={"hELLO","mELLO","TELLO"};
        reqRecyclerView.setAdapter(reqAdapter);
    }

    public class ReqPropertyAdapter extends RecyclerView.Adapter<ReqPropertyAdapter.MyViewHolder>{
        //Explore Property adapter
        private List<Request> reqList;
        int i=0;
        public class MyViewHolder extends RecyclerView.ViewHolder{
            public TextView requesterTV, propertyidTV;
            public Button acceptButton,rejectButton;
            public MyViewHolder(View view){
                super(view);
                requesterTV = view.findViewById(R.id.requesterTV);
                propertyidTV = view.findViewById(R.id.propertyidTV);
                acceptButton = view.findViewById(R.id.acceptButton);
                rejectButton = view.findViewById(R.id.rejectButton);
            }

        }
        public ReqPropertyAdapter(List<Request> reqList)
        {
            this.reqList = reqList;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.request_card, parent, false);
            Log.e("Create",""+i);
            i++;
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            try{
                final Request request = reqList.get(position);
                holder.propertyidTV.setText(request.propertyid+"");
                holder.requesterTV.setText(request.requester);
                holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AcceptRejectRequest(request.requester).execute(1,request.requestid,request.propertyid);
                    }
                });
                holder.rejectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AcceptRejectRequest(request.requester).execute(0,request.requestid,request.propertyid);
                    }
                });
                //Thread.sleep(2000);
            }
            catch(Exception e)
            {
                Log.e("Error",e+"");
            }


        }

        @Override
        public int getItemCount() {
            Log.e("SIZE",""+reqList.size());
            return reqList.size();
        }


    }

    //Fetch My Property
    public class FetchReqProperty extends AsyncTask<String, Void, String> {
        String activity;
        Context context;
        ConstraintLayout loadingScreen;
        public FetchReqProperty(String activity, Context context, ConstraintLayout loadingScreen)
        {
            this.activity = activity;
            this.context = context;
            this.loadingScreen = loadingScreen;
        }
        private String home_url=getString(R.string.home_url);
        @Override
        protected void onPreExecute() {
            switch(activity)
            {
                case "EXPLORE_FETCH":
                    if(loadingScreen!=null)
                        loadingScreen.setVisibility(View.VISIBLE);
                    break;
            }
            super.onPreExecute();
        }
        private String onPropertyFetch(String[] voids) {
            String owner = voids[0];
            String FETCH_URL = home_url+"/rent/getrequest.php";
            try{
                URL url = new URL(FETCH_URL);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //SEND REQUEST to SERVER
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("owner","UTF-8") + "=" + URLEncoder.encode(owner,"UTF-8");
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
        protected String doInBackground(String... strings) {
            switch (activity)
            {
                case "EXPLORE_FETCH":
                    return onPropertyFetch(strings);
            }
            return null;
        }

        void afterPropertyFetch(String s)
        {
            Log.e("Fetched",s);
            Toast.makeText(context,s,Toast.LENGTH_LONG);
            try{

                home.reqList = new ArrayList<Request>();
                if(loadingScreen!=null)
                    loadingScreen.setVisibility(View.GONE);
                JSONArray jsonArray = new JSONArray(s);
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    home.reqList.add(new Request(
                            jsonObject.getInt("requestid"),
                            jsonObject.getInt("propertyid"),
                            jsonObject.getBoolean("status"),
                            jsonObject.getString("owner"),
                            jsonObject.getString("requester")
                            ));
                }

            }catch(Exception e)
            {
                Toast.makeText(context,e+"",Toast.LENGTH_LONG).show();
            }
            //Hide Loading Bar
            //implementRecyclerView();

        }
        @Override
        protected void onPostExecute(String s) {
            switch (activity)
            {
                case "EXPLORE_FETCH":
                    afterPropertyFetch(s);
                    implementRequestsRecyclerView();
                    break;
            }

            super.onPostExecute(s);
        }
    }
    class AcceptRejectRequest extends AsyncTask<Integer, Void, String>{
        String requester;
        AcceptRejectRequest(String requester)
        {
            this.requester = requester;
        }
        private String home_url=getString(R.string.home_url);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(home.this, s,Toast.LENGTH_LONG).show();
            super.onPostExecute(s);
        }
        @Override
        protected String doInBackground(Integer... strings) {
            String SEND_REQ_URL = home_url+"/rent/accept_reject_request.php";
            try{
                URL url = new URL(SEND_REQ_URL);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //SEND REQUEST to SERVER
                Integer isAccepted = strings[0];
                Integer requestid = strings[1];
                Integer propertyid = strings[2];
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("accepted","UTF-8") + "=" + URLEncoder.encode(isAccepted+"","UTF-8")
                        +"&"+URLEncoder.encode("requestid","UTF-8")+"="+URLEncoder.encode(requestid+"","UTF-8")
                        +"&"+URLEncoder.encode("tenant","UTF-8")+"="+URLEncoder.encode(requester,"UTF-8")
                        +"&"+URLEncoder.encode("propertyid","UTF-8")+"="+URLEncoder.encode(propertyid+"","UTF-8");
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
    class DelPropertyAsync extends AsyncTask<Integer, Void, String>{
        private String home_url=getString(R.string.home_url);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(home.this, s,Toast.LENGTH_LONG).show();
            super.onPostExecute(s);
        }
        @Override
        protected String doInBackground(Integer... strings) {
            String SEND_REQ_URL = home_url+"/rent/deleteproperty.php";
            try{
                URL url = new URL(SEND_REQ_URL);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //SEND REQUEST to SERVER\
                Integer propertyid = strings[0];
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("propertyid","UTF-8")+"="+URLEncoder.encode(propertyid+"","UTF-8");
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

    class DonePaymentAsync extends AsyncTask<Integer, Void, String>{
        private String home_url=getString(R.string.home_url);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(home.this, s,Toast.LENGTH_LONG).show();
            super.onPostExecute(s);
        }
        @Override
        protected String doInBackground(Integer... strings) {
            String SEND_REQ_URL = home_url+"/rent/donepayment.php";
            try{
                URL url = new URL(SEND_REQ_URL);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //SEND REQUEST to SERVER\
                Integer paymentid = strings[0];
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("paymentid","UTF-8")+"="+URLEncoder.encode(paymentid+"","UTF-8");
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







    //PAYMENTS FETCH
    //FOR REQUESTS ACCEPT AND REJECT

    private RecyclerView payRecyclerView;
    private RecyclerView.Adapter payAdapter;
    private RecyclerView.LayoutManager payLayoutManager;
    public static List<Payment> payList;
    void implementPayRecyclerView()
    {

        payRecyclerView = findViewById(R.id.paymentsRV);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true);
        payAdapter = new PayPropertyAdapter(payList);
        // use a linear layout manager
        payLayoutManager = new LinearLayoutManager(this);
        payRecyclerView.setLayoutManager(payLayoutManager);
        // specify an adapter (see also next example)
        //String[] a={"hELLO","mELLO","TELLO"};
        payRecyclerView.setAdapter(payAdapter);
    }

    public class PayPropertyAdapter extends RecyclerView.Adapter<PayPropertyAdapter.MyViewHolder>{
        //Explore Property adapter
        private List<Payment> payList;
        int i=0;
        public class MyViewHolder extends RecyclerView.ViewHolder{
            public TextView pay_amountTV, pay_prop_idTV, pay_date_TV;
            public Button pay_rent_button;
            //public TextView requesterTV, propertyidTV;
            //public Button acceptButton,rejectButton;
            public MyViewHolder(View view){
                super(view);
                pay_amountTV = view.findViewById(R.id.pay_amountTV);
                pay_date_TV = view.findViewById(R.id.pay_dateTV);
                pay_prop_idTV = view.findViewById(R.id.pay_prop_idTV);
                pay_rent_button = view.findViewById(R.id.pay_rent_button);
            }

        }
        public PayPropertyAdapter(List<Payment> payList)
        {
            this.payList = payList;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.payment_card, parent, false);
            Log.e("Create",""+i);
            i++;
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            try{
                final Payment payment = payList.get(position);
                holder.pay_prop_idTV.setText("For Property: "+payment.propertyid);
                holder.pay_date_TV.setText("Due: "+payment.date);
                holder.pay_amountTV.setText("₹"+payment.amount);
                holder.pay_rent_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("VPA",payment.upi_id);
                        final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                                .with(home.this)
                                .setPayeeVpa(payment.upi_id)
                                .setPayeeName(payment.upi_name)
                                .setTransactionId(""+payment.txid)
                                .setTransactionRefId(""+payment.txid)
                                .setDescription("Paying for"+payment.propertyid)
                                .setAmount(payment.amount+".00")
                                .build();
                        easyUpiPayment.startPayment();
                        easyUpiPayment.setPaymentStatusListener(new PaymentStatusListener() {
                            @Override
                            public void onTransactionCompleted(TransactionDetails transactionDetails) {
                                Toast.makeText(home.this,"Completed Tran",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onTransactionSuccess() {
                                Toast.makeText(home.this,"success",Toast.LENGTH_LONG).show();
                                new DonePaymentAsync().execute(payment.txid);
                            }

                            @Override
                            public void onTransactionSubmitted() {

                            }

                            @Override
                            public void onTransactionFailed() {
                                Toast.makeText(home.this,"Failed",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onTransactionCancelled() {
                                Toast.makeText(home.this,"canceled",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onAppNotFound() {
                                Toast.makeText(home.this,"app not found",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
            catch(Exception e)
            {
                Log.e("Error",e+"");
            }


        }

        @Override
        public int getItemCount() {
            Log.e("SIZE",""+payList.size());
            return payList.size();
        }


    }

    //Fetch My Property
    public class FetchPayments extends AsyncTask<String, Void, String> {
        String activity;
        Context context;
        ConstraintLayout loadingScreen;
        public FetchPayments(String activity, Context context, ConstraintLayout loadingScreen)
        {
            this.activity = activity;
            this.context = context;
            this.loadingScreen = loadingScreen;
        }
        private String home_url=getString(R.string.home_url);
        @Override
        protected void onPreExecute() {
            switch(activity)
            {
                case "EXPLORE_FETCH":
                    if(loadingScreen!=null)
                        loadingScreen.setVisibility(View.VISIBLE);
                    break;
            }
            super.onPreExecute();
        }
        private String onPaymentFetch(String[] voids) {
            String tenant = voids[0];
            String FETCH_URL = home_url+"/rent/getpayments.php";
            try{
                URL url = new URL(FETCH_URL);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //SEND REQUEST to SERVER
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("tenant","UTF-8") + "=" + URLEncoder.encode(tenant,"UTF-8");
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
        protected String doInBackground(String... strings) {
            switch (activity)
            {
                case "EXPLORE_FETCH":
                    return onPaymentFetch(strings);
            }
            return null;
        }

        void afterPaymentFetch(String s)
        {
            Log.e("Fetched",s);
            Toast.makeText(context,s,Toast.LENGTH_LONG);
            try{

                home.payList = new ArrayList<Payment>();
                if(loadingScreen!=null)
                    loadingScreen.setVisibility(View.GONE);
                JSONArray jsonArray = new JSONArray(s);
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    home.payList.add(new Payment(jsonObject.getInt("txid"),
                            jsonObject.getInt("amount"),
                            jsonObject.getString("tenant"),
                            jsonObject.getString("date"),
                            jsonObject.getString("upi_id"),
                            jsonObject.getString("upi_name"),
                            jsonObject.getInt("successful"),
                            jsonObject.getInt("propertyid")));

                }

            }catch(Exception e)
            {
                Toast.makeText(context,e+"",Toast.LENGTH_LONG).show();
            }
            //Hide Loading Bar
            //implementRecyclerView();

        }
        @Override
        protected void onPostExecute(String s) {
            switch (activity)
            {
                case "EXPLORE_FETCH":
                    afterPaymentFetch(s);
                    implementPayRecyclerView();
                    break;
            }

            super.onPostExecute(s);
        }
    }

}
