package com.translatealll.anguagesapp.adapter;


import static com.translatealll.anguagesapp.activity.LanguageActivity.isSelected;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.model.Country;

import java.util.ArrayList;

public final class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyViewHolder> {
    Context context;
    ArrayList<Country> countries;

    public LanguageAdapter(Context context, ArrayList<Country> countries) {
        this.context = context;
        this.countries = countries;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View myView = LayoutInflater.from(this.context).inflate(R.layout.layout_item_language, parent, false);
        return new MyViewHolder(myView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int i) {

        holder.txthie.setText(countries.get(i).getName());
        holder.txtlanguage.setText(countries.get(i).getSuggestname());

        if (isSelected == i) {
            holder.imgviewlang.setImageResource(R.drawable.card_language);
            holder.txthie.setTextColor(ContextCompat.getColor(context,R.color.white));
            holder.txtlanguage.setTextColor(ContextCompat.getColor(context,R.color.white));
        } else {
            holder.imgviewlang.setImageResource(R.drawable.card_language_unselect);
            holder.txthie.setTextColor(ContextCompat.getColor(context,R.color.black));
            holder.txtlanguage.setTextColor(ContextCompat.getColor(context,R.color.select_dot));
        }

        holder.itemView.setOnClickListener(view2 -> {
            isSelected = i;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgviewlang;
        public TextView txthie, txtlanguage;

        public MyViewHolder(View view) {
            super(view);
            imgviewlang = view.findViewById(R.id.imgviewlang);
            txthie = view.findViewById(R.id.txthie);
            txtlanguage = view.findViewById(R.id.txtlanguage);

        }
    }
}