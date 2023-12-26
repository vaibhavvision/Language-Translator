package com.translatealll.anguagesapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.intuit.sdp.BuildConfig;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.crop.CropFragment;
import com.translatealll.anguagesapp.database.DownloadedLngsTable;
import com.translatealll.anguagesapp.database.ExCommon;
import com.translatealll.anguagesapp.database.RoomDB;
import com.translatealll.anguagesapp.database.WordsHistoryTable;
import com.translatealll.anguagesapp.databinding.ActivityMainBinding;
import com.translatealll.anguagesapp.inter.DialogFragmentClick;
import com.translatealll.anguagesapp.utils.AllLanguage;
import com.translatealll.anguagesapp.utils.BottomSheetFragment;
import com.translatealll.anguagesapp.utils.Const;
import com.translatealll.anguagesapp.utils.PrefFile;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kotlin.text.Typography;

public class MainActivity extends AppCompatActivity implements DialogFragmentClick, CropFragment.OnCropPhoto {
    public static String CameraPic = "";

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
    boolean Is_btn_translate_auto_click = false;
    ActivityMainBinding binding;
    public static String from;
    public static TextView tv_lang1;
    public static TextView tv_lang2;
    DrawerLayout mDrawerLayout;
    ImageView menu;
    ActivityResultLauncher<Intent> activityResultLauncher;
    String[] permission;
    public static final int CAMERA_PERM_CODE = 101;
    Bitmap bitmap;
    private Uri image_uri;

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
        binding.etUserinput.setInputType(InputType.TYPE_NULL);
        binding.btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.SUBJECT", "English Learning App");
                intent.putExtra("android.intent.extra.TEXT", "\nFind the best app for your All Language Translate is here. \n\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.LIBRARY_PACKAGE_NAME + "\n\n");
                startActivity(Intent.createChooser(intent, "choose one"));
            }
        });
        binding.btnrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });
        binding.btnConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
        binding.btnprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PolicyActivity.class);
                startActivity(intent);
            }
        });
        binding.etUserinput.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (binding.etUserinput.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });
        Log.e("dgdfgdgf", "onCreate: " + temp_downloadedlngs_list.size());
        Log.e("dgdfgdgf", "downloadd: " + downloadedlngs_list.size());
        binding.phrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PhrasesActivity.class));
            }
        });
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

        translatedcard_anim = AnimationUtils.loadAnimation(this, R.anim.translatedcard_anim);
        binding.etUserinput.setMovementMethod(new ScrollingMovementMethod());
        kprogresshud = KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Translating ").setCancellable(true).setAnimationSpeed(2).setDimAmount(0.5f);
        Log.e("flow", "onCreate:size od downloaded and temp list " + downloadedlngs_list.size() + "////" + temp_downloadedlngs_list.size());
        lng1name = getPref(this).getString("lng1name", "ENGLISH");
        lng2name = getPref(this).getString("lng2name", "FRENCH");
        if (!lng1name.equals(lng2name)) {
            binding.tvLang1.setText(lng1name);
            binding.tvLang2.setText(lng2name);
        }
        binding.etUserinput.setText("");

        if (!binding.etUserinput.getText().toString().trim().isEmpty()) {
            binding.ivClearText.setVisibility(View.VISIBLE);
        } else {
            binding.ivClearText.setVisibility(View.GONE);
        }

        binding.conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ConvsersationActivity.class));
            }
        });
        binding.history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
        binding.linearLeftLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lang_no = "1";
                BottomSheetFragment bottomsheetFrag = new BottomSheetFragment(MainActivity.this);
                Bundle bundle = new Bundle();
                bundle.putString("langno", "1");
                bundle.putString("from", "mainActivity");
                bottomsheetFrag.setArguments(bundle);
                bottomsheetFrag.show(getSupportFragmentManager(), "TAG");
                PrefFile.getInstance().setString(Const.DROPDOWN, "main");
            }
        });
        binding.linearRightLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lang_no = "2";
                BottomSheetFragment bottomsheetFrag = new BottomSheetFragment(MainActivity.this);
                Bundle bundle = new Bundle();
                bundle.putString("langno", "2");
                bundle.putString("from", "mainActivity");
                bottomsheetFrag.setArguments(bundle);
                bottomsheetFrag.show(getSupportFragmentManager(), "TAG");
                PrefFile.getInstance().setString(Const.DROPDOWN, "main");
            }
        });

        binding.ivLanguageConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 180.0f, 1, 0.5f, 1, 0.5f);
                rotateAnimation.setDuration(500L);
                binding.ivLanguageConvert.startAnimation(rotateAnimation);
                lng2name = getPref(MainActivity.this).getString("lng1name", "ENGLISH");
                lng1name = getPref(MainActivity.this).getString("lng2name", "URDU");
                binding.tvLang1.setText(lng1name);
                binding.tvLang2.setText(lng2name);
                getPref(MainActivity.this).edit().putString("lng1name", lng1name).apply();
                getPref(MainActivity.this).edit().putString("lng2name", lng2name).apply();
                lng1name = getPref(MainActivity.this).getString("lng1name", "ENGLISH");
                lng2name = getPref(MainActivity.this).getString("lng2name", "URDU");
                binding.tvLang1.setText(lng1name);
                binding.tvLang2.setText(lng2name);
                Log.d("TAG", "language1" + lng1name);
                Log.d("TAG", "language2" + lng2name);

                if (lng1name.equals("KOREAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));
                } else if (lng1name.equals("RUSSIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("ALBANIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("POLISH")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("SLOVAK")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("TELUGU")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("GEORGIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("ESPERANTO")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("ITALIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("CROATIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("SPANISH")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("ESTONIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("SWAHILI")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("SWEDISH")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("GALICIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("NORWEGIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("BELARUSIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("ENGLISH")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT).toString());

                } else if (lng1name.equals("HUNGARIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("TAGALOG")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("SLOVENIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("GUJARATI")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("BULGARIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("TURKISH")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("KANNADA")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("FINNISH")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("THAI")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("URDU")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("JAPANESE")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("PERSIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("CZECH")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("DUTCH")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("GREEK")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("HINDI")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT).toString());

                } else if (lng1name.equals("IRISH")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("MALAY")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("TAMIL")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("WELSH")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("MACEDONIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("UKRAINIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("BENGALI")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("ROMANIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("LATVIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("HAITIAN_CREOLE")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT).toString());

                } else if (lng1name.equals("VIETNAMESE")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("ICELANDIC")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("INDONESIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("CATALAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("PORTUGUESE")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("CHINESE")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("LITHUANIAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("MALTESE")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("MARATHI")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("ARABIC")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("DANISH")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("FRENCH")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("GERMAN")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));

                } else if (lng1name.equals("HEBREW")) {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));
                } else {
                    binding.etUserinput.setText(PrefFile.getInstance().getString(Const.LANCONVERT));
                }

                if (!binding.etUserinput.getText().toString().trim().isEmpty()) {
                    TranslateWords(binding.etUserinput.getText().toString().trim());
                }

            }
        });
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    ArrayList<String> resultData = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String recognizedText = resultData.get(0);
                    someActivityResultLauncher.launch(new Intent(MainActivity.this, TranslateActivity.class).putExtra("mic", recognizedText.toString()).putExtra("pos", "1"));
                    if (!binding.etUserinput.getText().toString().trim().isEmpty()) {
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
                someActivityResultLauncher.launch(new Intent(MainActivity.this, TranslateActivity.class).putExtra("mic", binding.etUserinput.getText().toString()).putExtra("pos", "1"));
            }
        });
        binding.ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPermission();
            }
        });
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
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
                image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                activityCameraLauncher.launch(cameraIntent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    ActivityResultLauncher<Intent> activityCameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() != RESULT_CANCELED) {
                Log.e("RESULT_CANCELED", "onAxctivityResult: ");
                if (result.getResultCode() == RESULT_OK) {
                    Bitmap inputImage = uriToBitmap(image_uri);
                    Log.e(":fdfdf", "onActivityResult: " + image_uri);
                    CropFragment.show(MainActivity.this, MainActivity.this, inputImage);
                }
            }
        }
    });

    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void TranslateWords(String texttotranslate) {
        lng1code = Chooselng1Code(lng1name);
        lng2code = Chooselng2Code(lng2name);
        Translation.getClient(new TranslatorOptions.Builder().setSourceLanguage(lng1code).setTargetLanguage(lng2code).build()).translate(texttotranslate).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object obj) {
                binding.etUserinput.setText(obj.toString());
                PrefFile.getInstance().setString(Const.LANCONVERT, binding.etUserinput.getText().toString());

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
                Toast.makeText(MainActivity.this, "failed to translate", Toast.LENGTH_SHORT).show();
            }
        });
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

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                binding.tvLang1.setText(lng1name);
                binding.tvLang2.setText(lng2name);
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

    private void pasteTextFromClipboard() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null && clipboardManager.hasPrimaryClip()) {
            ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                ClipData.Item item = clipData.getItemAt(0);
                CharSequence pasteText = item.getText();
                if (pasteText != null) {
                    someActivityResultLauncher.launch(new Intent(MainActivity.this, TranslateActivity.class).putExtra("mic", pasteText.toString()).putExtra("pos", "1"));
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
        if (roomDB != null) {
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
    }

    @Override
    public void onDownloadComplete(boolean isDownload) {
        if (is_btn_translate && isDownload) {
            Is_btn_translate_auto_click = true;
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

    @Override
    public void finishCrop(Bitmap bitmap) {
        if (bitmap != null) {
            TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

            if (!recognizer.isOperational()) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            } else {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> items = recognizer.detect(frame);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < items.size(); i++) {
                    TextBlock myItem = items.valueAt(i);
                    sb.append(myItem.getValue());
                    sb.append("\n");
                }
                someActivityResultLauncher.launch(new Intent(MainActivity.this, TranslateActivity.class).putExtra("mic", sb.substring(0, sb.toString().length() - 1)).putExtra("pos", "1"));
                return;
            }
            Toast.makeText(this, "No text found", Toast.LENGTH_SHORT).show();
        }
        Log.e("sfdsfdfdfd", "finishCrop: " + bitmap);
    }
}