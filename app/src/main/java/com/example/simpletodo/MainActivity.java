package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.*;
import java.io.*;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;


    List<String> items;
    List<String> dates;

    RelativeLayout plusButton;
    Button addButton;
    EditText editItem;
    RecyclerView listItem;

    ItemsAdapter itemsAdapter;

    DatePicker datePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plusButton = findViewById(R.id.adBtn);
        addButton = findViewById(R.id.addButton);
        editItem = findViewById(R.id.editItem);
        listItem = findViewById(R.id.listItem);

        getPrefs();


        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model
                items.remove(position);
                dates.remove(position);
                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                savePrefs();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(View v, int position) { // take to date activity
                if (v.getId() == R.id.time) {
                    final int index = position;
                    final RelativeLayout myLayout = findViewById(R.id.main);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    //inflate datepicker
                    datePicker = (DatePicker) inflater.inflate(R.layout.date_picker, null);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, R.id.frameLyt);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    datePicker.setLayoutParams(params);
                    myLayout.addView(datePicker); // add datePicker to view

                    // set add button
                    final Button setDateButton = (Button) inflater.inflate(R.layout.set_date_button, null);
                    params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, R.id.datePicker);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    setDateButton.setLayoutParams(params);
                    myLayout.addView(setDateButton);

                    setDateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StringBuilder dateText = new StringBuilder();
                            dateText.append((datePicker.getMonth() + 1)+"/");
                            dateText.append(datePicker.getDayOfMonth()+"/");
                            dateText.append(datePicker.getYear());

                            ItemsAdapter.ViewHolder holder = (ItemsAdapter.ViewHolder) listItem.findViewHolderForLayoutPosition(index);
                            holder.dateItem.setText(dateText.toString());

                            myLayout.removeView(datePicker);
                            myLayout.removeView(setDateButton);
                            dates.set(index, dateText.toString());
                            savePrefs();
                        }
                    });
                }
                else if (v.getId() == R.id.text1) {
                    // create the new activity
                    Intent i = new Intent(MainActivity.this, EditActivity.class);
                    // pass the data being edited
                    i.putExtra(KEY_ITEM_TEXT, items.get(position));
                    i.putExtra(KEY_ITEM_POSITION, position);
                    // display the activity
                    startActivityForResult(i, EDIT_TEXT_CODE);
                }
            }
        };



        itemsAdapter = new ItemsAdapter(items, dates, onLongClickListener, onClickListener);
        listItem.setAdapter(itemsAdapter);
        listItem.setLayoutManager(new LinearLayoutManager(this));


        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItem.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.VISIBLE);
                editItem.requestFocus();
                editItem.setFocusableInTouchMode(true);
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editItem, InputMethodManager.SHOW_IMPLICIT);
                plusButton.setVisibility(View.INVISIBLE);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = editItem.getText().toString();
                // check if empty
                if (TextUtils.isEmpty(todoItem)) {
                    Toast.makeText(getApplicationContext(), "Cannot add an empty item", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Add item to model
                items.add(todoItem);
                dates.add("none"); // set default empty date
                // Notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                editItem.setText("");

                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                savePrefs();

                editItem.setVisibility(View.GONE);
                addButton.setVisibility(View.GONE);
                hideKeyboard(editItem);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retrieve the updated text value
            String item = data.getStringExtra(KEY_ITEM_TEXT);
            // Extract the original position
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            // Update the model with the new item text
            items.set(position, item);
            // Notify the adapter
            itemsAdapter.notifyItemChanged(position);
            // Persist the changes
            savePrefs();
            Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private SharedPreferences getSharedPrefs() {
        return getPreferences(Context.MODE_PRIVATE);
    }

    private void savePrefs() {
        SharedPreferences.Editor editor = getSharedPrefs().edit();
        editor.clear();
        editor.apply();

        for (int i = 0; i < items.size(); i++) {
            String date;

            date = dates.get(i);

            editor.putString(items.get(i), date);
            editor.apply();
        }
        itemsAdapter.updateDates(dates);
    }

    private void getPrefs() {
        SharedPreferences sharedPrefs = getSharedPrefs();
        items = new ArrayList<>();
        dates = new ArrayList<>();
        for (String item : sharedPrefs.getAll().keySet()) {
            items.add(item);
            dates.add(sharedPrefs.getString(item, "none"));
        }
    }




//    private File getDataFile() {
//        return new File(getFilesDir(), "data.txt");
//    }
//
//    // This function will load functions by reading every line of the data file
//    private void loadItems() {
//        try {
//            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
//        } catch (IOException e) {
//            Log.e("MainActivity", "error reading items", e);
//            items = new ArrayList<>();
//        }
//    }
//
//    // This function saves items by writing them into the data file
//    private void saveItems() {
//        try {
//            FileUtils.writeLines(getDataFile(), items);
//        } catch (IOException e) {
//            Log.e("MainActivity", "error writing items", e);
//        }
//    }

    @Override
    public void onBackPressed() {
        editItem.setVisibility(View.GONE);
        addButton.setVisibility(View.GONE);
        plusButton.setVisibility(View.VISIBLE);
        editItem.setText("");
    }
    

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        plusButton.setVisibility(View.VISIBLE);
    }


}