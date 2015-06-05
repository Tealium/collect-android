package com.tealium.kitchensink.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tealium.collect.TealiumCollect;
import com.tealium.kitchensink.R;
import com.tealium.kitchensink.model.Model;
import com.tealium.kitchensink.util.Constant;

/**
 * A placeholder fragment containing a simple view.
 */
public class TealiumFeaturesFragment extends StyleableFragment implements View.OnClickListener {

    private final BroadcastReceiver colorChangeReveiver;
    private final IntentFilter colorChangeIntentFilter;
    private EditText traceInput;
    private TextView traceButton;


    public TealiumFeaturesFragment() {
        this.colorChangeReveiver = createColorChangeBroadcastReceiver();
        this.colorChangeIntentFilter = new IntentFilter(Constant.LocalBroadcast.ACTION_COLOR_CHANGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tealium_features, container, false);
        this.traceInput = (EditText) rootView.findViewById(R.id.tealium_features_input_trace);
        this.traceButton = (TextView) rootView.findViewById(R.id.tealium_features_button_trace);

        this.setupView();

        rootView.findViewById(R.id.tealium_features_button_trace)
                .setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(
                this.colorChangeReveiver, this.colorChangeIntentFilter);
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(
                this.colorChangeReveiver);
        super.onPause();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tealium_features_button_trace) {

            if (this.traceInput.isEnabled()) {
                final String traceId = this.traceInput.getText().toString();

                if (traceId.length() > 0) {
                    this.getModel(v.getContext()).setActiveTraceId(traceId);
                    this.setupView();
                    TealiumCollect.joinTrace(traceId);
                }
            } else {
                this.getModel(v.getContext()).setActiveTraceId(null);
                this.setupView();
                TealiumCollect.leaveTrace();
            }
        }
    }

    private void setupView() {
        Model model = this.getModel(this.getActivity());

        final String traceId = model.getActiveTraceId();

        if (traceId == null) {
            this.traceInput.setEnabled(true);
            this.traceInput.setText("");

            this.traceButton.setText(R.string.tealium_features_button_trace_title_join);
        } else {
            this.traceInput.setEnabled(false);
            this.traceInput.setText(traceId);

            this.traceButton.setText(R.string.tealium_features_button_trace_title_leave);
        }
    }

    private BroadcastReceiver createColorChangeBroadcastReceiver() {
        return new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                final View view = getView();
                if (view == null) {
                    return;
                }

                final int oldColor = intent.getIntExtra(
                        Constant.LocalBroadcast.EXTRA_OLD_COLOR, Color.WHITE);

                final int newColor = intent.getIntExtra(
                        Constant.LocalBroadcast.EXTRA_NEW_COLOR, Color.WHITE);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    view.setBackgroundColor(newColor);
                    return;
                }

                animateBackground(view, oldColor, newColor);
            }

            @TargetApi(11)
            private void animateBackground(final View v, int oldColor, int newColor) {
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), oldColor, newColor);
                colorAnimation.setDuration(1000);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        v.setBackgroundColor((Integer) animator.getAnimatedValue());
                    }
                });
                colorAnimation.start();
            }
        };
    }
}
