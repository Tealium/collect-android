package com.tealium.kitchensink.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tealium.kitchensink.model.Model;

abstract class StyleableFragment extends Fragment {

    private Model model;

    protected final Model getModel(Context context) {
        if (this.model == null) {
            this.model = new Model(context);
        }
        return this.model;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
