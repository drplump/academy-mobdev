package com.drplump.droid.academy;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drplump.droid.academy.hist.History;
import com.drplump.droid.academy.hist.HistoryItem;


public class FavouritesFragment extends Fragment {


    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState == null) {
            Fragment fragment = HistoryFragment.newInstance(false);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fv_fragment_container, fragment);
            transaction.commit();
        }

        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

/*
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout container = (LinearLayout) view.findViewById(R.id.favourite_container);
        History history = History.newInstance();
        for (HistoryItem h : history.list()) {
            if (!h.isFavourites()) continue;
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewItem = inflater.inflate(R.layout.history_item, container, false);
            TextView textDirect = (TextView) viewItem.findViewById(R.id.hitem_direct);
            textDirect.setText(h.direct.toUpperCase());
            TextView textSource = (TextView) viewItem.findViewById(R.id.hitem_source);
            textSource.setText(h.source);
            TextView textTranslated = (TextView) viewItem.findViewById(R.id.hitem_translated);
            textTranslated.setText(h.translated);
            ImageButton imageButton = (ImageButton) viewItem.findViewById(R.id.hitem_favor);
            imageButton.setColorFilter(R.color.tabIndicator, PorterDuff.Mode.SRC_IN);
            container.addView(viewItem);
        }


    }
*/

}
