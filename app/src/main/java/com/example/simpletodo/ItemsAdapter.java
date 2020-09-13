package com.example.simpletodo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {


    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    public interface OnClickListener {
        void onItemClicked(View v, int position);
    }


    List<String> items;
    List<String> dates;
    OnClickListener clickListener;
    OnLongClickListener longClickListener;

    public ItemsAdapter(List<String> items, List<String> dates, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items;
        this.dates = dates;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    public void updateDates(List<String> dates) {
        this.dates = dates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // Use layout inflater to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        // Wrap it inside a view holder and return it
        return new ViewHolder(todoView);
     }

    @Override
    // Responsible for binding data to a particular view holder
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // Grab the item at the position
        String item = items.get(position);
        String date = dates.get(position);
        viewHolder.dateItem.setText("");
        // Bind the item into the specified view holder
        viewHolder.bind(item, date);


        // set touchlistener for clock item
//        viewHolder.timeItem.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.setSelected(event.getAction() == MotionEvent.ACTION_DOWN);
//                Log.d("ItemsAdapter", "touched");
//                return false;
//            }
//        });
    }



    @Override
    // Tells the RV how many items are in the list
    public int getItemCount() {
        return items.size();
    }


    // Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;
        ImageView timeItem;
        TextView dateItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.text1);
            timeItem = itemView.findViewById(R.id.time);
            dateItem = itemView.findViewById(R.id.date);
        }

        // Update the view inside the view holder with this data
        @SuppressLint("ClickableViewAccessibility")
        public void bind(String item, String date) {
            if (! date.equals("none")) {
                dateItem.setText(date);
            }
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(v, getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Notify the listener which position was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
            dateItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });

//            timeItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    v.setSelected(true);
//                    clickListener.onItemClicked(v, getAdapterPosition());
//                }
//            });

            timeItem.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.setSelected(event.getAction() == MotionEvent.ACTION_DOWN);
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        clickListener.onItemClicked(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }
}
