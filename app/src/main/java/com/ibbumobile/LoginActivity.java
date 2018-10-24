package com.ibbumobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ibbumobile.model.Student;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnGuest;
    EditText id;
    EditText password;
    TextView errorMsg;

    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnLogin = findViewById(R.id.btnLogin);
        btnGuest = findViewById(R.id.btnGuest);
        errorMsg = findViewById(R.id.errorMsg);

        id = findViewById(R.id.edtID);
        password = findViewById(R.id.edtPass);

        preferences = getSharedPreferences("appSettings", MODE_PRIVATE);
        editor = preferences.edit();

//        if (!isNetworkAvailable()) {
//
//            errorMsg.setText("No Internet connecton detetected");
//            id.setEnabled(false);
//            password.setEnabled(false);
//            btnLogin.setEnabled(false);
//            btnGuest.setEnabled(false);
//
//        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Student student = verifyUser();

                if (student == null) {

                    errorMsg.setVisibility(View.VISIBLE);
                    errorMsg.setText("Sorry, unrecognized username or password");
                    return;
                }

                //////////// Save info to DB
                DBHelper helper = new DBHelper(getBaseContext());
                helper.insertUser(student);

                ///////////  Start Intent
                //SyncAdapter.syncImmediately(getBaseContext());
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                SyncAdapter.syncImmediately(getBaseContext());

            }
        });

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString("userType", "Guest");
                editor.commit();

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);


            }
        });


    }

    Student verifyUser() {

        String name = id.getText().toString();
        String pass = password.getText().toString();

//        WebDriver webDriver = new RemoteWebDriver(DesiredCapabilities.android());
//        webDriver.get("http://portals.ibbu.edu.ng/ugreg/");
//
//        webDriver.findElement(By.cssSelector("#edit-name")).sendKeys(name);
//        webDriver.findElement(By.cssSelector("#edit-pass")).sendKeys(pass);
//        webDriver.findElement(By.cssSelector("#edit-submit")).click();
//
//        webDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
//
//        webDriver.close();
//        webDriver.quit();
//
//        String errorResponse = webDriver.findElement(By.cssSelector("resp")).getText();

//        if (errorResponse.contains("Sorry, unrecognized username or password")) {
//            return null;
//        }

        Student student = new Student("firstName", "lastName", "othername", "mat", "gender", "mail", "jhdh", "54");

        editor.putString("userType", "Member");
        editor.commit();

        return student;
    }


    public boolean isNetworkAvailable() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;
        }

        return connected;
    }

    boolean validateCredential(){

        if (id.getText().toString().length() < 15 && password.getText().toString().length() < 6){
            return false;
        }

        return false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        isNetworkAvailable();
    }
}
