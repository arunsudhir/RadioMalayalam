package com.arunsudhir.radiomalayalam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class NoConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);

        // Add click listener to the retry (Try again) button
        final Button buttonTryAgain = (Button) findViewById(R.id.button_try_again);
        final Activity noConnectionActivity = this;
        buttonTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playListIntent = new Intent(noConnectionActivity, PlayListActivity.class);
                try {
                        startActivity(playListIntent);
                } catch (Exception e) {
                    //LOG.error(e, "Failed to toggle state");
                }
            }
        });
    }
}
