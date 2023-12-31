package com.translatealll.anguagesapp.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.activity.ChatActivity;
import com.translatealll.anguagesapp.activity.MainActivity;
import com.translatealll.anguagesapp.activity.TranslateNewActivity;
import com.translatealll.anguagesapp.database.DownloadedLngsTable;
import com.translatealll.anguagesapp.database.RoomDB;
import com.translatealll.anguagesapp.inter.LangDownloadInterface;
import com.translatealll.anguagesapp.model.LngsModelClass;
import com.translatealll.anguagesapp.utils.BottomsheetFrag;
import com.translatealll.anguagesapp.utils.Constant;
import com.translatealll.anguagesapp.utils.PrefFile;
import com.translatealll.anguagesapp.utils.TranslateLanguage;

import java.util.ArrayList;
import java.util.List;

import kotlin.text.Typography;


public class DownlodlngsAdapter extends RecyclerView.Adapter<DownlodlngsAdapter.AdapterViewHolder> {
    NetworkInfo active_network;

    ConnectivityManager f518cm;
    Context context;
    String from;
    LangDownloadInterface langDownloadInterface;
    String lngno;
    List<LngsModelClass> lngs_list;
    RemoteModelManager modelManager;
    RoomDB roomDB;
    DownloadedLngsTable downloadedlngs_list = new DownloadedLngsTable();
    String language = null;
    Boolean isdownloaded = false;
    boolean isdownloading = false;

    public DownlodlngsAdapter(Context context, List<LngsModelClass> downloadedlngs_list, LangDownloadInterface langDownloadInterface, String lngno, String from) {
        this.context = context;
        lngs_list = downloadedlngs_list;
        this.langDownloadInterface = langDownloadInterface;
        this.lngno = lngno;
        this.from = from;
        Log.d("LangNo", lngno);
        Log.d("LangNo", from);
    }

