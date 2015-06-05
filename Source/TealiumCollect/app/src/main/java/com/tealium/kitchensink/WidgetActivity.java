package com.tealium.kitchensink;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.tealium.kitchensink.helper.TMSHelper;


public class WidgetActivity extends ActionBarActivity {

    public static final String EXTRA_FRAGMENT_CLASS_NAME = "fragment_class_name";
    public static final String EXTRA_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        final String fragmentClassName = this.getIntent().getStringExtra(EXTRA_FRAGMENT_CLASS_NAME);
        final String title = this.getIntent().getStringExtra(EXTRA_TITLE);
        if (title != null) {
            this.getSupportActionBar().setTitle(title);
        }

        if (fragmentClassName == null) {
            this.fail();
            return;
        }


        if (savedInstanceState == null) {
            try {
                Fragment fragment = (Fragment) Class.forName(fragmentClassName).newInstance();

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, fragment)
                        .commit();
            } catch (Throwable t) {
                fail();
            }
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

    private void fail() {
        Toast.makeText(this, "Unable to show this widget", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
