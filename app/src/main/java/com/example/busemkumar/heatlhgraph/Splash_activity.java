package com.example.busemkumar.heatlhgraph;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash_activity extends AppCompatActivity {
        ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getSupportActionBar().hide();
        iv = (ImageView)findViewById(R.id.logo);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.splash_effect);
        iv.startAnimation(myanim);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        else {
            new CountDownTimer(3200,500) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    final Intent i=new Intent(Splash_activity.this,Login_screen_activity.class);
                    startActivity(i);
                }
            }.start();
        }
    }
}
