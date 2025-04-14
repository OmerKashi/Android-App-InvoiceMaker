package com.example.invoicemaker.Screen;

import static com.example.invoicemaker.Screen.CartActivity.itemArrayList;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InvoiceUserDetailActivity extends AppCompatActivity {

    public ArrayList<Item> useritemArrayList=new ArrayList<Item>();
    ItemAdapter itemAdapter;
    RecyclerView recyclerView;
    private Dialog loadingDialog;
    TextView total_price;
    int totalPrice=0;
    TextView invoice_id,invoice_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_user_detail);
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
        useritemArrayList.clear();
        String id=getIntent().getStringExtra("id");
        invoice_date.setText(getCurrentDate());
        invoice_id.setText(id);
        for(int i = 0 ; i < itemArrayList.size();i++) {
            totalPrice=totalPrice+Integer.parseInt(itemArrayList.get(i).getTotalPrice());
        }
        itemAdapter=new ItemAdapter();
        recyclerView.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
        loadingDialog.dismiss();
        total_price.setText("Total Price : "+totalPrice+" $");
    }

    public String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return  formattedDate;
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ImageViewHolder> {

        public ItemAdapter(){
            useritemArrayList=itemArrayList;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(InvoiceUserDetailActivity.this).inflate(R.layout.item_list2,parent,false);
            return  new ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.name.setText(useritemArrayList.get(position).getName());
            holder.quantity.setText(useritemArrayList.get(position).getTotalQuantity());
            holder.price.setText(useritemArrayList.get(position).getPrice()+" $");
            holder.final_price.setText(useritemArrayList.get(position).getTotalPrice()+" $");
        }

        @Override
        public int getItemCount() {
            return useritemArrayList.size();
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
}