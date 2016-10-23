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
        import android.widget.Button;
        import android.widget.DatePicker;
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
    String dateVar;
    String timeVar;
    String startDateTime;
    String endDateTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_booking);

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
                sendJson(sportid,userid,"33.89",startDateTime,endDateTime);

            }
        });


        Button btnPickDate=(Button) findViewById(R.id.btnPickDate);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                TextView pickDateTxtBox = (TextView)findViewById(R.id.pickDate);
                dateVar=year + "-" + monthOfYear + "-" + dayOfMonth;
                pickDateTxtBox.setText(dateVar);
                pd=ProgressDialog.show(SubBookingActivity.this, "", "Querying for available timeslot", false);
                pd.dismiss();


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
                        } else if (timeslot[which].equals("2pm-3pm")) {
                            startDateTime= dateVar + " 14:00:00";
                            endDateTime=dateVar + " 15:00:00";
                        } else if (timeslot[which].equals("3pm-4pm")) {
                            startDateTime= dateVar + " 15:00:00";
                            endDateTime=dateVar + " 16:00:00";
                        }else if (timeslot[which].equals("5pm-6pm")) {
                            startDateTime= dateVar + " 14:00:00";
                            endDateTime=dateVar + " 15:00:00";
                        }

                    }
                });
                builder.show();
            }

        };

        btnPickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SubBookingActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });





    }


    protected void sendJson(final String sid, final String uid, final String price, final String starttime, final String endtime) {
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

                    /*Checking response */
                    if (response != null) {
                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Looper.loop(); //Loop in the message queue
            }
        };
        t.start();
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
                    case R.id.mHome:
                        break;
                    case R.id.mBook:
                        break;

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
