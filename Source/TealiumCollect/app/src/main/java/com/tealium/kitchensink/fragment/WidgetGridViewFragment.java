package com.tealium.kitchensink.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.tealium.kitchensink.R;
import com.tealium.kitchensink.adapter.GridViewAdapter;

import java.util.Locale;

/**
 * Created by chadhartman on 1/14/15.
 */
public class WidgetGridViewFragment extends StyleableFragment implements AdapterView.OnItemClickListener {
    public WidgetGridViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        GridView view = (GridView) inflater.inflate(R.layout.fragment_widget_gridview, container, false);
        view.setAdapter(new GridViewAdapter(this.getActivity()));
        view.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(
                this.getActivity(),
                String.format(Locale.ROOT, "%s Clicked!", parent.getAdapter().getItem(position)),
                Toast.LENGTH_SHORT).show();
    }
}
