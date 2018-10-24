package com.ibbumobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class PhotosActivity extends AppCompatActivity {

    CardView btnFresh;
    CardView btnReturn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnFresh = findViewById(R.id.fresh);
        btnFresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FreshStudentsFragment freshStudentsFragment = new FreshStudentsFragment();
                freshStudentsFragment.setArguments(getIntent().getExtras());

                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, freshStudentsFragment).commit();

            }
        });

        ////////////////////////////////
        btnReturn = findViewById(R.id.returing);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReturningStudentsFragment returningStudentsFragment = new ReturningStudentsFragment();
                returningStudentsFragment.setArguments(getIntent().getExtras());

                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, returningStudentsFragment).commit();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
