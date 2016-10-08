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
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

        setMenu();
        setNavigation();



        Button submit=(Button)findViewById(R.id.bSubmit);
        final EditText username=(EditText)findViewById(R.id.ptUsername);
        final EditText password=(EditText)findViewById(R.id.ptPassword);


        //url="http://hsienyan.pagekite.me:8080/CZ2006/getUserServlet?requestType=login&email=%27kck@hotmail.com%27&password=Password";
        //http://hsienyan.pagekite.me:8080/CZ2006/getUserServlet?=verifytracychang@gmail.com&email=red
        //http://hsienyan.pagekite.me:8080/CZ2006/getUserServlet?requestType=verifyemail&tracychang1996@gmail.com=red
        //http://hsienyan.pagekite.me:8080/CZ2006/getUserServlet?requestType=login&email=%27kck@hotmail.com%27&password=asdf

        TextView register=(TextView)findViewById(R.id.tvRegister);
        // Create the text message with a string

        submit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                String usernameString=username.getText().toString();
                String passwordString=password.getText().toString();
                url = "http://hsienyan.pagekite.me:8080/CZ2006/getUserServlet?requestType=login&email=%27"+usernameString+"%27&password="+passwordString;
                Log.d("LoginURL",url);

                if(validation()==true){
                    //Toast.makeText(view.getContext(), "username= "+usernameString+"\n"+"password= "+passwordString, Toast.LENGTH_LONG).show();

                    new JSONParse().execute();
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
                    case R.id.mHome:
                        break;
                    case R.id.mBook:
                        navigate.setClass(LoginActivity.this,LoginActivity.class);
                        startActivity(navigate);
                        break;

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
        actionBar.setTitle("LOGIN");
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,myToolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }



    private boolean validation(){
        final EditText username = (EditText) findViewById(R.id.ptUsername);
        final EditText password = (EditText) findViewById(R.id.ptPassword);

        String usernameString = username.getText().toString();
        String passwordString = username.getText().toString();
        if (usernameString.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(usernameString).matches()) {
            username.setError("Invalid Email Address");
            return true;
        }
        else if(passwordString.isEmpty()||passwordString.length()<4||passwordString.length()<10){
            password.setError("Please key in a password that is between 4 to 10 alphabetically");
            return true;
        }
        else{

            return true;
        }



    }

    private class JSONParse extends AsyncTask<String, String, JSONArray> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            //    date = (TextView)findViewById(R.id.tDate);
            //  facility= (TextView)findViewById(R.id.tFacility);
            //payment = (TextView)findViewById(R.id.tPayment);
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Sending Data ...");


            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected JSONArray doInBackground(String... args)  {



            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONArray jsonA = jParser.getJSONArrayFromUrl(url);



            return jsonA;

        }
// user reader to read & parse response
        //reader.close();





        @Override
        protected void onPostExecute(JSONArray json) {
            pDialog.dismiss();

            //JSONArray login = json.getJSONArray("");
            // try {
            //  Log.e("DEBUG!!!!!", "3");
            try {
                JSONObject c = json.getJSONObject(0);
                Log.d("Login Result",c.toString());
                if (c.toString().equals("{\"Result\":\"Fail\"}") ) {

                    Toast.makeText(LoginActivity.this,"Please register first",Toast.LENGTH_LONG).show();
                    //print text success
                    Intent toRegister = new Intent();
                    toRegister.setClass(LoginActivity.this, RegisterActivity.class);//go to main menu
                    startActivity(toRegister);


                }
                else{
                    Log.d("Login Result",c.toString());
                    Toast.makeText(LoginActivity.this,"Log in sucessfully!",Toast.LENGTH_LONG).show();
                    //fail to log in
                    //pDialog.dismiss();

                }





            } catch (JSONException e) {
                e.printStackTrace();
            }





        }

    }





}

