package com.example.chen_hsi.androidtutnonfregment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Calendar;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class SubBookingActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;


    ProgressDialog pd;
    String sportid = "100";
    String userid = "100";
    String facilityid="";
    String dateVar;
    String timeVar;
    String startDateTime;
    String endDateTime;
    String sportType;
    Facility facility;


    double price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_booking);
        facility=(Facility) getIntent().getSerializableExtra("facility_key");
        //facilityid= String.valueOf(facility.getFacility_id());
        //userid=AccountInfo.getInstance().getUserId();
        setMenu();
        setNavigation();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Button fab = (Button) findViewById(R.id.confirm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Booking In Progress", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                sendJson(sportid,userid,Double.toString(price),startDateTime,endDateTime,sportType,facility);

            }
        });


        final Button btnPickDate=(Button) findViewById(R.id.btnPickDate);
        final Button btnPickTime=(Button) findViewById(R.id.btnPickTime);
        final TextView fnameTv = (TextView)findViewById(R.id.facility_name);

        Spinner dropdown = (Spinner)findViewById(R.id.sportType);
        String[] items = new String[]{"","Swimming", "Badminton", "Basketball"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int pos, long id) {
                //Toast.makeText(getBaseContext(), "You've selected " + parentView.getItemAtPosition(pos), Toast.LENGTH_LONG).show();
                sportType=parentView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        if(facility!=null){
            fnameTv.setText(facility.getFacility_name());
        }else{
            fnameTv.setText("Default Facility Name");
        }

        final Calendar myCalendarStart = Calendar.getInstance();
        final Calendar myCalendarEnd = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, monthOfYear);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                //TextView pickDateTxtBox = (TextView)findViewById(R.id.pickDate);
                dateVar=year + "-" + monthOfYear + "-" + dayOfMonth;
                //pickDateTxtBox.setText(dateVar);
                pd=ProgressDialog.show(SubBookingActivity.this, "", "Querying for available timeslot", false);
                pd.dismiss();
                btnPickDate.setText(dateVar);
            }

        };

        btnPickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog dialog =new DatePickerDialog(SubBookingActivity.this, date, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH));
                Calendar cal = Calendar.getInstance();
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                cal.add(Calendar.DATE,14);
                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                dialog.show();

            }
        });



        btnPickTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final CharSequence timeslot[] = new CharSequence[] {"1pm-2pm", "2pm-3pm", "3pm-4pm", "4pm-5pm"};

                AlertDialog.Builder builder = new AlertDialog.Builder(SubBookingActivity.this);
                builder.setTitle("Select a timeslot");
                builder.setItems(timeslot, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "You've selected " + timeslot[which], Toast.LENGTH_LONG).show();
                        if (timeslot[which].equals("1pm-2pm")) {
                            startDateTime = dateVar + " 13:00:00";
                            endDateTime = dateVar + " 14:00:00";
                            myCalendarStart.set(Calendar.HOUR,13);
                            myCalendarEnd.set(Calendar.HOUR,14);
                            price=20.00;
                        } else if (timeslot[which].equals("2pm-3pm")) {
                            startDateTime= dateVar + " 14:00:00";
                            endDateTime=dateVar + " 15:00:00";
                            myCalendarStart.set(Calendar.HOUR,14);
                            myCalendarEnd.set(Calendar.HOUR,15);
                            price=20.00;
                        } else if (timeslot[which].equals("3pm-4pm")) {
                            startDateTime= dateVar + " 15:00:00";
                            endDateTime=dateVar + " 16:00:00";
                            myCalendarStart.set(Calendar.HOUR,15);
                            myCalendarEnd.set(Calendar.HOUR,16);
                            price=20.00;
                        }else if (timeslot[which].equals("5pm-6pm")) {
                            startDateTime= dateVar + " 17:00:00";
                            endDateTime=dateVar + " 18:00:00";
                            myCalendarStart.set(Calendar.HOUR,17);
                            myCalendarEnd.set(Calendar.HOUR,18);
                            price=20.00;
                        }
                        btnPickTime.setText(timeslot[which]);
                    }
                });
                builder.show();
            }
        });




    }


    protected void sendJson(final String sid, final String uid, final String price, final String starttime, final String endtime,final String sportType,final Facility facility) {

        Intent intent = new Intent(getBaseContext(), PaymentActivity.class);
        intent.putExtra("sid", sid);
        intent.putExtra("uid", uid);
        intent.putExtra("price", price);
        intent.putExtra("starttime", starttime);
        intent.putExtra("endtime", endtime);
        intent.putExtra("sportType", sportType);
        intent.putExtra("facility_key", facility);
        startActivity(intent);
/*

        Thread t = new Thread() {

            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;
                JSONObject json = new JSONObject();

                try {
                    HttpPost post = new HttpPost("http://192.168.31.177:8080/CZ2006/createBookingServlet");//Input Server url
                    json.put("sid", sid);
                    json.put("uid", uid);
                    json.put("price", price);
                    json.put("starttime", starttime);
                    json.put("endtime", endtime);
                    StringEntity se = new StringEntity(json.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                    //Checking response
                    if (response != null) {
                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                        Intent intent = new Intent(getBaseContext(), PaymentActivity.class);
                        intent.putExtra("sid", sid);
                        intent.putExtra("uid", uid);
                        intent.putExtra("price", price);
                        intent.putExtra("starttime", starttime);
                        intent.putExtra("endtime", endtime);
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Looper.loop(); //Loop in the message queue
            }
        };
        t.start(); */

    }

    private void setMenu(){

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar=getSupportActionBar();

        if(AccountInfo.getInstance().getLoginStatus()==true){
            actionBar.setSubtitle("Hi,"+AccountInfo.getInstance().getUserName());
        }

        actionBar.setElevation((float) 2.5);
        actionBar.setTitle("Booking");
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,myToolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
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
                        navigate.setClass(SubBookingActivity.this,HistoryActivity.class);
                        startActivity(navigate);
                        break;
                    case R.id.mSearch:
                        navigate.setClass(SubBookingActivity.this,SearchActivity.class);
                        startActivity(navigate);
                        break;

                    case R.id.mLogin:
                        if(AccountInfo.getInstance().getLoginStatus()==true){
                            AccountInfo.getInstance().setLoginStatus(false);
                            Toast.makeText(SubBookingActivity.this,"You have logged out successfully!",Toast.LENGTH_LONG).show();
                            navigate.setClass(SubBookingActivity.this,SearchActivity.class);
                            startActivity(navigate);
                        }
                        else{
                            navigate.setClass(SubBookingActivity.this,LoginActivity.class);
                            startActivity(navigate);

                        }

                        break;

                    case R.id.mRegister:
                        navigate.setClass(SubBookingActivity.this,RegisterActivity.class);
                        startActivity(navigate);
                        break;

                }
                return false;
            }

        });

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }



}
