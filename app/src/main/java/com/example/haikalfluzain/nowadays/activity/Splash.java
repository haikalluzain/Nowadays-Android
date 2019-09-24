package com.example.haikalfluzain.nowadays.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haikalfluzain.nowadays.R;

public class Splash extends AppCompatActivity {
    ImageView img;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getIntent().getBooleanExtra("EXIT", false))
        {
            finish();
        }else{
            img = findViewById(R.id.img_splash);
            text = findViewById(R.id.txt_splash);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
            animation.setDuration(500);
            img.startAnimation(animation);
            text.startAnimation(animation);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Splash.this,Main.class));
                }
            },1000);
        }


    }
}
