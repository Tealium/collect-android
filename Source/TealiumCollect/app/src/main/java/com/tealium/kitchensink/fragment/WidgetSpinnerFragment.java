package com.tealium.kitchensink.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.tealium.kitchensink.R;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class WidgetSpinnerFragment extends StyleableFragment implements AdapterView.OnItemSelectedListener {

    public WidgetSpinnerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_widget_spinner, container, false);
        ((Spinner) view.findViewById(R.id.widget_spinner)).setOnItemSelectedListener(this);


        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final Object item = parent.getAdapter().getItem(position);
        Toast.makeText(
                this.getActivity(),
                String.format(Locale.ROOT, "%s selected!", item),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
