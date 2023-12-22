package com.translatealll.anguagesapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.adapter.ParagraphDialogAdapter;
import com.translatealll.anguagesapp.adapter.PhrasesAdapter;
import com.translatealll.anguagesapp.databinding.ActivityPhrasesBinding;
import com.translatealll.anguagesapp.model.PhraseTittleRepo;
import com.translatealll.anguagesapp.utils.PharsesFragment;
import com.translatealll.anguagesapp.utils.AllLanguage;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity implements ParagraphDialogAdapter.PhraseDialogInterface, PhrasesAdapter.PhraseInterface {

    public static int isSelected = -1;
    String lang1;
    String lang2;
    PhrasesAdapter phrasemainAdapter;

    String[] phrasetitles_list;

    int[] title_icon = {R.drawable.ic_pgs_1, R.drawable.ic_pgs_2, R.drawable.ic_pgs_3, R.drawable.ic_pgs_4, R.drawable.ic_pgs_5, R.drawable.ic_pgs_6, R.drawable.ic_pgs_7, R.drawable.ic_pgs_8, R.drawable.ic_pgs_9};

    PharsesFragment bottomSheetParagraphFragment;
    boolean isDropDownOpen = false;
    ArrayList<PhraseTittleRepo> phrasetitlemodellist = new ArrayList<>();

    ActivityPhrasesBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhrasesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int i = 0;
        lang1 = AllLanguage.ENGLISH;
        lang2 = AllLanguage.URDU;
        binding.tvLang1.setText("English");
        if (phrasetitlemodellist.size() > 0) {
            phrasetitlemodellist.clear();
        }
        phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_en);
        while (true) {
            String[] strArr = phrasetitles_list;
            if (i < strArr.length) {
                phrasetitlemodellist.add(new PhraseTittleRepo(strArr[i], title_icon[i]));
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
                phrasemainAdapter = new PhrasesAdapter(this, phrasetitlemodellist, lang1, lang2, this);
                binding.mainphraserecview.setAdapter(phrasemainAdapter);
                binding.linearLeftLang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetParagraphFragment = new PharsesFragment(PhrasesActivity.this);
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
            lang1 = AllLanguage.URDU;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_ur);
        } else if (position == 2) {
            lang1 = AllLanguage.ARABIC;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_arabic);
        } else if (position == 3) {
            lang1 = AllLanguage.CHINESE;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_chinese);
        } else if (position == 4) {
            lang1 = AllLanguage.GERMAN;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_german);
        } else if (position == 5) {
            lang1 = AllLanguage.JAPANESE;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_japanese);
        } else if (position == 6) {
            lang1 = AllLanguage.ITALIAN;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_italian);
        } else if (position == 7) {
            lang1 = AllLanguage.RUSSIAN;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_russian);
        } else if (position == 8) {
            lang1 = AllLanguage.FRENCH;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_french);
        } else if (position == 9) {
            lang1 = AllLanguage.HINDI;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_hindi);
        } else {
            lang1 = AllLanguage.ENGLISH;
            binding.tvLang1.setText(phraseTitle);
            phrasetitles_list = getResources().getStringArray(R.array.phrasetitles_en);
        }
        for (int i = 0; i < phrasetitles_list.length; i++) {
            phrasetitlemodellist.add(new PhraseTittleRepo(phrasetitles_list[i], title_icon[i]));
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int totalItems = binding.mainphraserecview.getAdapter().getItemCount();
                    return (position == totalItems - 1) ? 2 : 1;
                }
            });
            binding.mainphraserecview.setLayoutManager(gridLayoutManager);
            binding.mainphraserecview.setAdapter(new PhrasesAdapter(this, phrasetitlemodellist, lang1, lang2, this));
        }
    }


    @Override
    public void OnPhraseClick(String phrasetitle, int position) {
        startActivity(new Intent(PhrasesActivity.this, PhrasesDetailActivity.class).putExtra("phrasetitle", phrasetitle).putExtra("position", position).putExtra("lang1name", lang1).putExtra("lang2name", lang2));
    }
}