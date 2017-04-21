package com.drplump.droid.test02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.drplump.droid.test02.yapi.ELang;
import com.drplump.droid.test02.yapi.Lang;
import com.drplump.droid.test02.yapi.TranslateAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class TranslateFragment extends Fragment {

    final static String LANG_FROM_KEY = "com.drplump.droid.test02.lang.from";
    final static String LANG_TO_KEY = "com.drplump.droid.test02.lang.to";

    private String sourceText;
    private String translatedText;
    private String direct;

    private TextView tr_text;

    private Spinner spinFrom;
    private Spinner spinTo;

    private FrameLayout fragmentContainer;

    //List<Lang> langList = new ArrayList<>();
    ArrayAdapter<Lang> langArrayAdapter;


    public TranslateFragment() {
        // Required empty public constructor
    }

    public TranslateFragment newInstance() {
        TranslateFragment fragment = new TranslateFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        langArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<Lang>());
        langArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        new GetLangsTask().execute(getResources().getConfiguration().locale);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_translate, container, false);

        spinFrom = (Spinner) view.findViewById(R.id.spin_from);
        spinFrom.setAdapter(langArrayAdapter);
        spinFrom.setOnItemSelectedListener(new LangSpinnerItemSelectedListener());
        spinTo = (Spinner) view.findViewById(R.id.spin_to);
        spinTo.setAdapter(langArrayAdapter);
        spinTo.setOnItemSelectedListener(new LangSpinnerItemSelectedListener());

        // init text translate label
        tr_text = (TextView) view.findViewById(R.id.tr_text);
        tr_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TapToTranslateActivity.class);
                intent.putExtra(LANG_FROM_KEY, ((Lang) spinFrom.getSelectedItem()).code);
                intent.putExtra(LANG_TO_KEY, ((Lang) spinTo.getSelectedItem()).code);
                startActivityForResult(intent, TapToTranslateActivity.REQUEST_ID);

            }
        });


        tr_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Toast.makeText(tr_text.getContext(), s.toString(), Toast.LENGTH_SHORT).show();
                showTranslation(s.toString());
            }
        });

        ImageButton buttonClear = (ImageButton) view.findViewById(R.id.button_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTranslation();
            }
        });

        ImageButton buttonSwipe = (ImageButton) view.findViewById(R.id.button_swipe);
        buttonSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fromPosition = spinFrom.getSelectedItemPosition();
                int toPosition = spinTo.getSelectedItemPosition();

                spinFrom.setSelection(toPosition);
                spinTo.setSelection(fromPosition);
            }
        });

        fragmentContainer = (FrameLayout) view.findViewById(R.id.fragment_container);



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TapToTranslateActivity.REQUEST_ID && resultCode == Activity.RESULT_OK) {
            sourceText = data.getStringExtra(TapToTranslateActivity.REQUEST_KEY);
            translatedText = data.getStringExtra(TapToTranslateActivity.REQUEST_RESULT_KEY);
            tr_text.setText(sourceText);
            //spinFrom.setSelection();

        }
    }

    private void showTranslation(String text) {
        if (text.isEmpty()) {
            Fragment fragment = new HistoryFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();

        } else {
            DictionaryFragment fragment = DictionaryFragment.newInstance(translatedText, direct);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        }
    }

    public void clearTranslation() {
        tr_text.setText(null);
    }

    private class GetLangsTask extends AsyncTask<Locale, Void, List<Lang>> {

        @Override
        protected List<Lang> doInBackground(Locale... params) {
            List<Lang> list;
            TranslateAPI api = new TranslateAPI(getContext().getFilesDir());
            String locale_lang = params[0].getLanguage();
            try {
                list = api.getLangs(locale_lang);
            }catch (Exception ex) {
                list = new ArrayList<>();
                //list.add(new Lang("en", "English", true));
                //list.add(new Lang("ru", "Russian", false));
            }
            if (list.isEmpty()) {
                for (ELang l : ELang.values()) {
                    list.add(new Lang(l.toString(), l.name(), l.toString().equals(locale_lang)));
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Lang> langs) {
            //super.onPostExecute(langs);
            //langList = langs;
            for (int i = 0; i < langs.size(); i++) {
                Lang l = langs.get(i);
                if (l.preferred) {
                    langArrayAdapter.insert(l, 0);
                } else {
                    langArrayAdapter.add(l);
                }
            }

            //add sort

            //spinFrom.setAdapter(langArrayAdapter);
            //spinTo.setAdapter(langArrayAdapter);

        }
    }

    private class LangSpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Spinner t;
            if (parent.getId() == R.id.spin_from) {
                t = spinTo;
            } else if (parent.getId() == R.id.spin_to) {
                t = spinFrom;
            } else {
                return;
            }

            if (t.getSelectedItemPosition() == position) {
                if (++position == t.getAdapter().getCount()) {
                    t.setSelection(--position);
                } else {
                    t.setSelection(++position);
                }
            }

            direct = ((Lang) spinFrom.getSelectedItem()).code + "-" + ((Lang) spinTo.getSelectedItem()).code;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}
