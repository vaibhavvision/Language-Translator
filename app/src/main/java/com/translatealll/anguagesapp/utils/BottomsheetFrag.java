package com.translatealll.anguagesapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.activity.MainActivity;
import com.translatealll.anguagesapp.adapter.DownlodlngsAdapter;
import com.translatealll.anguagesapp.database.RoomDB;
import com.translatealll.anguagesapp.inter.BottomSheetFragclicks;
import com.translatealll.anguagesapp.inter.LangDownloadInterface;
import com.translatealll.anguagesapp.model.LngsModelClass;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;


public class BottomsheetFrag extends BottomSheetDialogFragment {
    public static KProgressHUD hud;
    public static int iconlanguage;
    public static String languagepack;
    BottomSheetFragclicks bottomSheetFragclicks;
    RecyclerView downldlng_recview;
    DownlodlngsAdapter downlodlngs_adapter;
    RoomDB roomDB;
    SearchView searchView;
    TextView title_text;
    boolean islangdownloaded = false;
    ArrayList<LngsModelClass> langs_list = new ArrayList<>();
    LangDownloadInterface langDownloadInterface = new LangDownloadInterface() {
        @Override
        public void OnDownloadComplete(Boolean bool, String str, int i) {
            lambda$new$0(bool, str, i);
        }
    };


