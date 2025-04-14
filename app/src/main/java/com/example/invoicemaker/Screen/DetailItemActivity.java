package com.example.invoicemaker.Screen;

import static com.example.invoicemaker.MainActivity.itemArrayList;
import static com.example.invoicemaker.Utils.Constant.getCartData;
import static com.example.invoicemaker.Utils.Constant.saveCartData;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.invoicemaker.Model.Item;
import com.example.invoicemaker.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class DetailItemActivity extends AppCompatActivity {
    private EditText et_item_name,et_item_price,et_item_quantity;

    private Dialog loadingDialog;
    int index=0;
    String itemId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);
        index=getIntent().getIntExtra("index",-1);
        et_item_name=findViewById(R.id.et_item_name);
        et_item_quantity=findViewById(R.id.et_item_quantity);
        et_item_price=findViewById(R.id.et_item_price);

        //loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onStart() {
        et_item_name.setText(itemArrayList.get(index).getName());
        et_item_price.setText(itemArrayList.get(index).getPrice());
        itemId=itemArrayList.get(index).getItemId();
        super.onStart();
    }

    public void addToCart(View view){
        if(et_item_quantity.getText().toString().isEmpty()){
            et_item_quantity.setError("required");
        } else {
            if(Integer.parseInt(et_item_quantity.getText().toString())<=0){
                Toast.makeText(DetailItemActivity.this,"quantity must be greater then zero",Toast.LENGTH_LONG).show();
            } else {
                int total =Integer.parseInt(et_item_quantity.getText().toString())*Integer.parseInt(et_item_price.getText().toString());
                ArrayList<Item> itemArrayList=getCartData(DetailItemActivity.this);

                itemArrayList.add(new Item(et_item_name.getText().toString(),et_item_price.getText().toString(),
                        total+"",et_item_quantity.getText().toString()
                        ,itemId));
                saveCartData(this,itemArrayList);
                Toast.makeText(DetailItemActivity.this,"item added to cart",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

}