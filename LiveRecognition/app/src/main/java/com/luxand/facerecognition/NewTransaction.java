package com.luxand.facerecognition;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.support.v4.content.ContextCompat;
import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

public class NewTransaction extends AppCompatActivity {

    private Button emailReceipt;
    private Button skipBtn;
    private Button payBtn;
    private Button cancelBtn;
    private Toast mToast;
    private ProgressDialog progress;
    private View pin;
    private View receipt;

    public static final String TAG = "PinLockView";
    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.d(TAG, "Pin complete: " + pin);
            if(pin.equalsIgnoreCase("2828")) {
                openReceipt();
            }else{
                showToast("INCORRECT PIN", Toast.LENGTH_SHORT);
                mPinLockView.resetPinLockView();
            }
        }

        @Override
        public void onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };

    private void showToast(String message, int duration) {
        if (duration != Toast.LENGTH_SHORT && duration != Toast.LENGTH_LONG)
            throw new IllegalArgumentException();
        if (mToast != null && mToast.getView().isShown())
            mToast.cancel(); // Close the toast if it is already open
        mToast = Toast.makeText(this, message, duration);
        mToast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        mToast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_new_transaction);

        emailReceipt = (Button)findViewById(R.id.emailBtn);
        emailReceipt.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                showToast("RECEIPT EMAILED", Toast.LENGTH_SHORT);
                newTransaction();

            }
        });

        skipBtn = (Button)findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){

                newTransaction();

            }
        });

        payBtn = (Button)findViewById(R.id.processPayment);
        payBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){

                openPinSelection();
            }
        });

        TextView dateView = (TextView)findViewById(R.id.textView3);
        setDate(dateView);

        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("username");
        TextView userText = (TextView)findViewById(R.id.textView8);
        userText.setText(username);

        receipt = (View)findViewById(R.id.receipt);
        receipt.setVisibility(View.INVISIBLE);

        pin = (View)findViewById(R.id.pin);
        pin.setVisibility(View.INVISIBLE);

        mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        //mPinLockView.setCustomKeySet(new int[]{1,2,3,4,5,6,7,8,9,0});
        mPinLockView.enableLayoutShuffling();
        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);

        cancelBtn = (Button) findViewById(R.id.cancelPayment);
        cancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                cancelPayment();

            }
        });
    }

    public void setDate (TextView view){

        //String str = String.format("%tc", new Date());
        //view.setText(str);

        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss");//formating according to my need
        String date = formatter.format(today);
        view.setText(date);
    }

    public void showLoadingDialog() {

        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setTitle(getString(R.string.pay_title));
            progress.setMessage(getString(R.string.loading_message));
        }
        progress.show();
    }

    public void newTransaction(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void cancelPayment(){
        showToast("TRANSACTION CANCELLED", Toast.LENGTH_LONG);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openPinSelection(){
        payBtn.setVisibility(View.GONE);
        cancelBtn.setVisibility(View.GONE);
        pin.setVisibility(View.VISIBLE);
    }

    public void openReceipt(){
        showToast("PAYMENT COMPLETE", Toast.LENGTH_LONG);
        pin.setVisibility(View.GONE);
        receipt.setVisibility(View.VISIBLE);
    }
}
