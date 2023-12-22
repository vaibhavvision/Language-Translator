package com.translatealll.anguagesapp.adapter;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.database.RoomDB;
import com.translatealll.anguagesapp.database.WordsHistoryTable;

import java.util.ArrayList;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Historyviewholder> {
    private final ArrayList<String> chatlist;
    private final ArrayList<WordsHistoryTable> wordslist;
    Chatclicklistener chatclicklistener;
    String condition;
    Context context;
    RoomDB roomDB;
    TextView tv_nohistory;
    RecyclerView historyRecView;

    public HistoryAdapter(String condition, ArrayList<String> chatlist, ArrayList<WordsHistoryTable> wordslist, Context context, Chatclicklistener chatclicklistener, TextView tv_nohistory, RecyclerView historyRecView) {
        this.wordslist = wordslist;
        this.context = context;
        this.condition = condition;
        this.chatlist = chatlist;
        this.chatclicklistener = chatclicklistener;
        this.tv_nohistory = tv_nohistory;
        this.historyRecView = historyRecView;
    }

    @Override
    public Historyviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Historyviewholder(LayoutInflater.from(context).inflate(R.layout.history_singlerow, parent, false));
    }

    @Override
    public void onBindViewHolder(Historyviewholder holder, @SuppressLint("RecyclerView") int position) {

        if (this.condition.equals("savedchat")) {
            holder.tv_text2.setPadding(0, 10, 0, 0);
            holder.tv_text2.setTextSize(15.0f);
            holder.tv_text2.setText(String.format("%s", this.chatlist.get(position)));
            holder.tv_text1.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
            holder.tv_text2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chatclicklistener.OnChatclick(chatlist.get(position));
                }
            });


            holder.btn_more.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("MissingInflatedId")
                @Override
                public void onClick(View view) {

                    final Dialog dialog = new Dialog(context, R.style.WideDialog100);
                    dialog.setContentView(R.layout.popup_delete);

                    TextView itDeleteee = dialog.findViewById(R.id.itdeleteee);

                    itDeleteee.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final Dialog dialog1 = new Dialog(context, R.style.WideDialog200);
                            dialog1.setContentView(R.layout.layout_delete);
                            Button btnDelete = dialog1.findViewById(R.id.btnDelete);
                            Button btnCancel = dialog1.findViewById(R.id.btnCancel);
                            btnDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RoomDB roomDBInstance = RoomDB.getRoomDBInstance(context);
                                    roomDB = roomDBInstance;
                                    roomDBInstance.downloadedlngs_dao().delete(chatlist.get(position));
                                    ArrayList<String> arrayList = chatlist;
                                    arrayList.remove(arrayList.get(position));
                                    if(arrayList.size()==0)
                                    {
                                        tv_nohistory.setVisibility(View.VISIBLE);
                                        historyRecView.setVisibility(View.GONE);
                                    }
                                    notifyDataSetChanged();
                                    dialog1.dismiss();
                                }
                            });

                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog1.dismiss();
                                }
                            });
                            dialog1.show();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        } else if (condition.equals("history")) {
            holder.tv_text1.setText(String.format("%s", wordslist.get(position).getTexttotranslate()));
            holder.tv_text2.setText(String.format("%s", wordslist.get(position).getTranslatedtext()));
            holder.btn_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(context, R.style.WideDialog100);
                    dialog.setContentView(R.layout.popup_save);

                    TextView itDelete = dialog.findViewById(R.id.itdelete);
                    TextView itCopy = dialog.findViewById(R.id.itcopy);

                    itDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog1 = new Dialog(context, R.style.WideDialog200);
                            dialog1.setContentView(R.layout.layout_delete);
                            Button btnDelete = dialog1.findViewById(R.id.btnDelete);
                            Button btnCancel = dialog1.findViewById(R.id.btnCancel);
                            btnDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RoomDB roomDBInstance = RoomDB.getRoomDBInstance(context);
                                    roomDB = roomDBInstance;
                                    roomDBInstance.downloadedlngs_dao().deleteword(wordslist.get(position));
                                    tv_nohistory.setVisibility(View.VISIBLE);
                                    wordslist.remove(position);
                                    if(wordslist.size()==0)
                                    {
                                        tv_nohistory.setVisibility(View.VISIBLE);
                                        historyRecView.setVisibility(View.GONE);
                                    }
                                    notifyDataSetChanged();
                                    dialog1.dismiss();
                                }
                            });

                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog1.dismiss();
                                }
                            });
                            dialog1.show();
                            dialog.dismiss();
                        }
                    });

                    itCopy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle Copy action
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (condition.equals("savedchat")) {
            return chatlist.size();
        }
        return wordslist.size();
    }


    public interface Chatclicklistener {
        void OnChatclick(String chatname);
    }

    public static class Historyviewholder extends RecyclerView.ViewHolder {
        ImageView btn_more;
        TextView tv_text1;
        TextView tv_text2;

        View view;

        public Historyviewholder(View itemView) {
            super(itemView);
            tv_text1 = (TextView) itemView.findViewById(R.id.historytxt1);
            tv_text2 = (TextView) itemView.findViewById(R.id.historytxt2);
            btn_more = (ImageView) itemView.findViewById(R.id.btnmore);
            view = itemView.findViewById(R.id.view);
        }
    }
}
