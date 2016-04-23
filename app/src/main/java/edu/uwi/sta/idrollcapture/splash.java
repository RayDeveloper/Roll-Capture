package edu.uwi.sta.idrollcapture;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Raydon on 4/16/2016.
 */
public class splash extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;//1sec
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);//sets custom layout for splash screen


                /* New Handler to start the Menu-Activity
         * and close the Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Creates an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(splash.this, MainActivity.class);//after time elapses launch the main activity
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
