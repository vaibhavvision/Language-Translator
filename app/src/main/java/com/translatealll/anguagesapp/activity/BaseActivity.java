package com.translatealll.anguagesapp.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.utils.TranslateLanguage;


public class BaseActivity extends AppCompatActivity {
    String language;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public String LanguageSelection(View v, final TextView tv_lang, final String langg) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.lngs_menu);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                boolean languageSelection = selectLanguage(tv_lang, langg, menuItem);
                return languageSelection;
            }
        });
        return language;
    }


    public boolean selectLanguage(TextView textView, String str, MenuItem menuItem) {
        textView.setText(menuItem.getTitle().toString());
        boolean equals = textView.getText().toString().equals("English");
        String str2 = "ur";
        if (!equals) {
            if (!textView.getText().toString().equals("Urdu")) {
                if (textView.getText().toString().equals("Arabic")) {
                    str2 = "ar";
                } else if (textView.getText().toString().equals("Chinese")) {
                    str2 = "zh";
                } else if (textView.getText().toString().equals("German")) {
                    str2 = "de";
                } else if (textView.getText().toString().equals("AFRIKAANS")) {
                    str2 = "af";
                } else if (textView.getText().toString().equals("ALBANIAN")) {
                    str2 = "sq";
                } else if (textView.getText().toString().equals("BELARUSIAN")) {
                    str2 = "be";
                } else if (textView.getText().toString().equals("BENGALI")) {
                    str2 = "bn";
                } else if (textView.getText().toString().equals("BULGARIAN")) {
                    str2 = "bg";
                } else if (textView.getText().toString().equals("GALICIAN")) {
                    str2 = "gl";
                } else if (textView.getText().toString().equals("FINNISH")) {
                    str2 = "fi";
                } else if (textView.getText().toString().equals("FRENCH")) {
                    str2 = "fr";
                } else if (textView.getText().toString().equals("ESTONIAN")) {
                    str2 = "et";
                } else if (textView.getText().toString().equals("ESPERANTO")) {
                    str2 = "eo";
                } else if (textView.getText().toString().equals("DUTCH")) {
                    str2 = "nl";
                } else if (textView.getText().toString().equals("DANISH")) {
                    str2 = "da";
                } else if (textView.getText().toString().equals("CZECH")) {
                    str2 = "cs";
                } else if (textView.getText().toString().equals("CROATIAN")) {
                    str2 = "hr";
                } else if (textView.getText().toString().equals("CATALAN")) {
                    str2 = "ca";
                } else if (textView.getText().toString().equals("MALTESE")) {
                    str2 = "mt";
                } else if (textView.getText().toString().equals("MALAY")) {
                    str2 = "ms";
                } else if (textView.getText().toString().equals("MACEDONIAN")) {
                    str2 = "mk";
                } else if (textView.getText().toString().equals("LITHUANIAN")) {
                    str2 = TranslateLanguage.LITHUANIAN;
                } else if (textView.getText().toString().equals("LATVIAN")) {
                    str2 = TranslateLanguage.LATVIAN;
                } else if (textView.getText().toString().equals("KOREAN")) {
                    str2 = TranslateLanguage.KOREAN;
                } else if (textView.getText().toString().equals("KANNADA")) {
                    str2 = TranslateLanguage.KANNADA;
                } else if (textView.getText().toString().equals("JAPANESE")) {
                    str2 = TranslateLanguage.JAPANESE;
                } else if (textView.getText().toString().equals("ITALIAN")) {
                    str2 = TranslateLanguage.ITALIAN;
                } else if (textView.getText().toString().equals("IRISH")) {
                    str2 = TranslateLanguage.IRISH;
                } else if (textView.getText().toString().equals("INDONESIAN")) {
                    str2 = "id";
                } else if (textView.getText().toString().equals("ICELANDIC")) {
                    str2 = TranslateLanguage.ICELANDIC;
                } else if (textView.getText().toString().equals("HUNGARIAN")) {
                    str2 = TranslateLanguage.HUNGARIAN;
                } else if (textView.getText().toString().equals("HINDI")) {
                    str2 = TranslateLanguage.HINDI;
                } else if (textView.getText().toString().equals("HEBREW")) {
                    str2 = TranslateLanguage.HEBREW;
                } else if (textView.getText().toString().equals("HAITIAN_CREOLE")) {
                    str2 = TranslateLanguage.HAITIAN_CREOLE;
                } else if (textView.getText().toString().equals("GUJARATI")) {
                    str2 = TranslateLanguage.GUJARATI;
                } else if (textView.getText().toString().equals("GREEK")) {
                    str2 = TranslateLanguage.GREEK;
                } else if (textView.getText().toString().equals("GEORGIAN")) {
                    str2 = TranslateLanguage.GEORGIAN;
                } else if (textView.getText().toString().equals("WELSH")) {
                    str2 = TranslateLanguage.WELSH;
                } else if (textView.getText().toString().equals("VIETNAMESE")) {
                    str2 = TranslateLanguage.VIETNAMESE;
                } else if (textView.getText().toString().equals("UKRAINIAN")) {
                    str2 = TranslateLanguage.UKRAINIAN;
                } else if (textView.getText().toString().equals("TURKISH")) {
                    str2 = TranslateLanguage.TURKISH;
                } else if (textView.getText().toString().equals("THAI")) {
                    str2 = TranslateLanguage.THAI;
                } else if (textView.getText().toString().equals("TELUGU")) {
                    str2 = TranslateLanguage.TELUGU;
                } else if (textView.getText().toString().equals("TAMIL")) {
                    str2 = TranslateLanguage.TAMIL;
                } else if (textView.getText().toString().equals("TAGALOG")) {
                    str2 = TranslateLanguage.TAGALOG;
                } else if (textView.getText().toString().equals("SWEDISH")) {
                    str2 = TranslateLanguage.SWEDISH;
                } else if (textView.getText().toString().equals("SWAHILI")) {
                    str2 = TranslateLanguage.SWAHILI;
                } else if (textView.getText().toString().equals("SPANISH")) {
                    str2 = TranslateLanguage.SPANISH;
                } else if (textView.getText().toString().equals("SLOVENIAN")) {
                    str2 = TranslateLanguage.SLOVENIAN;
                } else if (textView.getText().toString().equals("SLOVAK")) {
                    str2 = TranslateLanguage.SLOVAK;
                } else if (textView.getText().toString().equals("RUSSIAN")) {
                    str2 = TranslateLanguage.RUSSIAN;
                } else if (textView.getText().toString().equals("ROMANIAN")) {
                    str2 = TranslateLanguage.ROMANIAN;
                } else if (textView.getText().toString().equals("PORTUGUESE")) {
                    str2 = TranslateLanguage.PORTUGUESE;
                } else if (textView.getText().toString().equals("POLISH")) {
                    str2 = TranslateLanguage.POLISH;
                } else if (textView.getText().toString().equals("PERSIAN")) {
                    str2 = TranslateLanguage.PERSIAN;
                } else if (textView.getText().toString().equals("NORWEGIAN")) {
                    str2 = TranslateLanguage.NORWEGIAN;
                } else if (textView.getText().toString().equals("MARATHI")) {
                    str2 = TranslateLanguage.MARATHI;
                } else if (!str.equals("lang1")) {
                    if (!str.equals("lang2")) {
                        str2 = null;
                    }
                }
            }
            language = str2;
            return true;
        }
        str2 = TranslateLanguage.ENGLISH;
        language = str2;
        return true;
    }
}
