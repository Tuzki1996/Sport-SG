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
import android.view.Menu;
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
    String loginUrl;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setMenu();
        setNavigation();
        final EditText username = (EditText) findViewById(R.id.registeremail);
        final EditText password = (EditText) findViewById(R.id.registerpassword);
        final EditText retypePassword = (EditText) findViewById(R.id.retypepassword);
        final EditText firstname = (EditText) findViewById(R.id.first_name);
        final EditText lastname=(EditText)findViewById(R.id.last_name);
        final EditText contact=(EditText)findViewById(R.id.contact);

        Button submitregister = (Button) findViewById(R.id.submitregister);


        password.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String usernameString= username.getText().toString();
                if (usernameString.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(usernameString).matches()) {
                    username.setError("Invalid Email Address");

                }
                else{
                    emailCheckUrl = "http://hsienyan1994.pagekite.me:8080/CZ2006/getUserServlet?requestType=verifyemail&email=" + usernameString;
                    Log.d("emailcheckURL", emailCheckUrl);
                    new EmailCheckJSONParse().execute(emailCheckUrl);
                }
            }
        });


        contact.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String passwordString= password.getText().toString();
                String retypePasswordString= retypePassword.getText().toString();
                if(!passwordString.equals(retypePasswordString)){
                    retypePassword.setError("Password inconsistent!");
                }
            }
        });

        submitregister.setOnClickListener(new Button.OnClickListener() {




            public void onClick(View view) {
               /* String usernameString = username.getText().toString();
                String passwordString = password.getText().toString();
                String firstnameString=firstname.getText().toString();
                String lastnameString=lastname.getText().toString();
                String contactString=contact.getText().toString();*/
                if (validation(username,password,retypePassword,firstname,lastname,contact) == true) {

                    new RegisterJSONParse().execute(registerUrl);

                }
            }
        });
    }





    private void setMenu(){

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar=getSupportActionBar();

        if(AccountInfo.getInstance().getLoginStatus()==true){
            actionBar.setSubtitle("Hi,"+AccountInfo.getInstance().getUserName());
        }

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
        Menu drawerMenu=navigationView.getMenu();
        MenuItem loginItem=drawerMenu.findItem(R.id.mLogin);

        if(AccountInfo.getInstance().getLoginStatus()==true) {
            loginItem.setTitle("LOGOUT");
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent navigate = new Intent();

                switch (item.getItemId()) {


                    case R.id.mHistory:
                        navigate.setClass(RegisterActivity.this, HistoryActivity.class);
                        startActivity(navigate);
                        break;
                    case R.id.mSearch:
                        navigate.setClass(RegisterActivity.this, SearchActivity.class);
                        startActivity(navigate);
                        break;
                    case R.id.mLogin:
                        if(AccountInfo.getInstance().getLoginStatus()==true){
                            AccountInfo.getInstance().setLoginStatus(false);
                            Toast.makeText(RegisterActivity.this,"You have logged out successfully!",Toast.LENGTH_LONG).show();
                            navigate.setClass(RegisterActivity.this,SearchActivity.class);
                            startActivity(navigate);
                        }
                        else{
                            navigate.setClass(RegisterActivity.this, LoginActivity.class);
                            startActivity(navigate);

                        }

                        break;

                    case R.id.mRegister:
                        break;

                }
                return false;
            }

        });


    }

    private class LoginJSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Login ...");


            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected JSONObject doInBackground(String... args)  {



            JSONParser jParser = new JSONParser();

            JSONArray loginJsonArray = jParser.getJSONArrayFromUrl(loginUrl);
            JSONObject json=null;
            try {
                json=loginJsonArray.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return json;
        }





        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();





                Log.d("Login result",json.toString());
                if (json.toString().equals("{\"Result\":\"Fail\"}") ) {

                    Toast.makeText(RegisterActivity.this, "Something went wrong, please try again later.", Toast.LENGTH_LONG).show();
                    //print text success


                }
                else{
                    try {

                        String userId=json.getString("id");
                        String firstName=json.getString("firstname");
                        String email=json.getString("email");
                        String lastName=json.getString("lastname");

                        AccountInfo.getInstance().setLoginStatus(true);
                        AccountInfo.getInstance().setUserId(userId);
                        AccountInfo.getInstance().setUserName(firstName);
                        AccountInfo.getInstance().setUserName(email);
                        AccountInfo.getInstance().setLastName(lastName);

                        //************** DEBUG USE************************************
                        String loginDebugString;
                        if(AccountInfo.getInstance().getLoginStatus()==true){
                            loginDebugString="true";
                        }
                        else{
                            loginDebugString="false";
                        }
                        //************** DEBUG USE************************************
                        Log.d("LoginStatus",loginDebugString);
                        Log.d("UserId",AccountInfo.getInstance().getUserId());
                        Log.d("NameOfUser",AccountInfo.getInstance().getUserName());

                        Toast.makeText(RegisterActivity.this,"sucessful login !",Toast.LENGTH_LONG).show();
                        Intent toSearchActivity=new Intent();
                        toSearchActivity.setClass(RegisterActivity.this, SearchActivity.class);
                        startActivity(toSearchActivity);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }








        }

    }

    private class RegisterJSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Register ...");


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

                    Toast.makeText(RegisterActivity.this,"you've registered successfully",Toast.LENGTH_LONG).show();
                    //print text success
                    new LoginJSONParse().execute(loginUrl);



                }
               else{
                    Toast.makeText(RegisterActivity.this,"Email Exists!Please use another email!",Toast.LENGTH_LONG).show();
                }








        }

    }



    private class EmailCheckJSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();


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




            final EditText username = (EditText) findViewById(R.id.registeremail);
            Log.d("register result",json.toString());
            if (json.toString().equals("{\"result\":\"Success\"}") ) {
                username.setError("username has been used");


            }
            else{

            }








        }

    }

    private boolean validation(EditText username,EditText password,EditText retypePassword,EditText firstname,EditText lastname, EditText contact){



        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();
        String retypePasswordString = retypePassword.getText().toString();
        String firstnameString=firstname.getText().toString();
        String lastnameString=lastname.getText().toString();
        String contactString=contact.getText().toString();
        boolean returnResult=true;

        if (usernameString.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(usernameString).matches()) {
            username.setError("Invalid Email Address");
            returnResult=false;
        }
        if(passwordString.isEmpty()||passwordString.length()<4||passwordString.length()>10){
            password.setError("Please key in a password that is between 4 to 10 alphabetically");
            returnResult=false;
        }
       if(firstnameString.isEmpty()){
            firstname.setError("Please key in your first name!");
           returnResult=false;
        }

        if(lastnameString.isEmpty()){
            lastname.setError("Please key in a your last name!");
            returnResult=false;
        }
        if(contactString.isEmpty()){
            contact.setError("Please key in a your contact number!");
            returnResult=false;
        }
        if(!passwordString.equals(retypePasswordString)){
            retypePassword.setError("Password inconsistent!");
            returnResult=false;
        }



        if(returnResult==true) {


            //new EmailCheckJSONParse().execute(emailCheckUrl);

            firstnameString.replace(" ","_");
            lastnameString.replace(" ","_");
            registerUrl = "http://hsienyan1994.pagekite.me:8080/CZ2006/getUserServlet?requestType=register&email=" + usernameString + "&password=" + passwordString + "&firstname=" + firstnameString + "&lastname=" + lastnameString + "&contact=" + contactString;
            Log.d("registerUrl", registerUrl);

            loginUrl = "http://hsienyan1994.pagekite.me:8080/CZ2006/getUserServlet?requestType=login&email="+usernameString+"&password="+passwordString;

        }


        return returnResult;
    }



}


