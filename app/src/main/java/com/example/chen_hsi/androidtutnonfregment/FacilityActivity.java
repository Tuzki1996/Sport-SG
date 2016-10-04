package com.example.chen_hsi.androidtutnonfregment;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
        facility=(Facility) getIntent().getSerializableExtra("facility_key");
        facilityName=(TextView) findViewById(R.id.facility_name);
        facilityAddress=(TextView) findViewById(R.id.facility_address);
        facilityImage=(ImageView)findViewById(R.id.facility_photo) ;
        facilityName.setText(facility.getFacility_name());
        facilityAddress.setText(facility.getFacility_address());
        new ImageLoadTask("https://pbs.twimg.com/profile_images/695684826565840897/VNZVklL7.jpg", facilityImage).execute();
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

}
