package com.drplump.droid.academy;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drplump.droid.academy.yapi.Dict;
import com.drplump.droid.academy.yapi.DictionaryAPI;

import java.util.ArrayList;
import java.util.List;

public class DictionaryFragment extends Fragment {
    private static final String TEXT_PARAMETER_KEY = "dictionary.text";
    private static final String DIRECT_PARAMETER_KEY = "dictionary.direct";

    private String text;
    private String direct;

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
            new GetDictTask().execute(text, direct);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);
        TextView textView = (TextView) view.findViewById(R.id.dict_text);
        textView.setText(text);

        ImageButton favorButton = (ImageButton) view.findViewById(R.id.button_favor_dict);
        favorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void showDictList(List<Dict> dicts) {
        if (dicts.isEmpty()) return;
        LinearLayout container = (LinearLayout) getView().findViewById(R.id.dict_container);
        TextView dictTs = (TextView) getView().findViewById(R.id.dict_ts);
        dictTs.setText(dicts.get(0).getTs());

        for (Dict d : dicts) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout dictView = (LinearLayout) inflater.inflate(R.layout.dictionary_item, container, false);
            TextView textPos = (TextView) dictView.findViewById(R.id.dict_pos);
            textPos.setText(d.getPos());
            for(Dict.Trans t : d.getTr()) {
                View trView = inflater.inflate(R.layout.dictionary_tr_item, container, false);
                TextView textSource = (TextView) trView.findViewById(R.id.ditem_tr);
                textSource.setText(t.getText());
                TextView textMean = (TextView) trView.findViewById(R.id.ditem_mean);
                textMean.setText(TextUtils.join(", ", t.getMean()));
                dictView.addView(trView);
            }
            container.addView(dictView);
        }

    }



    private class GetDictTask extends AsyncTask<String, Void, List<Dict>> {

        @Override
        protected List<Dict> doInBackground(String... params) {
            DictionaryAPI api = new DictionaryAPI(getContext().getFilesDir());
            List<Dict> list;
            try {
                list = api.lookup(params[0], params[1], getContext().getResources().getConfiguration().locale.getLanguage());
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
