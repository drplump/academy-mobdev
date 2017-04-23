package com.drplump.droid.academy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.drplump.droid.academy.hist.History;
import com.drplump.droid.academy.hist.HistoryItem;
import com.drplump.droid.academy.yapi.ELang;
import com.drplump.droid.academy.yapi.Lang;
import com.drplump.droid.academy.yapi.TranslateAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class TranslateFragment extends Fragment {

    final static String LANG_FROM_KEY = "com.drplump.droid.test02.lang.from";
    final static String LANG_TO_KEY = "com.drplump.droid.test02.lang.to";

    private String sourceText;
    private String translatedText;
    private String direct;
    private String indirect;

    private TextView sourceTextView;
    private Spinner spinFrom;
    private Spinner spinTo;

    ArrayAdapter<Lang> langArrayAdapter;

    public TranslateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        langArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<Lang>());
        langArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        new GetLangsTask().execute(getResources().getConfiguration().locale);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, container, false);

        spinFrom = (Spinner) view.findViewById(R.id.spin_from);
        spinFrom.setAdapter(langArrayAdapter);
        spinFrom.setOnItemSelectedListener(new LangSpinnerItemSelectedListener());
        spinTo = (Spinner) view.findViewById(R.id.spin_to);
        spinTo.setAdapter(langArrayAdapter);
        spinTo.setOnItemSelectedListener(new LangSpinnerItemSelectedListener());

        sourceTextView = (TextView) view.findViewById(R.id.tr_text);
        sourceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TapToTranslateActivity.class);
                intent.putExtra(LANG_FROM_KEY, ((Lang) spinFrom.getSelectedItem()).code);
                intent.putExtra(LANG_TO_KEY, ((Lang) spinTo.getSelectedItem()).code);
                startActivityForResult(intent, TapToTranslateActivity.REQUEST_ID);

            }
        });

        sourceTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Toast.makeText(sourceTextView.getContext(), s.toString(), Toast.LENGTH_SHORT).show();
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

                String tmp = direct;
                direct = indirect;
                indirect = tmp;

                tmp = sourceText;
                sourceText = translatedText;
                translatedText = tmp;
                sourceTextView.setText(sourceText);
            }
        });

        if(savedInstanceState == null) {
            Fragment fragment = new HistoryFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TapToTranslateActivity.REQUEST_ID && resultCode == Activity.RESULT_OK) {
            sourceText = data.getStringExtra(TapToTranslateActivity.REQUEST_KEY);
            translatedText = data.getStringExtra(TapToTranslateActivity.REQUEST_RESULT_KEY);
            sourceTextView.setText(sourceText);
            History.newInstance().add(new HistoryItem(sourceText, translatedText, direct));
        }
    }

    private void showTranslation(String text) {
        if (text.isEmpty()) {
            Fragment fragment = new HistoryFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();

        } else {
            DictionaryFragment fragment = DictionaryFragment.newInstance(translatedText, indirect);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        }
    }

    public void clearTranslation() {
        sourceTextView.setText(null);
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
            spinFrom.setOnItemSelectedListener(null);
            spinTo.setOnItemSelectedListener(null);
            int index = 0;
            for (Lang l : langs) {
                if (l.preferred) {
                    langArrayAdapter.insert(l, index);
                    index++;
                } else {
                    langArrayAdapter.add(l);
                }
            }
            spinFrom.setSelection(0); // stupid
            spinTo.setSelection(1); // very stupid
            spinFrom.setOnItemSelectedListener(new LangSpinnerItemSelectedListener());
            spinTo.setOnItemSelectedListener(new LangSpinnerItemSelectedListener());
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
            indirect = ((Lang) spinTo.getSelectedItem()).code + "-" + ((Lang) spinFrom.getSelectedItem()).code;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}
