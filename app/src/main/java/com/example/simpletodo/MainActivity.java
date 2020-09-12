package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.*;
import java.io.*;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;


    List<String> items;

    RelativeLayout plusButton;
    Button addButton;
    EditText editItem;
    RecyclerView listItem;

    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "test");

        plusButton = findViewById(R.id.adBtn);
        addButton = findViewById(R.id.addButton);
        editItem = findViewById(R.id.editItem);
        listItem = findViewById(R.id.listItem);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model
                items.remove(position);
                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "inside onitemclicked");
                // create the new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };



        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
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
                // Notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                editItem.setText("");

                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();

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
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // This function will load functions by reading every line of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "error reading items", e);
            items = new ArrayList<>();
        }
    }

    // This function saves items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "error writing items", e);
        }
    }

    @Override
    public void onBackPressed() {
        editItem.setVisibility(View.GONE);
        addButton.setVisibility(View.GONE);
    }
    

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        plusButton.setVisibility(View.VISIBLE);
    }
}