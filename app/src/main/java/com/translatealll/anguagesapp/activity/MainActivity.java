package com.translatealll.anguagesapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.database.DownloadedLngsTable;
import com.translatealll.anguagesapp.database.ExCommon;
import com.translatealll.anguagesapp.database.RoomDB;
import com.translatealll.anguagesapp.databinding.ActivityMainBinding;
import com.translatealll.anguagesapp.inter.BottomSheetFragclicks;
import com.translatealll.anguagesapp.utils.BottomsheetFrag;
import com.translatealll.anguagesapp.utils.Constant;
import com.translatealll.anguagesapp.utils.PrefFile;
import com.translatealll.anguagesapp.utils.TranslateLanguage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kotlin.text.Typography;

public class MainActivity extends AppCompatActivity implements BottomSheetFragclicks {

    public static int iconlang1;
    public static int iconlang2;
    public static String lang_no;
    public static String lng1code;
    public static String lng1name;
    public static String lng2code;
    public static String lng2name;
    public static SharedPreferences main_pref;
    public static RoomDB roomDB;

    public static List<DownloadedLngsTable> downloadedlngs_list = new ArrayList();
    public static ArrayList<String> temp_downloadedlngs_list = new ArrayList<>();
    boolean is_btn_translate;
    KProgressHUD kprogresshud;
    Animation translatedcard_anim;
    TextToSpeech txttospeech;
    boolean Is_btn_translate_auto_click = false;
    ActivityMainBinding binding;
    public static String from;
    public static TextView tvLanguageDownload;
    public static TextView tv_lang1;
    public static TextView tv_lang2;
    DrawerLayout mDrawerLayout;
    ImageView menu;

    ActivityResultLauncher<Intent> activityResultLauncher;

    String[] permission;
    public static final int CAMERA_PERM_CODE = 101;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         bitmap = (Bitmap) getIntent().getParcelableExtra("imageCrop");
        mDrawerLayout = findViewById(R.id.drawer_layout);
        tv_lang1 = findViewById(R.id.tv_lang1);
        tv_lang2 = findViewById(R.id.tv_lang2);
        menu = findViewById(R.id.menu);
        roomDB = RoomDB.getRoomDBInstance(this);
        temp_downloadedlngs_list.clear();
        downloadedlngs_list.clear();
        downloadedlngs_list = roomDB.downloadedlngs_dao().SelectDownloadedLngs();
        for (int i = 0; i < downloadedlngs_list.size(); i++) {
            Log.d("flow", "onCreatezzz: " + downloadedlngs_list.get(i).getDownloadedlng_name());
            temp_downloadedlngs_list.add(downloadedlngs_list.get(i).getDownloadedlng_name());
        }

