package com.translatealll.anguagesapp.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.database.ChatTable;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final ArrayList<ChatTable> chat_list;
    Context context;
    TextToSpeech txttospeech;

    public ChatAdapter(Context context, ArrayList<ChatTable> chat_list) {
        this.context = context;
        this.chat_list = chat_list;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_singlerow, parent, false));
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, @SuppressLint("RecyclerView") int position) {


        if (chat_list.get(position).getUser().equals("user1")) {
            String translatedText = chat_list.get(position).getTranslatedtext();
            holder.tvSend1.setText(chat_list.get(position).getTexttotranslate());
            holder.tvSend2.setText(chat_list.get(position).getTranslatedtext());
            holder.llReceive.setVisibility(View.GONE);
            txttospeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        holder.ivSpeck1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                speakText(translatedText);
                            }
                        });
                    } else {
                        Log.e("TextToSpeech", "Initialization failed");
                    }
                }
            });
        }
        if (chat_list.get(position).getUser().equals("user2")) {
            String translatedText1 = chat_list.get(position).getTranslatedtext();
            holder.tvReceive1.setText(chat_list.get(position).getTexttotranslate());
            holder.tvReceive2.setText(chat_list.get(position).getTranslatedtext());
            holder.llSend.setVisibility(View.GONE);
            txttospeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        holder.ivSpeck2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                speakText(translatedText1);
                            }
                        });
                    } else {
                        Log.e("TextToSpeech", "Initialization failed");
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return chat_list.size();
    }

    private void speakText(String text) {
        if (txttospeech != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                txttospeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                txttospeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }


    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tvSend1, tvSend2;
        TextView tvReceive1, tvReceive2;
        LinearLayout llReceive, llSend;
        ImageView ivSpeck1, ivSpeck2;

        public ChatViewHolder(View itemView) {
            super(itemView);
            llReceive = (LinearLayout) itemView.findViewById(R.id.llReceive);
            llSend = (LinearLayout) itemView.findViewById(R.id.llSend);
            tvSend1 = (TextView) itemView.findViewById(R.id.tvSend1);
            tvSend2 = (TextView) itemView.findViewById(R.id.tvSend2);
            tvReceive1 = (TextView) itemView.findViewById(R.id.tvReceive1);
            tvReceive2 = (TextView) itemView.findViewById(R.id.tvReceive2);
            ivSpeck1 = itemView.findViewById(R.id.ivSpeck1);
            ivSpeck2 = itemView.findViewById(R.id.ivSpeck2);
        }
    }
}
