package com.translatealll.anguagesapp.activity;

import static com.translatealll.anguagesapp.activity.MainActivity.downloadedlngs_list;
import static com.translatealll.anguagesapp.activity.MainActivity.getPref;
import static com.translatealll.anguagesapp.activity.MainActivity.iconlang1;
import static com.translatealll.anguagesapp.activity.MainActivity.iconlang2;
import static com.translatealll.anguagesapp.activity.MainActivity.lang_no;
import static com.translatealll.anguagesapp.activity.MainActivity.lng1name;
import static com.translatealll.anguagesapp.activity.MainActivity.lng2name;
import static com.translatealll.anguagesapp.activity.MainActivity.temp_downloadedlngs_list;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.adapter.ChatAdapter;
import com.translatealll.anguagesapp.database.ChatTable;
import com.translatealll.anguagesapp.database.RoomDB;
import com.translatealll.anguagesapp.database.WordsHistoryTable;
import com.translatealll.anguagesapp.inter.BottomSheetFragclicks;
import com.translatealll.anguagesapp.utils.BottomsheetFrag;
import com.translatealll.anguagesapp.utils.Constant;
import com.translatealll.anguagesapp.utils.PrefFile;
import com.translatealll.anguagesapp.utils.TranslateLanguage;

import java.util.ArrayList;
import java.util.Arrays;

import kotlin.text.Typography;


public class ChatActivity extends AppCompatActivity implements BottomSheetFragclicks {
    public static ImageView btn_M1;
    public static ImageView btn_M2;
    public static ImageView btn_lng_transition;
    public static ImageView btn_mic1;
    public static ImageView btn_mic2;
    public static RecyclerView chatRecView;
    public static ChatAdapter chat_adapter;

    public static Context context;
    static ArrayList<ChatTable> chatlist = new ArrayList<>();
    static LinearLayout mics_layout;
    static ConstraintLayout nochatanim_layout;
    static RoomDB roomDB;
    static TextView tv_lang1;
    static TextView tv_lang2;
    LinearLayout btn_Select1;
    LinearLayout btn_Select2;
    String chatname;
    ChatTable chatobj;
    KProgressHUD hud;
    TranslatorOptions options;

