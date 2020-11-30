package com.example.groceryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Product> {
    public ItemAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item, parent, false);
        }
        TextView Name = convertView.findViewById(R.id.list_item_name),
                Price = convertView.findViewById(R.id.list_item_price),
                Quantity = convertView.findViewById(R.id.list_item_quantity);
        ImageView delete = convertView.findViewById(R.id.list_delete),
                edit = convertView.findViewById(R.id.list_edit),
                editUpload = convertView.findViewById(R.id.edit_upload_button);
        final Product product = getItem(position);
        if (product != null) {
            Name.setText(product.getName());
            Price.setText(product.getPrice());
            Quantity.setText(product.getQuantity());
        }
        final View finalConvertView = convertView;
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editClickListener(finalConvertView);
            }
        });
        editUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUploadClickListener(finalConvertView,product);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("list")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(product.getListName())
                        .child(product.getItemId())
                        .removeValue();
                Toast.makeText(getContext(),"Item Deleted",Toast.LENGTH_SHORT).show();

            }
        });
        return convertView;
    }

    private void editClickListener(View view) {
        TextView textView = view.findViewById(R.id.list_item_name);
        EditText editText = view.findViewById(R.id.edit_add_name);
        editText.setText(textView.getText().toString());
        textView = view.findViewById(R.id.list_item_price);
        editText = view.findViewById(R.id.edit_add_prise);
        editText.setText(textView.getText().toString());
        textView = view.findViewById(R.id.list_item_quantity);
        editText = view.findViewById(R.id.edit_add_quantity);
        editText.setText(textView.getText().toString());
        view.findViewById(R.id.list_adapter_main_view).setVisibility(View.GONE);
        view.findViewById(R.id.item_adapter_edit_view).setVisibility(View.VISIBLE);
    }

    private void editUploadClickListener(View view,Product oldProduct) {
        EditText editText = view.findViewById(R.id.edit_add_name);
        Product product = new Product();
        product.setName(editText.getText().toString());
        editText = view.findViewById(R.id.edit_add_prise);
        product.setPrice(editText.getText().toString());
        editText = view.findViewById(R.id.edit_add_quantity);
        product.setQuantity(editText.getText().toString());
        FirebaseDatabase.getInstance().getReference("list")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(oldProduct.getListName())
                .child(oldProduct.getItemId())
                .setValue(product);
        view.findViewById(R.id.list_adapter_main_view).setVisibility(View.VISIBLE);
        view.findViewById(R.id.item_adapter_edit_view).setVisibility(View.GONE);
        Toast.makeText(getContext(),"Item Updated",Toast.LENGTH_SHORT).show();
    }
}