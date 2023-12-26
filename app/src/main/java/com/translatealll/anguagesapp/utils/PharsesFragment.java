package com.translatealll.anguagesapp.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.adapter.ParagraphDialogAdapter;
import com.translatealll.anguagesapp.model.ParagraphRepo;

import java.util.ArrayList;


public class PharsesFragment extends BottomSheetDialogFragment {
    RecyclerView recyclerViewParagraph;
    ParagraphDialogAdapter.PhraseDialogInterface phraseInterface;
    String[] paragraphName = new String[]{"English", "Urdu", "Arabic", "Chinese", "German", "Japanese", "Italian", "Russian", "French", "Hindi"};
    int[] paragraphImage = new int[]{R.drawable.flg_english, R.drawable.flg_urdu, R.drawable.flg_arabic, R.drawable.flg_china, R.drawable.flg_germany, R.drawable.flg_japani, R.drawable.flg_itlay, R.drawable.flg_russian, R.drawable.flg_franch, R.drawable.flg_hindi};
    ArrayList<ParagraphRepo> arrayList = new ArrayList<>();

    public PharsesFragment(ParagraphDialogAdapter.PhraseDialogInterface phraseInterface) {
        this.phraseInterface = phraseInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_paragraph_bottomsheet, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewParagraph = view.findViewById(R.id.recyclerViewParagraph);
        for (int i = 0; i < paragraphName.length; i++) {
            arrayList.add(new ParagraphRepo(paragraphName[i], paragraphImage[i]));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewParagraph.setLayoutManager(linearLayoutManager);
        ParagraphDialogAdapter paragraphDialogAdapter = new ParagraphDialogAdapter(getActivity(), arrayList, phraseInterface);
        recyclerViewParagraph.setAdapter(paragraphDialogAdapter);
    }
}
