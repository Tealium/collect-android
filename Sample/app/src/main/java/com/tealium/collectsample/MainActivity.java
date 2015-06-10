package com.tealium.collectsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tealium.collect.TealiumCollect;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main_button).setOnClickListener(createClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();

        Map<String, String> data = new HashMap<>(1);
        data.put("screen_title", "main");
        TealiumCollect.sendView(data);
    }

    private static View.OnClickListener createClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        v.getContext(),
                        R.string.main_button_click_toast,
                        Toast.LENGTH_SHORT).show();

                Map<String, String> data = new HashMap<>(1);
                data.put("ui_event", "main_button_click");
                TealiumCollect.sendEvent(data);
            }
        };
    }
}
