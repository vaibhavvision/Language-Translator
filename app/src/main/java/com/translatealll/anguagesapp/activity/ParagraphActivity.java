package com.translatealll.anguagesapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.adapter.ParagraphDialogAdapter;
import com.translatealll.anguagesapp.adapter.PhrasemainAdapter;
import com.translatealll.anguagesapp.databinding.ActivityParagraphBinding;
import com.translatealll.anguagesapp.model.PhraseTitlesModelClass;
import com.translatealll.anguagesapp.utils.BottomSheetParagraphFragment;
import com.translatealll.anguagesapp.utils.TranslateLanguage;

import java.util.ArrayList;

public class ParagraphActivity extends AppCompatActivity implements ParagraphDialogAdapter.PhraseDialogInterface, PhrasemainAdapter.PhraseInterface {

    public static int isSelected = -1;
    String lang1;
    String lang2;
    PhrasemainAdapter phrasemainAdapter;

    String[] phrasetitles_list;

    int[] title_icon = {R.drawable.ic_pgs_1, R.drawable.ic_pgs_2, R.drawable.ic_pgs_3, R.drawable.ic_pgs_4, R.drawable.ic_pgs_5, R.drawable.ic_pgs_6, R.drawable.ic_pgs_7, R.drawable.ic_pgs_8, R.drawable.ic_pgs_9};

    BottomSheetParagraphFragment bottomSheetParagraphFragment;
    boolean isDropDownOpen = false;
    ArrayList<PhraseTitlesModelClass> phrasetitlemodellist = new ArrayList<>();

    ActivityParagraphBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParagraphBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int i = 0;
        lang1 = TranslateLanguage.ENGLISH;
        lang2 = TranslateLanguage.URDU;
        binding.tvLang1.setText("English");
        if (phrasetitlemodellist.size() > 0) {
            phrasetitlemodellist.clear();
        }
        phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_en);
        while (true) {
            String[] strArr = phrasetitles_list;
            if (i < strArr.length) {
                phrasetitlemodellist.add(new PhraseTitlesModelClass(strArr[i], title_icon[i]));
                i++;
            } else {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int totalItems = binding.mainphraserecview.getAdapter().getItemCount();
                        return (position == totalItems - 1) ? 2 : 1;
                    }
                });
                binding.mainphraserecview.setLayoutManager(gridLayoutManager);
                phrasemainAdapter = new PhrasemainAdapter(this, phrasetitlemodellist, lang1, lang2, this);
                binding.mainphraserecview.setAdapter(phrasemainAdapter);
                binding.linearLeftLang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetParagraphFragment = new BottomSheetParagraphFragment(ParagraphActivity.this);
                        bottomSheetParagraphFragment.show(getSupportFragmentManager(), "Tag");
                    }
                });
                binding.ivBackArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });

                return;
            }
        }
    }

    @Override
    public void OnPhraseDialogClick(String phraseTitle, int position) {
        bottomSheetParagraphFragment.dismiss();
        phrasetitlemodellist.clear();
        if (position == 1) {
            lang1 = TranslateLanguage.URDU;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_ur);
        } else if (position == 2) {
            lang1 = TranslateLanguage.ARABIC;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_arabic);
        } else if (position == 3) {
            lang1 = TranslateLanguage.CHINESE;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_chinese);
        } else if (position == 4) {
            lang1 = TranslateLanguage.GERMAN;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_german);
        } else if (position == 5) {
            lang1 = TranslateLanguage.JAPANESE;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_japanese);
        } else if (position == 6) {
            lang1 = TranslateLanguage.ITALIAN;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_italian);
        } else if (position == 7) {
            lang1 = TranslateLanguage.RUSSIAN;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_russian);
        } else if (position == 8) {
            lang1 = TranslateLanguage.FRENCH;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_french);
        } else if (position == 9) {
            lang1 = TranslateLanguage.HINDI;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_hindi);
        } else {
            lang1 = TranslateLanguage.ENGLISH;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_en);
        }
        for (int i = 0; i < phrasetitles_list.length; i++) {
            phrasetitlemodellist.add(new PhraseTitlesModelClass(phrasetitles_list[i], title_icon[i]));
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int totalItems = binding.mainphraserecview.getAdapter().getItemCount();
                    return (position == totalItems - 1) ? 2 : 1;
                }
            });
            binding.mainphraserecview.setLayoutManager(gridLayoutManager);
            binding.mainphraserecview.setAdapter(new PhrasemainAdapter(this, phrasetitlemodellist, lang1, lang2, this));
        }
    }


    @Override
    public void OnPhraseClick(String phrasetitle, int position) {
        startActivity(new Intent(ParagraphActivity.this, PhrasesDetailedActivity.class).putExtra("phrasetitle", phrasetitle).putExtra("position", position).putExtra("lang1name", lang1).putExtra("lang2name", lang2));
    }
}