        Log.e("dgdfgdgf", "onCreate: " + temp_downloadedlngs_list.size());
        Log.e("dgdfgdgf", "downloadd: " + downloadedlngs_list.size());
        binding.imgPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pasteTextFromClipboard();
            }
        });

        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        binding.etUserinput.setInputType(InputType.TYPE_CLASS_TEXT);
        binding.etUserinput.requestFocus();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(binding.etUserinput, InputMethodManager.SHOW_FORCED);
        translatedcard_anim = AnimationUtils.loadAnimation(this, R.anim.translatedcard_anim);
        binding.etUserinput.setMovementMethod(new ScrollingMovementMethod());
        kprogresshud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Translating ").setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f);
        Log.e("flow", "onCreate:size od downloaded and temp list " + downloadedlngs_list.size() + "////" + temp_downloadedlngs_list.size());
        lng1name = getPref(this).getString("lng1name", "English");
        lng2name = getPref(this).getString("lng2name", "French");
        binding.tvLang1.setText(lng1name);
        binding.tvLang2.setText(lng2name);
        binding.etUserinput.setText("");

        binding.conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
            }
        });
        binding.history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,HistoryActivity.class));
            }
        });
        binding.linearLeftLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lang_no = "1";
                BottomsheetFrag bottomsheetFrag = new BottomsheetFrag(MainActivity.this);
                Bundle bundle = new Bundle();
                bundle.putString("langno", "1");
                bundle.putString("from", "mainActivity");
                bottomsheetFrag.setArguments(bundle);
                bottomsheetFrag.show(getSupportFragmentManager(), "TAG");
                PrefFile.getInstance().setString(Constant.LEFTRIGHT, "main");
            }
        });
        binding.linearRightLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lang_no = "2";
                BottomsheetFrag bottomsheetFrag = new BottomsheetFrag(MainActivity.this);
                Bundle bundle = new Bundle();
                bundle.putString("langno", "2");
                bundle.putString("from", "mainActivity");
                bottomsheetFrag.setArguments(bundle);
                bottomsheetFrag.show(getSupportFragmentManager(), "TAG");
                PrefFile.getInstance().setString(Constant.LEFTRIGHT, "main");
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    ArrayList<String> resultData = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String recognizedText = resultData.get(0);
//                    Toast.makeText(MainActivity.this, "You said: " + recognizedText, Toast.LENGTH_LONG).show();
                    binding.etUserinput.setText(recognizedText);
                    if (!binding.etUserinput.getText().toString().isEmpty()) {
                        binding.ivClearText.setVisibility(View.VISIBLE);
                    } else {
                        binding.ivClearText.setVisibility(View.GONE);
                    }
                }

            }
        });
        binding.ivClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ivClearText.setVisibility(View.GONE);
                binding.etUserinput.setText("");
            }
        });
        binding.ivMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lng1code = Chooselng1Code(lng1name);
                if (ExCommon.isOnline(MainActivity.this)) {
                    try {
                        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
                        intent.putExtra("android.speech.extra.LANGUAGE", lng1code);
                        intent.putExtra("android.speech.extra.PROMPT", "Speak something");
                        activityResultLauncher.launch(intent);
                        return;
                    } catch (Exception unused) {
                        Toast.makeText(MainActivity.this, "This mic is not supported by the Device", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(MainActivity.this, "Internet is needed for speech to text translation", Toast.LENGTH_SHORT).show();
            }
        });
        binding.etUserinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                someActivityResultLauncher.launch(new Intent(MainActivity.this, TranslateNewActivity.class).putExtra("mic", binding.etUserinput.getText().toString()).putExtra("pos", "1"));
            }
        });

        binding.ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPermission();
            }
        });


        if (bitmap != null) {
            TextRecognizer build = new TextRecognizer.Builder(getApplicationContext()).build();
            if (!build.isOperational()) {
                binding.etUserinput.setText("error");
                return;
            }
            Frame build2 = new Frame.Builder().setBitmap(bitmap).build();
            StringBuilder sb = new StringBuilder();
            SparseArray<TextBlock> detect = build.detect(build2);
            if (detect.size() > 0) {
                for (int i = 0; i < detect.size(); i++) {
                    TextBlock valueAt = detect.valueAt(i);
                    sb.append(valueAt.getValue());
                    sb.append("\n");
                    for (Text text : valueAt.getComponents()) {
                        Log.e("lines", text.getValue());
                        for (Text text2 : text.getComponents()) {
                            Log.e("element", text2.getValue());
                        }
                    }
                }
                binding.etUserinput.setText(sb.substring(0, sb.toString().length() - 1));
                return;
            }
            Toast.makeText(this, "No text found", Toast.LENGTH_SHORT).show();
        }


    }

    private void cameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO};
        } else {
            permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        }

        Dexter.withContext(this).withPermissions(permission).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(MainActivity.this);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (requestCode != 203) {
            if (resultCode != 204) {
                return;
            }
            throw null;
        } else if (data != null) {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);
            if (resultCode == -1) {
                if (activityResult != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), activityResult.getUri());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
                    }
                }
                if (bitmap != null) {
                    TextRecognizer build = new TextRecognizer.Builder(getApplicationContext()).build();
                    if (!build.isOperational()) {
                        binding.etUserinput.setText("error");
                        return;
                    }
                    Frame build2 = new Frame.Builder().setBitmap(bitmap).build();
                    StringBuilder sb = new StringBuilder();
                    SparseArray<TextBlock> detect = build.detect(build2);
                    if (detect.size() > 0) {
                        for (int i = 0; i < detect.size(); i++) {
                            TextBlock valueAt = detect.valueAt(i);
                            sb.append(valueAt.getValue());
                            sb.append("\n");
                            for (Text text : valueAt.getComponents()) {
                                Log.e("lines", text.getValue());
                                for (Text text2 : text.getComponents()) {
                                    Log.e("element", text2.getValue());
                                }
                            }
                        }
                        binding.etUserinput.setText(sb.substring(0, sb.toString().length() - 1));
                        return;
                    }
                    Toast.makeText(this, "No text found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() != RESULT_CANCELED) {
                Log.e("RESULT_CANCELED", "onActivityResult: ");
                CropImage.ActivityResult resultImage = CropImage.getActivityResult(result.getData());
                if (result.getResultCode() == RESULT_OK) {
                    Uri resultUri = resultImage.getUri();
                } else if (result.getResultCode() == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = resultImage.getError();
                }

            }
        }
    });


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                binding.etUserinput.setText("");
                binding.ivClearText.setVisibility(View.GONE);
            }
        }
    });


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

    private void pasteTextFromClipboard() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null && clipboardManager.hasPrimaryClip()) {
            ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                ClipData.Item item = clipData.getItemAt(0);
                CharSequence pasteText = item.getText();
                if (pasteText != null) {
                    binding.etUserinput.setText(pasteText);
                }
            }
        }
    }

    public static SharedPreferences getPref(Context context) {
        SharedPreferences sharedPreferences = main_pref;
        if (sharedPreferences != null) {
            return sharedPreferences;
        }
        SharedPreferences sharedPreferences2 = context.getSharedPreferences("mainactivityprefernce", 0);
        main_pref = sharedPreferences2;
        return sharedPreferences2;
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
        getPref(context).edit().putString("lng1name", tv_lang1.getText().toString()).apply();
        getPref(context).edit().putString("lng2name", tv_lang2.getText().toString()).apply();
        getPref(context).edit().putInt("iconlang1", iconlang1).apply();
        getPref(context).edit().putInt("iconlang2", iconlang2).apply();
    }

    @Override
    public void onDownloadComplete(boolean isDownload) {
        if (is_btn_translate && isDownload) {
            Is_btn_translate_auto_click = true;
//            btn_translate.performClick();
            is_btn_translate = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (from == "fromChat") {
            setData(this);
            Log.e("fromChatfromChat", "onResume: ");
        }
        if (kprogresshud.isShowing()) {
            kprogresshud.dismiss();
        }
    }
}