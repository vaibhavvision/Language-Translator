package com.translatealll.anguagesapp.activity;

import static com.translatealll.anguagesapp.activity.MainActivity.getPref;
import static com.translatealll.anguagesapp.activity.MainActivity.lng1name;
import static com.translatealll.anguagesapp.activity.MainActivity.lng2name;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.database.DownloadedLngsTable;
import com.translatealll.anguagesapp.database.RoomDB;
import com.translatealll.anguagesapp.database.WordsHistoryTable;
import com.translatealll.anguagesapp.databinding.ActivityTranslateBinding;
import com.translatealll.anguagesapp.inter.DialogFragmentClick;
import com.translatealll.anguagesapp.utils.BottomSheetFragment;
import com.translatealll.anguagesapp.utils.Const;
import com.translatealll.anguagesapp.utils.PrefFile;
import com.translatealll.anguagesapp.utils.AllLanguage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.text.Typography;

public class TranslateActivity extends AppCompatActivity implements DialogFragmentClick {

    public static String CameraPic = "";

    public static int iconlang1;
    public static int iconlang2;
    public static String lang_no;
    public static String lng1code;
    public static String lng2code;
    public static SharedPreferences main_pref;
    public static RoomDB roomDB;

    public static List<DownloadedLngsTable> downloadedlngs_list = new ArrayList();
    public static ArrayList<String> temp_downloadedlngs_list = new ArrayList<>();
    boolean is_btn_translate;
    KProgressHUD kprogresshud;
    Animation translatedcard_anim;
    TextToSpeech txttospeech;
    boolean Is_btn_translate_auto_click = false;
    public static String from;
    public static TextView tv_lang1;
    public static TextView tv_lang2;

    public static boolean isFirstSpeacker = false;
    ActivityTranslateBinding binding;
    String pasteText;

    boolean isClearMain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTranslateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pasteText = getIntent().getStringExtra("mic");

