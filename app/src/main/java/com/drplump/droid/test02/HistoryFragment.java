package com.drplump.droid.test02;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.drplump.droid.test02.hist.History;
import com.drplump.droid.test02.hist.HistoryItem;
import com.drplump.droid.test02.yapi.Dict;

import java.util.List;

public class HistoryFragment extends Fragment {
    private static final String TEXT_PARAMETER_KEY = "dictionary.text";

    private String tr_text;
    private History history;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        return view;
    }

    private void updateHistory() {
        ScrollView scroller = (ScrollView) getView().findViewById(R.id.dict_scroller);

        for (HistoryItem h : history.list()) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.history_item, scroller, false);
            TextView textSource = (TextView) view.findViewById(R.id.hitem_source);
            textSource.setText(h.getText());
            TextView textTranslated = (TextView) view.findViewById(R.id.hitem_translated);
            textTranslated.setText(h.getTs());
            scroller.addView(view);
        }

    }

}
