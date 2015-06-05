package com.tealium.kitchensink.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tealium.kitchensink.R;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class WidgetCheckableFragment extends StyleableFragment implements
        CompoundButton.OnCheckedChangeListener,
        RadioGroup.OnCheckedChangeListener {

    public WidgetCheckableFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_widget_checkable, container, false);

        ((CompoundButton) view.findViewById(R.id.widget_checkable_checkbox))
                .setOnCheckedChangeListener(this);

        ((CompoundButton) view.findViewById(R.id.widget_checkable_togglebutton))
                .setOnCheckedChangeListener(this);

        CompoundButton switchButton = ((CompoundButton) view.findViewById(R.id.widget_checkable_switch));
        if (switchButton != null) {
            // API 14+
            switchButton.setOnCheckedChangeListener(this);
        }

        ((RadioGroup) view.findViewById(R.id.widget_checkable_radiogroup))
                .setOnCheckedChangeListener(this);

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Toast.makeText(
                this.getActivity(),
                String.format(
                        Locale.ROOT,
                        "%s: %s",
                        buttonView.getClass().getSimpleName(),
                        isChecked ? "Checked" : "Unchecked"),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        final String selectedName;

        switch (checkedId) {
            case R.id.widget_checkable_radio_red:
                selectedName = "Red";
                break;
            case R.id.widget_checkable_radio_green:
                selectedName = "Green";
                break;
            case R.id.widget_checkable_radio_blue:
                selectedName = "Blue";
                break;
            default:
                selectedName = "(unknown)";
                break;
        }

        Toast.makeText(
                this.getActivity(),
                String.format(Locale.ROOT, "Selected %s", selectedName),
                Toast.LENGTH_SHORT).show();
    }
}
