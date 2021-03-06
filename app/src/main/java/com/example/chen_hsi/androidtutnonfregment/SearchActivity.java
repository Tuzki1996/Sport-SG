package com.example.chen_hsi.androidtutnonfregment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
public class SearchActivity extends AppCompatActivity {
    ListView facilityListView;
    EditText facilitySearch;
    ArrayList<Facility> facilities=new ArrayList<Facility>() ;
    FacilityAdapter facilityAdapter;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    static Date updateDateTime;
    RadioGroup rgSort;

    String searchText="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setMenu();
        setNavigation();
        facilityListView=(ListView)findViewById(R.id.list_view);
        facilitySearch=(EditText) findViewById(R.id.myFilter);
        facilityAdapter =new FacilityAdapter(getApplicationContext(),R.layout.row_layout);
        facilityListView.setAdapter(facilityAdapter);
        rgSort=(RadioGroup)findViewById(R.id.rgrSort);
        if(updateDateTime==null){
        new JSONParse().execute();
            ((MyApplication) this.getApplication()).setOriginalList(facilities);
        }
        else
        {
            Date currentDateTime=new Date();
            long diff = updateDateTime.getTime() - currentDateTime.getTime();
            long diffMinutes = diff / (60 * 1000);
            Log.e("DEBUG!!!!!",""+diffMinutes );
            if(diffMinutes>=30)
            {
                new JSONParse().execute();
                ((MyApplication) this.getApplication()).setOriginalList(facilities);

            }
            else
            {
                facilityAdapter.clear();
                for(Facility facility: ((MyApplication) this.getApplication()).getOriginalList() ){
                    facilityAdapter.add(facility);
                }
            }
        }
        rgSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rbtSortName) {
                    facilityAdapter.setSortDistance(false);
                } else if(checkedId == R.id.rbtSortDistance) {
                    facilityAdapter.setSortDistance(true);
                }
                facilityAdapter.getFilter().filter(searchText);
            }
        });
        facilityListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Facility facilitySelected=(Facility)adapterView.getItemAtPosition(i);
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
                searchText=charSequence.toString();
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
                        navigate.setClass(SearchActivity.this,HistoryActivity.class);
                        startActivity(navigate);
                        break;
                    case R.id.mSearch:

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
                        if(AccountInfo.getInstance().getLoginStatus()==false){
                            navigate.setClass(SearchActivity.this,RegisterActivity.class);
                            startActivity(navigate);
                        }

                        break;



                }
                return false;
            }

        });


    }
    static String[] items=Sport.SPORT_TYPE.names();
    static boolean[] checkedItems = new boolean[items.length];
    public void selectSport(View view) {
        class SportSelection extends DialogFragment {

            @NonNull

            ArrayList<Integer> list=new ArrayList<Integer>();

            public Dialog onCreateDialog(Bundle savedInstanceState) {

                final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Sport");
                builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        checkedItems[which]=isChecked;
                        if(isChecked)
                        {
                            list.add(which);
                        }
                        else if(isChecked==false){
                            list.remove(new Integer(which));
                        }
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        facilityAdapter.setSportlist(list);
                        facilityAdapter.getFilter().filter(searchText);
                    }
                });
                builder.setCancelable(false);
                return builder.create();
            }
        }
        SportSelection sportSelection=new SportSelection();
        sportSelection.show(getSupportFragmentManager(),"show");
    }


    private class JSONParse extends AsyncTask<String, String, JSONArray> {
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
        protected JSONArray doInBackground(String... params) {
            Log.e("DEBUG!!!!!", "2");
            String url = "http://hsienyan1994.pagekite.me:8080/CZ2006/getUserServlet?requestType=search";

            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONArray facilitiesJS=jParser.getJSONArrayFromUrl(url);

return  facilitiesJS;
        }
        @Override
        protected void onPostExecute(JSONArray facilitiesJS) {
            pDialog.dismiss();
            try {
                Log.e("DEBUG!!!!!","3" );
                // Getting JSON Array from URL
                for (int i = 0; i < facilitiesJS.length(); i++) {
                    JSONObject facilityJS = facilitiesJS.getJSONObject(i);
                    int id;
                    double lng, lat,rating;
                    String name, address, telephone, photo,description;

                    id = facilityJS.getInt("id");
                    name = facilityJS.getString("name");
                    telephone = facilityJS.getString("phone");
                    address = facilityJS.getString("location");
                    lng = facilityJS.getDouble("longtitude");
                    lat = facilityJS.getDouble("lattitude");
                    photo = facilityJS.getString("url");
                    description=facilityJS.getString("description");
                    rating=facilityJS.getDouble("Rating");
                    Facility facility = new Facility(id,name, address, lng, lat, telephone, photo,description,rating);
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
                    if(!facilityJS.isNull("review")){
                        JSONArray reviewsJS=facilityJS.getJSONArray("review");
                        for(int j=0;j<reviewsJS.length();j++)
                        {
                            JSONObject reviewJS=reviewsJS.getJSONObject(j);
                            if(reviewJS!=null){
                                int reviewId=reviewJS.getInt("reviewid");
                                String acc=reviewJS.getString("user");
                                String text=reviewJS.getString("text");
                                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date=format1.parse(reviewJS.getString("date"));
                                double review_rating=reviewJS.getDouble("rating");
                                Review review=new Review(reviewId,acc,text,date,review_rating);
                                facility.addReview(review);}
                        }
                    }
                    facilities.add(facility);
                }
                facilityAdapter.clear();
for(Facility facility:facilities ){
    facilityAdapter.add(facility);
}
                facilityAdapter.getFilter().filter(searchText);
                updateDateTime=new Date();
            } catch (Exception e) {
                e.printStackTrace();
                    Toast.makeText(SearchActivity.this,"Please check your network connection",Toast.LENGTH_LONG).show();

            }

        }



    }


}