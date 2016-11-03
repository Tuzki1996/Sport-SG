package com.example.chen_hsi.androidtutnonfregment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class PaymentActivity extends AppCompatActivity {

    int sid,uid;
    String price,starttime,endtime,sportType;
    Facility facility;

    public boolean validate() {
        boolean valid = true;
        EditText cardexpirydate = (EditText)findViewById(R.id.cardexpirydate);
        EditText cardno = (EditText)findViewById(R.id.cardno);
        EditText cardname = (EditText)findViewById(R.id.cardname);
        EditText cardcsc = (EditText)findViewById(R.id.cardcsccode);



        if (cardno.getText().toString().isEmpty() || cardno.length()<16||cardno.length()>16) {
            cardno.setError("Please Enter a valid Card Number!");
            valid = false;
        }

        if (cardexpirydate.getText().toString().isEmpty() ) {
            cardexpirydate.setError("Please key in an expiry date!");
            valid = false;
        }


        if (cardname.getText().toString().isEmpty() ) {
            cardname.setError("Please key in a valid name!");
            valid = false;
        }

        if (cardcsc.getText().toString().isEmpty() ) {
            cardcsc.setError("Please key in a CSC code!");
            valid = false;
        }

        return valid;
    }


    public void onPaymentSuccess() {

        Intent myIntent = new Intent(PaymentActivity.this, SearchActivity.class);
        PaymentActivity.this.startActivity(myIntent);
        Toast.makeText(getBaseContext(), "Successful Payment ", Toast.LENGTH_LONG).show();
        this.finish();

    }

    public void onPaymentFailed() {
        Toast.makeText(getBaseContext(), "Payment failed", Toast.LENGTH_LONG).show();
    }

    public void finishpayment(){
        final ProgressDialog progressDialog = new ProgressDialog(PaymentActivity.this,
                R.style.AppTheme_Dark_Dialog);


        if(!validate()){
            onPaymentFailed();
            return;
        }
        else {
            progressDialog.show();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            Random rnd=new Random();
            final int otp = 100000 + rnd.nextInt(900000);
                Thread t = new Thread() {

                    public void run() {
                        Looper.prepare(); //For Preparing Message Pool for the child Thread
                        HttpClient client = new DefaultHttpClient();
                        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                        HttpResponse response;
                        JSONObject json = new JSONObject();

                        try {
                            HttpPost post = new HttpPost("http://smsgateway.me/api/v3/messages/send");//Input Server url
                            json.put("email", "xiaojia1993@gmail.com");
                            json.put("password", "forsmsgateway");
                            json.put("device", "32326");
                            json.put("number", AccountInfo.getInstance().getPhoneNo());
                            json.put("message", "Welcome to Sports@SG, Your Payment OTP is " + otp);
                            StringEntity se = new StringEntity(json.toString());
                            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                            post.setEntity(se);
                            response = client.execute(post);

                            //Checking response
                            if (response != null) {
                                InputStream in = response.getEntity().getContent(); //Get the data in the entity
                                progressDialog.cancel();


                                LayoutInflater layoutInflater = LayoutInflater.from(PaymentActivity.this);
                                View promptView = layoutInflater.inflate(R.layout.otp_dialog, null);
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PaymentActivity.this);
                                alertDialogBuilder.setView(promptView);

                                final EditText editOTP = (EditText) promptView.findViewById(R.id.editOTP);
                                // setup a dialog window
                                alertDialogBuilder.setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int id) {

                                                int editOTPInt=Integer.parseInt(editOTP.getText().toString());
                                                if(editOTPInt==otp){
                                                    onPaymentSuccess();
                                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                    Calendar calstart = Calendar.getInstance();
                                                    Calendar calend = Calendar.getInstance();
                                                    try {
                                                        calstart.setTime(format.parse(starttime));
                                                        calend.setTime(format.parse(endtime));
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }

                                                    Toast.makeText(getBaseContext(), "Successful Payment ", Toast.LENGTH_LONG).show();
                                                    Intent calintent = new Intent(Intent.ACTION_EDIT);
                                                    calintent.setType("vnd.android.cursor.item/event");
                                                    calintent.putExtra("eventLocation", facility.getFacility_name());
                                                    calintent.putExtra("title", "Sport @ SG booking reminder");
                                                    calintent.putExtra("description", "Reminder "+facility.getFacility_name()+"-"+sportType);
                                                    calintent.putExtra("beginTime",  calstart.getTimeInMillis());
                                                    calintent.putExtra("endTime",calend.getTimeInMillis());
                                                    startActivity(calintent);
                                                }else {
                                                    Toast.makeText(getBaseContext(), "OTP Incorrect, Please Submit Again", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        })
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                // create an alert dialog
                                AlertDialog alert = alertDialogBuilder.create();
                                alert.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Looper.loop(); //Loop in the message queue
                    }
                };
                t.start();




        }

    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Button button = (Button) findViewById(R.id.submitpayment);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finishpayment();
            }
        });
        Bundle vars = getIntent().getExtras();
        TextView facilityName = (TextView)findViewById(R.id.facility_name);
        TextView startTime = (TextView)findViewById(R.id.startTime);
        TextView endTime = (TextView)findViewById(R.id.endTime);
        TextView priceInput = (TextView)findViewById(R.id.price);
        TextView sportTypeInput = (TextView)findViewById(R.id.sportType);


        if(vars!=null){
            sid = vars.getInt("sid");
            uid = vars.getInt("uid");
            price = vars.getString("price");
            starttime = vars.getString("starttime");
            endtime = vars.getString("endtime");
            sportType=vars.getString("sportType");
            facility=(Facility) getIntent().getSerializableExtra("facility_key");

            startTime.setText(starttime);
            endTime.setText(endtime);
            priceInput.setText(price);
            sportTypeInput.setText(sportType);
            if(facility!=null){
                facilityName.setText(facility.getFacility_name());
            }else{
                facilityName.setText("Default Facility Name");
            }

        }






    }
}
