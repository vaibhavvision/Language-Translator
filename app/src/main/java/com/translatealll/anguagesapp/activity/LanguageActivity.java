package com.translatealll.anguagesapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.adapter.LanguageAdapter;
import com.translatealll.anguagesapp.model.CountryRepo;
import com.translatealll.anguagesapp.utils.Const;
import com.translatealll.anguagesapp.utils.PrefFile;

import java.util.ArrayList;

public class LanguageActivity extends AppCompatActivity {
    public static int isSelected = -1;
    private final ArrayList<CountryRepo> mLanguages = new ArrayList<>();
    public String[] langTitle = new String[]{"Hi!\nI'm Sam",
            "Hola!\nSoy Sam", "Salut!\nJe suis Sam", "Hallo!\nIch bin Sam", "Hola!\nSoy Sam", "привет\nя сэм", "嗨\n我是山姆", "merhaba\nben sam", "hoi ik\nben sam", "أنا سام يا\n", "হে\nআমি স্যাম", "Oi,\nEu sou Sam"};
    public String[] langName = new String[]{"English", "Spanish", "French", "German", "Spanish", "Russian", "Chinese", "Turkish", "Dutch", "Arabic", "Bangla", "Portuguese"};
    private LanguageAdapter mItemLanguageAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_language);
        initData();
        handleEvents();
    }

    private void initData() {
        for (int i = 0; i < langTitle.length; i++) {
            CountryRepo country = new CountryRepo(langTitle[i], langName[i]);
            mLanguages.add(country);
        }

        mItemLanguageAdapter = new LanguageAdapter(LanguageActivity.this, mLanguages);
        RecyclerView recyclerView = findViewById(R.id.rvLanguage);
        recyclerView.setAdapter(mItemLanguageAdapter);
    }

    private void handleEvents() {
        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelected == -1) {
                    Toast.makeText(LanguageActivity.this, "Please select your language!", Toast.LENGTH_SHORT).show();
                } else {
                    PrefFile.getInstance().setString(Const.NEXT, "lang");
                    Intent intent = new Intent(LanguageActivity.this, StartActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


}