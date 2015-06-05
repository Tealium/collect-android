package com.tealium.kitchensink.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tealium.kitchensink.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WidgetPickersFragment extends StyleableFragment {


    public WidgetPickersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_widget_pickers, container, false);
    }


}
