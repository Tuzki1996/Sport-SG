package com.example.chen_hsi.androidtutnonfregment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {
    ListView facilityListView;
    EditText facilitySearch;
    int [] facility_photo_resource={R.drawable.picture1,R.drawable.picture2,R.drawable.picture3};
    String [] facility_name;
    String [] facility_address;
    String [] facility_xaddr;
    String [] facility_yaddr;
    String [] facility_telephone;
    Facility[] facilities=new Facility[100];
    FacilityAdapter facilityAdapter;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
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
        dbHelper=new DbHelper(this);
        sqLiteDatabase=dbHelper.getReadableDatabase();
        cursor=dbHelper.getFacility(sqLiteDatabase);
        int i=0;
        if(cursor.moveToFirst())
        {
            do{
                String name,address,telephone,xaddr,yaddr;
                name=cursor.getString(0);
                telephone=cursor.getString(4);
                address=cursor.getString(3);
                xaddr=cursor.getString(1);
                yaddr=cursor.getString(2);
                facilities[i]=new Facility(name,address,Double.parseDouble(xaddr),Double.parseDouble(yaddr),telephone);

                Facility dataProvider=new Facility(facility_photo_resource[1],
                        name,address);
                facilityAdapter.add(dataProvider);
                i++;
            }while (cursor.moveToNext());
        }
        /*for(String name:facility_name){
            Facility dataProvider=new Facility(facility_photo_resource[i],
                    facility_name[i],facility_address[i]);
        facilityAdapter.add(dataProvider);
             dbHelper.addFacility(facility_name[i],Double.parseDouble(facility_xaddr[i]),Double.parseDouble(facility_yaddr[i]),facility_address[i],facility_telephone[i],sqLiteDatabase);
            i++;
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


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.relative_layout);
        switch (item.getItemId())
        {
            case R.id.id_book:
                Toast.makeText(getBaseContext(),"u click book", Toast.LENGTH_LONG).show();
                relativeLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
                break;
            case R.id.id_search:
                Toast.makeText(getBaseContext(),"u click search", Toast.LENGTH_LONG).show();
                relativeLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));

                break;
        }
        return true;
        //return super.onOptionsItemSelected(item);
    }*/

    public void searchFacility(View view) {
        /*facilityListView.setAdapter(facilityAdapter);
String search=facilitySearch.getText().toString().trim();


        for(Facility facility:facilities){
            if (facility==null)
            if(search=="")
                facilityAdapter.add(facility);
            else if(facility.getFacility_name().contains(search))
                facilityAdapter.add(facility);
        }*/
    }
    private void setMenu(){

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar=getSupportActionBar();

        if(myToolbar==null){
            Log.e("null","NUlllll,");
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
                        navigate.setClass(SearchActivity.this,LoginActivity.class);
                        startActivity(navigate);
                        break;

                    case R.id.mRegister:
                        break;

                }
                return false;
            }

        });


    }

}