        binding.etUserinput.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (binding.etUserinput.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

        if (getIntent().getStringExtra("pos").equals("1") && (!pasteText.isEmpty())) {
            binding.etUserinput.setText(pasteText);
            binding.ivCopy.setVisibility(View.VISIBLE);
            binding.ivSpeaker.setVisibility(View.VISIBLE);
            binding.ivNewTranslation.setVisibility(View.VISIBLE);
            binding.ivClearText.setVisibility(View.VISIBLE);
        } else {
            binding.etUserinput.setText("");
            binding.tvTranslatedtext.setText("");
        }

        tv_lang1 = findViewById(R.id.tv_lang1);
        tv_lang2 = findViewById(R.id.tv_lang2);

        roomDB = RoomDB.getRoomDBInstance(this);
        temp_downloadedlngs_list.clear();
        downloadedlngs_list.clear();
        downloadedlngs_list = roomDB.downloadedlngs_dao().SelectDownloadedLngs();
        for (int i = 0; i < downloadedlngs_list.size(); i++) {
            Log.d("flow", "onCreatezzz: " + downloadedlngs_list.get(i).getDownloadedlng_name());
            temp_downloadedlngs_list.add(downloadedlngs_list.get(i).getDownloadedlng_name());
        }

        translatedcard_anim = AnimationUtils.loadAnimation(this, R.anim.translatedcard_anim);
        binding.etUserinput.setMovementMethod(new ScrollingMovementMethod());
        kprogresshud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Translating ").setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f);
        Log.e("flow", "onCreate:size od downloaded and temp list " + downloadedlngs_list.size() + "////" + temp_downloadedlngs_list.size());
        lng1name = getPref(this).getString("lng1name", "ENGLISH");
        lng2name = getPref(this).getString("lng2name", "FRENCH");

        binding.tvLang1.setText(lng1name);
        binding.tvLang2.setText(lng2name);

        binding.linearLeftLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lang_no = "1";
                BottomSheetFragment bottomsheetFrag = new BottomSheetFragment(TranslateActivity.this);
                Bundle bundle = new Bundle();
                bundle.putString("langno", "1");
                bundle.putString("from", "mainActivity");
                bottomsheetFrag.setArguments(bundle);
                bottomsheetFrag.show(getSupportFragmentManager(), "TAG");
                PrefFile.getInstance().setString(Const.LEFTRIGHT, "new");
            }
        });

        binding.linearRightLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lang_no = "2";
                BottomSheetFragment bottomsheetFrag = new BottomSheetFragment(TranslateActivity.this);
                Bundle bundle = new Bundle();
                bundle.putString("langno", "2");
                bundle.putString("from", "mainActivity");
                bottomsheetFrag.setArguments(bundle);
                bottomsheetFrag.show(getSupportFragmentManager(), "TAG");
                PrefFile.getInstance().setString(Const.LEFTRIGHT, "new");
            }
        });

        binding.ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.ivClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClearMain = true;
                binding.etUserinput.setText("");
                binding.ivNewTranslation.setVisibility(View.GONE);
                binding.ivClearText.setVisibility(View.GONE);
                binding.viewLine.setVisibility(View.GONE);
                binding.linearTranslate.setVisibility(View.GONE);
            }
        });


        binding.etUserinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("beforeTextChanged", "beforeTextChanged: " + charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("onTextChanged", "onTextChanged: " + charSequence);
                if (charSequence.length() > 0) {
                    binding.ivCopy.setVisibility(View.VISIBLE);
                    binding.ivSpeaker.setVisibility(View.VISIBLE);
                    binding.ivNewTranslation.setVisibility(View.VISIBLE);
                    binding.ivClearText.setVisibility(View.VISIBLE);
                } else {
                    binding.ivCopy.setVisibility(View.INVISIBLE);
                    binding.ivSpeaker.setVisibility(View.INVISIBLE);
                    binding.ivNewTranslation.setVisibility(View.GONE);
                    binding.ivClearText.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("afterTextChanged", "afterTextChanged: " + editable.toString());
            }
        });


        binding.ivNewTranslation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClearMain = true;
                binding.etUserinput.setText("");
                binding.ivNewTranslation.setVisibility(View.GONE);
                binding.ivClearText.setVisibility(View.GONE);
                binding.linearTranslate.setVisibility(View.GONE);
                binding.viewLine.setVisibility(View.GONE);
            }
        });

        binding.imgbtnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.tvTranslatedtext.getText().toString().equals("")) {
                    Toast.makeText(TranslateActivity.this, "No text found", Toast.LENGTH_SHORT).show();
                    return;
                }
                ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setText(binding.tvTranslatedtext.getText().toString());
                Toast.makeText(TranslateActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        binding.shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String charSequence = binding.tvTranslatedtext.getText().toString();
                if (charSequence.equals("")) {
                    Toast.makeText(TranslateActivity.this, "No Text Found", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(Intent.createChooser(new Intent("android.intent.action.SEND").putExtra("android.intent.extra.TEXT", charSequence).setType("text/plain").putExtra("android.intent.extra.SUBJECT", "choose one"), "Share through"));
                }
            }
        });
        binding.volumeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kprogresshud.setLabel("Text to speech").show();
                if (binding.tvTranslatedtext.getText().toString().equals("")) {
                    Toast.makeText(TranslateActivity.this, "No text detected", Toast.LENGTH_SHORT).show();
                } else {
                     txttospeech = new TextToSpeech(TranslateActivity.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int i) {
                            kprogresshud.dismiss();
                            if (i == 0) {
                                Locale locale = new Locale(lng2code);
                                Log.d("text", "locale:: " + locale.getLanguage());
                                int language = txttospeech.setLanguage(locale);
                                Log.d("text", "result:: " + language);
                                Log.d("text", binding.tvTranslatedtext.getText().toString());
                                txttospeech.speak(binding.tvTranslatedtext.getText().toString(), 0, null, null);
                            }
                        }
                    });
                }
            }
        });

        binding.ivSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kprogresshud.setLabel("Text to speech").show();
                if (binding.etUserinput.getText().toString().equals("")) {
                    Toast.makeText(TranslateActivity.this, "No text detected", Toast.LENGTH_SHORT).show();
                } else {
                    txttospeech = new TextToSpeech(TranslateActivity.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int i) {
                            kprogresshud.dismiss();
                            if (i == 0) {
                                Locale locale = new Locale(lng2code);
                                Log.d("text", "locale:: " + locale.getLanguage());
                                int language = txttospeech.setLanguage(locale);
                                Log.d("text", "result:: " + language);
                                Log.d("text", binding.etUserinput.getText().toString());
                                txttospeech.speak(binding.etUserinput.getText().toString(), 0, null, null);
                            }
                        }
                    });
                }
            }
        });


        binding.ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etUserinput.getText().toString().equals("")) {
                    Toast.makeText(TranslateActivity.this, "No text found", Toast.LENGTH_SHORT).show();
                    return;
                }
                ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setText(binding.tvTranslatedtext.getText().toString());
                Toast.makeText(TranslateActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        binding.ivTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.etUserinput.getText().toString().trim().isEmpty()) {
                    translateLanguage();
                }
            }
        });

    }

    public void translateLanguage() {
        is_btn_translate = true;
        if (temp_downloadedlngs_list.size() == 0) {
            Log.d("TAG", "onCreatexcxc: ");
            BottomSheetFragment bottomsheetFrag = new BottomSheetFragment(this);
            Bundle bundle = new Bundle();
            bundle.putString("langno", "1");
            bundle.putString("from", "mainActivity");
            bundle.putString("lan", lng2name);
            bundle.putString("belong", "lan1");
            bottomsheetFrag.setArguments(bundle);
            bottomsheetFrag.show(getSupportFragmentManager(), "TAG");
        } else if (!temp_downloadedlngs_list.contains(lng2name)) {
            Log.d("TAG", "onCreatexcxc1: " + temp_downloadedlngs_list.size());
            Log.d("TAG", "onCreatexcxc1: " + lng2name);
            BottomSheetFragment bottomsheetFrag2 = new BottomSheetFragment(this);
            Bundle bundle2 = new Bundle();
            bundle2.putString("langno", "2");
            bundle2.putString("from", "mainActivity");
            bundle2.putString("lan", lng2name);
            bundle2.putString("belong", "lan1");
            bottomsheetFrag2.setArguments(bundle2);
            bottomsheetFrag2.show(getSupportFragmentManager(), "TAG");
        } else {
            Log.e("aaaaaaaaa", "The ad was dismissed.");
            TranslateWords(binding.etUserinput.getText().toString());
        }
    }

    public void TranslateWords(String texttotranslate) {
        lng1code = Chooselng1Code(lng1name);
        lng2code = Chooselng2Code(lng2name);
        Translation.getClient(new TranslatorOptions.Builder().setSourceLanguage(lng1code).setTargetLanguage(lng2code).build()).translate(texttotranslate).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object obj) {
                binding.tvTranslatedtext.setText(obj.toString());
                binding.tvTranslatedtext.setMovementMethod(new ArrowKeyMovementMethod());
                binding.linearTranslate.setAnimation(translatedcard_anim);
                binding.linearTranslate.setVisibility(View.VISIBLE);

                binding.viewLine.setVisibility(View.VISIBLE);
                binding.ivNewTranslation.setVisibility(View.VISIBLE);

                WordsHistoryTable wordsHistoryTable = new WordsHistoryTable();
                wordsHistoryTable.setLanguage1(lng1name);
                wordsHistoryTable.setLanguage2(lng2name);
                wordsHistoryTable.setTexttotranslate(texttotranslate);
                wordsHistoryTable.setTranslatedtext(obj.toString());
                roomDB.downloadedlngs_dao().insert_lngs(wordsHistoryTable);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exc) {
                Log.e("faileddsd", "TranslateWords:failed to translate  " + exc);
                Toast.makeText(TranslateActivity.this, "failed to translate", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void setData(Context context) {
        downloadedlngs_list.clear();
        temp_downloadedlngs_list.clear();
        downloadedlngs_list = roomDB.downloadedlngs_dao().SelectDownloadedLngs();
        for (int i = 0; i < downloadedlngs_list.size(); i++) {
            temp_downloadedlngs_list.add(downloadedlngs_list.get(i).getDownloadedlng_name());
        }
        Log.e("flow", "onResume: downloaded and temp list size" + downloadedlngs_list.size() + "////" + temp_downloadedlngs_list.size());
        String str = lang_no;
        if (str != null && str.equals("1") && BottomSheetFragment.languagepack != null) {
            lng1name = BottomSheetFragment.languagepack;
        } else {
            String str2 = lang_no;
            if (str2 != null && str2.equals("2") && BottomSheetFragment.languagepack != null) {
                lng2name = BottomSheetFragment.languagepack;
            }
        }
        tv_lang1.setText(lng1name);
        tv_lang2.setText(lng2name);
        getPref(context).edit().putString("lng1name", lng1name).apply();
        getPref(context).edit().putString("lng2name", lng2name).apply();
        getPref(context).edit().putInt("iconlang1", iconlang1).apply();
        getPref(context).edit().putInt("iconlang2", iconlang2).apply();
    }


    private String Chooselng1Code(String language1name) {
        language1name.hashCode();
        char c = 65535;
        switch (language1name.hashCode()) {
            case -2072311548:
                if (language1name.equals("KOREAN")) {
                    c = 0;
                    break;
                }
                break;
            case -2021434509:
                if (language1name.equals("RUSSIAN")) {
                    c = 1;
                    break;
                }
                break;
            case -1998693422:
                if (language1name.equals("ALBANIAN")) {
                    c = 2;
                    break;
                }
                break;
            case -1929340143:
                if (language1name.equals("POLISH")) {
                    c = 3;
                    break;
                }
                break;
            case -1846121942:
                if (language1name.equals("SLOVAK")) {
                    c = 4;
                    break;
                }
                break;
            case -1824047576:
                if (language1name.equals("TELUGU")) {
                    c = 5;
                    break;
                }
                break;
            case -1661654192:
                if (language1name.equals("GEORGIAN")) {
                    c = 6;
                    break;
                }
                break;
            case -1496931977:
                if (language1name.equals("ESPERANTO")) {
                    c = 7;
                    break;
                }
                break;
            case -1464494112:
                if (language1name.equals("ITALIAN")) {
                    c = '\b';
                    break;
                }
                break;
            case -1405627549:
                if (language1name.equals("CROATIAN")) {
                    c = '\t';
                    break;
                }
                break;
            case -1293848364:
                if (language1name.equals("SPANISH")) {
                    c = '\n';
                    break;
                }
                break;
            case -1171574191:
                if (language1name.equals("ESTONIAN")) {
                    c = 11;
                    break;
                }
                break;
            case -1093623269:
                if (language1name.equals("SWAHILI")) {
                    c = '\f';
                    break;
                }
                break;
            case -1090048133:
                if (language1name.equals("SWEDISH")) {
                    c = '\r';
                    break;
                }
                break;
            case -1011019926:
                if (language1name.equals("GALICIAN")) {
                    c = 14;
                    break;
                }
                break;
            case -981927346:
                if (language1name.equals("NORWEGIAN")) {
                    c = 15;
                    break;
                }
                break;
            case -948864834:
                if (language1name.equals("BELARUSIAN")) {
                    c = 16;
                    break;
                }
                break;
            case -885774768:
                if (language1name.equals("ENGLISH")) {
                    c = 17;
                    break;
                }
                break;
            case -871655265:
                if (language1name.equals("HUNGARIAN")) {
                    c = 18;
                    break;
                }
                break;
            case -830625347:
                if (language1name.equals("TAGALOG")) {
                    c = 19;
                    break;
                }
                break;
            case -758693139:
                if (language1name.equals("SLOVENIAN")) {
                    c = 20;
                    break;
                }
                break;
            case -505022199:
                if (language1name.equals("GUJARATI")) {
                    c = 21;
                    break;
                }
                break;
            case -391870441:
                if (language1name.equals("BULGARIAN")) {
                    c = 22;
                    break;
                }
                break;
            case -247588444:
                if (language1name.equals("TURKISH")) {
                    c = 23;
                    break;
                }
                break;
            case -221382872:
                if (language1name.equals("KANNADA")) {
                    c = 24;
                    break;
                }
                break;
            case -134892613:
                if (language1name.equals("FINNISH")) {
                    c = 25;
                    break;
                }
                break;
            case 2573724:
                if (language1name.equals("THAI")) {
                    c = 26;
                    break;
                }
                break;
            case 2613230:
                if (language1name.equals("URDU")) {
                    c = 27;
                    break;
                }
                break;
            case 29896625:
                if (language1name.equals("JAPANESE")) {
                    c = 28;
                    break;
                }
                break;
            case 39535488:
                if (language1name.equals("PERSIAN")) {
                    c = 29;
                    break;
                }
                break;
            case 64625555:
                if (language1name.equals("CZECH")) {
                    c = 30;
                    break;
                }
                break;
            case 65414536:
                if (language1name.equals("DUTCH")) {
                    c = 31;
                    break;
                }
                break;
            case 68081376:
                if (language1name.equals("GREEK")) {
                    c = ' ';
                    break;
                }
                break;
            case 68745394:
                if (language1name.equals("HINDI")) {
                    c = '!';
                    break;
                }
                break;
            case 69932693:
                if (language1name.equals("IRISH")) {
                    c = '\"';
                    break;
                }
                break;
            case 73122672:
                if (language1name.equals("MALAY")) {
                    c = '#';
                    break;
                }
                break;
            case 79588515:
                if (language1name.equals("TAMIL")) {
                    c = Typography.dollar;
                    break;
                }
                break;
            case 82477587:
                if (language1name.equals("WELSH")) {
                    c = '%';
                    break;
                }
                break;
            case 167462569:
                if (language1name.equals("MACEDONIAN")) {
                    c = Typography.amp;
                    break;
                }
                break;
            case 243547852:
                if (language1name.equals("UKRAINIAN")) {
                    c = '\'';
                    break;
                }
                break;
            case 495326914:
                if (language1name.equals("BENGALI")) {
                    c = '(';
                    break;
                }
                break;
            case 541742905:
                if (language1name.equals("ROMANIAN")) {
                    c = ')';
                    break;
                }
                break;
            case 671907871:
                if (language1name.equals("LATVIAN")) {
                    c = '*';
                    break;
                }
                break;
            case 799935903:
                if (language1name.equals("HAITIAN_CREOLE")) {
                    c = '+';
                    break;
                }
                break;
            case 1010710335:
                if (language1name.equals("VIETNAMESE")) {
                    c = ',';
                    break;
                }
                break;
            case 1055466096:
                if (language1name.equals("ICELANDIC")) {
                    c = '-';
                    break;
                }
                break;
            case 1236562858:
                if (language1name.equals("INDONESIAN")) {
                    c = '.';
                    break;
                }
                break;
            case 1273686606:
                if (language1name.equals("CATALAN")) {
                    c = '/';
                    break;
                }
                break;
            case 1322880565:
                if (language1name.equals("PORTUGUESE")) {
                    c = '0';
                    break;
                }
                break;
            case 1464313037:
                if (language1name.equals("CHINESE")) {
                    c = '1';
                    break;
                }
                break;
            case 1488524197:
                if (language1name.equals("LITHUANIAN")) {
                    c = '2';
                    break;
                }
                break;
            case 1551960507:
                if (language1name.equals("MALTESE")) {
                    c = '3';
                    break;
                }
                break;
            case 1556949682:
                if (language1name.equals("MARATHI")) {
                    c = '4';
                    break;
                }
                break;
            case 1938625708:
                if (language1name.equals("ARABIC")) {
                    c = '5';
                    break;
                }
                break;
            case 2009207629:
                if (language1name.equals("DANISH")) {
                    c = '6';
                    break;
                }
                break;
            case 2081901978:
                if (language1name.equals("FRENCH")) {
                    c = '7';
                    break;
                }
                break;
            case 2098911622:
                if (language1name.equals("GERMAN")) {
                    c = '8';
                    break;
                }
                break;
            case 2127069055:
                if (language1name.equals("HEBREW")) {
                    c = '9';
                    break;
                }
                break;
            case 2139267348:
                if (language1name.equals("AFRIKAANS")) {
                    c = ':';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return AllLanguage.KOREAN;
            case 1:
                return AllLanguage.RUSSIAN;
            case 2:
                return AllLanguage.ALBANIAN;
            case 3:
                return AllLanguage.POLISH;
            case 4:
                return AllLanguage.SLOVAK;
            case 5:
                return AllLanguage.TELUGU;
            case 6:
                return AllLanguage.GEORGIAN;
            case 7:
                return AllLanguage.ESPERANTO;
            case '\b':
                return AllLanguage.ITALIAN;
            case '\t':
                return AllLanguage.CROATIAN;
            case '\n':
                return AllLanguage.SPANISH;
            case 11:
                return AllLanguage.ESTONIAN;
            case '\f':
                return AllLanguage.SWAHILI;
            case '\r':
                return AllLanguage.SWEDISH;
            case 14:
                return AllLanguage.GALICIAN;
            case 15:
                return AllLanguage.NORWEGIAN;
            case 16:
                return AllLanguage.BELARUSIAN;
            case 17:
            default:
                return AllLanguage.ENGLISH;
            case 18:
                return AllLanguage.HUNGARIAN;
            case 19:
                return AllLanguage.TAGALOG;
            case 20:
                return AllLanguage.SLOVENIAN;
            case 21:
                return AllLanguage.GUJARATI;
            case 22:
                return AllLanguage.BULGARIAN;
            case 23:
                return AllLanguage.TURKISH;
            case 24:
                return AllLanguage.KANNADA;
            case 25:
                return AllLanguage.FINNISH;
            case 26:
                return AllLanguage.THAI;
            case 27:
                return AllLanguage.URDU;
            case 28:
                return AllLanguage.JAPANESE;
            case 29:
                return AllLanguage.PERSIAN;
            case 30:
                return AllLanguage.CZECH;
            case 31:
                return AllLanguage.DUTCH;
            case ' ':
                return AllLanguage.GREEK;
            case '!':
                return AllLanguage.HINDI;
            case '\"':
                return AllLanguage.IRISH;
            case '#':
                return AllLanguage.MALAY;
            case '$':
                return AllLanguage.TAMIL;
            case '%':
                return AllLanguage.WELSH;
            case '&':
                return AllLanguage.MACEDONIAN;
            case '\'':
                return AllLanguage.UKRAINIAN;
            case '(':
                return AllLanguage.BENGALI;
            case ')':
                return AllLanguage.ROMANIAN;
            case '*':
                return AllLanguage.LATVIAN;
            case '+':
                return AllLanguage.HAITIAN_CREOLE;
            case ',':
                return AllLanguage.VIETNAMESE;
            case '-':
                return AllLanguage.ICELANDIC;
            case '.':
                return "id";
            case '/':
                return AllLanguage.CATALAN;
            case '0':
                return AllLanguage.PORTUGUESE;
            case '1':
                return AllLanguage.CHINESE;
            case '2':
                return AllLanguage.LITHUANIAN;
            case '3':
                return AllLanguage.MALTESE;
            case '4':
                return AllLanguage.MARATHI;
            case '5':
                return AllLanguage.ARABIC;
            case '6':
                return AllLanguage.DANISH;
            case '7':
                return AllLanguage.FRENCH;
            case '8':
                return AllLanguage.GERMAN;
            case '9':
                return AllLanguage.HEBREW;
            case ':':
                return AllLanguage.AFRIKAANS;
        }
    }

    private String Chooselng2Code(String language2name) {
        language2name.hashCode();
        char c = 65535;
        switch (language2name.hashCode()) {
            case -2072311548:
                if (language2name.equals("KOREAN")) {
                    c = 0;
                    break;
                }
                break;
            case -2021434509:
                if (language2name.equals("RUSSIAN")) {
                    c = 1;
                    break;
                }
                break;
            case -1998693422:
                if (language2name.equals("ALBANIAN")) {
                    c = 2;
                    break;
                }
                break;
            case -1929340143:
                if (language2name.equals("POLISH")) {
                    c = 3;
                    break;
                }
                break;
            case -1846121942:
                if (language2name.equals("SLOVAK")) {
                    c = 4;
                    break;
                }
                break;
            case -1824047576:
                if (language2name.equals("TELUGU")) {
                    c = 5;
                    break;
                }
                break;
            case -1661654192:
                if (language2name.equals("GEORGIAN")) {
                    c = 6;
                    break;
                }
                break;
            case -1496931977:
                if (language2name.equals("ESPERANTO")) {
                    c = 7;
                    break;
                }
                break;
            case -1464494112:
                if (language2name.equals("ITALIAN")) {
                    c = '\b';
                    break;
                }
                break;
            case -1405627549:
                if (language2name.equals("CROATIAN")) {
                    c = '\t';
                    break;
                }
                break;
            case -1293848364:
                if (language2name.equals("SPANISH")) {
                    c = '\n';
                    break;
                }
                break;
            case -1171574191:
                if (language2name.equals("ESTONIAN")) {
                    c = 11;
                    break;
                }
                break;
            case -1093623269:
                if (language2name.equals("SWAHILI")) {
                    c = '\f';
                    break;
                }
                break;
            case -1090048133:
                if (language2name.equals("SWEDISH")) {
                    c = '\r';
                    break;
                }
                break;
            case -1011019926:
                if (language2name.equals("GALICIAN")) {
                    c = 14;
                    break;
                }
                break;
            case -981927346:
                if (language2name.equals("NORWEGIAN")) {
                    c = 15;
                    break;
                }
                break;
            case -948864834:
                if (language2name.equals("BELARUSIAN")) {
                    c = 16;
                    break;
                }
                break;
            case -885774768:
                if (language2name.equals("ENGLISH")) {
                    c = 17;
                    break;
                }
                break;
            case -871655265:
                if (language2name.equals("HUNGARIAN")) {
                    c = 18;
                    break;
                }
                break;
            case -830625347:
                if (language2name.equals("TAGALOG")) {
                    c = 19;
                    break;
                }
                break;
            case -758693139:
                if (language2name.equals("SLOVENIAN")) {
                    c = 20;
                    break;
                }
                break;
            case -505022199:
                if (language2name.equals("GUJARATI")) {
                    c = 21;
                    break;
                }
                break;
            case -391870441:
                if (language2name.equals("BULGARIAN")) {
                    c = 22;
                    break;
                }
                break;
            case -247588444:
                if (language2name.equals("TURKISH")) {
                    c = 23;
                    break;
                }
                break;
            case -221382872:
                if (language2name.equals("KANNADA")) {
                    c = 24;
                    break;
                }
                break;
            case -134892613:
                if (language2name.equals("FINNISH")) {
                    c = 25;
                    break;
                }
                break;
            case 2573724:
                if (language2name.equals("THAI")) {
                    c = 26;
                    break;
                }
                break;
            case 2613230:
                if (language2name.equals("URDU")) {
                    c = 27;
                    break;
                }
                break;
            case 29896625:
                if (language2name.equals("JAPANESE")) {
                    c = 28;
                    break;
                }
                break;
            case 39535488:
                if (language2name.equals("PERSIAN")) {
                    c = 29;
                    break;
                }
                break;
            case 64625555:
                if (language2name.equals("CZECH")) {
                    c = 30;
                    break;
                }
                break;
            case 65414536:
                if (language2name.equals("DUTCH")) {
                    c = 31;
                    break;
                }
                break;
            case 68081376:
                if (language2name.equals("GREEK")) {
                    c = ' ';
                    break;
                }
                break;
            case 68745394:
                if (language2name.equals("HINDI")) {
                    c = '!';
                    break;
                }
                break;
            case 69932693:
                if (language2name.equals("IRISH")) {
                    c = '\"';
                    break;
                }
                break;
            case 73122672:
                if (language2name.equals("MALAY")) {
                    c = '#';
                    break;
                }
                break;
            case 79588515:
                if (language2name.equals("TAMIL")) {
                    c = Typography.dollar;
                    break;
                }
                break;
            case 82477587:
                if (language2name.equals("WELSH")) {
                    c = '%';
                    break;
                }
                break;
            case 167462569:
                if (language2name.equals("MACEDONIAN")) {
                    c = Typography.amp;
                    break;
                }
                break;
            case 243547852:
                if (language2name.equals("UKRAINIAN")) {
                    c = '\'';
                    break;
                }
                break;
            case 495326914:
                if (language2name.equals("BENGALI")) {
                    c = '(';
                    break;
                }
                break;
            case 541742905:
                if (language2name.equals("ROMANIAN")) {
                    c = ')';
                    break;
                }
                break;
            case 671907871:
                if (language2name.equals("LATVIAN")) {
                    c = '*';
                    break;
                }
                break;
            case 799935903:
                if (language2name.equals("HAITIAN_CREOLE")) {
                    c = '+';
                    break;
                }
                break;
            case 1010710335:
                if (language2name.equals("VIETNAMESE")) {
                    c = ',';
                    break;
                }
                break;
            case 1055466096:
                if (language2name.equals("ICELANDIC")) {
                    c = '-';
                    break;
                }
                break;
            case 1236562858:
                if (language2name.equals("INDONESIAN")) {
                    c = '.';
                    break;
                }
                break;
            case 1273686606:
                if (language2name.equals("CATALAN")) {
                    c = '/';
                    break;
                }
                break;
            case 1322880565:
                if (language2name.equals("PORTUGUESE")) {
                    c = '0';
                    break;
                }
                break;
            case 1464313037:
                if (language2name.equals("CHINESE")) {
                    c = '1';
                    break;
                }
                break;
            case 1488524197:
                if (language2name.equals("LITHUANIAN")) {
                    c = '2';
                    break;
                }
                break;
            case 1551960507:
                if (language2name.equals("MALTESE")) {
                    c = '3';
                    break;
                }
                break;
            case 1556949682:
                if (language2name.equals("MARATHI")) {
                    c = '4';
                    break;
                }
                break;
            case 1938625708:
                if (language2name.equals("ARABIC")) {
                    c = '5';
                    break;
                }
                break;
            case 2009207629:
                if (language2name.equals("DANISH")) {
                    c = '6';
                    break;
                }
                break;
            case 2081901978:
                if (language2name.equals("FRENCH")) {
                    c = '7';
                    break;
                }
                break;
            case 2098911622:
                if (language2name.equals("GERMAN")) {
                    c = '8';
                    break;
                }
                break;
            case 2127069055:
                if (language2name.equals("HEBREW")) {
                    c = '9';
                    break;
                }
                break;
            case 2139267348:
                if (language2name.equals("AFRIKAANS")) {
                    c = ':';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return AllLanguage.KOREAN;
            case 1:
                return AllLanguage.RUSSIAN;
            case 2:
                return AllLanguage.ALBANIAN;
            case 3:
                return AllLanguage.POLISH;
            case 4:
                return AllLanguage.SLOVAK;
            case 5:
                return AllLanguage.TELUGU;
            case 6:
                return AllLanguage.GEORGIAN;
            case 7:
                return AllLanguage.ESPERANTO;
            case '\b':
                return AllLanguage.ITALIAN;
            case '\t':
                return AllLanguage.CROATIAN;
            case '\n':
                return AllLanguage.SPANISH;
            case 11:
                return AllLanguage.ESTONIAN;
            case '\f':
                return AllLanguage.SWAHILI;
            case '\r':
                return AllLanguage.SWEDISH;
            case 14:
                return AllLanguage.GALICIAN;
            case 15:
                return AllLanguage.NORWEGIAN;
            case 16:
                return AllLanguage.BELARUSIAN;
            case 17:
                return AllLanguage.ENGLISH;
            case 18:
                return AllLanguage.HUNGARIAN;
            case 19:
                return AllLanguage.TAGALOG;
            case 20:
                return AllLanguage.SLOVENIAN;
            case 21:
                return AllLanguage.GUJARATI;
            case 22:
                return AllLanguage.BULGARIAN;
            case 23:
                return AllLanguage.TURKISH;
            case 24:
                return AllLanguage.KANNADA;
            case 25:
                return AllLanguage.FINNISH;
            case 26:
                return AllLanguage.THAI;
            case 27:
            default:
                return AllLanguage.URDU;
            case 28:
                return AllLanguage.JAPANESE;
            case 29:
                return AllLanguage.PERSIAN;
            case 30:
                return AllLanguage.CZECH;
            case 31:
                return AllLanguage.DUTCH;
            case ' ':
                return AllLanguage.GREEK;
            case '!':
                return AllLanguage.HINDI;
            case '\"':
                return AllLanguage.IRISH;
            case '#':
                return AllLanguage.MALAY;
            case '$':
                return AllLanguage.TAMIL;
            case '%':
                return AllLanguage.WELSH;
            case '&':
                return AllLanguage.MACEDONIAN;
            case '\'':
                return AllLanguage.UKRAINIAN;
            case '(':
                return AllLanguage.BENGALI;
            case ')':
                return AllLanguage.ROMANIAN;
            case '*':
                return AllLanguage.LATVIAN;
            case '+':
                return AllLanguage.HAITIAN_CREOLE;
            case ',':
                return AllLanguage.VIETNAMESE;
            case '-':
                return AllLanguage.ICELANDIC;
            case '.':
                return "id";
            case '/':
                return AllLanguage.CATALAN;
            case '0':
                return AllLanguage.PORTUGUESE;
            case '1':
                return AllLanguage.CHINESE;
            case '2':
                return AllLanguage.LITHUANIAN;
            case '3':
                return AllLanguage.MALTESE;
            case '4':
                return AllLanguage.MARATHI;
            case '5':
                return AllLanguage.ARABIC;
            case '6':
                return AllLanguage.DANISH;
            case '7':
                return AllLanguage.FRENCH;
            case '8':
                return AllLanguage.GERMAN;
            case '9':
                return AllLanguage.HEBREW;
            case ':':
                return AllLanguage.AFRIKAANS;
        }
    }

    @Override
    public void onDownloadComplete(boolean isDownload) {
        if (is_btn_translate && isDownload) {
            Is_btn_translate_auto_click = true;
            is_btn_translate = false;
        }
    }


    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("clear", isClearMain);
        setResult(RESULT_OK, resultIntent);
        finish();
        super.onBackPressed();
    }
}