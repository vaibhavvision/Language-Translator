package com.translatealll.anguagesapp.adapter;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.model.PhrasesRepo;
import com.translatealll.anguagesapp.utils.AllLanguage;

import java.util.ArrayList;
import java.util.Locale;


public class PhrasesDetailAdapter extends RecyclerView.Adapter<PhrasesDetailAdapter.PhrasesdetailViewHolder> {
    Context context;
    String lng1name;
    String lng2name;
    ArrayList<PhrasesRepo> phraseslist;
    TextToSpeech txttospeech;


    boolean dropDown = false;

    public PhrasesDetailAdapter(Context context, ArrayList<PhrasesRepo> phraseslist, String lng1name, String lng2name) {
        this.context = context;
        this.phraseslist = phraseslist;
        this.lng1name = lng1name;
        this.lng2name = lng2name;
    }

    @Override
    public PhrasesdetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhrasesdetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phrases_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(PhrasesdetailViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.setIsRecyclable(false);

        if (position % 10 == 0) {
            Log.e("TAG", "onBindViewHolder: " + position);
            holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_tab));
            holder.tv_phrase.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tv_phrase.setEnabled(false);
            holder.tv_phrase.setText(this.phraseslist.get(position).getPhrase());
            holder.ivArrowUpDown.setVisibility(View.INVISIBLE);
            holder.linearItem.setVisibility(View.GONE);
        } else {
            holder.tv_phrase.setTextColor(this.context.getResources().getColor(R.color.black));
            holder.tv_phrase.setTypeface(null, Typeface.NORMAL);
            holder.tv_phrase.setEnabled(true);
            holder.tv_phrase.setText(this.phraseslist.get(position).getPhrase());
            holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.et_border_translate_shape));
            holder.ivArrowUpDown.setVisibility(View.VISIBLE);
        }
        if (this.lng1name.equals(AllLanguage.URDU) || this.lng1name.equals(AllLanguage.ARABIC)) {
            holder.tv_phrase.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        holder.ivArrowUpDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dropDown) {
                    holder.linearItem.setVisibility(View.GONE);
                    holder.ivArrowUpDown.setImageResource(R.drawable.ic_down_arrow);
                    dropDown = false;
                } else {
                    holder.linearItem.setVisibility(View.VISIBLE);
                    holder.ivArrowUpDown.setImageResource(R.drawable.ic_up_arrow);
                    dropDown = true;
                }
            }
        });


        holder.tv_phrase.setText(phraseslist.get(position).getPhrase());

        holder.volume_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phraseslist.get(position).getPhrase().equals("")) {
                    Toast.makeText(context, "No text detected", Toast.LENGTH_SHORT).show();
                } else {
                    txttospeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int i2) {
                            String translation = phraseslist.get(position).getPhrase();
                            if (i2 != -1) {
                                if (translation.contains("_")) {
                                    translation = translation.replaceAll("_", "");
                                }
                                txttospeech.setLanguage(new Locale(lng2name));
                                txttospeech.speak(translation, 0, null);
                            }
                        }
                    });
                }
            }
        });

        holder.imgbtn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phraseslist.get(position).getPhrase().equals("")) {
                    Toast.makeText(context, "No text found", Toast.LENGTH_SHORT).show();
                    return;
                }
                ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setText(phraseslist.get(position).getPhrase());
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        holder.share_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String translation = phraseslist.get(position).getPhrase();
                if (translation.equals("")) {
                    Toast.makeText(context, "No Text Found", Toast.LENGTH_SHORT).show();
                } else {
                    context.startActivity(Intent.createChooser(new Intent("android.intent.action.SEND").putExtra("android.intent.extra.TEXT", translation).setType("text/plain").putExtra("android.intent.extra.SUBJECT", "choose one"), "Share through"));
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return this.phraseslist.size();
    }


    public static class PhrasesdetailViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearItem;
        ImageView share_img, imgbtn_copy, volume_img;
        ImageView ivArrowUpDown;
        TextView tv_phrase;

        public PhrasesdetailViewHolder(View itemView) {
            super(itemView);
            tv_phrase = (TextView) itemView.findViewById(R.id.tv_phrase);
            ivArrowUpDown = (ImageView) itemView.findViewById(R.id.ivArrowUpDown);
            linearItem = (LinearLayout) itemView.findViewById(R.id.linearItem);
            share_img = (ImageView) itemView.findViewById(R.id.share_img);
            imgbtn_copy = (ImageView) itemView.findViewById(R.id.imgbtn_copy);
            volume_img = (ImageView) itemView.findViewById(R.id.volume_img);
        }
    }


}
