package com.tealium.kitchensink.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.tealium.kitchensink.R;
import com.tealium.kitchensink.helper.TMSHelper;
import com.tealium.kitchensink.model.Model;

import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class TealiumSettingsFragment extends Fragment implements View.OnClickListener {

    private Model model;
    private AutoCompleteTextView accountTextView;
    private AutoCompleteTextView profileTextView;
    private AutoCompleteTextView environmentTextView;

    public TealiumSettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tealium_settings, container, false);

        rootView.findViewById(R.id.tealium_settings_button_update).setOnClickListener(this);
        rootView.findViewById(R.id.tealium_settings_button_reset).setOnClickListener(this);

        this.model = new Model(getActivity());

        this.accountTextView = (AutoCompleteTextView) rootView
                .findViewById(R.id.tealium_settings_actextview_account);
        this.accountTextView.setAdapter(this.model.getAccountAdapter());
        this.accountTextView.setText(this.model.getAccountName());

        this.profileTextView = (AutoCompleteTextView) rootView
                .findViewById(R.id.tealium_settings_actextview_profile);
        this.profileTextView.setAdapter(this.model.getProfileAdapter());
        this.profileTextView.setText(this.model.getProfileName());

        this.environmentTextView = (AutoCompleteTextView) rootView
                .findViewById(R.id.tealium_settings_actextview_env);
        this.environmentTextView.setAdapter(this.model.getEnvironmentAdapter());
        this.environmentTextView.setText(this.model.getEnvironmentName());


        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tealium_settings_button_update:
                update();
                break;
            case R.id.tealium_settings_button_reset:
                reset();
                break;
        }
    }

    private void update() {

        final String accountName = this.accountTextView.getText().toString();
        final String profileName = this.profileTextView.getText().toString();
        final String environmentName = this.environmentTextView.getText().toString();

        if (accountName.length() == 0 || profileName.length() == 0 || environmentName.length() == 0) {
            Toast.makeText(
                    getActivity(),
                    "account and profile name must be provided.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        this.model.setConfig(
                accountName,
                profileName,
                environmentName);
        
        TMSHelper.setup(this.getActivity().getApplication());

        Toast.makeText(
                getActivity(),
                String.format(Locale.ROOT, "Successfully reset using %s/%s/%s.",
                        accountName, profileName, environmentName),
                Toast.LENGTH_LONG).show();

        this.getActivity().finish();
    }

    private void reset() {
        this.accountTextView.setText(Model.DEFAULT_ACCOUNT);
        this.profileTextView.setText(Model.DEFAULT_PROFILE);
        this.environmentTextView.setText(Model.DEFAULT_ENV);
    }

}
