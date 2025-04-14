package com.example.invoicemaker.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.invoicemaker.Model.Invoice;
import com.example.invoicemaker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InvoiceActivity extends AppCompatActivity {

    public static ArrayList<Invoice> invoiceArrayList=new ArrayList<Invoice>();
    ArrayList<Invoice> invoiceArrayList1=new ArrayList<Invoice>();
    private Dialog loadingDialog;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    EditText search_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView=findViewById(R.id.recylerView);
        search_item=findViewById(R.id.search_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
                ArrayList<Invoice> filterlist=new ArrayList<>();
                for(Invoice item: invoiceArrayList1){
                    if(item.getDate().toLowerCase().contains(text.toLowerCase())
                    ||item.getId().toLowerCase().contains(text.toLowerCase())){
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
        invoiceArrayList1.clear();
        DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("Invoices");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    invoiceArrayList1.add(new Invoice(dataSnapshot1.child("InvoiceId").getValue(String.class)
                    ,dataSnapshot1.child("Date").getValue(String.class)));
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

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ImageViewHoler> {

        public ItemAdapter(){
            invoiceArrayList=invoiceArrayList1;
        }
        @NonNull
        @Override
        public ItemAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(InvoiceActivity.this).inflate(R.layout.item_invoice,parent,false);
            return  new ItemAdapter.ImageViewHoler(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {
            holder.invoice_id.setText("Invoice ID "+invoiceArrayList.get(position).getId());
            holder.invoice_date.setText("Invoice Date "+invoiceArrayList.get(position).getDate());
            holder.cardView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                                 startActivity(new Intent(InvoiceActivity.this,InvoiceDetailActivity.class)
                                         .putExtra("id",invoiceArrayList.get(position).getId())
                                         .putExtra("index",position));
                }
            });
        }

        public void filteredList(ArrayList<Invoice> filterlist) {
            invoiceArrayList=filterlist;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return invoiceArrayList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {
            TextView invoice_id,invoice_date;
            CardView cardView;

            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                invoice_id=itemView.findViewById(R.id.invoice_id);
                invoice_date=itemView.findViewById(R.id.invoice_date);
                cardView=itemView.findViewById(R.id.card);
            }
        }
    }
}