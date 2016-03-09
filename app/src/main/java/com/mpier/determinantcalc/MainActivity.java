package com.mpier.determinantcalc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

public class MainActivity extends AppCompatActivity {

    Button mGoButton;
    NumberPicker mNumberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNumberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        mNumberPicker.setMinValue(2);// restricted number to minimum value i.e 1
        mNumberPicker.setMaxValue(30);// restricked number to maximum value i.e. 31
        mNumberPicker.setWrapSelectorWheel(true);
        mNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mGoButton = (Button) findViewById(R.id.submit_button);
        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalcActivity.class);
                intent.putExtra("number", mNumberPicker.getValue());
                startActivity(intent);
            }
        });
    }

}
