package com.example.invoicemaker.Screen;

import static com.example.invoicemaker.Utils.Constant.getCartData;
import static com.example.invoicemaker.Utils.Constant.saveCartData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.invoicemaker.Model.Item;
import com.example.invoicemaker.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CartActivity extends AppCompatActivity {
    public static   ArrayList<Item> itemArrayList=new ArrayList<Item>();
    ItemAdapter itemAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
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
        itemAdapter=new ItemAdapter();
        recyclerView.setAdapter(itemAdapter);
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ImageViewHolder> {

        public ItemAdapter(){
            itemArrayList=  getCartData(CartActivity.this);
        }
        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(CartActivity.this).inflate(R.layout.item_list1,parent,false);
            return  new ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.name.setText("Name :"+itemArrayList.get(position).getName());
            holder.quantity.setText("Total Quantity :"+itemArrayList.get(position).getTotalQuantity());
            holder.price.setText("Price :"+itemArrayList.get(position).getPrice()+" $");
            holder.final_price.setText("Total Price :"+itemArrayList.get(position).getTotalPrice()+" $");
        }

        @Override
        public int getItemCount() {
            return itemArrayList.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {
            TextView name,price,quantity,final_price;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.name);
                price=itemView.findViewById(R.id.price);
                quantity=itemView.findViewById(R.id.quantity);
                final_price=itemView.findViewById(R.id.total_price);
            }
        }
    }

    public void createInvoice(View view){
        if(itemArrayList.size()==0){
            Toast.makeText(CartActivity.this,"your cart list is empty",Toast.LENGTH_LONG).show();
        } else {
            try {
                String id =createFavId().substring(0,8);
                DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("Invoices").child(id);
                myRef.child("Data").setValue(itemArrayList);
                myRef.child("InvoiceId").setValue(id);
                myRef.child("Date").setValue(getCurrentDate());
                saveCartData(CartActivity.this,null);
                startActivity(new Intent(CartActivity.this,InvoiceUserDetailActivity.class)
                        .putExtra("id",id));
                Toast.makeText(CartActivity.this,"invoice created",Toast.LENGTH_LONG).show();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return  formattedDate;
    }

    public String createFavId() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
}