package com.cs446.group18.timetracker.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.cs446.group18.timetracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class IconListAdaptor extends ArrayAdapter<Integer> {
    private final Context context;
    private final ArrayList<Integer> icons;
    private int selected = -1;
    public IconListAdaptor(Context context, int resource,  ArrayList<Integer> icons) {
        super(context, resource, icons);
        this.context = context;
        this.icons = icons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_icon, parent, false);

        FloatingActionButton button = rowView.findViewById(R.id.button_icon);
        button.setImageResource(icons.get(position));
        if (position != selected){
            button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0000ffff")));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = position;
                notifyDataSetChanged();
            }
        });
        return rowView;
    }
}
