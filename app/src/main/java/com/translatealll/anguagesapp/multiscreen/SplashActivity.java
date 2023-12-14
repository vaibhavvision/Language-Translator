package com.translatealll.anguagesapp.multiscreen;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.multiscreen.Intro.IntroActivity;
import com.translatealll.anguagesapp.multiscreen.language.activity.LanguageActivity;


public class SplashActivity extends AppCompatActivity {


    Intent intent;

    ImageView imageView,imglogo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        imageView = findViewById(R.id.imganim);
        imglogo = findViewById(R.id.imglogo);


        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_popup);
        imageView.startAnimation(animation);

        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_ai_popup);
        imglogo.startAnimation(animation1);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                inNext();
            }
        }, 3000);
    }


    private void inNext() {
        /*if (PrefFile.getInstance().getString(Constant.NEXT).equals("intro")) {
            intent = new Intent(SplashActivity.this, LanguageActivity.class);
        } else if (PrefFile.getInstance().getString(Constant.NEXT).equals("lang")) {
            intent = new Intent(SplashActivity.this, StartActivity.class);
        } else if (PrefFile.getInstance().getString(Constant.NEXT).equals("start")) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, TutorialActivity.class);
        }*/
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(new Intent(SplashActivity.this, IntroActivity.class));
//        startActivity(intent);
        finish();
    }


}