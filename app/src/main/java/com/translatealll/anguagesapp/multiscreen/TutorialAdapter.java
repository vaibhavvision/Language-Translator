package com.translatealll.anguagesapp.multiscreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.translatealll.anguagesapp.R;


public class TutorialAdapter extends PagerAdapter {

    Context context;
    int[] images;
    String[] title;
    String[] detail;
    LayoutInflater layoutInflater;

    public TutorialAdapter(Context context, int[] images, String[] title, String[] detail) {
        this.context = context;
        this.images = images;
        this.title = title;
        this.detail = detail;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.adapter_tutorial, container, false);
        ImageView Img = itemView.findViewById(R.id.img);
        TextView lblTitle = itemView.findViewById(R.id.txtTittle);
        TextView lblSubTitle = itemView.findViewById(R.id.txtSubTitle);
        Img.setImageResource(images[position]);
        lblSubTitle.setText(detail[position]);
        lblTitle.setText(title[position]);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
