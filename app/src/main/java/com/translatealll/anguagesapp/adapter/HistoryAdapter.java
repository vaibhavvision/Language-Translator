package com.translatealll.anguagesapp.adapter;


import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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


    public HistoryAdapter(String condition, ArrayList<String> chatlist, ArrayList<WordsHistoryTable> wordslist, Context context, Chatclicklistener chatclicklistener) {
        this.wordslist = wordslist;
        this.context = context;
        this.condition = condition;
        this.chatlist = chatlist;
        this.chatclicklistener = chatclicklistener;
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
                @Override
                public void onClick(View view) {

                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popup = layoutInflater.inflate(R.layout.popup_delete, null);

                    PopupWindow popupWindow = new PopupWindow(
                            popup,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.showAsDropDown(view);

                    popup.findViewById(R.id.itdeleteee).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(context).setTitle("Delete").setMessage("Delete Chat?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i2) {
                                    RoomDB roomDBInstance = RoomDB.getRoomDBInstance(context);
                                    roomDB = roomDBInstance;
                                    roomDBInstance.downloadedlngs_dao().delete(chatlist.get(position));
                                    ArrayList<String> arrayList = chatlist;
                                    arrayList.remove(arrayList.get(position));
                                    dialogInterface.dismiss();
                                    notifyDataSetChanged();
                                    popupWindow.dismiss();

                                }
                            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i2) {
                                    dialogInterface.dismiss();
                                    popupWindow.dismiss();
                                }
                            }).create().show();
                        }
                    });
                }
            });
        } else if (condition.equals("history")) {
            holder.tv_text1.setText(String.format("%s", wordslist.get(position).getTexttotranslate()));
            holder.tv_text2.setText(String.format("%s", wordslist.get(position).getTranslatedtext()));
            holder.btn_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View popup = layoutInflater.inflate(R.layout.popup_save, null);

                    PopupWindow popupWindow = new PopupWindow(
                            popup,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.showAsDropDown(view);
                    popup.findViewById(R.id.itdelete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(context).setTitle("Delete").setMessage("Delete word?").setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i2) {
                                    RoomDB roomDBInstance = RoomDB.getRoomDBInstance(context);
                                    roomDB = roomDBInstance;
                                    roomDBInstance.downloadedlngs_dao().deleteword(wordslist.get(position));
                                    wordslist.remove(position);
                                    notifyDataSetChanged();
                                    dialogInterface.dismiss();
                                    popupWindow.dismiss();

                                }
                            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i2) {
                                    dialogInterface.dismiss();
                                    popupWindow.dismiss();

                                }
                            }).create().show();
                        }
                    });

                    popup.findViewById(R.id.itcopy).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setText(holder.tv_text1.getText().toString() + "\n" + holder.tv_text2.getText().toString());
                            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                            popupWindow.dismiss();
                        }
                    });
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
