package com.mathius.dd.milestokm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dds86 on 20-Feb-18.
 */

public class MyAdapter extends ArrayAdapter<Model> {
    private List<Model> objects;

    public MyAdapter(@NonNull Context context, @NonNull List<Model> objects) {
        super(context, 0, objects);
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.items, parent, false);

        Model product = getItem(position);

        TextView textView1 = (TextView) view.findViewById(R.id.text1);
        TextView textView2 = (TextView) view.findViewById(R.id.text2);
        TextView edinica1 = (TextView) view.findViewById(R.id.tvEdinica1);
        TextView edinica2 = (TextView) view.findViewById(R.id.tvEdinica2);

        textView1.setText(product.getTextView1());
        textView2.setText(product.getTextView2());
        edinica1.setText(product.getEdinica1());
        edinica2.setText(product.getEdinica2());

        return view;
    }
}