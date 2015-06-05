package com.tealium.kitchensink.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tealium.kitchensink.R;

import java.util.Locale;

public class ExpandableListAdapter extends BaseExpandableListAdapter implements
        View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_widget_explist_child, parent, false);
            convertView.findViewById(R.id.cell_widget_explist_child_button)
                    .setOnClickListener(this);
            ((CompoundButton) convertView.findViewById(R.id.cell_widget_explist_child_checkbox))
                    .setOnCheckedChangeListener(this);
        }

        ((TextView) convertView.findViewById(R.id.explist_child_label))
                .setText(String.format(Locale.US, "%d:%d", groupPosition, childPosition));

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 3;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return 3;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView layout;

        if ((layout = (TextView) convertView) == null) {
            layout = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_widget_explist_group, parent, false);
        }

        layout.setText("Group " + groupPosition);

        return layout;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Toast.makeText(
                buttonView.getContext(),
                String.format(
                        Locale.ROOT,
                        "%s: %s!",
                        buttonView.getClass().getSimpleName(),
                        isChecked ? "Checked" : "Unchecked"),
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(
                v.getContext(),
                String.format(
                        Locale.ROOT,
                        "%s clicked!",
                        v.getClass().getSimpleName()),
                Toast.LENGTH_SHORT).show();
    }
}
