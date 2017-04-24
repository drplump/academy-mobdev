package com.drplump.droid.academy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.drplump.droid.academy.hist.History;

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

        final View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        ImageButton buttonClear = (ImageButton) view.findViewById(R.id.button_clear_history);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage(getString(R.string.message_clear_history_confirm))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                History h = History.newInstance();
                                h.clear();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        return view;
    }

}
