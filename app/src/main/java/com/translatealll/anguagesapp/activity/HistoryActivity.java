package com.translatealll.anguagesapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.adapter.HistoryAdapter;
import com.translatealll.anguagesapp.database.ChatTable;
import com.translatealll.anguagesapp.database.RoomDB;
import com.translatealll.anguagesapp.database.WordsHistoryTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class HistoryActivity extends AppCompatActivity {
    public static ArrayList<ChatTable> chatnameslist = new ArrayList<>();
    public static String selectedchatname;
    TextView btn_top_saved_chat;
    TextView btn_top_translator;

    RoomDB roomDB;
    HistoryAdapter historyAdapter;
    RecyclerView historyRecView;
    TextView tv_nohistory;
    ImageView back;
    ArrayList<WordsHistoryTable> historylist = new ArrayList<>();
    ArrayList<String> temp_chatnameslist = new ArrayList<>();

    HistoryAdapter.Chatclicklistener chatclicklistener = new HistoryAdapter.Chatclicklistener() {
        @Override
        public void OnChatclick(String str) {
            selectedchatname = str;
            startActivity(new Intent(HistoryActivity.this, ConvsersationActivity.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        roomDB = RoomDB.getRoomDBInstance(this);
        historyRecView = (RecyclerView) findViewById(R.id.historyrecview);
        tv_nohistory = (TextView) findViewById(R.id.tv_nohistory);
        btn_top_translator = (TextView) findViewById(R.id.btn_top_translator);
        btn_top_saved_chat = (TextView) findViewById(R.id.btn_top_saved_chat);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_top_saved_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_nohistory.setVisibility(View.GONE);
                btn_top_translator.setBackground(ContextCompat.getDrawable(HistoryActivity.this, R.drawable.et_border_translate_shape));
                btn_top_translator.setTextColor(ContextCompat.getColor(HistoryActivity.this, R.color.black));
                btn_top_saved_chat.setBackground(ContextCompat.getDrawable(HistoryActivity.this, R.drawable.bg_lang_1));
                btn_top_saved_chat.setTextColor(ContextCompat.getColor(HistoryActivity.this, R.color.white));
                if (temp_chatnameslist.size() > 0) {
                    historyRecView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                    ArrayList<String> arrayList = temp_chatnameslist;
                    ArrayList<WordsHistoryTable> arrayList2 = historylist;
                    historyAdapter = new HistoryAdapter("savedchat", arrayList, arrayList2, HistoryActivity.this, chatclicklistener, tv_nohistory, historyRecView);
                    historyRecView.setAdapter(historyAdapter);
                    historyRecView.setVisibility(View.VISIBLE);
                    return;
                }
                tv_nohistory.setVisibility(View.VISIBLE);
                historyRecView.setVisibility(View.GONE);
            }
        });

        btn_top_translator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_top_translator.setBackground(ContextCompat.getDrawable(HistoryActivity.this, R.drawable.bg_lang_1));
                btn_top_translator.setTextColor(ContextCompat.getColor(HistoryActivity.this, R.color.white));
                btn_top_saved_chat.setBackground(ContextCompat.getDrawable(HistoryActivity.this, R.drawable.et_border_translate_shape));
                btn_top_saved_chat.setTextColor(ContextCompat.getColor(HistoryActivity.this, R.color.black));
                if (historylist.size() == 0) {
                    historyRecView.setVisibility(View.GONE);
                    tv_nohistory.setVisibility(View.VISIBLE);
                } else {
                    historyRecView.setVisibility(View.VISIBLE);
                    tv_nohistory.setVisibility(View.GONE);
                }
                historyRecView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                HistoryActivity historyActivity = HistoryActivity.this;
                ArrayList<String> arrayList = temp_chatnameslist;
                ArrayList<WordsHistoryTable> arrayList2 = historylist;
                historyActivity.historyAdapter = new HistoryAdapter("history", arrayList, arrayList2, HistoryActivity.this, chatclicklistener,tv_nohistory, historyRecView);
                historyRecView.setAdapter(historyAdapter);
            }
        });

        historylist.clear();
        chatnameslist.clear();
        historylist.addAll(Arrays.asList(roomDB.downloadedlngs_dao().selectalllngs()));
        temp_chatnameslist.addAll(Arrays.asList(roomDB.downloadedlngs_dao().getSavedChats()));
        Log.e("TAG", "onCreate: " + temp_chatnameslist.size());
        Collections.reverse(historylist);
        Collections.reverse(chatnameslist);
        historyRecView.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new HistoryAdapter("history", temp_chatnameslist, historylist, this, chatclicklistener, tv_nohistory,historyRecView);
        historyRecView.setAdapter(historyAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_nohistory.setVisibility(View.GONE);
        btn_top_translator.setBackground(ContextCompat.getDrawable(HistoryActivity.this, R.drawable.et_border_translate_shape));
        btn_top_translator.setTextColor(ContextCompat.getColor(HistoryActivity.this, R.color.black));
        btn_top_saved_chat.setBackground(ContextCompat.getDrawable(HistoryActivity.this, R.drawable.bg_lang_1));
        btn_top_saved_chat.setTextColor(ContextCompat.getColor(HistoryActivity.this, R.color.white));
        if (temp_chatnameslist.size() > 0) {
            historyRecView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
            ArrayList<String> arrayList = temp_chatnameslist;
            ArrayList<WordsHistoryTable> arrayList2 = historylist;
            historyAdapter = new HistoryAdapter("savedchat", arrayList, arrayList2, HistoryActivity.this, chatclicklistener, tv_nohistory, historyRecView);
            historyRecView.setAdapter(historyAdapter);
            historyRecView.setVisibility(View.VISIBLE);
            return;
        }
        tv_nohistory.setVisibility(View.VISIBLE);
        historyRecView.setVisibility(View.GONE);
    }
}