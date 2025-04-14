package com.example.invoicemaker.Screen;

import static com.example.invoicemaker.Screen.InvoiceActivity.invoiceArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.invoicemaker.Model.Item;
import com.example.invoicemaker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InvoiceDetailActivity extends AppCompatActivity {

    public ArrayList<Item> itemArrayList=new ArrayList<Item>();
     ItemAdapter itemAdapter;
    RecyclerView recyclerView;
    private Dialog loadingDialog;
    TextView total_price;
    int totalPrice=0;
    TextView invoice_id,invoice_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);
        invoice_date=findViewById(R.id.invoice_date);
        invoice_id=findViewById(R.id.invoice_id);
        total_price=findViewById(R.id.total_price);
        //loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onStart() {
        getData();
        super.onStart();
    }

    public void getData(){
        itemArrayList.clear();
        String id=getIntent().getStringExtra("id");
        int index=getIntent().getIntExtra("index",-1);
        invoice_date.setText(invoiceArrayList.get(index).getDate());
        invoice_id.setText(invoiceArrayList.get(index).getId());
        DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("Invoices").child(id).child("Data");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    totalPrice=totalPrice+Integer.parseInt(dataSnapshot1.child("totalPrice").getValue(String.class));
                    itemArrayList.add(new Item(
                            dataSnapshot1.child("name").getValue(String.class)
                            , dataSnapshot1.child("price").getValue(String.class)
                            , dataSnapshot1.child("totalPrice").getValue(String.class)
                            , dataSnapshot1.child("totalQuantity").getValue(String.class)
                            , dataSnapshot1.child("itemId").getValue(String.class)
                    ));
                }

                itemAdapter=new ItemAdapter();
                recyclerView.setAdapter(itemAdapter);
                itemAdapter.notifyDataSetChanged();
                loadingDialog.dismiss();
                total_price.setText("Total Price : "+totalPrice+" $");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ImageViewHoler> {

        public ItemAdapter(){ }

        @NonNull
        @Override
        public ItemAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(InvoiceDetailActivity.this).inflate(R.layout.item_list2,parent,false);
            return  new ItemAdapter.ImageViewHoler(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {
            holder.name.setText(itemArrayList.get(position).getName());
            holder.quantity.setText(itemArrayList.get(position).getTotalQuantity());
            holder.price.setText(itemArrayList.get(position).getPrice()+" $");
            holder.final_price.setText(itemArrayList.get(position).getTotalPrice()+" $");
        }

        @Override
        public int getItemCount() {
            return itemArrayList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {
            TextView name,price,quantity,final_price;

            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.name);
                price=itemView.findViewById(R.id.price);
                quantity=itemView.findViewById(R.id.quantity);
                final_price=itemView.findViewById(R.id.total_price);
            }
        }
    }
}