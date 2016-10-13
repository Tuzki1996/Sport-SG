package com.example.chen_hsi.androidtutnonfregment;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FacilityActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    ReviewAdapter reviewAdapter;
    Facility facility;
    TextView facilityName;
    TextView facilityAddress;
    TextView facilityPhone;
    ImageView facilityImage;
    RatingBar facilityRating;
    RatingBar userRating;
    private ArrayList<Sport> facilitySportList=new ArrayList<Sport>();

    private ArrayList<Review> facilityReviewList=new ArrayList<Review>();

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
        facilityRating=(RatingBar)findViewById(R.id.ratingBar) ;
        userRating=(RatingBar)findViewById(R.id.ratingBarUser);
        facilityName.setText(facility.getFacility_name());
        facilityAddress.setText(facility.getFacility_address());
        facilityPhone.setText("Phone: "+facility.getFacility_phone());
        facilitySportList=facility.getSportList();
        facilityReviewList=facility.getReviewList();
        Picasso.with(getBaseContext()).load(facility.getFacility_photo_resource()).into(facilityImage);
        ListView list = (ListView) findViewById(R.id.reviewList);
        reviewAdapter=new ReviewAdapter(getApplicationContext(),R.layout.review_list);
        for(Review review:facilityReviewList ){
            if(review.getText().trim()!="")
            reviewAdapter.add(review);
        }
        list.setAdapter(reviewAdapter);
        facilityRating.setRating(3);
    }
    private void setMenu(){

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar=getSupportActionBar();

        if(AccountInfo.getInstance().getLoginStatus()==true){
            actionBar.setSubtitle("Hi,"+AccountInfo.getInstance().getUserName());
        }

        actionBar.setElevation((float) 2.5);
        actionBar.setTitle("Facility");
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
                        if(AccountInfo.getInstance().getLoginStatus()==true){
                            AccountInfo.getInstance().setLoginStatus(false);
                            Toast.makeText(FacilityActivity.this,"You have logged out successfully!",Toast.LENGTH_LONG).show();
                            navigate.setClass(FacilityActivity.this,SearchActivity.class);
                            startActivity(navigate);
                        }
                        else{
                            navigate.setClass(FacilityActivity.this,LoginActivity.class);
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

    public void submitReview(View view) {
        if(AccountInfo.getInstance().getLoginStatus()==true){



        }
        else{

            Toast.makeText(FacilityActivity.this,"Please log in to submit your review.",Toast.LENGTH_LONG).show();
        }
    }
}