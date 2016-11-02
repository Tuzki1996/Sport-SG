package com.example.chen_hsi.androidtutnonfregment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.example.chen_hsi.androidtutnonfregment.JSONParser.is;
import static com.example.chen_hsi.androidtutnonfregment.JSONParser.json;
import static com.example.chen_hsi.androidtutnonfregment.R.id.occupancyRate;
import static com.example.chen_hsi.androidtutnonfregment.R.id.textView;
import static com.example.chen_hsi.androidtutnonfregment.SearchActivity.items;
import static java.util.Calendar.HOUR_OF_DAY;

public class SubBookingActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    ProgressDialog pd;
    String userid = "";
    String facilityid = "";
    String dateVar;
    String timeVar;
    String timeslotVar;
    String startDateTime;
    String endDateTime;
    String sportType;
    Sport sport = null;
    Facility facility;
    int occupancyRateCur;
    int occupancyRateMax;
    List<String> timeSlotStr = new ArrayList<String>();


    double price;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_booking);
        facility = (Facility) getIntent().getSerializableExtra("facility_key");
        facilityid = String.valueOf(facility.getFacility_id());
        userid = AccountInfo.getInstance().getUserId();
        setMenu();
        setNavigation();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        final Button fab = (Button) findViewById(R.id.confirm);
        fab.setEnabled(false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Booking In Progress", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                sendJson(Integer.toString(sport.getId()), userid, Double.toString(price), startDateTime, endDateTime, sportType, facility);

            }
        });


        final Button btnPickDate = (Button) findViewById(R.id.btnPickDate);
        final Button btnPickTime = (Button) findViewById(R.id.btnPickTime);
        final TextView fnameTv = (TextView) findViewById(R.id.facility_name);
        final TextView occupancyRate = (TextView) findViewById(R.id.occupancyRate);


        Spinner dropdown = (Spinner) findViewById(R.id.sportType);
        final ArrayList<Sport> facilitySportList = facility.getSportList();
        String[] items = new String[facilitySportList.size()];
        int i = 0;
        for (Sport sport : facilitySportList) {
            items[i] = sport.getSport_type().getName();
            i++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int pos, long id) {
                //Toast.makeText(getBaseContext(), "You've selected " + parentView.getItemAtPosition(pos), Toast.LENGTH_LONG).show();
                sportType = parentView.getItemAtPosition(pos).toString();
                sport = facilitySportList.get(pos);
                price = sport.getPrice();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        if (facility != null) {
            fnameTv.setText(facility.getFacility_name());
        } else {
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
                dateVar = year + "-" + monthOfYear + "-" + dayOfMonth;
                //pickDateTxtBox.setText(dateVar);
                pd = ProgressDialog.show(SubBookingActivity.this, "", "Querying for available timeslot", false);
                pd.dismiss();

                btnPickDate.setText(dateVar);

                btnPickTime.setEnabled(true);
                btnPickTime.setText("PICK A TIME");


                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH");
                int hour = Integer.parseInt(sdf.format(cal.getTime()));

                //Define Time Slots
                if (DateUtils.isToday(myCalendarStart.getTimeInMillis())) {
                    if (hour <= 18) {
                        timeSlotStr.add("7pm-8pm");
                        if (hour <= 17) {
                            timeSlotStr.add("6pm-7pm");
                            if (hour <= 16) {
                                timeSlotStr.add("5pm-6pm");
                                if (hour <= 15) {
                                    timeSlotStr.add("4pm-5pm");
                                    if (hour <= 14) {
                                        timeSlotStr.add("3pm-4pm");
                                        if (hour <= 13) {
                                            timeSlotStr.add("2pm-3pm");
                                            if (hour <= 12) {
                                                timeSlotStr.add("1pm-2pm");
                                                if (hour <= 11) {
                                                    timeSlotStr.add("12pm-1pm");
                                                    if (hour <= 10) {
                                                        timeSlotStr.add("11am-12pm");
                                                        if (hour <= 9) {
                                                            timeSlotStr.add("10am-11am");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        btnPickTime.setEnabled(false);
                        btnPickTime.setText("No Time Slot Available");

                    }
                }
                ;
            }

        };

        btnPickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog dialog = new DatePickerDialog(SubBookingActivity.this, date, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH));
                Calendar cal = Calendar.getInstance();
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                cal.add(Calendar.DATE, 14);
                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                dialog.show();

            }
        });


        btnPickTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                final CharSequence timeslot[] = new CharSequence[]{"10am-11am", "11am-12pm", "12pm-1pm", "1pm-2pm", "2pm-3pm", "3pm-4pm", "4pm-5pm", "5pm-6pm", "6pm-7pm", "7pm-8pm"};
                final CharSequence[] items;
                if (DateUtils.isToday(myCalendarStart.getTimeInMillis())) {
                    items = timeSlotStr.toArray(new String[timeSlotStr.size()]);
                } else {
                    items = timeslot;
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(SubBookingActivity.this);
                builder.setTitle("Select a timeslot");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getBaseContext(), "You've selected " + timeslot[which], Toast.LENGTH_LONG).show();
                        if (items[which].equals("10am-11am")) {
                            startDateTime = dateVar + " 10:00:00";
                            endDateTime = dateVar + " 11:00:00";
                            myCalendarStart.set(Calendar.HOUR, 10);
                            myCalendarEnd.set(Calendar.HOUR, 11);
                            timeslotVar = "1";
                        } else if (items[which].equals("11am-12pm")) {
                            startDateTime = dateVar + " 11:00:00";
                            endDateTime = dateVar + " 12:00:00";
                            myCalendarStart.set(Calendar.HOUR, 11);
                            myCalendarEnd.set(Calendar.HOUR, 12);
                            timeslotVar = "2";
                        } else if (items[which].equals("12pm-1pm")) {
                            startDateTime = dateVar + " 12:00:00";
                            endDateTime = dateVar + " 13:00:00";
                            myCalendarStart.set(Calendar.HOUR, 12);
                            myCalendarEnd.set(Calendar.HOUR, 13);
                            timeslotVar = "3";
                        } else if (items[which].equals("1pm-2pm")) {
                            startDateTime = dateVar + " 13:00:00";
                            endDateTime = dateVar + " 14:00:00";
                            myCalendarStart.set(Calendar.HOUR, 13);
                            myCalendarEnd.set(Calendar.HOUR, 14);
                            timeslotVar = "4";
                        } else if (items[which].equals("2pm-3pm")) {
                            startDateTime = dateVar + " 14:00:00";
                            endDateTime = dateVar + " 15:00:00";
                            myCalendarStart.set(Calendar.HOUR, 14);
                            myCalendarEnd.set(Calendar.HOUR, 15);
                            timeslotVar = "5";
                        } else if (items[which].equals("3pm-4pm")) {
                            startDateTime = dateVar + " 15:00:00";
                            endDateTime = dateVar + " 16:00:00";
                            myCalendarStart.set(Calendar.HOUR, 15);
                            myCalendarEnd.set(Calendar.HOUR, 16);
                            timeslotVar = "6";
                        } else if (items[which].equals("4pm-5pm")) {
                            startDateTime = dateVar + " 16:00:00";
                            endDateTime = dateVar + " 17:00:00";
                            myCalendarStart.set(Calendar.HOUR, 16);
                            myCalendarEnd.set(Calendar.HOUR, 17);
                            timeslotVar = "7";
                        } else if (items[which].equals("5pm-6pm")) {
                            startDateTime = dateVar + " 17:00:00";
                            endDateTime = dateVar + " 18:00:00";
                            myCalendarStart.set(Calendar.HOUR, 17);
                            myCalendarEnd.set(Calendar.HOUR, 18);
                            timeslotVar = "8";
                        } else if (items[which].equals("6pm-7pm")) {
                            startDateTime = dateVar + " 18:00:00";
                            endDateTime = dateVar + " 19:00:00";
                            myCalendarStart.set(Calendar.HOUR, 18);
                            myCalendarEnd.set(Calendar.HOUR, 19);
                            timeslotVar = "9";
                        } else if (items[which].equals("7pm-8pm")) {
                            startDateTime = dateVar + " 19:00:00";
                            endDateTime = dateVar + " 20:00:00";
                            myCalendarStart.set(Calendar.HOUR, 19);
                            myCalendarEnd.set(Calendar.HOUR, 20);
                            timeslotVar = "10";
                        }


                        btnPickTime.setText(timeslot[which]);
                        Thread t = new Thread() {

                            public void run() {
                                Looper.prepare(); //For Preparing Message Pool for the child Thread
                                HttpClient client = new DefaultHttpClient();
                                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                                HttpResponse response;


                                try {
                                    HttpPost post = new HttpPost("http://hsienyan1994.pagekite.me:8080/CZ2006/getUserServlet?requestType=occupancyrate&sid=" + Integer.toString(sport.getId()) + "&date=" + dateVar + "&timeslot=" + timeslotVar);//Input Server url

                                    response = client.execute(post);
                                    //Checking response
                                    if (response != null) {
                                        //Toast.makeText(getBaseContext(), "Result Response", Toast.LENGTH_LONG).show();
                                        HttpEntity httpEntity = response.getEntity();
                                        is = httpEntity.getContent();

                                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                                is, "iso-8859-1"), 8);
                                        StringBuilder sb = new StringBuilder();
                                        String line = null;
                                        while ((line = reader.readLine()) != null) {
                                            sb.append(line + "\n");
                                        }
                                        is.close();


                                        final JSONObject json = new JSONObject(sb.toString());
                                        occupancyRate.post(new Runnable() {
                                            public void run() {
                                                try {
                                                    occupancyRateCur = Integer.parseInt(json.getString("count"));
                                                    occupancyRateMax = sport.getSport_type().getSizeAllow();
                                                    occupancyRate.setText(json.getString("count") + "/" + Integer.toString(occupancyRateMax));
                                                    if (occupancyRateCur >= occupancyRateMax) {
                                                        Toast.makeText(getBaseContext(), "Current time slot is full", Toast.LENGTH_LONG).show();
                                                        fab.setEnabled(false);
                                                        fab.setText("Please Select another Time Slot");
                                                    } else {
                                                        fab.setEnabled(true);
                                                        fab.setText("CONFIRM");
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });


                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Looper.loop(); //Loop in the message queue
                            }
                        };
                        t.start();
                    }
                });
                builder.show();

            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    protected void sendJson(final String sid, final String uid, final String price, final String starttime, final String endtime, final String sportType, final Facility facility) {
        Thread t = new Thread() {

            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;
                JSONObject json = new JSONObject();

                try {
                    HttpPost post = new HttpPost("http://hsienyan1994.pagekite.me:8080/CZ2006/getUserServlet?requestType=booking");//Input Server url
                    json.put("sid", sid);
                    json.put("uid", uid);
                    json.put("price", price);
                    json.put("date", dateVar);
                    json.put("timeslot", timeslotVar);
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
                        intent.putExtra("sportType", sportType);
                        intent.putExtra("facility_key", facility);
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Looper.loop(); //Loop in the message queue
            }
        };
        t.start();

    }

    private void setMenu() {

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (AccountInfo.getInstance().getLoginStatus() == true) {
            actionBar.setSubtitle("Hi," + AccountInfo.getInstance().getUserName());
        }

        actionBar.setElevation((float) 2.5);
        actionBar.setTitle("Booking");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    private void setNavigation() {
        navigationView = (NavigationView) findViewById(R.id.left_drawer);
        Menu drawerMenu = navigationView.getMenu();
        View headerView = navigationView.inflateHeaderView(R.layout.navigation_drawer_header);
        TextView accountInfo = (TextView) headerView.findViewById(R.id.tAccountInfo);
        TextView name = (TextView) headerView.findViewById(R.id.tName);
        TextView firstName = (TextView) headerView.findViewById(R.id.tFirstName);
        TextView lastName = (TextView) headerView.findViewById(R.id.tLastName);
        TextView email = (TextView) headerView.findViewById(R.id.tEmail);
        TextView emailAdd = (TextView) headerView.findViewById(R.id.tEmalAdd);

        MenuItem loginItem = drawerMenu.findItem(R.id.mLogin);
        MenuItem regisgerItem = drawerMenu.findItem(R.id.mRegister);

        if (AccountInfo.getInstance().getLoginStatus() == true) {
            loginItem.setTitle("LOGOUT");
            regisgerItem.setVisible(false);
            accountInfo.setText("ACCOUNT INFO");
            name.setText("NAME:");
            firstName.setText(AccountInfo.getInstance().getUserName());
            lastName.setText(AccountInfo.getInstance().getLastName());
            email.setText("EMAIL:");
            emailAdd.setText(AccountInfo.getInstance().getEmail());


        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent navigate = new Intent();

                switch (item.getItemId()) {

                    case R.id.mHistory:
                        navigate.setClass(SubBookingActivity.this, HistoryActivity.class);
                        startActivity(navigate);
                        break;
                    case R.id.mSearch:
                        navigate.setClass(SubBookingActivity.this, SearchActivity.class);
                        startActivity(navigate);
                        break;

                    case R.id.mLogin:
                        if (AccountInfo.getInstance().getLoginStatus() == true) {
                            AccountInfo.getInstance().setLoginStatus(false);
                            Toast.makeText(SubBookingActivity.this, "You have logged out successfully!", Toast.LENGTH_LONG).show();
                            navigate.setClass(SubBookingActivity.this, SearchActivity.class);
                            startActivity(navigate);
                        } else {
                            navigate.setClass(SubBookingActivity.this, LoginActivity.class);
                            startActivity(navigate);

                        }

                        break;

                    case R.id.mRegister:
                        navigate.setClass(SubBookingActivity.this, RegisterActivity.class);
                        startActivity(navigate);
                        break;

                }
                return false;
            }

        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SubBooking Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
