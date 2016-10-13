package com.example.chen_hsi.androidtutnonfregment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ListView facilityListView;
    EditText facilitySearch;
    int [] facility_photo_resource={R.drawable.picture1,R.drawable.picture2,R.drawable.picture3};
    String [] facility_photo;
    String [] facility_name;
    String [] facility_address;
    String [] facility_xaddr;
    String [] facility_yaddr;
    String [] facility_telephone;
    ArrayList<Facility> facilities=new ArrayList<Facility>() ;
    FacilityAdapter facilityAdapter;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setMenu();
        setNavigation();
        facilityListView=(ListView)findViewById(R.id.list_view);
        facilitySearch=(EditText) findViewById(R.id.myFilter);
        facilityAdapter =new FacilityAdapter(getApplicationContext(),R.layout.row_layout);
        facilityListView.setAdapter(facilityAdapter);
        facility_name=getResources().getStringArray(R.array.facility_titles);
        facility_address=getResources().getStringArray(R.array.facility_address);
        facility_xaddr=getResources().getStringArray(R.array.facility_xaddress);
        facility_yaddr=getResources().getStringArray(R.array.facility_yaddress);
        facility_telephone=getResources().getStringArray(R.array.facility_phone);
        facility_photo=getResources().getStringArray(R.array.facility_photo);
        new JSONParse().execute();
        //dbHelper=new DbHelper(this);
        //sqLiteDatabase=dbHelper.getReadableDatabase();
        //cursor=dbHelper.getFacility(sqLiteDatabase);
        //createDatabase();
        int i=0;
       /* if(cursor.moveToFirst())
        {
            do{
                String name,address,telephone,xaddr,yaddr,photo;
                name=cursor.getString(0);
                telephone=cursor.getString(4);
                address=cursor.getString(3);
                xaddr=cursor.getString(1);
                yaddr=cursor.getString(2);
                photo=cursor.getString(5);
                Facility facility=new Facility(name,address,Double.parseDouble(xaddr),Double.parseDouble(yaddr),telephone,photo);
                facilities.add(facility);
                facilityAdapter.add(facility);
                i++;
            }while (cursor.moveToNext());
        }*/
        facilityListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Facility facilitySelected=(Facility)adapterView.getItemAtPosition(i);
                //TextView selected=(TextView)view.findViewById(R.id.facility_name);
                Toast.makeText(SearchActivity.this,"You click "+facilitySelected.getFacility_name().toString(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), FacilityActivity.class);

                intent.putExtra("facility_key", facilitySelected);
                startActivity(intent);
            }
        });
        facilitySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                facilityAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
        actionBar.setTitle("SEARCH");
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,myToolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    private void setNavigation(){
        navigationView=(NavigationView)findViewById(R.id.left_drawer);
        Menu drawerMenu=navigationView.getMenu();
        MenuItem loginItem=drawerMenu.findItem(R.id.mLogin);

        if(AccountInfo.getInstance().getLoginStatus()==true) {
            loginItem.setTitle("LOGOUT");
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                Intent navigate=new Intent();

                switch (item.getItemId())
                {
                    case R.id.mHome:
                        break;
                    case R.id.mBook:
                        navigate.setClass(SearchActivity.this,BookingActivity.class);
                        startActivity(navigate);
                        break;

                    case R.id.mHistory:
                        navigate.setClass(SearchActivity.this,HistoryActivity.class);
                        startActivity(navigate);
                        break;
                    case R.id.mSearch:
                        navigate.setClass(SearchActivity.this,SearchActivity.class);
                        startActivity(navigate);
                        break;
                    case R.id.mLogin:
                        if(AccountInfo.getInstance().getLoginStatus()==true){
                            AccountInfo.getInstance().setLoginStatus(false);
                            Toast.makeText(SearchActivity.this,"You have logged out successfully!",Toast.LENGTH_LONG).show();
                            navigate.setClass(SearchActivity.this,SearchActivity.class);
                            startActivity(navigate);
                        }
                        else{
                            navigate.setClass(SearchActivity.this,LoginActivity.class);
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


    private class JSONParse extends AsyncTask<Void,Void,Void> {
        private ProgressDialog pDialog;



        @Override
        protected void onPreExecute() {
            Log.e("DEBUG!!!!!","1" );
            super.onPreExecute();
            pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.e("DEBUG!!!!!", "2");
            String url = "http://hsienyan.pagekite.me:8080/CZ2006/getUserServlet?requestType=search";

            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            String json = jParser.getJSONFromUrl2(url);
            try {
                JSONArray facilitiesJS = new JSONArray(json);
                Log.e("FOUND!", String.valueOf(facilitiesJS.length()));
                for (int i = 0; i < facilitiesJS.length(); i++) {
                    JSONObject facilityJS = facilitiesJS.getJSONObject(i);
                    int id;
                    double xaddr, yaddr;
                    String name, address, telephone, photo;

                    id = facilityJS.getInt("id");
                    name = facilityJS.getString("name");
                    telephone = facilityJS.getString("phone");
                    address = facilityJS.getString("address");
                    xaddr = facilityJS.getDouble("longtitude");
                    yaddr = facilityJS.getDouble("lattitude");
                    photo = facilityJS.getString("url");
                    Facility facility = new Facility(id,name, address, xaddr, yaddr, telephone, photo);
                    if(!facilityJS.isNull("Sport")){
                        JSONArray sportsJS=facilityJS.getJSONArray("Sport");
                        for(int j=0;j<sportsJS.length();j++)
                        {
                            JSONObject sportJS=sportsJS.getJSONObject(j);
                            if(sportJS!=null){
                                int type=sportJS.getInt("sporttype");
                                double price=sportJS.getDouble("price");
                                int sport_id=sportJS.getInt("id");
                                Sport sport=new Sport(type,price,sport_id);
                                facility.addSport(sport);}
                        }
                    }

                    facilities.add(facility);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
return  null;
        }
        @Override
        protected void onPostExecute(Void json) {
            pDialog.dismiss();
            try {
                Log.e("DEBUG!!!!!","3" );
                // Getting JSON Array from URL

                facilityAdapter.clear();
for(Facility facility:facilities ){
    facilityAdapter.add(facility);
}

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}