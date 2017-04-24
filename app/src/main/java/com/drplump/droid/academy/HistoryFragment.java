package com.drplump.droid.academy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drplump.droid.academy.hist.History;
import com.drplump.droid.academy.hist.HistoryItem;

public class HistoryFragment extends Fragment {

    public static final String SHOW_ALL_KEY = "history.show.all";

    private boolean favouritesOnly;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(boolean showAll) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(SHOW_ALL_KEY, String.valueOf(showAll));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String text = getArguments().getString(SHOW_ALL_KEY);
            boolean showAll = true;
            try {
                showAll = Boolean.parseBoolean(text);
            } catch (Exception ex) {
                Log.e(HistoryFragment.class.getName(), "Boolean parsing error");
            }
            favouritesOnly = !showAll;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final SwipeRefreshLayout view = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_history, container, false);
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                view.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refresh();
    }

    public void refresh(){
        LinearLayout container = (LinearLayout) getView().findViewById(R.id.history_container);
        container.removeAllViewsInLayout();
        History history = History.newInstance();
        for (HistoryItem h : history.list()) {
            if(favouritesOnly && !h.isFavourites()) continue;
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewItem = inflater.inflate(R.layout.history_item, container, false);
            TextView textDirect = (TextView) viewItem.findViewById(R.id.hitem_direct);
            textDirect.setText(h.direct.toUpperCase());
            TextView textSource = (TextView) viewItem.findViewById(R.id.hitem_source);
            textSource.setText(h.source);
            TextView textTranslated = (TextView) viewItem.findViewById(R.id.hitem_translated);
            textTranslated.setText(h.translated);
            ImageButton imageButton = (ImageButton) viewItem.findViewById(R.id.hitem_favor);
            if (h.isFavourites()) imageButton.setColorFilter(android.R.color.background_light);
            imageButton.setOnClickListener(new FavouriteItClickListener(h));
            View itemHolder = viewItem.findViewById(R.id.hitem_holder);
            itemHolder.setOnClickListener(new HistoryItemClickListener());
            container.addView(viewItem);
        }

    }

    private class HistoryItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //TODO: Запилить нажималку
        }
    }

    private class FavouriteItClickListener implements View.OnClickListener {
        private final HistoryItem item;

        FavouriteItClickListener(HistoryItem item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            History history = History.newInstance();
            item.changeStatus();
            if(history.update(item)) {
                refresh();
            } else {
                item.changeStatus();
            }
        }
    }

}
