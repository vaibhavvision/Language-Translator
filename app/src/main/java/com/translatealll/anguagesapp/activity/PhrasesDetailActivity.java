package com.translatealll.anguagesapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.adapter.PhrasesDetailAdapter;
import com.translatealll.anguagesapp.model.PhrasesRepo;
import com.translatealll.anguagesapp.utils.AllLanguage;

import java.util.ArrayList;


public class PhrasesDetailActivity extends AppCompatActivity {
    ArrayList<PhrasesRepo> complete_list = new ArrayList<>();
    String lang1name;
    String lang2name;
    int phraseposition;
    String[] phrases_list;
    RecyclerView phrasesdetail_recview;
    String phrasetitle;
    String[] translation_list;
    TextView tv_phrasetitle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        char c = 0;
        char c2 = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrases_detail);
        tv_phrasetitle = (TextView) findViewById(R.id.tvTitle);
        ImageView imageView = (ImageView) findViewById(R.id.ivBackArrow);
        phrasesdetail_recview = (RecyclerView) findViewById(R.id.phrasesdetail_recview);

        phrasetitle = getIntent().getStringExtra("phrasetitle");
        phraseposition = getIntent().getIntExtra("position", 0);
        lang1name = getIntent().getStringExtra("lang1name");
        lang2name = getIntent().getStringExtra("lang2name");
        tv_phrasetitle.setText(phrasetitle);

        Log.e("Language1", "onCreate: " + lang1name);
        Log.e("Language2", "onCreate: " + lang2name);
        Log.e("phraseposition", "phraseposition: " + phraseposition);
        String str = lang1name;

        if (str.equals(AllLanguage.ARABIC)) {
            c = 0;
        } else if (str.equals(AllLanguage.GERMAN)) {
            c = 1;
        } else if (str.equals(AllLanguage.ENGLISH)) {
            c = 2;
        } else if (str.equals(AllLanguage.FRENCH)) {
            c = 3;
        } else if (str.equals(AllLanguage.HINDI)) {
            c = 4;
        } else if (str.equals(AllLanguage.ITALIAN)) {
            c = 5;
        } else if (str.equals(AllLanguage.JAPANESE)) {
            c = 6;
        } else if (str.equals(AllLanguage.RUSSIAN)) {
            c = 7;
        } else if (str.equals(AllLanguage.URDU)) {
            c = 8;
        } else if (str.equals(AllLanguage.CHINESE)) {
            c = 9;
        } else {
            c = 0;
        }


        switch (c) {
            case 0:
                switch (phraseposition) {
                    case 0:
                        phrases_list = getResources().getStringArray(R.array.essential_phrases_arabic);
                        break;
                    case 1:
                        phrases_list = getResources().getStringArray(R.array.traveling_phrases_arabic);
                        break;
                    case 2:
                        phrases_list = getResources().getStringArray(R.array.medical_phrases_arabic);
                        break;
                    case 3:
                        phrases_list = getResources().getStringArray(R.array.hotel_phrases_arabic);
                        break;
                    case 4:
                        phrases_list = getResources().getStringArray(R.array.store_phrases_arabic);
                        break;
                    case 5:
                        phrases_list = getResources().getStringArray(R.array.work_phrases_arabic);
                        break;
                    case 6:
                        phrases_list = getResources().getStringArray(R.array.education_phrases_arabic);
                        break;
                    case 7:
                        phrases_list = getResources().getStringArray(R.array.entertainment_phrases_arabic);
                        break;
                    case 8:
                        phrases_list = getResources().getStringArray(R.array.cmnprblms_phrases_arabic);
                        break;
                }
                break;
            case 1:
                switch (phraseposition) {
                    case 0:
                        phrases_list = getResources().getStringArray(R.array.essential_phrases_german);
                        break;
                    case 1:
                        phrases_list = getResources().getStringArray(R.array.traveling_phrases_german);
                        break;
                    case 2:
                        phrases_list = getResources().getStringArray(R.array.medical_phrases_german);
                        break;
                    case 3:
                        phrases_list = getResources().getStringArray(R.array.hotel_phrases_german);
                        break;
                    case 4:
                        phrases_list = getResources().getStringArray(R.array.store_phrases_german);
                        break;
                    case 5:
                        phrases_list = getResources().getStringArray(R.array.work_phrases_german);
                        break;
                    case 6:
                        phrases_list = getResources().getStringArray(R.array.education_phrases_german);
                        break;
                    case 7:
                        phrases_list = getResources().getStringArray(R.array.entertainment_phrases_german);
                        break;
                    case 8:
                        phrases_list = getResources().getStringArray(R.array.cmnprblms_phrases_german);
                        break;
                }
                break;
            case 2:
                switch (phraseposition) {
                    case 0:
                        phrases_list = getResources().getStringArray(R.array.essential_phrases_en);
                        break;
                    case 1:
                        phrases_list = getResources().getStringArray(R.array.traveling_phrases_eng);
                        break;
                    case 2:
                        phrases_list = getResources().getStringArray(R.array.medical_phrases_eng);
                        break;
                    case 3:
                        phrases_list = getResources().getStringArray(R.array.hotel_phrases_eng);
                        break;
                    case 4:
                        phrases_list = getResources().getStringArray(R.array.store_phrases_eng);
                        break;
                    case 5:
                        phrases_list = getResources().getStringArray(R.array.work_phrases_eng);
                        break;
                    case 6:
                        phrases_list = getResources().getStringArray(R.array.education_phrases_eng);
                        break;
                    case 7:
                        phrases_list = getResources().getStringArray(R.array.entertainment_phrases_eng);
                        break;
                    case 8:
                        phrases_list = getResources().getStringArray(R.array.cmnprblms_phrases_eng);
                        break;
                }
                break;
            case 3:
                switch (phraseposition) {
                    case 0:
                        phrases_list = getResources().getStringArray(R.array.essential_phrases_french);
                        break;
                    case 1:
                        phrases_list = getResources().getStringArray(R.array.traveling_phrases_french);
                        break;
                    case 2:
                        phrases_list = getResources().getStringArray(R.array.medical_phrases_french);
                        break;
                    case 3:
                        phrases_list = getResources().getStringArray(R.array.hotel_phrases_french);
                        break;
                    case 4:
                        phrases_list = getResources().getStringArray(R.array.store_phrases_french);
                        break;
                    case 5:
                        phrases_list = getResources().getStringArray(R.array.work_phrases_french);
                        break;
                    case 6:
                        phrases_list = getResources().getStringArray(R.array.education_phrases_french);
                        break;
                    case 7:
                        phrases_list = getResources().getStringArray(R.array.entertainment_phrases_french);
                        break;
                    case 8:
                        phrases_list = getResources().getStringArray(R.array.cmnprblms_phrases_french);
                        break;
                }
                break;
            case 4:
                switch (phraseposition) {
                    case 0:
                        phrases_list = getResources().getStringArray(R.array.essential_phrases_hindi);
                        break;
                    case 1:
                        phrases_list = getResources().getStringArray(R.array.traveling_phrases_hindi);
                        break;
                    case 2:
                        phrases_list = getResources().getStringArray(R.array.medical_phrases_hindi);
                        break;
                    case 3:
                        phrases_list = getResources().getStringArray(R.array.hotel_phrases_hindi);
                        break;
                    case 4:
                        phrases_list = getResources().getStringArray(R.array.store_phrases_hindi);
                        break;
                    case 5:
                        phrases_list = getResources().getStringArray(R.array.work_phrases_hindi);
                        break;
                    case 6:
                        phrases_list = getResources().getStringArray(R.array.education_phrases_hindi);
                        break;
                    case 7:
                        phrases_list = getResources().getStringArray(R.array.entertainment_phrases_hindi);
                        break;
                    case 8:
                        phrases_list = getResources().getStringArray(R.array.cmnprblms_phrases_hindi);
                        break;
                }
                break;
            case 5:
                switch (phraseposition) {
                    case 0:
                        phrases_list = getResources().getStringArray(R.array.essential_phrases_italian);
                        break;
                    case 1:
                        phrases_list = getResources().getStringArray(R.array.traveling_phrases_italian);
                        break;
                    case 2:
                        phrases_list = getResources().getStringArray(R.array.medical_phrases_italian);
                        break;
                    case 3:
                        phrases_list = getResources().getStringArray(R.array.hotel_phrases_italian);
                        break;
                    case 4:
                        phrases_list = getResources().getStringArray(R.array.store_phrases_italian);
                        break;
                    case 5:
                        phrases_list = getResources().getStringArray(R.array.work_phrases_italian);
                        break;
                    case 6:
                        phrases_list = getResources().getStringArray(R.array.education_phrases_italian);
                        break;
                    case 7:
                        phrases_list = getResources().getStringArray(R.array.entertainment_phrases_italian);
                        break;
                    case 8:
                        phrases_list = getResources().getStringArray(R.array.cmnprblms_phrases_italian);
                        break;
                }
                break;
            case 6:
                switch (phraseposition) {
                    case 0:
                        phrases_list = getResources().getStringArray(R.array.essential_phrases_japanese);
                        break;
                    case 1:
                        phrases_list = getResources().getStringArray(R.array.traveling_phrases_japanese);
                        break;
                    case 2:
                        phrases_list = getResources().getStringArray(R.array.medical_phrases_japanese);
                        break;
                    case 3:
                        phrases_list = getResources().getStringArray(R.array.hotel_phrases_japanese);
                        break;
                    case 4:
                        phrases_list = getResources().getStringArray(R.array.store_phrases_japanese);
                        break;
                    case 5:
                        phrases_list = getResources().getStringArray(R.array.work_phrases_japanese);
                        break;
                    case 6:
                        phrases_list = getResources().getStringArray(R.array.education_phrases_japanese);
                        break;
                    case 7:
                        phrases_list = getResources().getStringArray(R.array.entertainment_phrases_japanese);
                        break;
                    case 8:
                        phrases_list = getResources().getStringArray(R.array.cmnprblms_phrases_japanese);
                        break;
                }
                break;
            case 7:
                switch (phraseposition) {
                    case 0:
                        phrases_list = getResources().getStringArray(R.array.essential_phrases_russian);
                        break;
                    case 1:
                        phrases_list = getResources().getStringArray(R.array.traveling_phrases_russian);
                        break;
                    case 2:
                        phrases_list = getResources().getStringArray(R.array.medical_phrases_russian);
                        break;
                    case 3:
                        phrases_list = getResources().getStringArray(R.array.hotel_phrases_russian);
                        break;
                    case 4:
                        phrases_list = getResources().getStringArray(R.array.store_phrases_russian);
                        break;
                    case 5:
                        phrases_list = getResources().getStringArray(R.array.work_phrases_russian);
                        break;
                    case 6:
                        phrases_list = getResources().getStringArray(R.array.education_phrases_russian);
                        break;
                    case 7:
                        phrases_list = getResources().getStringArray(R.array.entertainment_phrases_russian);
                        break;
                    case 8:
                        phrases_list = getResources().getStringArray(R.array.cmnprblms_phrases_russian);
                        break;
                }
                break;
            case 8:
                switch (phraseposition) {
                    case 0:
                        phrases_list = getResources().getStringArray(R.array.essential_phrases_urdu);
                        break;
                    case 1:
                        phrases_list = getResources().getStringArray(R.array.traveling_phrases_urdu);
                        break;
                    case 2:
                        phrases_list = getResources().getStringArray(R.array.medical_phrases_urdu);
                        break;
                    case 3:
                        phrases_list = getResources().getStringArray(R.array.hotel_phrases_urdu);
                        break;
                    case 4:
                        phrases_list = getResources().getStringArray(R.array.store_phrases_urdu);
                        break;
                    case 5:
                        phrases_list = getResources().getStringArray(R.array.work_phrases_urdu);
                        break;
                    case 6:
                        phrases_list = getResources().getStringArray(R.array.education_phrases_urdu);
                        break;
                    case 7:
                        phrases_list = getResources().getStringArray(R.array.entertainment_phrases_urdu);
                        break;
                    case 8:
                        phrases_list = getResources().getStringArray(R.array.cmnprblms_phrases_urdu);
                        break;
                }
                break;
            case 9:
                switch (phraseposition) {
                    case 0:
                        phrases_list = getResources().getStringArray(R.array.essential_phrases_chinese);
                        break;
                    case 1:
                        phrases_list = getResources().getStringArray(R.array.traveling_phrases_chinese);
                        break;
                    case 2:
                        phrases_list = getResources().getStringArray(R.array.medical_phrases_chinese);
                        break;
                    case 3:
                        phrases_list = getResources().getStringArray(R.array.hotel_phrases_chinese);
                        break;
                    case 4:
                        phrases_list = getResources().getStringArray(R.array.store_phrases_chinese);
                        break;
                    case 5:
                        phrases_list = getResources().getStringArray(R.array.work_phrases_chinese);
                        break;
                    case 6:
                        phrases_list = getResources().getStringArray(R.array.education_phrases_chinese);
                        break;
                    case 7:
                        phrases_list = getResources().getStringArray(R.array.entertainment_phrases_chinese);
                        break;
                    case 8:
                        phrases_list = getResources().getStringArray(R.array.cmnprblms_phrases_chinese);
                        break;
                }
        }

        for (int i = 0; i < phrases_list.length - 1; i++) {
            complete_list.add(new PhrasesRepo(phrases_list[i]));
        }
        phrasesdetail_recview.setLayoutManager(new LinearLayoutManager(this));
        phrasesdetail_recview.setAdapter(new PhrasesDetailAdapter(this, complete_list, lang1name, lang2name));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}
