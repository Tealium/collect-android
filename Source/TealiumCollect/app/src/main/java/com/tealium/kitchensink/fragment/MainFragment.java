package com.tealium.kitchensink.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tealium.kitchensink.R;
import com.tealium.kitchensink.WidgetActivity;
import com.tealium.kitchensink.helper.TMSHelper;

import java.util.HashMap;
import java.util.Map;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends ListFragment {

    private Map<String, Class<? extends Fragment>> fragmentClassNames;

    public MainFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Resources res = this.getResources();

        this.fragmentClassNames = new HashMap<>(10);

        this.fragmentClassNames.put(
                res.getString(R.string.widget_category_clickables),
                WidgetClickableFragment.class);
        this.fragmentClassNames.put(
                res.getString(R.string.widget_category_checkables),
                WidgetCheckableFragment.class);
        this.fragmentClassNames.put(
                res.getString(R.string.widget_category_gridview),
                WidgetGridViewFragment.class);
        this.fragmentClassNames.put(
                res.getString(R.string.widget_category_pickers),
                WidgetPickersFragment.class);

        this.fragmentClassNames.put(
                res.getString(R.string.widget_category_seekables),
                WidgetSeekableFragment.class);
        this.fragmentClassNames.put(
                res.getString(R.string.widget_category_spinner),
                WidgetSpinnerFragment.class);
        this.fragmentClassNames.put(
                res.getString(R.string.widget_category_videoview),
                WidgetVideoViewFragment.class);
        this.fragmentClassNames.put(
                res.getString(R.string.widget_category_expandablelist),
                WidgetExpandableListFragment.class);

        this.fragmentClassNames.put(
                res.getString(R.string.widget_category_dialog_fragment),
                WidgetDialogFragment.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.fragmentClassNames.put(
                    res.getString(R.string.widget_category_stackview),
                    WidgetStackViewFragment.class);
        }

        setListAdapter(
                ArrayAdapter.createFromResource(
                        getActivity(),
                        R.array.widget_categories,
                        R.layout.cell_widget_name));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final Object selectedCategory = this.getListAdapter().getItem(position);
        final Class<? extends Fragment> fragmentClass = this.fragmentClassNames.get(selectedCategory);

        if (fragmentClass != null) {
            TMSHelper.trackEvent(
                    TMSHelper.Key.EVENT, "main_catagory_click",
                    TMSHelper.Key.EVENT_ITEM, fragmentClass.getSimpleName());
            this.selectFragment(selectedCategory.toString(), fragmentClass);
            return;
        }

        Toast.makeText(getActivity(), "Not yet implemented.", Toast.LENGTH_SHORT).show();
    }

    private void selectFragment(String categoryName, Class<?> fragmentClass) {

        if (WidgetDialogFragment.class.equals(fragmentClass)) {
            new WidgetDialogFragment().show(
                    getFragmentManager().beginTransaction(),
                    "tag:dialog"
            );
            return;
        }

        if (getActivity().findViewById(R.id.widget_container) == null) {
            Intent i = new Intent(getActivity(), WidgetActivity.class);
            i.putExtra(WidgetActivity.EXTRA_FRAGMENT_CLASS_NAME, fragmentClass.getName());
            i.putExtra(WidgetActivity.EXTRA_TITLE, categoryName);
            startActivity(i);
            return;
        }

        try {
            getFragmentManager().beginTransaction()
                    .replace(R.id.widget_container, (Fragment) fragmentClass.newInstance())
                    .commit();
        } catch (Throwable t) {
            Toast.makeText(getActivity(), "Uh-oh, this widget is not available.", Toast.LENGTH_SHORT).show();
        }
    }
}
