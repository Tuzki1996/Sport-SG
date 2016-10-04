package com.example.chen_hsi.androidtutnonfregment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class HistoryActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    ListView list;
    TextView date;
    TextView facility;
    TextView payment;
    //Button Btngetdata;
    ArrayList<HashMap<String, String>> historylist = new ArrayList<HashMap<String, String>>();

    //URL to get JSON Array
    private static String url = "http://hsienyan.pagekite.me:8080/CZ2006/getUserServlet?requestType=login&email=red";

    //JSON Node Names
    private static final String TAG_OS = "android";
    private static final String TAG_DATE = "ver";
    private static final String TAG_FACILITY = "name";
    private static final String TAG_PAYMENT = "api";

    JSONArray history = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);
        setMenu();
        displayHistoryList();
        setNavigation();






    }


    private void setNavigation(){
        navigationView=(NavigationView)findViewById(R.id.left_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                Intent navigate=new Intent();

                switch (item.getItemId())
                {
                    case R.id.mHome:
                        break;
                    case R.id.mBook:
                        navigate.setClass(HistoryActivity.this,BookingActivity.class);
                        startActivity(navigate);
                        break;

                    case R.id.mHistory:
                        break;

                    case R.id.mSearch:
                        navigate.setClass(HistoryActivity.this,SearchActivity.class);
                        startActivity(navigate);
                        break;
                    case R.id.mLogin:
                        navigate.setClass(HistoryActivity.this,LoginActivity.class);
                        startActivity(navigate);
                        break;
                    case R.id.mRegister:
                        navigate.setClass(HistoryActivity.this,RegisterActivity.class);
                        startActivity(navigate);
                        break;
                }
                return false;
            }

        });


    }

    private void setMenu(){

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar=getSupportActionBar();

        actionBar.setElevation((float) 2.5);
        actionBar.setTitle("Booking History");
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,myToolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }


    private void displayHistoryList(){
        historylist = new ArrayList<HashMap<String, String>>();

        //new JSONParse().execute();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            Log.e("DEBUG!!!!!","1" );
            super.onPreExecute();
            date = (TextView)findViewById(R.id.tDate);
            facility= (TextView)findViewById(R.id.tFacility);
            payment = (TextView)findViewById(R.id.tPayment);
            pDialog = new ProgressDialog(HistoryActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);


            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                Log.e("DEBUG!!!!!","3" );
                // Getting JSON Array from URL
                history = json.getJSONArray(TAG_OS);
                for(int i = 0; i < history.length(); i++){
                    JSONObject c = history.getJSONObject(i);

                    // Storing  JSON item in a Variable
                    String date = "Date: "+c.getString(TAG_DATE);
                    String facility = "Faclilty: "+c.getString(TAG_FACILITY);
                    String payment = "Payment: "+c.getString(TAG_PAYMENT);

                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_DATE, date);
                    map.put(TAG_FACILITY, facility);
                    map.put(TAG_PAYMENT, payment);

                    historylist.add(map);
                    list=(ListView)findViewById(R.id.historyList);

                    ListAdapter adapter = new SimpleAdapter(HistoryActivity.this, historylist,
                            R.layout.history_list,
                            new String[] { TAG_DATE,TAG_FACILITY, TAG_PAYMENT }, new int[] {
                            R.id.tDate,R.id.tFacility, R.id.tPayment});

                    list.setAdapter(adapter);
                   /* list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Toast.makeText(HistoryActivity.this, "You Clicked at "+historylist.get(+position).get("name"), Toast.LENGTH_SHORT).show();

                        }
                    });*/

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