    public void searchInfoList(ArrayList<LngsModelClass> filteredlist) {
        lngs_list = filteredlist;
        notifyDataSetChanged();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.downldlngs_singlerow, parent, false));
    }

    @Override
    public void onBindViewHolder(final AdapterViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tv_langname.setText(lngs_list.get(position).getTitle());
        holder.flag.setImageResource(lngs_list.get(position).getImage());
        if (lngno.equals("1") && lngs_list.get(position).getTitle().equals(MainActivity.lng1name)) {
            holder.singlerowlayout.setBackgroundResource(R.drawable.downloadedlngcard_bg);
        } else if (lngno.equals("2") && lngs_list.get(position).getTitle().equals(MainActivity.lng2name)) {
            holder.singlerowlayout.setBackgroundResource(R.drawable.downloadedlngcard_bg);
        } else {
            holder.singlerowlayout.setBackgroundResource(R.drawable.undownloadedlngcard_bg);
        }
        if (MainActivity.temp_downloadedlngs_list.contains(lngs_list.get(position).getTitle())) {
            holder.imgbtn_downloadlng.setImageResource(R.drawable.ic_path_349);
        } else {
            holder.imgbtn_downloadlng.setImageResource(R.drawable.ic_download__2_);
        }
        holder.singlerowlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBindViewHolder(position, holder, view);
            }
        });
    }


    public void onBindViewHolder(int i, AdapterViewHolder adapterViewHolder, View view) {
        if (isdownloading) {
            return;
        }
        if (MainActivity.temp_downloadedlngs_list.contains(lngs_list.get(i).getTitle())) {
            if (lngno.equals("1")) {
                MainActivity.lng1name = lngs_list.get(i).getTitle();
                MainActivity.iconlang1 = lngs_list.get(i).getImage();
            } else if (lngno.equals("2")) {
                MainActivity.lng2name = lngs_list.get(i).getTitle();
                MainActivity.iconlang2 = lngs_list.get(i).getImage();
            }
            if (lngno.equals("1")) {
                isdownloaded = false;
                langDownloadInterface.OnDownloadComplete(false, adapterViewHolder.tv_langname.getText().toString(), MainActivity.iconlang1);
            } else {
                isdownloaded = false;
                langDownloadInterface.OnDownloadComplete(false, adapterViewHolder.tv_langname.getText().toString(), MainActivity.iconlang2);
            }

            if (PrefFile.getInstance().getString(Constant.LEFTRIGHT).equals("main")) {
                MainActivity.setData(context);
            } else {
                if (PrefFile.getInstance().getString(Constant.LEFTRIGHT).equals("chat")) {
                    ChatActivity.setData(context);
                } else {
                    TranslateNewActivity.setData(context);
                }
            }

            notifyDataSetChanged();
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        f518cm = connectivityManager;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        active_network = activeNetworkInfo;
        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() == 1 || active_network.getType() == 0) {
                adapterViewHolder.imgbtn_downloadlng.setVisibility(View.GONE);
                adapterViewHolder.progressBar.setVisibility(View.VISIBLE);
                language = ChooseLng(i);

                isdownloading = DownloadLanguage(language, adapterViewHolder, i).booleanValue();
                return;
            }
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
            adapterViewHolder.imgbtn_downloadlng.setVisibility(View.VISIBLE);
            adapterViewHolder.progressBar.setVisibility(View.GONE);
            return;
        }
        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        adapterViewHolder.imgbtn_downloadlng.setVisibility(View.VISIBLE);
        adapterViewHolder.progressBar.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return lngs_list.size();
    }

    public String ChooseLng(int position) {
        String title = lngs_list.get(position).getTitle();
        title.hashCode();
        char c = 65535;
        switch (title.hashCode()) {
            case -2072311548:
                if (title.equals("KOREAN")) {
                    c = 0;
                    break;
                }
                break;
            case -2021434509:
                if (title.equals("RUSSIAN")) {
                    c = 1;
                    break;
                }
                break;
            case -1998693422:
                if (title.equals("ALBANIAN")) {
                    c = 2;
                    break;
                }
                break;
            case -1929340143:
                if (title.equals("POLISH")) {
                    c = 3;
                    break;
                }
                break;
            case -1846121942:
                if (title.equals("SLOVAK")) {
                    c = 4;
                    break;
                }
                break;
            case -1824047576:
                if (title.equals("TELUGU")) {
                    c = 5;
                    break;
                }
                break;
            case -1661654192:
                if (title.equals("GEORGIAN")) {
                    c = 6;
                    break;
                }
                break;
            case -1496931977:
                if (title.equals("ESPERANTO")) {
                    c = 7;
                    break;
                }
                break;
            case -1464494112:
                if (title.equals("ITALIAN")) {
                    c = '\b';
                    break;
                }
                break;
            case -1405627549:
                if (title.equals("CROATIAN")) {
                    c = '\t';
                    break;
                }
                break;
            case -1293848364:
                if (title.equals("SPANISH")) {
                    c = '\n';
                    break;
                }
                break;
            case -1171574191:
                if (title.equals("ESTONIAN")) {
                    c = 11;
                    break;
                }
                break;
            case -1093623269:
                if (title.equals("SWAHILI")) {
                    c = '\f';
                    break;
                }
                break;
            case -1090048133:
                if (title.equals("SWEDISH")) {
                    c = '\r';
                    break;
                }
                break;
            case -1011019926:
                if (title.equals("GALICIAN")) {
                    c = 14;
                    break;
                }
                break;
            case -981927346:
                if (title.equals("NORWEGIAN")) {
                    c = 15;
                    break;
                }
                break;
            case -948864834:
                if (title.equals("BELARUSIAN")) {
                    c = 16;
                    break;
                }
                break;
            case -885774768:
                if (title.equals("ENGLISH")) {
                    c = 17;
                    break;
                }
                break;
            case -871655265:
                if (title.equals("HUNGARIAN")) {
                    c = 18;
                    break;
                }
                break;
            case -830625347:
                if (title.equals("TAGALOG")) {
                    c = 19;
                    break;
                }
                break;
            case -758693139:
                if (title.equals("SLOVENIAN")) {
                    c = 20;
                    break;
                }
                break;
            case -505022199:
                if (title.equals("GUJARATI")) {
                    c = 21;
                    break;
                }
                break;
            case -391870441:
                if (title.equals("BULGARIAN")) {
                    c = 22;
                    break;
                }
                break;
            case -247588444:
                if (title.equals("TURKISH")) {
                    c = 23;
                    break;
                }
                break;
            case -221382872:
                if (title.equals("KANNADA")) {
                    c = 24;
                    break;
                }
                break;
            case -134892613:
                if (title.equals("FINNISH")) {
                    c = 25;
                    break;
                }
                break;
            case 2573724:
                if (title.equals("THAI")) {
                    c = 26;
                    break;
                }
                break;
            case 2613230:
                if (title.equals("URDU")) {
                    c = 27;
                    break;
                }
                break;
            case 29896625:
                if (title.equals("JAPANESE")) {
                    c = 28;
                    break;
                }
                break;
            case 39535488:
                if (title.equals("PERSIAN")) {
                    c = 29;
                    break;
                }
                break;
            case 64625555:
                if (title.equals("CZECH")) {
                    c = 30;
                    break;
                }
                break;
            case 65414536:
                if (title.equals("DUTCH")) {
                    c = 31;
                    break;
                }
                break;
            case 68081376:
                if (title.equals("GREEK")) {
                    c = ' ';
                    break;
                }
                break;
            case 68745394:
                if (title.equals("HINDI")) {
                    c = '!';
                    break;
                }
                break;
            case 69932693:
                if (title.equals("IRISH")) {
                    c = '\"';
                    break;
                }
                break;
            case 73122672:
                if (title.equals("MALAY")) {
                    c = '#';
                    break;
                }
                break;
            case 79588515:
                if (title.equals("TAMIL")) {
                    c = Typography.dollar;
                    break;
                }
                break;
            case 82477587:
                if (title.equals("WELSH")) {
                    c = '%';
                    break;
                }
                break;
            case 167462569:
                if (title.equals("MACEDONIAN")) {
                    c = Typography.amp;
                    break;
                }
                break;
            case 243547852:
                if (title.equals("UKRAINIAN")) {
                    c = '\'';
                    break;
                }
                break;
            case 495326914:
                if (title.equals("BENGALI")) {
                    c = '(';
                    break;
                }
                break;
            case 541742905:
                if (title.equals("ROMANIAN")) {
                    c = ')';
                    break;
                }
                break;
            case 671907871:
                if (title.equals("LATVIAN")) {
                    c = '*';
                    break;
                }
                break;
            case 799935903:
                if (title.equals("HAITIAN_CREOLE")) {
                    c = '+';
                    break;
                }
                break;
            case 1010710335:
                if (title.equals("VIETNAMESE")) {
                    c = ',';
                    break;
                }
                break;
            case 1055466096:
                if (title.equals("ICELANDIC")) {
                    c = '-';
                    break;
                }
                break;
            case 1236562858:
                if (title.equals("INDONESIAN")) {
                    c = '.';
                    break;
                }
                break;
            case 1273686606:
                if (title.equals("CATALAN")) {
                    c = '/';
                    break;
                }
                break;
            case 1322880565:
                if (title.equals("PORTUGUESE")) {
                    c = '0';
                    break;
                }
                break;
            case 1464313037:
                if (title.equals("CHINESE")) {
                    c = '1';
                    break;
                }
                break;
            case 1488524197:
                if (title.equals("LITHUANIAN")) {
                    c = '2';
                    break;
                }
                break;
            case 1551960507:
                if (title.equals("MALTESE")) {
                    c = '3';
                    break;
                }
                break;
            case 1556949682:
                if (title.equals("MARATHI")) {
                    c = '4';
                    break;
                }
                break;
            case 1938625708:
                if (title.equals("ARABIC")) {
                    c = '5';
                    break;
                }
                break;
            case 2009207629:
                if (title.equals("DANISH")) {
                    c = '6';
                    break;
                }
                break;
            case 2081901978:
                if (title.equals("FRENCH")) {
                    c = '7';
                    break;
                }
                break;
            case 2098911622:
                if (title.equals("GERMAN")) {
                    c = '8';
                    break;
                }
                break;
            case 2127069055:
                if (title.equals("HEBREW")) {
                    c = '9';
                    break;
                }
                break;
            case 2139267348:
                if (title.equals("AFRIKAANS")) {
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

    private Boolean DownloadLanguage(String Language, final AdapterViewHolder holder, final int position) {
        KProgressHUD kProgressHUD = BottomsheetFrag.hud;
        kProgressHUD.setDetailsLabel("Downloading " + lngs_list.get(position).getTitle()).show();
        modelManager = RemoteModelManager.getInstance();
        modelManager.download(new TranslateRemoteModel.Builder(Language).build(), new DownloadConditions.Builder().build()).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object obj) {
                holder.progressBar.setVisibility(View.GONE);
                holder.imgbtn_downloadlng.setVisibility(View.VISIBLE);
                holder.imgbtn_downloadlng.setImageResource(R.drawable.ic_path_349);
                holder.singlerowlayout.setBackgroundResource(R.drawable.downloadedlngcard_bg);
                BottomsheetFrag.hud.dismiss();
                roomDB = RoomDB.getRoomDBInstance(context);
                downloadedlngs_list.setDownloadedlng_name(lngs_list.get(position).getTitle());
                roomDB.downloadedlngs_dao().InsertDownloaded_lngs(downloadedlngs_list);
                MainActivity.temp_downloadedlngs_list.add(lngs_list.get(position).getTitle());
                if (lngno.equals("1")) {
                    MainActivity.lng1name = lngs_list.get(position).getTitle();
                    MainActivity.iconlang1 = lngs_list.get(position).getImage();
                } else if (lngno.equals("2")) {
                    MainActivity.lng2name = lngs_list.get(position).getTitle();
                    MainActivity.iconlang2 = lngs_list.get(position).getImage();
                }
                if (lngno.equals("1")) {
                    isdownloaded = false;
                    langDownloadInterface.OnDownloadComplete(false, lngs_list.get(position).getTitle(), MainActivity.iconlang1);
                } else {
                    isdownloaded = true;
                    langDownloadInterface.OnDownloadComplete(true, lngs_list.get(position).getTitle(), MainActivity.iconlang2);
                }

                if (PrefFile.getInstance().getString(Constant.LEFTRIGHT).equals("main")) {
                    MainActivity.setData(context);
                } else if (PrefFile.getInstance().getString(Constant.LEFTRIGHT).equals("chat")) {
                    ChatActivity.setData(context);
                } else {
                    TranslateNewActivity.setData(context);
                }


                notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exc) {
                BottomsheetFrag.hud.dismiss();
                isdownloaded = false;
                langDownloadInterface.OnDownloadComplete(false, lngs_list.get(position).getTitle(), 0);
            }
        });
        return true;
    }

    public static class AdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView flag;
        ImageView imgbtn_downloadlng;
        ProgressBar progressBar;
        ConstraintLayout singlerowlayout;
        TextView tv_langname;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            singlerowlayout = (ConstraintLayout) itemView.findViewById(R.id.singlerowlayout);
            tv_langname = (TextView) itemView.findViewById(R.id.tv_lngname);
            imgbtn_downloadlng = (ImageView) itemView.findViewById(R.id.imgbtn_lngdownload);
            progressBar = (ProgressBar) itemView.findViewById(R.id.imgbar_lngdownloaded);
            flag = (ImageView) itemView.findViewById(R.id.flag_image);
        }
    }


}
