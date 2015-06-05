package com.tealium.kitchensink.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.tealium.kitchensink.R;
import com.tealium.kitchensink.adapter.ExpandableListAdapter;

public class WidgetExpandableListFragment extends StyleableFragment {

    public WidgetExpandableListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ExpandableListView view = (ExpandableListView) inflater.inflate(R.layout.fragment_widget_expandablelist, container, false);
        view.setAdapter(new ExpandableListAdapter());
        return view;
    }
}
