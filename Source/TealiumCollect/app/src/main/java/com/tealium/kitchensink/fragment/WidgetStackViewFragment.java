package com.tealium.kitchensink.fragment;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.StackView;

import com.tealium.kitchensink.R;
import com.tealium.kitchensink.adapter.StackViewAdapter;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WidgetStackViewFragment extends StyleableFragment {


    public WidgetStackViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        StackView stackView = (StackView) inflater.inflate(R.layout.fragment_widget_stackview, container, false);
        stackView.setAdapter(new StackViewAdapter());
        return stackView;
    }


}
