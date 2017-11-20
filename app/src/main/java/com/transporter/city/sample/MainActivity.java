package com.transporter.city.sample;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.transporter.sdk.Transporter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button start, stop, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    public void initViews(){
        start = (Button) findViewById(R.id.start_tracking);
        stop = (Button) findViewById(R.id.stop_tracking);
        logout = (Button) findViewById(R.id.logout);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_tracking:
                Transporter.startTracking(this);
                Toast.makeText(MainActivity.this,"Tracking Enabled",Toast.LENGTH_SHORT).show();
                break;
            case R.id.stop_tracking:
                Transporter.stopTracking(this);
                Toast.makeText(MainActivity.this,"Tracking Disabled",Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                Transporter.stopTracking(this);
                setLogin(false);
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
        }
    }

    public void setLogin(Boolean login){
        SharedPreferences prefs = this.getSharedPreferences("SampleTransporter", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLogin", login);
        editor.apply();
    }
}
