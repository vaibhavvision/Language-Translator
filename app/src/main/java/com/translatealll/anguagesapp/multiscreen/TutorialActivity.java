package com.translatealll.anguagesapp.multiscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.databinding.ActivityTutorialBinding;


public class TutorialActivity extends AppCompatActivity implements View.OnClickListener {
    private static int currentPage = 0;
    private static final int NUM_PAGES = 0;
    public String single_choice_selected;
    TutorialAdapter tutorialAdapter;
    int pos = 0;

    int[] images = {R.drawable.ic_intro_1, R.drawable.ic_intro_2, R.drawable.ic_intro_3};

    String[] title = {"Translation Text", "Translate Camera", "Translate Conversation"};

    String[] detail = {"Easily translate long or short passages of text\n" +
            "from any language into the target language\n" +
            "in just a few seconds.", "Capture images to translate directly from\n" +
            "information boards, documents, or any\n" +
            "bold text quickly and efficie..", "Translate every conversation in real-time, no\n" +
            "more language barriers you and the\n" +
            "world."};


    ActivityTutorialBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnNext.setOnClickListener(this);
        tutorialAdapter = new TutorialAdapter(this, images, title, detail);
        binding.viewPager.setAdapter(tutorialAdapter);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                pos = position;
                if (pos == 0) {
                    binding.ivDots.setImageResource(R.drawable.ic_dot_1);
                    binding.btnNext.setText("Next");
                } else if (pos == 1) {
                    binding.btnNext.setText("Next");
                    binding.ivDots.setImageResource(R.drawable.ic_dot_2);
                } else if (pos == 2) {
                    binding.btnNext.setText("Get Started");
                    binding.ivDots.setImageResource(R.drawable.ic_dot_3);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnNext) {
            if (pos == 0) {
                binding.viewPager.setCurrentItem(pos + 1);
            } else if (pos == 1) {
                binding.viewPager.setCurrentItem(pos + 1);
            } else if (pos == 2) {
                PrefFile.getInstance().setString(Constant.NEXT, "intro");
                Intent intent = new Intent(TutorialActivity.this, LanguageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                InterstitialClickAd.loadAndShowInterAds(TutorialActivity.this, intent, true, true, AD_TYPE1.ADMOB1,false);

            }
        }
    }
}