package com.example.chen_hsi.androidtutnonfregment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentActivity extends AppCompatActivity {


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
                cardcsc.setError("Please key in a valid name!");
                valid = false;
            }

            return valid;
        }


        public void onPaymentSuccess() {

            Intent myIntent = new Intent(PaymentActivity.this, SearchActivity.class);
            PaymentActivity.this.startActivity(myIntent);
        }

        public void onPaymentFailed() {
            Toast.makeText(getBaseContext(), "Payment failed", Toast.LENGTH_LONG).show();


        }

        public void finishpayment(){
            final ProgressDialog progressDialog = new ProgressDialog(PaymentActivity.this,
                    R.style.AppTheme_Dark_Dialog);


            if(!validate()){
                onPaymentFailed();
                return;}
            else {
                progressDialog.show();
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                onPaymentSuccess();

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



    }
}
