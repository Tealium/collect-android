package com.tealium.kitchensink.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tealium.kitchensink.R;

import java.util.Locale;

public class WidgetClickableFragment extends StyleableFragment implements OnClickListener {

    public WidgetClickableFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_widget_clickable, container, false);
        view.findViewById(R.id.widget_clickable_button).setOnClickListener(this);
        view.findViewById(R.id.widget_clickable_imagebutton).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(
                this.getActivity(),
                String.format(Locale.ROOT, "%s clicked!", v.getClass().getSimpleName()),
                Toast.LENGTH_SHORT).show();

    }
}
