package com.drplump.droid.test02;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.drplump.droid.test02.yapi.API;
import com.drplump.droid.test02.yapi.Dict;
import com.drplump.droid.test02.yapi.DictionaryAPI;

import java.util.ArrayList;
import java.util.List;

public class DictionaryFragment extends Fragment {
    private static final String TEXT_PARAMETER_KEY = "dictionary.text";
    private static final String DIRECT_PARAMETER_KEY = "dictionary.direct";

    private String text;
    private String direct;
    private List<Dict> dictList;

    public DictionaryFragment() {
        // Required empty public constructor
    }

    public static DictionaryFragment newInstance(String text, String direct) {
        DictionaryFragment fragment = new DictionaryFragment();
        Bundle args = new Bundle();
        args.putString(TEXT_PARAMETER_KEY, text);
        args.putString(DIRECT_PARAMETER_KEY, direct);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            text = getArguments().getString(TEXT_PARAMETER_KEY);
            direct = getArguments().getString(DIRECT_PARAMETER_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);
        TextView textView = (TextView) view.findViewById(R.id.dict_text);
        textView.setText(text);
        return view;
    }

    private void showDictList(List<Dict> dicts) {
    }



    private class GetDictTask extends AsyncTask<String, Void, List<Dict>> {

        @Override
        protected List<Dict> doInBackground(String... params) {
            DictionaryAPI api = new DictionaryAPI(getContext().getFilesDir());
            List<Dict> list;
            try {
                list = api.lookup(text, direct, getContext().getResources().getConfiguration().locale.getLanguage());
            } catch (Exception ex) {
                list = new ArrayList<>();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Dict> dicts) {
            super.onPostExecute(dicts);
            showDictList(dicts);
        }
    }

}
