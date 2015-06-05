package com.tealium.kitchensink.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.tealium.kitchensink.R;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class WidgetSeekableFragment extends StyleableFragment implements
        RatingBar.OnRatingBarChangeListener,
        SeekBar.OnSeekBarChangeListener {

    public WidgetSeekableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_widget_seekable, container, false);

        ((SeekBar) view.findViewById(R.id.widget_seekable_seekbar))
                .setOnSeekBarChangeListener(this);

        ((RatingBar) view.findViewById(R.id.widget_seekable_ratingbar))
                .setOnRatingBarChangeListener(this);

        return view;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        Toast.makeText(
                this.getActivity(),
                String.format(Locale.ROOT, "%1.1f stars!", rating),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Toast.makeText(
                this.getActivity(),
                String.format(Locale.ROOT, "Progess: %d%%!", progress),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