    public BottomsheetFrag(BottomSheetFragclicks bottomS) {
        bottomSheetFragclicks = bottomS;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog onCreateDialog = super.onCreateDialog(savedInstanceState);
        onCreateDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
//                setupRatio((BottomSheetDialog) dialogInterface);
            }
        });
        return onCreateDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.frag_bottomsheet, container, false);
        title_text = (TextView) inflate.findViewById(R.id.title_text);
        Bundle arguments = getArguments();
        String string = arguments.getString("langno", "");
        String string2 = arguments.getString("from", "");
        String string3 = arguments.getString("lan", "");
        String string4 = arguments.getString("belong", "");
        Log.d("TAG", "langno");
        Log.d("from", string2);
        Log.d("belong", "acha kia ");
        roomDB = RoomDB.getRoomDBInstance(requireContext());
        downldlng_recview = (RecyclerView) inflate.findViewById(R.id.downldlng_recview);
        searchView = (SearchView) inflate.findViewById(R.id.rel_search);
        downldlng_recview.setNestedScrollingEnabled(true);
        langs_list.addAll(Setting_langlist());
        if (Build.VERSION.SDK_INT >= 24) {
            Collections.sort(langs_list, Comparator.comparing(new Function() {
                @Override
                public Object apply(Object obj) {
                    return ((LngsModelClass) obj).getTitle();
                }
            }));
        } else {
            Collections.sort(langs_list, new Comparator<LngsModelClass>() {
                @Override
                public int compare(LngsModelClass o1, LngsModelClass o2) {
                    return o1.getTitle().compareTo(o2.getTitle());
                }
            });
        }
        roomDB = RoomDB.getRoomDBInstance(requireContext());
        MainActivity.temp_downloadedlngs_list.clear();
        MainActivity.downloadedlngs_list.clear();
        MainActivity.downloadedlngs_list = roomDB.downloadedlngs_dao().SelectDownloadedLngs();
        for (int i = 0; i < MainActivity.downloadedlngs_list.size(); i++) {
            MainActivity.temp_downloadedlngs_list.add(MainActivity.downloadedlngs_list.get(i).getDownloadedlng_name());
        }
        hud = KProgressHUD.create(requireContext()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait").setCancellable(false).setDimAmount(0.5f);
        DownlodlngsAdapter downlodlngs_Adapter = new DownlodlngsAdapter(requireContext(), langs_list, langDownloadInterface, string, string2);
        downlodlngs_adapter = downlodlngs_Adapter;
        downldlng_recview.setAdapter(downlodlngs_Adapter);
        downldlng_recview.setLayoutManager(new LinearLayoutManager(requireContext()));
        if (string4.equals("lan1")) {
            title_text.setVisibility(View.VISIBLE);
            filter(string3);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });
        return inflate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }

    public void lambda$new$0(Boolean bool, String str, int i) {
        islangdownloaded = bool.booleanValue();
        languagepack = str;
        iconlanguage = i;
        if (bool.booleanValue() && languagepack != null) {
            String str2 = MainActivity.lang_no;
        }
        try {
            Context requireContext = requireContext();
            Toast.makeText(requireContext, BottomsheetFrag.languagepack + " downloaded", Toast.LENGTH_SHORT).show();
            dismiss();
            bottomSheetFragclicks.onDownloadComplete(true);
            Toast.makeText(requireContext, languagepack + " downloaded", Toast.LENGTH_SHORT).show();
            dismiss();
            bottomSheetFragclicks.onDownloadComplete(true);
            Toast.makeText(requireContext, languagepack + " downloaded", Toast.LENGTH_SHORT).show();
            dismiss();
            bottomSheetFragclicks.onDownloadComplete(true);
        } catch (Exception unused) {
            Log.d("TAG", ": ");
        }
    }

    private ArrayList<LngsModelClass> Setting_langlist() {
        ArrayList<LngsModelClass> arrayList = new ArrayList<>();
        arrayList.add(new LngsModelClass("ENGLISH", R.drawable.flg_english));
        arrayList.add(new LngsModelClass("ARABIC", R.drawable.flg_arabic));
        arrayList.add(new LngsModelClass("CHINESE", R.drawable.flg_china));
        arrayList.add(new LngsModelClass("GERMAN", R.drawable.flg_germany));
        arrayList.add(new LngsModelClass("AFRIKAANS", R.drawable.flg_afrikans));
        arrayList.add(new LngsModelClass("ALBANIAN", R.drawable.flg_albania));
        arrayList.add(new LngsModelClass("BELARUSIAN", R.drawable.flg_belarus));
        arrayList.add(new LngsModelClass("BENGALI", R.drawable.flg_bangali));
        arrayList.add(new LngsModelClass("BULGARIAN", R.drawable.flg_balgeria));
        arrayList.add(new LngsModelClass("GALICIAN", R.drawable.flg_glacian));
        arrayList.add(new LngsModelClass("FINNISH", R.drawable.flg_finnish));
        arrayList.add(new LngsModelClass("FRENCH", R.drawable.flg_franch));
        arrayList.add(new LngsModelClass("ESTONIAN", R.drawable.flg_estonia));
        arrayList.add(new LngsModelClass("ESPERANTO", R.drawable.flg_esperanto));
        arrayList.add(new LngsModelClass("DUTCH", R.drawable.flg_dutch));
        arrayList.add(new LngsModelClass("DANISH", R.drawable.flg_danish));
        arrayList.add(new LngsModelClass("CZECH", R.drawable.flg_czech));
        arrayList.add(new LngsModelClass("CROATIAN", R.drawable.flg_croatian));
        arrayList.add(new LngsModelClass("CATALAN", R.drawable.flg_catalan));
        arrayList.add(new LngsModelClass("MALTESE", R.drawable.flg_maltese));
        arrayList.add(new LngsModelClass("URDU", R.drawable.flg_urdu));
        arrayList.add(new LngsModelClass("MALAY", R.drawable.flg_malay));
        arrayList.add(new LngsModelClass("MACEDONIAN", R.drawable.flg_macedoian));
        arrayList.add(new LngsModelClass("LITHUANIAN", R.drawable.flg_lithuania));
        arrayList.add(new LngsModelClass("LATVIAN", R.drawable.flg_latvia));
        arrayList.add(new LngsModelClass("KOREAN", R.drawable.flg_korean));
        arrayList.add(new LngsModelClass("KANNADA", R.drawable.flg_kannada));
        arrayList.add(new LngsModelClass("JAPANESE", R.drawable.flg_japani));
        arrayList.add(new LngsModelClass("ITALIAN", R.drawable.flg_itlay));
        arrayList.add(new LngsModelClass("IRISH", R.drawable.flg_irish));
        arrayList.add(new LngsModelClass("INDONESIAN", R.drawable.flg_indonasian));
        arrayList.add(new LngsModelClass("ICELANDIC", R.drawable.flg_icelandic));
        arrayList.add(new LngsModelClass("HUNGARIAN", R.drawable.flg_hungarian));
        arrayList.add(new LngsModelClass("HINDI", R.drawable.flg_hindi));
        arrayList.add(new LngsModelClass("HEBREW", R.drawable.flag_hebrew));
        arrayList.add(new LngsModelClass("TURKISH", R.drawable.flg_turkey));
        arrayList.add(new LngsModelClass("SWAHILI", R.drawable.flag_swahali));
        arrayList.add(new LngsModelClass("SPANISH", R.drawable.flg_spanish));
        arrayList.add(new LngsModelClass("SLOVENIAN", R.drawable.flg_slovenian));
        arrayList.add(new LngsModelClass("UKRAINIAN", R.drawable.flg_ukranian));
        arrayList.add(new LngsModelClass("VIETNAMESE", R.drawable.flg_vietnamen));
        arrayList.add(new LngsModelClass("WELSH", R.drawable.flg_walish));
        arrayList.add(new LngsModelClass("GEORGIAN", R.drawable.flg_feorgian));
        arrayList.add(new LngsModelClass("GREEK", R.drawable.flg_greek));
        arrayList.add(new LngsModelClass("GUJARATI", R.drawable.flg_hindi));
        arrayList.add(new LngsModelClass("HAITIAN_CREOLE", R.drawable.flg_haitian));
        arrayList.add(new LngsModelClass("SWEDISH", R.drawable.flg_swedish));
        arrayList.add(new LngsModelClass("TAGALOG", R.drawable.flg_tagalog));
        arrayList.add(new LngsModelClass("TAMIL", R.drawable.flg_hindi));
        arrayList.add(new LngsModelClass("TELUGU", R.drawable.flg_hindi));
        arrayList.add(new LngsModelClass("THAI", R.drawable.flg_thai));
        arrayList.add(new LngsModelClass("NORWEGIAN", R.drawable.flg_norwedian));
        arrayList.add(new LngsModelClass("PERSIAN", R.drawable.flg_persian));
        arrayList.add(new LngsModelClass("POLISH", R.drawable.flg_polish));
        arrayList.add(new LngsModelClass("PORTUGUESE", R.drawable.flg_portugas));
        arrayList.add(new LngsModelClass("ROMANIAN", R.drawable.flg_romanian));
        arrayList.add(new LngsModelClass("RUSSIAN", R.drawable.flg_russian));
        arrayList.add(new LngsModelClass("SLOVAK", R.drawable.flg_slovenian));
        arrayList.add(new LngsModelClass("MARATHI", R.drawable.flg_hindi));
        return arrayList;
    }


//    public void setupRatio(BottomSheetDialog bottomSheetDialog) {
//        FrameLayout frameLayout = (FrameLayout) bottomSheetDialog.findViewById(R.id.);
//        BottomSheetBehavior from = BottomSheetBehavior.from(frameLayout);
//        ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
//        layoutParams.height = getBottomSheetDialogDefaultHeight();
//        frameLayout.setLayoutParams(layoutParams);
//        from.setState(BottomSheetBehavior.STATE_EXPANDED);
//    }


    public void filter(String text) {
        ArrayList<LngsModelClass> arrayList = new ArrayList<>();
        Iterator<LngsModelClass> it = langs_list.iterator();
        while (it.hasNext()) {
            LngsModelClass next = it.next();
            if (next.getTitle().toLowerCase().contains(text.toLowerCase())) {
                arrayList.add(next);
            }
        }
        if (arrayList.isEmpty()) {
            Toast.makeText(requireContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            downlodlngs_adapter.searchInfoList(arrayList);
        }
    }

    private int getBottomSheetDialogDefaultHeight() {
        return (getWindowHeight() * 78) / 100;
    }

    private int getWindowHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}
