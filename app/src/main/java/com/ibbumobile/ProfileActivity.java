package com.ibbumobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibbumobile.model.Student;

public class ProfileActivity extends AppCompatActivity {

    TextView textName;
    TextView textMat;
    TextView textLevel;
    TextView textDept;
    TextView textMail;
    ImageView imageView;

    Button btnLogout;
    Button btnNewSesion;

    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textName = findViewById(R.id.textName);
        textMat = findViewById(R.id.textMat);
        textLevel = findViewById(R.id.textLevel);
        textDept = findViewById(R.id.textDept);
        textMail = findViewById(R.id.textMail);

        ///////////////// Get User Detail
        DBHelper helper = new DBHelper(getBaseContext());
        student = helper.getUser();

        textName.setText(student.getFirstName()+" "+student.getLastName());
        textMat.setText(student.getMatNum().toUpperCase());
        textLevel.setText(student.getLevel() +" Level");
        textDept.setText(student.getDept());
        textMail.setText(student.getEmail().toLowerCase());

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        btnNewSesion = findViewById(R.id.btnNewSession);

        btnNewSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newSession();
            }
        });

    }

    void logout() {

        DBHelper helper = new DBHelper(getBaseContext());
        helper.removeUser(student.getMatNum());
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    void newSession() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
