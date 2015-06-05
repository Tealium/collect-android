package com.tealium.kitchensink;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.tealium.kitchensink.fragment.TealiumFeaturesFragment;
import com.tealium.kitchensink.helper.TMSHelper;


public class TealiumFeaturesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tealium_features);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new TealiumFeaturesFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TMSHelper.onResume(this);
    }

    @Override
    protected void onPause() {
        TMSHelper.onPause(this);
        super.onPause();
    }

}
