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
import com.translatealll.anguagesapp.adapter.DownloadAdapter;
import com.translatealll.anguagesapp.database.RoomDB;
import com.translatealll.anguagesapp.inter.DialogFragmentClick;
import com.translatealll.anguagesapp.inter.DownloadClick;
import com.translatealll.anguagesapp.model.LnguageRepo;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;


public class BottomSheetFragment extends BottomSheetDialogFragment {
    public static KProgressHUD hud;
    public static int iconlanguage;
    public static String languagepack;
    DialogFragmentClick bottomSheetFragclicks;
    RecyclerView downldlng_recview;
    DownloadAdapter downlodlngs_adapter;
    RoomDB roomDB;
    SearchView searchView;
    TextView title_text;
    boolean islangdownloaded = false;
    ArrayList<LnguageRepo> langs_list = new ArrayList<>();
    DownloadClick langDownloadInterface = new DownloadClick() {
        @Override
        public void OnDownloadComplete(Boolean bool, String str, int i) {
            downloadLanguage(bool, str, i);
        }
    };


    public BottomSheetFragment(DialogFragmentClick bottomS) {
        bottomSheetFragclicks = bottomS;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog onCreateDialog = super.onCreateDialog(savedInstanceState);
        onCreateDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
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
                    return ((LnguageRepo) obj).getTitle();
                }
            }));
        } else {
            Collections.sort(langs_list, new Comparator<LnguageRepo>() {
                @Override
                public int compare(LnguageRepo o1, LnguageRepo o2) {
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
        DownloadAdapter downlodlngs_Adapter = new DownloadAdapter(requireContext(), langs_list, langDownloadInterface, string, string2);
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

    public void downloadLanguage(Boolean bool, String str, int i) {
        islangdownloaded = bool.booleanValue();
        languagepack = str;
        iconlanguage = i;
        if (bool.booleanValue() && languagepack != null) {
            String str2 = MainActivity.lang_no;
        }
        try {
            Context requireContext = requireContext();
//            Toast.makeText(requireContext, BottomSheetFragment.languagepack + " downloaded", Toast.LENGTH_SHORT).show();
            dismiss();
            bottomSheetFragclicks.onDownloadComplete(true);
//            Toast.makeText(requireContext, languagepack + " downloaded", Toast.LENGTH_SHORT).show();
            dismiss();
            bottomSheetFragclicks.onDownloadComplete(true);
//            Toast.makeText(requireContext, languagepack + " downloaded", Toast.LENGTH_SHORT).show();
            dismiss();
            bottomSheetFragclicks.onDownloadComplete(true);
        } catch (Exception unused) {
            Log.d("TAG", ": ");
        }
    }

    private ArrayList<LnguageRepo> Setting_langlist() {
        ArrayList<LnguageRepo> arrayList = new ArrayList<>();
        arrayList.add(new LnguageRepo("ENGLISH", R.drawable.flg_english));
        arrayList.add(new LnguageRepo("ARABIC", R.drawable.flg_arabic));
        arrayList.add(new LnguageRepo("CHINESE", R.drawable.flg_china));
        arrayList.add(new LnguageRepo("GERMAN", R.drawable.flg_germany));
        arrayList.add(new LnguageRepo("AFRIKAANS", R.drawable.flg_afrikans));
        arrayList.add(new LnguageRepo("ALBANIAN", R.drawable.flg_albania));
        arrayList.add(new LnguageRepo("BELARUSIAN", R.drawable.flg_belarus));
        arrayList.add(new LnguageRepo("BENGALI", R.drawable.flg_bangali));
        arrayList.add(new LnguageRepo("BULGARIAN", R.drawable.flg_balgeria));
        arrayList.add(new LnguageRepo("GALICIAN", R.drawable.flg_glacian));
        arrayList.add(new LnguageRepo("FINNISH", R.drawable.flg_finnish));
        arrayList.add(new LnguageRepo("FRENCH", R.drawable.flg_franch));
        arrayList.add(new LnguageRepo("ESTONIAN", R.drawable.flg_estonia));
        arrayList.add(new LnguageRepo("ESPERANTO", R.drawable.flg_esperanto));
        arrayList.add(new LnguageRepo("DUTCH", R.drawable.flg_dutch));
        arrayList.add(new LnguageRepo("DANISH", R.drawable.flg_danish));
        arrayList.add(new LnguageRepo("CZECH", R.drawable.flg_czech));
        arrayList.add(new LnguageRepo("CROATIAN", R.drawable.flg_croatian));
        arrayList.add(new LnguageRepo("CATALAN", R.drawable.flg_catalan));
        arrayList.add(new LnguageRepo("MALTESE", R.drawable.flg_maltese));
        arrayList.add(new LnguageRepo("URDU", R.drawable.flg_urdu));
        arrayList.add(new LnguageRepo("MALAY", R.drawable.flg_malay));
        arrayList.add(new LnguageRepo("MACEDONIAN", R.drawable.flg_macedoian));
        arrayList.add(new LnguageRepo("LITHUANIAN", R.drawable.flg_lithuania));
        arrayList.add(new LnguageRepo("LATVIAN", R.drawable.flg_latvia));
        arrayList.add(new LnguageRepo("KOREAN", R.drawable.flg_korean));
        arrayList.add(new LnguageRepo("KANNADA", R.drawable.flg_kannada));
        arrayList.add(new LnguageRepo("JAPANESE", R.drawable.flg_japani));
        arrayList.add(new LnguageRepo("ITALIAN", R.drawable.flg_itlay));
        arrayList.add(new LnguageRepo("IRISH", R.drawable.flg_irish));
        arrayList.add(new LnguageRepo("INDONESIAN", R.drawable.flg_indonasian));
        arrayList.add(new LnguageRepo("ICELANDIC", R.drawable.flg_icelandic));
        arrayList.add(new LnguageRepo("HUNGARIAN", R.drawable.flg_hungarian));
        arrayList.add(new LnguageRepo("HINDI", R.drawable.flg_hindi));
        arrayList.add(new LnguageRepo("HEBREW", R.drawable.flag_hebrew));
        arrayList.add(new LnguageRepo("TURKISH", R.drawable.flg_turkey));
        arrayList.add(new LnguageRepo("SWAHILI", R.drawable.flag_swahali));
        arrayList.add(new LnguageRepo("SPANISH", R.drawable.flg_spanish));
        arrayList.add(new LnguageRepo("SLOVENIAN", R.drawable.flg_slovenian));
        arrayList.add(new LnguageRepo("UKRAINIAN", R.drawable.flg_ukranian));
        arrayList.add(new LnguageRepo("VIETNAMESE", R.drawable.flg_vietnamen));
        arrayList.add(new LnguageRepo("WELSH", R.drawable.flg_walish));
        arrayList.add(new LnguageRepo("GEORGIAN", R.drawable.flg_feorgian));
        arrayList.add(new LnguageRepo("GREEK", R.drawable.flg_greek));
        arrayList.add(new LnguageRepo("GUJARATI", R.drawable.flg_hindi));
        arrayList.add(new LnguageRepo("HAITIAN_CREOLE", R.drawable.flg_haitian));
        arrayList.add(new LnguageRepo("SWEDISH", R.drawable.flg_swedish));
        arrayList.add(new LnguageRepo("TAGALOG", R.drawable.flg_tagalog));
        arrayList.add(new LnguageRepo("TAMIL", R.drawable.flg_hindi));
        arrayList.add(new LnguageRepo("TELUGU", R.drawable.flg_hindi));
        arrayList.add(new LnguageRepo("THAI", R.drawable.flg_thai));
        arrayList.add(new LnguageRepo("NORWEGIAN", R.drawable.flg_norwedian));
        arrayList.add(new LnguageRepo("PERSIAN", R.drawable.flg_persian));
        arrayList.add(new LnguageRepo("POLISH", R.drawable.flg_polish));
        arrayList.add(new LnguageRepo("PORTUGUESE", R.drawable.flg_portugas));
        arrayList.add(new LnguageRepo("ROMANIAN", R.drawable.flg_romanian));
        arrayList.add(new LnguageRepo("RUSSIAN", R.drawable.flg_russian));
        arrayList.add(new LnguageRepo("SLOVAK", R.drawable.flg_slovenian));
        arrayList.add(new LnguageRepo("MARATHI", R.drawable.flg_hindi));
        return arrayList;
    }

    public void filter(String text) {
        ArrayList<LnguageRepo> arrayList = new ArrayList<>();
        Iterator<LnguageRepo> it = langs_list.iterator();
        while (it.hasNext()) {
            LnguageRepo next = it.next();
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
}
