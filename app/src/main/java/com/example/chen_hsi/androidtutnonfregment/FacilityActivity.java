package com.example.chen_hsi.androidtutnonfregment;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    EditText facilityReview;
    String submitUrl;
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
        facilityReview=(EditText)findViewById(R.id.etComment);
        facilityName.setText(facility.getFacility_name());
        facilityAddress.setText(facility.getFacility_description());
        String sports="";
        for(Sport sport:facilitySportList)
        sports+=sport+", ";
        if(sports.length()!=0)
        sports.substring(0,sports.length()-2);
        facilityPhone.setText("Phone: "+facility.getFacility_phone()+"\nSports: "+sports);
        facilitySportList=facility.getSportList();
        loadReview();
        Picasso.with(getBaseContext()).load(facility.getFacility_photo_resource()).into(facilityImage);
        facilityRating.setRating((float)facility.getFacility_rating());

    }
    private void loadReview()
    {
        facilityReviewList=facility.getReviewList();
        ListView list = (ListView) findViewById(R.id.reviewList);
        reviewAdapter=new ReviewAdapter(getApplicationContext(),R.layout.review_list);
        for(Review review:facilityReviewList ){
            if(review.getText().trim()!="")
                reviewAdapter.add(review);
        }
        list.setAdapter(reviewAdapter);
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

    public void submitReview(View view) throws UnsupportedEncodingException {
        if(AccountInfo.getInstance().getLoginStatus()==true){
            int acc_id=Integer.parseInt( AccountInfo.getInstance().getUserId());
            String datetime=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            String review=facilityReview.getText().toString();
            review = URLEncoder.encode(review,"UTF-8");
            double userRatingValue=(double) userRating.getRating();
            submitUrl="http://hsienyan.pagekite.me:8080/CZ2006/getUserServlet?requestType=submitReview&text="+review+"&rating="+userRatingValue+"&date="+datetime+"&userid="+acc_id+"&facilityid="+facility.getFacility_id();

            new ReviewJSONParse().execute(submitUrl);
        }
        else{

            Toast.makeText(FacilityActivity.this,"Please log in to submit your review.",Toast.LENGTH_LONG).show();
        }
    }

    public void bookNow(View view) {
        Intent intent = new Intent(getApplicationContext(), BookingActivity.class);

        intent.putExtra("facility_key", facility);
        intent.putExtra("user_id",AccountInfo.getInstance().getUserId());
        startActivity(intent);
    }

    public void getDirection(View view) {
    }

    private class ReviewJSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            pDialog = new ProgressDialog(FacilityActivity.this);
            pDialog.setMessage("Getting Data ...");


            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected JSONObject doInBackground(String... args)  {



            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONObjectFromUrl(submitUrl);



            return json;
        }





        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();





            Log.d("register result",json.toString());
            if (json.toString().equals("{\"result\":\"Success\"}") ) {

                Toast.makeText(FacilityActivity.this,"Submit successfully",Toast.LENGTH_LONG).show();
                //print text success



            }
        }

    }
}