package com.arunsudhir.radiomalayalam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arunsudhir.radiomalayalam.service.PlayerService;

public class NoConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);

        // Add click listener to the retry (Try again) button
        final Button buttonTryAgain = (Button) findViewById(R.id.button_try_again);
        buttonTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(thisActivity, PlayListActivity.class);
                try {
                        startService(serviceIntent);
                } catch (Exception e) {
                    //LOG.error(e, "Failed to toggle state");
                }
            }
        });
    }
}
