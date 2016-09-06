package com.example.ashwin.customautocompletetextviewadapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView mCountries;
    private Button mClear;
    private TextView mSelectedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCountries = (AutoCompleteTextView) findViewById(R.id.countries);

        mClear = (Button) findViewById(R.id.clear);
        mClear.setVisibility(View.GONE);

        mSelectedCountry = (TextView) findViewById(R.id.selectedCountry);

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearText();
            }
        });

        String[] countries  = new String[]{"Switzerland", "Mexico", "Poland", "United States of Murica"};

        CustomAutoCompleteAdapter adapter = new CustomAutoCompleteAdapter(this, countries);

        mCountries.setThreshold(0);

        mCountries.setAdapter(adapter);

        mCountries.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus)
                    mCountries.showDropDown();
            }
        });

        mCountries.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                mCountries.showDropDown();
                return false;
            }
        });

        mCountries.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //do nothing
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    mClear.setVisibility(View.VISIBLE);
                    mCountries.showDropDown();
                } else {
                    mClear.setVisibility(View.GONE);
                }
            }
        });

        mCountries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String countryName = (String) arg0.getItemAtPosition(arg2);
                mSelectedCountry.setText(countryName);
            }
        });

    }

    private void clearText() {
        mCountries.setText("");
        mClear.setVisibility(View.GONE);
    }
}
