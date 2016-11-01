package com.example.chen_hsi.androidtutnonfregment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    String loginUrl;
    String emailCheckUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        setMenu();
        setNavigation();
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




        final EditText username=(EditText)findViewById(R.id.ptUsername);
        final EditText password=(EditText)findViewById(R.id.ptPassword);
        Button submit=(Button)findViewById(R.id.bSubmit);
        TextView register=(TextView)findViewById(R.id.tvRegister);




        submit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {


                if(validation(username,password)==true){
                    //Toast.makeText(view.getContext(), "username= "+usernameString+"\n"+"password= "+passwordString, Toast.LENGTH_LONG).show();

                    new LoginJSONParse().execute();
                }
                else{
                    Toast.makeText(LoginActivity.this,"Please enter your email address and password",Toast.LENGTH_LONG).show();
                }
            }



        });


        register.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {

                Intent toRegister=new Intent();
                toRegister.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(toRegister);


            }

        });




    }
    private void setNavigation(){
        navigationView=(NavigationView)findViewById(R.id.left_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                Intent navigate=new Intent();

                switch (item.getItemId())
                {

                    case R.id.mHistory:
                        navigate.setClass(LoginActivity.this,HistoryActivity.class);
                        startActivity(navigate);
                        break;
                    case R.id.mSearch:
                        navigate.setClass(LoginActivity.this,SearchActivity.class);
                        startActivity(navigate);
                        break;
                    case R.id.mLogin:
                        break;

                    case R.id.mRegister:
                        navigate.setClass(LoginActivity.this,RegisterActivity.class);
                        startActivity(navigate);
                        break;
                }
                return false;
            }

        });


    }

    private void setMenu(){

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar=getSupportActionBar();



        actionBar.setElevation((float) 2.5);

        //actionBar.setTitle("LOGIN");
        
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,myToolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }



    private boolean validation(EditText username, EditText password){
        //final EditText username = (EditText) findViewById(R.id.ptUsername);
        //final EditText password = (EditText) findViewById(R.id.ptPassword);

        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();
        if (usernameString.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(usernameString).matches()) {
            username.setError("Invalid Email Address");
            return false;
        }
        else if(passwordString.isEmpty()||passwordString.length()<=4||passwordString.length()>10){
            password.setError("Please key in a password that is between 4 to 10 alphabetically");
            return false;
        }


        loginUrl = "http://hsienyan1994.pagekite.me:8080/CZ2006/getUserServlet?requestType=login&email="+usernameString+"&password="+passwordString;
        emailCheckUrl ="http://hsienyan1994.pagekite.me:8080/CZ2006/getUserServlet?requestType=verifyemail&email="+usernameString;
        Log.d("LoginURL",loginUrl);
        Log.d("emailCheckURL",emailCheckUrl);




        return true;




    }

    private class LoginJSONParse extends AsyncTask<String, String, JSONObject[]> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();


            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Sending data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //pDialog.show();
        }


        @Override
        protected JSONObject[] doInBackground(String... args) {


            JSONParser jParser = new JSONParser();

            JSONArray loginResult = jParser.getJSONArrayFromUrl(loginUrl);
            JSONObject emailCheckExistResult = jParser.getJSONObjectFromUrl(emailCheckUrl);
            Log.d("Email Check String", emailCheckExistResult.toString());
            Log.d("Login Result String", loginResult.toString());


            JSONObject[] JsonObArray = new JSONObject[3];

            try {
                JsonObArray[0] = emailCheckExistResult;
                JsonObArray[1] = loginResult.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return JsonObArray;
        }


        @Override
        protected void onPostExecute(JSONObject... loginVerifyResult ) {
            pDialog.dismiss();

            String emailCheckeExistResultString=loginVerifyResult[1].toString();
            String loginResultString=loginVerifyResult[1].toString();

            Log.d("Login Result",loginResultString);
            if (loginResultString.equals("{\"Result\":\"Fail\"}")) {

                Toast.makeText(LoginActivity.this,"Username or Password Incorrect!",Toast.LENGTH_LONG).show();

            }

            else{

                try {

                    String userId=loginVerifyResult[1].getString("id");
                    String firstName=loginVerifyResult[1].getString("firstname");
                    String lastName=loginVerifyResult[1].getString("lastname");
                    String email=loginVerifyResult[1].getString("email");
                    AccountInfo.getInstance().setLoginStatus(true);
                    AccountInfo.getInstance().setUserId(userId);
                    AccountInfo.getInstance().setUserName(firstName);
                    AccountInfo.getInstance().setLastName(lastName);
                    AccountInfo.getInstance().setEmail(email);
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
                    Toast.makeText(LoginActivity.this,"sucessful login !",Toast.LENGTH_LONG).show();
                    Intent toSearchActivity=new Intent();
                    toSearchActivity.setClass(LoginActivity.this, SearchActivity.class);
                    startActivity(toSearchActivity);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //fail to log in


            }


        }


    }



}

