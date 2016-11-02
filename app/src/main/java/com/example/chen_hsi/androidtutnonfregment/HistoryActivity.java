package com.example.chen_hsi.androidtutnonfregment;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.Intent;
import android.media.Image;
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

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


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

    ArrayList<HashMap<String, String>> historylist = new ArrayList<HashMap<String, String>>();

    //private String userId;
    //private boolean loginStatus=true;
    private String nameOfUser;
    private String historyUrl;

    //JSON Node Names

    private static final String TAG_DATE = "date";
    private static final String TAG_FACILITY = "sportid";
    private static final String TAG_PAYMENT = "price";

    JSONArray history =new JSONArray();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);
        /*AccountInfo.getInstance().setLoginStatus(true);
        AccountInfo.getInstance().setUserId("12345");
        AccountInfo.getInstance().setUserName("Tracy");*/
        setMenu();
        setNavigation();
        ImageView plsLoginPic=(ImageView) findViewById(R.id.loginToViewPic);
        ListView hisList=(ListView)findViewById(R.id.historyList);


        if(AccountInfo.getInstance().getLoginStatus()==true){
            plsLoginPic.setVisibility(View.INVISIBLE);
            hisList.setVisibility(View.VISIBLE);

            historyUrl = "http://hsienyan1994.pagekite.me:8080/CZ2006/getUserServlet?requestType=getHistory&userid="+AccountInfo.getInstance().getUserId();

            Log.d("historyUrl",historyUrl);
            displayHistoryList();

        }
       else{
            hisList.setVisibility(View.INVISIBLE);
            plsLoginPic.setVisibility(View.VISIBLE);
            Toast.makeText(HistoryActivity.this,"Please log in to view your booking history.",Toast.LENGTH_LONG).show();
        }



    }




    private void setNavigation(){
        navigationView=(NavigationView)findViewById(R.id.left_drawer);
        Menu drawerMenu=navigationView.getMenu();
        View headerView=navigationView.inflateHeaderView(R.layout.navigation_drawer_header);
        TextView accountInfo=(TextView)headerView.findViewById(R.id.tAccountInfo);
        TextView name=(TextView)headerView.findViewById(R.id.tName);
        TextView firstName=(TextView)headerView.findViewById(R.id.tFirstName);
        TextView lastName=(TextView)headerView.findViewById(R.id.tLastName);
        TextView email=(TextView)headerView.findViewById(R.id.tEmail);
        TextView emailAdd=(TextView)headerView.findViewById(R.id.tEmalAdd);

        MenuItem loginItem=drawerMenu.findItem(R.id.mLogin);
        MenuItem regisgerItem=drawerMenu.findItem(R.id.mRegister);

        if(AccountInfo.getInstance().getLoginStatus()==true) {
            loginItem.setTitle("LOGOUT");
            regisgerItem.setVisible(false);
            accountInfo.setText("ACCOUNT INFO");
            name.setText("NAME:");
            firstName.setText(AccountInfo.getInstance().getUserName());
            lastName.setText(AccountInfo.getInstance().getLastName());
            email.setText("EMAIL:");
            emailAdd.setText(AccountInfo.getInstance().getEmail());


        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                Intent navigate=new Intent();

                switch (item.getItemId())
                {


                    case R.id.mHistory:
                        break;

                    case R.id.mSearch:
                        navigate.setClass(HistoryActivity.this,SearchActivity.class);
                        startActivity(navigate);
                        break;
                    case R.id.mLogin:
                        if(AccountInfo.getInstance().getLoginStatus()==true){
                            AccountInfo.getInstance().setLoginStatus(false);
                            Toast.makeText(HistoryActivity.this,"You have logged out successfully!",Toast.LENGTH_LONG).show();
                            navigate.setClass(HistoryActivity.this,SearchActivity.class);
                            startActivity(navigate);
                        }
                        else{
                            navigate.setClass(HistoryActivity.this,LoginActivity.class);
                            startActivity(navigate);
                        }


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
        if(AccountInfo.getInstance().getLoginStatus()==true){
            actionBar.setSubtitle("Hi,"+AccountInfo.getInstance().getUserName());
        }
        actionBar.setElevation((float) 2.5);
        actionBar.setTitle("Booking History");
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,myToolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }


    private void displayHistoryList(){
        historylist = new ArrayList<HashMap<String, String>>();

        new HistoryListJSONParse().execute();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private class HistoryListJSONParse extends AsyncTask<String, String, JSONArray> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //date = (TextView)findViewById(R.id.tDate);
            //facility= (TextView)findViewById(R.id.tFacility);
            //payment = (TextView)findViewById(R.id.tPayment);
            pDialog = new ProgressDialog(HistoryActivity.this);
            pDialog.setMessage("Getting your history ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
           // pDialog.show();


        }

        @Override
        protected JSONArray doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONArray historyListJsonArray = jParser.getJSONArrayFromUrl(historyUrl);




            return historyListJsonArray;
        }
        @Override
        protected void onPostExecute(JSONArray json) {
            pDialog.dismiss();
            try {

                history = json;
                for(int i = 0;i<history.length(); i++) {
                    JSONObject historyItem = history.getJSONObject(i);
                    Log.d("HistoryItem",historyItem.toString());
                    if(historyItem.toString().equals("{\"Result\":[\"Fail\"]}")){
                        Toast.makeText(HistoryActivity.this,"Sorry you don't have any booking record",Toast.LENGTH_LONG).show();
                        break;
                    }
                    String date = historyItem.getString(TAG_DATE);
                    Log.d("Date",historyItem.getString(TAG_DATE));
                    JSONObject sportItem=historyItem.getJSONObject("Sport");
                    JSONObject facilityItem= historyItem.getJSONObject("Facility");

                    String payment =  sportItem.getString("price");
                    Log.d("payment",payment);
                    String sportType= sportItem.getString("sporttype");

                    Log.d("sportType",sportType);
                    sportType= Sport.SPORT_TYPE.names()[Integer.parseInt(sportType)];
                    String facilityName=facilityItem.getString("name");
                    Log.d("facilityName",facilityName);
                    String phoneNum=facilityItem.getString("phone");
                    Log.d("phoneNum",phoneNum);
                    String webUrl=facilityItem.getString("url");
                    Log.d("webUrl",webUrl);
                    String description=facilityItem.getString("description");
                    Log.d("description",description);





                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("date", date);
                    map.put("payment", payment);
                    map.put("sportType", sportType);
                    map.put("facilityName", facilityName);
                    map.put("phoneNum", phoneNum);
                    map.put("webUrl", webUrl);
                    map.put("description", description);





                    historylist.add(map);
                    list = (ListView) findViewById(R.id.historyList);

                    ListAdapter adapter = new SimpleAdapter(HistoryActivity.this, historylist,
                            R.layout.history_list,
                            new String[]{"date", "payment", "sportType","facilityName","phoneNum"}, new int[]{
                            R.id.tDate, R.id.tPayment, R.id.tSportType,R.id.tFacilityName,R.id.tPhoneNum});


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
                Toast.makeText(HistoryActivity.this,"Please check your network connection",Toast.LENGTH_LONG).show();
            }

        }
    }


}
