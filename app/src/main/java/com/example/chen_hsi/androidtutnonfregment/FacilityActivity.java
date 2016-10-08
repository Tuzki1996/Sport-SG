package com.example.chen_hsi.androidtutnonfregment;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class FacilityActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    Facility facility;
    TextView facilityName;
    TextView facilityAddress;
    TextView facilityPhone;
    ImageView facilityImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);
        setMenu();
        setNavigation();
       facility=(Facility) getIntent().getSerializableExtra("facility_key");
        facilityName=(TextView) findViewById(R.id.facility_name);
        facilityAddress=(TextView) findViewById(R.id.facility_address);
        facilityImage=(ImageView)findViewById(R.id.facility_photo) ;
        facilityPhone=(TextView)findViewById(R.id.facility_phone);
        facilityName.setText(facility.getFacility_name());
        facilityAddress.setText(facility.getFacility_address());
        facilityPhone.setText("Phone: "+facility.getFacility_phone());
        Picasso.with(getBaseContext()).load(facility.getFacility_photo_resource()).into(facilityImage);
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
                        navigate.setClass(FacilityActivity.this,BookingActivity.class);
                        startActivity(navigate);
                        break;

                    case R.id.mHistory:
                        navigate.setClass(FacilityActivity.this,HistoryActivity.class);
                        startActivity(navigate);
                        break;
                    case R.id.mSearch:
                        navigate.setClass(FacilityActivity.this,SearchActivity.class);
                        startActivity(navigate);
                        break;
                    case R.id.mLogin:
                        navigate.setClass(FacilityActivity.this,LoginActivity.class);
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