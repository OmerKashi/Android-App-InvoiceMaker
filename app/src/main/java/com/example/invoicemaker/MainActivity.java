package com.example.invoicemaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.invoicemaker.Model.Item;
import com.example.invoicemaker.Screen.AddItemActivity;
import com.example.invoicemaker.Screen.CartActivity;
import com.example.invoicemaker.Screen.DetailItemActivity;
import com.example.invoicemaker.Screen.UpdateItemActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Item> itemArrayList=new ArrayList<Item>();
    ArrayList<Item> itemArrayLis1=new ArrayList<Item>();
    private Dialog loadingDialog;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    DatabaseReference databaseReference;
    EditText search_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        search_item=findViewById(R.id.search_item);
        recyclerView=findViewById(R.id.recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        search_item.addTextChangedListener(new TextWatcher() {

         @Override
         public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

         @Override
         public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

         @Override
         public void afterTextChanged(Editable editable) {
             filter(editable.toString());
         }

         private void filter(String text) {
             ArrayList<Item> filterlist=new ArrayList<>();

             for(Item item: itemArrayLis1){
                 if(item.getName().toLowerCase().contains(text.toLowerCase())){
                     filterlist.add(item);
                 }
             }
             itemAdapter.filteredList(filterlist);
         }
     });
    }

    @Override
    protected void onStart() {
        getData();
        super.onStart();
    }

    public void getData(){
        loadingDialog.show();
        itemArrayList.clear();
        itemArrayLis1.clear();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    itemArrayLis1.add(new Item(
                            dataSnapshot1.child("Name").getValue(String.class)
                            , dataSnapshot1.child("Price").getValue(String.class)
                            , dataSnapshot1.child("ItemId").getValue(String.class)
                    ));

                }

                itemAdapter=new ItemAdapter();
                recyclerView.setAdapter(itemAdapter);
                itemAdapter.notifyDataSetChanged();
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.add_item:
                   startActivity(new Intent(MainActivity.this, AddItemActivity.class));
                return true;
                case R.id.view_cart:
                    startActivity(new Intent(MainActivity.this, CartActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ImageViewHoler> {

        public ItemAdapter(){
            itemArrayList=  itemArrayLis1;
        }

        @NonNull
        @Override
        public ItemAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(MainActivity.this).inflate(R.layout.item_list,parent,false);
            return  new ItemAdapter.ImageViewHoler(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {
            holder.name.setText("Name :"+itemArrayList.get(position).getName());
            holder.price.setText("Price :"+itemArrayList.get(position).getPrice()+" $");
             holder.item_delete.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                     builder.setTitle("Delete Alert");
                     builder.setIcon(android.R.drawable.ic_dialog_alert);
                     builder.setMessage("Are yor sure?")
                             .setCancelable(false)
                             .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                     DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference().child("Products").child(itemArrayList.get(position).getItemId());
                                     myRef.removeValue();
                                     getData();
                                 }
                             })
                             .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                     dialog.cancel();
                                 }
                             });
                     AlertDialog alert = builder.create();
                     alert.show();

                 }
             });
             holder.item_edit.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     startActivity(new Intent(MainActivity.this, UpdateItemActivity.class)
                             .putExtra("index",position));
                 }
             });
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, DetailItemActivity.class)
                            .putExtra("index",position));
                }
            });
        }

        public void filteredList(ArrayList<Item> filterlist) {
            itemArrayList=filterlist;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return itemArrayList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {
            TextView name,price;
            ImageView item_delete,item_edit;
            CardView cardView;

            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.name);
                price=itemView.findViewById(R.id.price);
                item_delete=itemView.findViewById(R.id.item_delete);
                item_edit=itemView.findViewById(R.id.item_edit);
                cardView=itemView.findViewById(R.id.card);
            }
        }
    }

}