package com.translatealll.anguagesapp.multiscreen.Intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.databinding.ActivityIntroBinding;
import com.translatealll.anguagesapp.multiscreen.Constant;
import com.translatealll.anguagesapp.multiscreen.PrefFile;
import com.translatealll.anguagesapp.multiscreen.StartActivity;
import com.translatealll.anguagesapp.multiscreen.language.activity.LanguageActivity;


public class IntroActivity extends AppCompatActivity {

    ActivityIntroBinding binding;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txtnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == 0) {
                    binding.ivIntroImage.setImageResource(R.drawable.ic_intro_2);
                    binding.tvDot.setImageResource(R.drawable.ic_dot_2);
                    binding.tvTitle.setText(R.string.title2);
                    binding.tvDesc.setText(R.string.dec2);
                } else if (index == 1) {
                    binding.ivIntroImage.setImageResource(R.drawable.ic_intro_3);
                    binding.tvDot.setImageResource(R.drawable.ic_dot_3);
                    binding.tvTitle.setText(R.string.title3);
                    binding.tvDesc.setText(R.string.dec3);
                    binding.txtnext.setText(R.string.txtletsstrat);
                } else {
                    goToNextScreen();
                }
                index++;
            }
        });
    }

    public void goToNextScreen() {
//        PrefFile.getInstance().setString(Constant.NEXT, "intro");
        Intent intent = new Intent(this, LanguageActivity.class);
        startActivity(intent);
//        InterstitialAds.showAds(this, intent, false, true);
        finish();
    }
}