    String texttotranslate;
    String translatedtext1;
    Translator translator;
    String user;
    ImageView back;
    Toolbar chattoolbar_layout;
    ArrayList<WordsHistoryTable> lngslist = new ArrayList<>();
    int counter = 0;
    ActivityResultLauncher<Intent> intentforvoicetotext = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback() {
        @Override
        public void onActivityResult(Object obj) {
            translateText((ActivityResult) obj);
        }
    });

    public static void setChatData(Context context2) {
        MainActivity.downloadedlngs_list.clear();
        MainActivity.temp_downloadedlngs_list.clear();
        MainActivity.downloadedlngs_list = roomDB.downloadedlngs_dao().SelectDownloadedLngs();
        for (int i = 0; i < MainActivity.downloadedlngs_list.size(); i++) {
            MainActivity.temp_downloadedlngs_list.add(MainActivity.downloadedlngs_list.get(i).getDownloadedlng_name());
        }
        if (MainActivity.lang_no != null && BottomsheetFrag.languagepack != null && MainActivity.lang_no.equals("1")) {
            tv_lang1.setText(BottomsheetFrag.languagepack);
            BottomsheetFrag.languagepack = null;
            MainActivity.getPref(context2).edit().putInt("iconlang1", BottomsheetFrag.iconlanguage).apply();
        } else if (MainActivity.lang_no != null && BottomsheetFrag.languagepack != null && MainActivity.lang_no.equals(ExifInterface.GPS_MEASUREMENT_2D)) {
            tv_lang2.setText(BottomsheetFrag.languagepack);
            BottomsheetFrag.languagepack = null;
            MainActivity.getPref(context2).edit().putInt("iconlang2", BottomsheetFrag.iconlanguage).apply();
        }
        MainActivity.getPref(context2).edit().putString("lng1name", tv_lang1.getText().toString()).apply();
        MainActivity.getPref(context2).edit().putString("lng2name", tv_lang2.getText().toString()).apply();
        if (HistoryActivity.selectedchatname != null) {
            Log.e("testingg", "onResume: saved chat name" + HistoryActivity.selectedchatname);
            nochatanim_layout.setVisibility(View.GONE);
            chatRecView.setVisibility(View.VISIBLE);
            mics_layout.setVisibility(View.VISIBLE);
            if (chatlist.size() == 0) {
                chatlist = new ArrayList<>(roomDB.downloadedlngs_dao().specificchat(HistoryActivity.selectedchatname));
            }
            chatRecView.setLayoutManager(new LinearLayoutManager(context2));
            ChatAdapter chat_Adapter = new ChatAdapter(context2, chatlist);
            chat_adapter = chat_Adapter;
            chatRecView.setAdapter(chat_Adapter);
            if (chatlist.size() >= 2) {
                chatRecView.scrollToPosition(chatlist.size() - 1);
            }
        }
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
        if (str != null && str.equals("1") && BottomsheetFrag.languagepack != null) {
            lng1name = BottomsheetFrag.languagepack;
        } else {
            String str2 = lang_no;
            if (str2 != null && str2.equals("2") && BottomsheetFrag.languagepack != null) {
                lng2name = BottomsheetFrag.languagepack;
            }
        }
        tv_lang1.setText(lng1name);
        tv_lang2.setText(lng2name);
        getPref(context).edit().putString("lng1name", tv_lang1.getText().toString().substring(0, 1).toUpperCase() + tv_lang1.getText().toString().substring(1).toUpperCase()).apply();
        getPref(context).edit().putString("lng2name", tv_lang2.getText().toString().substring(0, 1).toUpperCase() + tv_lang2.getText().toString().substring(1).toUpperCase()).apply();
        getPref(context).edit().putInt("iconlang1", iconlang1).apply();
        getPref(context).edit().putInt("iconlang2", iconlang2).apply();
    }

    @Override
    public void onDownloadComplete(boolean isDownload) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Initialization();
        context = this;
    }

    private void Initialization() {
        roomDB = RoomDB.getRoomDBInstance(this);
        chatRecView = (RecyclerView) findViewById(R.id.chatRecView);
        nochatanim_layout = (ConstraintLayout) findViewById(R.id.nochatanim_layout);
        mics_layout = (LinearLayout) findViewById(R.id.linearLayout);
        btn_M1 = (ImageView) findViewById(R.id.mice_lang1);
        btn_M2 = (ImageView) findViewById(R.id.mice_lang2);
        tv_lang1 = (TextView) findViewById(R.id.tv_lang1);
        tv_lang2 = (TextView) findViewById(R.id.tv_lang2);
        btn_Select1 = (LinearLayout) findViewById(R.id.btn1);
        btn_Select2 = (LinearLayout) findViewById(R.id.btn2);
        back = findViewById(R.id.back);
        btn_lng_transition = (ImageView) findViewById(R.id.img_lng_transition);
        lng1name = MainActivity.getPref(this).getString("lng1name", "ENGLISH");
        lng2name = MainActivity.getPref(this).getString("lng2name", "URDU");
        iconlang1 = MainActivity.getPref(this).getInt("iconlang1", R.drawable.flg_english);
        iconlang2 = MainActivity.getPref(this).getInt("iconlang2", R.drawable.flg_urdu);
        tv_lang1.setText(lng1name);
        tv_lang2.setText(lng2name);

        chatlist.clear();

        btn_M1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.lng1name = MainActivity.getPref(ChatActivity.this).getString("lng1name", "ENGLISH");
                MainActivity.lng1code = Chooselng1Code(MainActivity.lng1name);
                MainActivity.lang_no = "1";

                if (isNetworkConnected()) {
                    if (!MainActivity.temp_downloadedlngs_list.contains(tv_lang1.getText().toString())) {
                        Toast.makeText(ChatActivity.this, "Download " + tv_lang1.getText().toString(), Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!MainActivity.temp_downloadedlngs_list.contains(tv_lang2.getText().toString())) {
                        Toast.makeText(ChatActivity.this, "Download " + tv_lang2.getText().toString(), Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        user = "user1";
                        GetUserVoice(MainActivity.lng1code);
                        return;
                    }
                }
                Toast.makeText(ChatActivity.this, "Internet is needed for speech to text translation", Toast.LENGTH_SHORT).show();
            }
        });
        btn_M2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.lng2code = Chooselng2Code(MainActivity.lng2name);
                MainActivity.lang_no = ExifInterface.GPS_MEASUREMENT_2D;
                if (isNetworkConnected()) {
                    if (!MainActivity.temp_downloadedlngs_list.contains(lng1name)) {
                        Toast.makeText(ChatActivity.this, "Download " + tv_lang1.getText().toString(), Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!MainActivity.temp_downloadedlngs_list.contains(lng2name)) {
                        Toast.makeText(ChatActivity.this, "Download " + tv_lang2.getText().toString(), Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        user = "user2";
                        GetUserVoice(MainActivity.lng2code);
                        return;
                    }
                }
                Toast.makeText(ChatActivity.this, "Internet is needed for speech to text translation", Toast.LENGTH_SHORT).show();
            }
        });
        btn_Select1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lang_no = "1";
                PrefFile.getInstance().setString(Constant.LEFTRIGHT, "chat");
                BottomsheetFrag bottomsheetFrag = new BottomsheetFrag(ChatActivity.this);
                Bundle bundle = new Bundle();
                bundle.putString("langno", "1");
                bundle.putString("from", "chatActivity");
                bottomsheetFrag.setArguments(bundle);
                bottomsheetFrag.show(getSupportFragmentManager(), "TAG");
            }
        });
        btn_Select2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefFile.getInstance().setString(Constant.LEFTRIGHT, "chat");
                lang_no = "2";
                BottomsheetFrag bottomsheetFrag = new BottomsheetFrag(ChatActivity.this);
                Bundle bundle = new Bundle();
                bundle.putString("langno", "2");
                bundle.putString("from", "chatActivity");
                bottomsheetFrag.setArguments(bundle);
                bottomsheetFrag.show(getSupportFragmentManager(), "TAG");
            }
        });
        tv_lang2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lang_no = "2";
                BottomsheetFrag bottomsheetFrag = new BottomsheetFrag(ChatActivity.this);
                Bundle bundle = new Bundle();
                bundle.putString("langno", "2");
                bundle.putString("from", "chatActivity");
                bottomsheetFrag.setArguments(bundle);
                bottomsheetFrag.show(getSupportFragmentManager(), "TAG");
            }
        });
        findViewById(R.id.btnmore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context, R.style.WideDialog100);
                dialog.setContentView(R.layout.popup_savechat);

                TextView btnSaveChat = dialog.findViewById(R.id.btnSaveChat);
                TextView btnClearChat = dialog.findViewById(R.id.btnClearChat);

                btnSaveChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chat();
                        dialog.dismiss();
                    }
                });

                btnClearChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chatlist.size() > 0) {
                            chatlist.clear();
                            mics_layout.setVisibility(View.VISIBLE);
                            chatRecView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                            ChatAdapter chat_Adapter = new ChatAdapter(ChatActivity.this, chatlist);
                            chat_adapter = chat_Adapter;
                            chatRecView.setAdapter(chat_Adapter);
                        }
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        btn_lng_transition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 180.0f, 1, 0.5f, 1, 0.5f);
                rotateAnimation.setDuration(500L);
                btn_lng_transition.startAnimation(rotateAnimation);
                lng1name = MainActivity.getPref(ChatActivity.this).getString("lng1name", "ENGLISH");
                lng2name = MainActivity.getPref(ChatActivity.this).getString("lng2name", "URDU");
                tv_lang1.setText(lng2name);
                tv_lang2.setText(lng1name);
                iconlang2 = MainActivity.getPref(ChatActivity.this).getInt("iconlang1", R.drawable.flg_english);
                iconlang1 = MainActivity.getPref(ChatActivity.this).getInt("iconlang2", R.drawable.flg_urdu);
                MainActivity.getPref(ChatActivity.this).edit().putString("lng1name", tv_lang1.getText().toString()).apply();
                MainActivity.getPref(ChatActivity.this).edit().putString("lng2name", tv_lang2.getText().toString()).apply();
                lng1name = MainActivity.getPref(ChatActivity.this).getString("lng1name", "ENGLISH");
                lng2name = MainActivity.getPref(ChatActivity.this).getString("lng2name", "URDU");
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void chat() {
        final Dialog dialog = new Dialog(this,R.style.WideDialog200);
        dialog.setContentView(R.layout.layout_savechat);

        EditText etChatName = dialog.findViewById(R.id.etChatName);
        Button btnDone = dialog.findViewById(R.id.btnDone);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lambda$SaveChatdialog$11(etChatName, dialog);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.mics_layout.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    private void lambda$SaveChatdialog$11(EditText etChatName, Dialog dialog) {
        String obj = etChatName.getText().toString();
        chatname = obj;
        if (obj.equals("")) {
            Toast.makeText(this, "Chat Name cannot be empty", Toast.LENGTH_SHORT).show();
            mics_layout.setVisibility(View.VISIBLE);
        } else if (chatlist.size() > 0) {
            for (int i2 = 0; i2 < chatlist.size(); i2++) {
                chatobj = new ChatTable(chatlist.get(i2).getLang1(), chatlist.get(i2).getLang2(), chatlist.get(i2).getTexttotranslate(), chatlist.get(i2).getTranslatedtext(), chatlist.get(i2).getUser(), chatname);
                roomDB.downloadedlngs_dao().insert_chat(chatobj);
                Log.e("chatdata", "SaveChatdialog: " + chatlist.size() + chatlist.get(i2).getTexttotranslate());
            }
            Toast.makeText(this, "Chat Saved", Toast.LENGTH_SHORT).show();
            chatlist.clear();
            chat_adapter.notifyDataSetChanged();
            mics_layout.setVisibility(View.VISIBLE);
        } else {
            mics_layout.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Chat can not be empty", Toast.LENGTH_SHORT).show();
        }
    }



    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void GetUserVoice(String lng) {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        intent.putExtra("android.speech.extra.LANGUAGE", lng);
        intent.putExtra("android.speech.extra.PROMPT", "Speak Something");
        try {
            intentforvoicetotext.launch(intent);
        } catch (ActivityNotFoundException e) {
            Log.e("", "GetUserVoice: " + e.getMessage());
        }
    }

    public void translateText(ActivityResult activityResult) {
        if (activityResult.getResultCode() == -1 && activityResult.getData() != null) {
            nochatanim_layout.setVisibility(View.GONE);
            chatRecView.setVisibility(View.VISIBLE);
            texttotranslate = String.valueOf(activityResult.getData().getStringArrayListExtra("android.speech.extra.RESULTS").get(0));
            lngslist.addAll(Arrays.asList(roomDB.downloadedlngs_dao().selectalllngs()));
            for (int i = 0; i < lngslist.size(); i++) {
                temp_downloadedlngs_list.add(lngslist.get(i).getLanguage1());
                temp_downloadedlngs_list.add(lngslist.get(i).getLanguage2());
            }
            if (temp_downloadedlngs_list.contains(tv_lang1.getText().toString()) && temp_downloadedlngs_list.contains(tv_lang2.getText().toString())) {
                TranslatingText(texttotranslate, user);
                return;
            }
            return;
        }
        nochatanim_layout.setVisibility(View.VISIBLE);
        chatRecView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.from = "fromChat";
    }

    private void TranslatingText(final String texttotranslate, final String user) {
        MainActivity.lng1code = Chooselng1Code(lng1name);
        MainActivity.lng2code = Chooselng2Code(lng2name);
        hud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Translating...").setCancellable(false).show();
        if (user.equals("user1")) {
            options = new TranslatorOptions.Builder().setSourceLanguage(MainActivity.lng1code).setTargetLanguage(MainActivity.lng2code).build();
        } else if (user.equals("user2")) {
            options = new TranslatorOptions.Builder().setSourceLanguage(MainActivity.lng2code).setTargetLanguage(MainActivity.lng1code).build();
        }
        Translator client = Translation.getClient(options);
        translator = client;
        client.translate(texttotranslate).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(@NonNull Object o) {
                hud.dismiss();
                try {
                    translatedtext1 = (String) o;
                    ChatTable chat_Table = new ChatTable(lng1name, lng2name, texttotranslate, translatedtext1, user, null);
                    chatobj = chat_Table;
                    chatlist.add(chat_Table);
                    chatRecView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                    ChatAdapter chat_Adapter = new ChatAdapter(ChatActivity.this, chatlist);
                    chat_adapter = chat_Adapter;
                    chatRecView.setAdapter(chat_Adapter);
                    if (chatlist.size() >= 2) {
                        chatRecView.scrollToPosition(chatlist.size() - 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exc) {
                Toast.makeText(ChatActivity.this, "Error:Failed to translate text", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setChatData(this);
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
                    c = '\n';
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
                return TranslateLanguage.KOREAN;
            case 1:
                return TranslateLanguage.RUSSIAN;
            case 2:
                return TranslateLanguage.ALBANIAN;
            case 3:
                return TranslateLanguage.POLISH;
            case 4:
                return TranslateLanguage.SLOVAK;
            case 5:
                return TranslateLanguage.TELUGU;
            case 6:
                return TranslateLanguage.GEORGIAN;
            case 7:
                return TranslateLanguage.ESPERANTO;
            case '\b':
                return TranslateLanguage.ITALIAN;
            case '\t':
                return TranslateLanguage.CROATIAN;
            case '\n':
                return TranslateLanguage.SPANISH;
            case 11:
                return TranslateLanguage.ESTONIAN;
            case '\f':
                return TranslateLanguage.SWAHILI;
            case '\r':
                return TranslateLanguage.SWEDISH;
            case 14:
                return TranslateLanguage.GALICIAN;
            case 15:
                return TranslateLanguage.NORWEGIAN;
            case 16:
                return TranslateLanguage.BELARUSIAN;
            case 17:
            default:
                return TranslateLanguage.ENGLISH;
            case 18:
                return TranslateLanguage.HUNGARIAN;
            case 19:
                return TranslateLanguage.TAGALOG;
            case 20:
                return TranslateLanguage.SLOVENIAN;
            case 21:
                return TranslateLanguage.GUJARATI;
            case 22:
                return TranslateLanguage.BULGARIAN;
            case 23:
                return TranslateLanguage.TURKISH;
            case 24:
                return TranslateLanguage.KANNADA;
            case 25:
                return TranslateLanguage.FINNISH;
            case 26:
                return TranslateLanguage.THAI;
            case 27:
                return TranslateLanguage.URDU;
            case 28:
                return TranslateLanguage.JAPANESE;
            case 29:
                return TranslateLanguage.PERSIAN;
            case 30:
                return TranslateLanguage.CZECH;
            case 31:
                return TranslateLanguage.DUTCH;
            case ' ':
                return TranslateLanguage.GREEK;
            case '!':
                return TranslateLanguage.HINDI;
            case '\"':
                return TranslateLanguage.IRISH;
            case '#':
                return TranslateLanguage.MALAY;
            case '$':
                return TranslateLanguage.TAMIL;
            case '%':
                return TranslateLanguage.WELSH;
            case '&':
                return TranslateLanguage.MACEDONIAN;
            case '\'':
                return TranslateLanguage.UKRAINIAN;
            case '(':
                return TranslateLanguage.BENGALI;
            case ')':
                return TranslateLanguage.ROMANIAN;
            case '*':
                return TranslateLanguage.LATVIAN;
            case '+':
                return TranslateLanguage.HAITIAN_CREOLE;
            case ',':
                return TranslateLanguage.VIETNAMESE;
            case '-':
                return TranslateLanguage.ICELANDIC;
            case '.':
                return "id";
            case '/':
                return TranslateLanguage.CATALAN;
            case '0':
                return TranslateLanguage.PORTUGUESE;
            case '1':
                return TranslateLanguage.CHINESE;
            case '2':
                return TranslateLanguage.LITHUANIAN;
            case '3':
                return TranslateLanguage.MALTESE;
            case '4':
                return TranslateLanguage.MARATHI;
            case '5':
                return TranslateLanguage.ARABIC;
            case '6':
                return TranslateLanguage.DANISH;
            case '7':
                return TranslateLanguage.FRENCH;
            case '8':
                return TranslateLanguage.GERMAN;
            case '9':
                return TranslateLanguage.HEBREW;
            case ':':
                return TranslateLanguage.AFRIKAANS;
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
                return TranslateLanguage.KOREAN;
            case 1:
                return TranslateLanguage.RUSSIAN;
            case 2:
                return TranslateLanguage.ALBANIAN;
            case 3:
                return TranslateLanguage.POLISH;
            case 4:
                return TranslateLanguage.SLOVAK;
            case 5:
                return TranslateLanguage.TELUGU;
            case 6:
                return TranslateLanguage.GEORGIAN;
            case 7:
                return TranslateLanguage.ESPERANTO;
            case '\b':
                return TranslateLanguage.ITALIAN;
            case '\t':
                return TranslateLanguage.CROATIAN;
            case '\n':
                return TranslateLanguage.SPANISH;
            case 11:
                return TranslateLanguage.ESTONIAN;
            case '\f':
                return TranslateLanguage.SWAHILI;
            case '\r':
                return TranslateLanguage.SWEDISH;
            case 14:
                return TranslateLanguage.GALICIAN;
            case 15:
                return TranslateLanguage.NORWEGIAN;
            case 16:
                return TranslateLanguage.BELARUSIAN;
            case 17:
                return TranslateLanguage.ENGLISH;
            case 18:
                return TranslateLanguage.HUNGARIAN;
            case 19:
                return TranslateLanguage.TAGALOG;
            case 20:
                return TranslateLanguage.SLOVENIAN;
            case 21:
                return TranslateLanguage.GUJARATI;
            case 22:
                return TranslateLanguage.BULGARIAN;
            case 23:
                return TranslateLanguage.TURKISH;
            case 24:
                return TranslateLanguage.KANNADA;
            case 25:
                return TranslateLanguage.FINNISH;
            case 26:
                return TranslateLanguage.THAI;
            case 27:
            default:
                return TranslateLanguage.URDU;
            case 28:
                return TranslateLanguage.JAPANESE;
            case 29:
                return TranslateLanguage.PERSIAN;
            case 30:
                return TranslateLanguage.CZECH;
            case 31:
                return TranslateLanguage.DUTCH;
            case ' ':
                return TranslateLanguage.GREEK;
            case '!':
                return TranslateLanguage.HINDI;
            case '\"':
                return TranslateLanguage.IRISH;
            case '#':
                return TranslateLanguage.MALAY;
            case '$':
                return TranslateLanguage.TAMIL;
            case '%':
                return TranslateLanguage.WELSH;
            case '&':
                return TranslateLanguage.MACEDONIAN;
            case '\'':
                return TranslateLanguage.UKRAINIAN;
            case '(':
                return TranslateLanguage.BENGALI;
            case ')':
                return TranslateLanguage.ROMANIAN;
            case '*':
                return TranslateLanguage.LATVIAN;
            case '+':
                return TranslateLanguage.HAITIAN_CREOLE;
            case ',':
                return TranslateLanguage.VIETNAMESE;
            case '-':
                return TranslateLanguage.ICELANDIC;
            case '.':
                return "id";
            case '/':
                return TranslateLanguage.CATALAN;
            case '0':
                return TranslateLanguage.PORTUGUESE;
            case '1':
                return TranslateLanguage.CHINESE;
            case '2':
                return TranslateLanguage.LITHUANIAN;
            case '3':
                return TranslateLanguage.MALTESE;
            case '4':
                return TranslateLanguage.MARATHI;
            case '5':
                return TranslateLanguage.ARABIC;
            case '6':
                return TranslateLanguage.DANISH;
            case '7':
                return TranslateLanguage.FRENCH;
            case '8':
                return TranslateLanguage.GERMAN;
            case '9':
                return TranslateLanguage.HEBREW;
            case ':':
                return TranslateLanguage.AFRIKAANS;
        }
    }
}
