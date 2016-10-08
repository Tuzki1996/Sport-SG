package com.example.chen_hsi.androidtutnonfregment;


import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    Account newaccount;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    String emailCheckUrl;
    String registerUrl;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setMenu();
        setNavigation();
        final EditText username = (EditText) findViewById(R.id.registeremail);
        final EditText password = (EditText) findViewById(R.id.registerpassword);
        final EditText firstname = (EditText) findViewById(R.id.first_name);
        final EditText lastname=(EditText)findViewById(R.id.last_name);
        final EditText contact=(EditText)findViewById(R.id.contact);

        Button submitregister = (Button) findViewById(R.id.submitregister);





        submitregister.setOnClickListener(new Button.OnClickListener() {




            public void onClick(View view) {
                String usernameString = username.getText().toString();
                String passwordString = password.getText().toString();
                String firstnameString=firstname.getText().toString();
                String lastnameString=lastname.getText().toString();
                String contactString=contact.getText().toString();
                if (validation() == true) {

                    registerUrl="http://hsienyan.pagekite.me:8080/CZ2006/getUserServlet?requestType=register&email="+usernameString+"&password="+passwordString+"&firstname="+firstnameString+"&lastname="+lastnameString+"&contact="+contactString;
                    Log.d("registerUrl",registerUrl);
                    new RegisterJSONParse().execute(registerUrl);

                }
            }
        });
    }





    private void setMenu(){

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar=getSupportActionBar();



        actionBar.setElevation((float) 2.5);
        actionBar.setTitle("REGISTER");
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,myToolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    private void setNavigation() {
        navigationView = (NavigationView) findViewById(R.id.left_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent navigate = new Intent();

                switch (item.getItemId()) {
                    case R.id.mHome:
                        break;
                    case R.id.mBook:
                        navigate.setClass(RegisterActivity.this, BookingActivity.class);
                        startActivity(navigate);
                        break;

                    case R.id.mHistory:
                        navigate.setClass(RegisterActivity.this, HistoryActivity.class);
                        startActivity(navigate);
                        break;
                    case R.id.mSearch:
                        navigate.setClass(RegisterActivity.this, SearchActivity.class);
                        startActivity(navigate);
                        break;
                    case R.id.mLogin:
                        navigate.setClass(RegisterActivity.this, LoginActivity.class);
                        startActivity(navigate);
                        break;

                    case R.id.mRegister:
                        break;

                }
                return false;
            }

        });


    }

    private class EmailCheckJSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Getting Data ...");


            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected JSONObject doInBackground(String... args)  {



            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONObjectFromUrl(emailCheckUrl);



            return json;
        }





        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();





                Log.d("email check result",json.toString());
                if (json.toString().equals("{\"result\":\"Success\"}") ) {

                    Toast.makeText(RegisterActivity.this, "Email Exists!Please use another email!", Toast.LENGTH_LONG).show();
                    //print text success


                }
                else{
                    Toast.makeText(RegisterActivity.this,"Email Checked!!",Toast.LENGTH_LONG).show();
                }








        }

    }

    private class RegisterJSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Getting Data ...");


            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected JSONObject doInBackground(String... args)  {



            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONObjectFromUrl(registerUrl);



            return json;
        }





        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();





                Log.d("register result",json.toString());
                if (json.toString().equals("{\"result\":\"Success\"}") ) {

                    Toast.makeText(RegisterActivity.this,"you've register successfully",Toast.LENGTH_LONG).show();
                    //print text success



                }








        }

    }

    private boolean validation(){
        final EditText username = (EditText) findViewById(R.id.registeremail);
        final EditText password = (EditText) findViewById(R.id.registerpassword);
        final EditText firstname = (EditText) findViewById(R.id.first_name);
        final EditText lastname=(EditText)findViewById(R.id.last_name);


        String usernameString = username.getText().toString();
        String passwordString = username.getText().toString();
        String firstnameString=firstname.getText().toString();
        String lastnameString=lastname.getText().toString();
        if (usernameString.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(usernameString).matches()) {
            username.setError("Invalid Email Address");
            return false;
        }
        else if(passwordString.isEmpty()||passwordString.length()<4||passwordString.length()<10){
            password.setError("Please key in a password that is between 4 to 10 alphabetically");
            return false;
        }
        else if(firstnameString.isEmpty()){
            firstname.setError("Please key in your first name!");
            return false;
        }

        else if(lastnameString.isEmpty()){
            lastname.setError("Please key in a your last name!");
            return false;
        }
        if(usernameString.isEmpty()==false){

            emailCheckUrl ="http://hsienyan.pagekite.me:8080/CZ2006/getUserServlet?requestType=verifyemail&email="+usernameString;
            Log.d("emailcheckURL", emailCheckUrl);
            new EmailCheckJSONParse().execute(emailCheckUrl);

        }


        return true;
    }



}


