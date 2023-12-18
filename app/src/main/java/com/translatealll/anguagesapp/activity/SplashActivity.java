package com.translatealll.anguagesapp.activity;


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
import com.translatealll.anguagesapp.database.DownloadedLngsTable;
import com.translatealll.anguagesapp.database.RoomDB;
import com.translatealll.anguagesapp.utils.Constant;
import com.translatealll.anguagesapp.utils.PrefFile;


public class SplashActivity extends AppCompatActivity {


    Intent intent;
    RoomDB roomDB;

    ImageView imageView,imglogo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        imageView = findViewById(R.id.imganim);
        imglogo = findViewById(R.id.imglogo);

        roomDB = RoomDB.getRoomDBInstance(this);
        DownloadedLngsTable downloadedLngs_Table = new DownloadedLngsTable();
        downloadedLngs_Table.setDownloadedlng_name("ENGLISH");
        if (roomDB.downloadedlngs_dao().SelectDownloadedLngs().size() == 0) {
            roomDB.downloadedlngs_dao().InsertDownloaded_lngs(downloadedLngs_Table);
        }

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

        if (PrefFile.getInstance().getString(Constant.NEXT).equals("intro")) {
            intent = new Intent(SplashActivity.this, LanguageActivity.class);
        } else if (PrefFile.getInstance().getString(Constant.NEXT).equals("lang")) {
            intent = new Intent(SplashActivity.this, StartActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, IntroActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


}