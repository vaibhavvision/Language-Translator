package com.translatealll.anguagesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.activity.ParagraphActivity;
import com.translatealll.anguagesapp.model.Paragraph;

import java.util.ArrayList;

public class ParagraphDialogAdapter extends RecyclerView.Adapter<ParagraphDialogAdapter.ViewHolder> {

    Context context;
    ArrayList<Paragraph> paragraphs;
    PhraseDialogInterface phraseInterface;

    public ParagraphDialogAdapter(Context context, ArrayList<Paragraph> paragraphs, PhraseDialogInterface phraseInterface) {
        this.context = context;
        this.paragraphs = paragraphs;
        this.phraseInterface = phraseInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paragraph, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (ParagraphActivity.isSelected == position) {
            holder.ivSelect.setVisibility(View.VISIBLE);
        }else{
            holder.ivSelect.setVisibility(View.GONE);
        }

        holder.txtName.setText(paragraphs.get(position).getName());
        holder.ivFlag.setImageResource(paragraphs.get(position).getFlag());

        holder.relativeMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phraseInterface.OnPhraseDialogClick(paragraphs.get(position).getName(), position);
                ParagraphActivity.isSelected = position;
                notifyDataSetChanged();

            }
        });



    }

    @Override
    public int getItemCount() {
        return paragraphs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView ivFlag, ivSelect;
        RelativeLayout relativeMain;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            ivFlag = itemView.findViewById(R.id.ivFlag);
            ivSelect = itemView.findViewById(R.id.ivSelect);
            relativeMain = itemView.findViewById(R.id.relativeMain);
        }
    }


    public interface PhraseDialogInterface {
        void OnPhraseDialogClick(String phraseTitle, int position);
    }
}
