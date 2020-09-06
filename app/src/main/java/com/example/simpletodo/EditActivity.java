package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText etItem;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItem = findViewById(R.id.etItem);
        btnSave = findViewById(R.id.btnSave);

        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // let user already be in edit mode with keyboard up
        etItem.requestFocus();
        etItem.setFocusableInTouchMode(true);
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//        imm.showSoftInput(etItem, InputMethodManager.SHOW_FORCED);

        // When the user is done editing, they click the save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create an intent which will contain the results
                Intent intent = new Intent();
                // pass the data (results of editing)
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, etItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                // set the result of the intent
                setResult(RESULT_OK, intent);

                //hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // finish activity, clear the screen and go back
                finish();
            }
        });
    }
}