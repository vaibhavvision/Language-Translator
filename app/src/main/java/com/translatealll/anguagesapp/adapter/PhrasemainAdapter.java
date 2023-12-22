package com.translatealll.anguagesapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.activity.ParagraphActivity;
import com.translatealll.anguagesapp.model.PhraseTitlesModelClass;

import java.util.ArrayList;


public class PhrasemainAdapter extends RecyclerView.Adapter<PhrasemainAdapter.Phrasemainadapterviewholder> {
    Context context;
    String lang1;
    String lang2;
    PhraseInterface phraseInterface;
    ArrayList<PhraseTitlesModelClass> phrasetitle_list;

    public PhrasemainAdapter(Context context, ArrayList<PhraseTitlesModelClass> phrasetitle_list, String lang1, String lang2, PhraseInterface phraseInterface) {
        this.context = context;
        this.phrasetitle_list = phrasetitle_list;
        this.phraseInterface = phraseInterface;
        this.lang1 = lang1;
        this.lang2 = lang2;
    }

    @Override
    public Phrasemainadapterviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Phrasemainadapterviewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mainphrase_singlerow, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Phrasemainadapterviewholder holder, int position) {
        holder.phrasemaintitle.setText(phrasetitle_list.get(position).getTitle());
        holder.ivParagraph.setImageResource(phrasetitle_list.get(position).getTitle_icon());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phraseInterface.OnPhraseClick(phrasetitle_list.get(position).getTitle(), position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return this.phrasetitle_list.size();
    }


    public static class Phrasemainadapterviewholder extends RecyclerView.ViewHolder {
        TextView phrasemaintitle;
        ImageView ivParagraph;

        public Phrasemainadapterviewholder(View itemView) {
            super(itemView);
            ivParagraph = (ImageView) itemView.findViewById(R.id.ivParagraph);
            phrasemaintitle = (TextView) itemView.findViewById(R.id.tv_phrasetitle);
        }
    }


    public interface PhraseInterface {
        void OnPhraseClick(String phrasetitle, int position);
    }
}
