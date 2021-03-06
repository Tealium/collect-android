package com.tealium.kitchensink.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tealium.kitchensink.R;

public class GridViewAdapter extends BaseAdapter {

    private final String[] data;

    public GridViewAdapter(Context context) {
        data = context.getResources().getStringArray(R.array.widget_month_values);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        if (position < 0) {
            return null;
        }

        if (position >= data.length) {
            return null;
        }

        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;

        if ((textView = ((TextView) convertView)) == null) {
            textView = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_widget_gridview, null);
        }

        textView.setText(data[position]);

        return textView;
    }
}
