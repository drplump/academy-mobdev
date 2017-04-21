package com.drplump.droid.test02;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.drplump.droid.test02.yapi.TranslateAPI;

public class TapToTranslateActivity extends AppCompatActivity {

    public final static int REQUEST_ID = 1;
    public final static String REQUEST_KEY = "com.drplump.droid.test02.translate.request";
    public final static String REQUEST_RESULT_KEY = "com.drplump.droid.test02.translate.result";

    private EditText textInput;
    private EditText textTranslated;

    private String langFrom;
    private String langTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_to_translate);

        langFrom = getIntent().getStringExtra(TranslateFragment.LANG_FROM_KEY);
        langTo = getIntent().getStringExtra(TranslateFragment.LANG_TO_KEY);

        textInput = (EditText) findViewById(R.id.text_input);
        textInput.requestFocus();
        textTranslated = (EditText) findViewById(R.id.text_translated);

        textInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    setResult(v);
                }
                return false;
            }
        });

        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //System.out.println(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //System.out.println(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0) {
                    textTranslated.setText(null);
                } else {
                    new TextTranslateTask().execute(textInput.getText().toString());
                }
            }
        });
    }

    public void goHome(View v) {
        setResult(Activity.RESULT_CANCELED, null);
        finish();
    }

    public void clearInput(View v) {
        textTranslated.setText(null);
        textInput.setText(null);
        textInput.requestFocus();
    }

    public void setResult(View v) {
        //return result to main activity
        Intent intent = new Intent();
        intent.putExtra(REQUEST_KEY, textInput.getText().toString());
        intent.putExtra(REQUEST_RESULT_KEY, textTranslated.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private class TextTranslateTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            TranslateAPI api = new TranslateAPI(getApplicationContext().getFilesDir());
            String result = "";
            try {
                result = api.translate(params[0], langFrom, langTo);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            textTranslated.setText(s);
        }
    }
}
