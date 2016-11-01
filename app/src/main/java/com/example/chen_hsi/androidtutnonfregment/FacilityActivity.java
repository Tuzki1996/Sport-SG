package com.example.chen_hsi.androidtutnonfregment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    TextView tvReview;
    ImageView facilityImage;
    RatingBar facilityRating;
    RatingBar userRating;
    EditText facilityReview;
    String submitUrl;
    NonScrollListView reviewList;
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
        tvReview=(TextView)findViewById(R.id.tvReview);
        facilityName.setText(facility.getFacility_name());
        facilityAddress.setText(facility.getFacility_description());
        facilitySportList=facility.getSportList();
        reviewAdapter=new ReviewAdapter(getApplicationContext(),R.layout.review_list);
        reviewList= (NonScrollListView) findViewById(R.id.reviewList);
        String sports="";
        for(Sport sport:facilitySportList)
        sports+=sport.getSport_type().getName()+", ";
        if(sports.length()!=0)
        sports.substring(0,sports.length()-2);
        facilityPhone.setText("Phone: "+facility.getFacility_phone()+"\nSports: "+sports);
        loadReview();
        Picasso.with(getBaseContext()).load(facility.getFacility_photo_resource()).into(facilityImage);
        facilityRating.setRating((float)facility.getFacility_rating());

    }
    private void loadReview()
    {
        facilityReviewList=facility.getReviewList();

        for(Review review:facilityReviewList ){
            if(review.getText().trim()!="")
                reviewAdapter.add(review);
        }
        if(facilityReviewList.size()==0)
        {
            tvReview.setVisibility(View.INVISIBLE);
        }
        else
        {
            reviewList.setAdapter(null);
            tvReview.setVisibility(View.VISIBLE);
            reviewList.setAdapter(reviewAdapter);
        }
        reviewList.setAdapter(reviewAdapter);
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
            String datetime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            String review=facilityReview.getText().toString();
            if(review.trim()=="")
            {   Toast.makeText(FacilityActivity.this,"Please enter your review before submit.",Toast.LENGTH_LONG).show();}
            else {
                review = URLEncoder.encode(review, "UTF-8");
                datetime= URLEncoder.encode(datetime, "UTF-8");
                double userRatingValue = (double) userRating.getRating();
                submitUrl = "http://hsienyan.pagekite.me:8080/CZ2006/getUserServlet?requestType=submitReview&text=" + review + "&rating=" + userRatingValue + "&date=" + datetime + "&userid=" + acc_id + "&facilityid=" + facility.getFacility_id();

                new ReviewJSONParse().execute(submitUrl);
                facilityReview.setText("");
                userRating.setRating(-1);
            }
        }
        else{

            Toast.makeText(FacilityActivity.this,"Please log in to submit your review.",Toast.LENGTH_LONG).show();
        }
    }

    public void bookNow(View view) {
        Intent intent = new Intent(getApplicationContext(), SubBookingActivity.class);

        intent.putExtra("facility_key", facility);
        startActivity(intent);
    }



    public void getDict(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + facility.getFacility_lat()+ "," + facility.getFacility_lng()));
        startActivity(intent);
    }

    private class ReviewJSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            pDialog = new ProgressDialog(FacilityActivity.this);
            pDialog.setMessage("Submitting review ...");


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
            if (json.toString().equals("{\"result\":\"Fail\"}") ) {
                Toast.makeText(FacilityActivity.this,"Submit fail",Toast.LENGTH_LONG).show();
            }
            else
            {
                try {
                    double rating=json.getDouble("Rating");
                    facility.setFacility_rating(rating);
                    facilityRating.setRating((float)rating);
                    if(!json.isNull("review")){
                        JSONArray reviewsJS=json.getJSONArray("review");
                        facility.clearReviewList();
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

                        reviewAdapter.resetList();
                        loadReview();
                        reviewAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}