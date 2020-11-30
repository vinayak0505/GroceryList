package com.example.groceryapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.List;

public class ListAdapter extends ArrayAdapter<ShoppingList> {
    Context context;

    public ListAdapter(@NonNull Context context, int resource, @NonNull List<ShoppingList> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_list, parent, false);
        }
        TextView listName = convertView.findViewById(R.id.adapter_list_name);
        final ShoppingList shoppingList = getItem(position);
        assert shoppingList != null;
        listName.setText(shoppingList.getListName());
        TextView listText = convertView.findViewById(R.id.adapter_list_count);
        listText.setText(String.format("%s Products", shoppingList.getListItemCount()));
        convertView.findViewById(R.id.list_adapter_main_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("listName", shoppingList.getListName());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
