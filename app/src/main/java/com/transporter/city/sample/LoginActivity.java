package com.transporter.city.sample;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.transporter.sdk.Transporter;

/**
 * Created by Transporter on 20/11/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_userID;
    Button login;
    String userId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        if(!isLogin()) {
            et_userID = (EditText) findViewById(R.id.userId);
            login = (Button) findViewById(R.id.login);
            login.setOnClickListener(this);
            Transporter.initialize(this, "YOUR PUBLISHABLE KEY");
        }else {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    public boolean isLogin(){
        SharedPreferences prefs = this.getSharedPreferences("SampleTransporter",Activity.MODE_PRIVATE);
        return prefs.getBoolean("isLogin",false);
    }

    @Override
    public void onClick(View view) {
        userId = et_userID.getText().toString();
        if (userId.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please Enter User Id", Toast.LENGTH_SHORT).show();
            return;
        }
        checkForLocationSettings();
    }

    public void checkForLocationSettings() {
        // Check for Location permission
        // This block is only for build versions 23 and above
        if (!Transporter.checkSelfPermissions(this)) {
            Transporter.requestPermissions(this);
            return;
        }

        // Check for Location settings
        if (!Transporter.isLocationEnabled(this)) {
            Transporter.requestLocationSettings(this);
            return;
        }
        appLogic();
    }

    public void appLogic() {
        Transporter.setUserId(this,userId);
        setLogin(true);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    public void setLogin(Boolean login){
        SharedPreferences prefs = this.getSharedPreferences("SampleTransporter", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLogin", login);
        editor.apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Transporter.REQUEST_LOCATION_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkForLocationSettings();
            } else {
                // Handle Location Permission denied error
                Toast.makeText(this, "Location Permission denied.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Transporter.REQUEST_LOCATION_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                checkForLocationSettings();
            } else {
                // Handle Enable Location Services request denied error
                Toast.makeText(this, "Enable Location Services request denied.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
