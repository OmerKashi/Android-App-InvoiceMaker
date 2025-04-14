package com.example.invoicemaker.Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.invoicemaker.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddItemActivity extends AppCompatActivity {
    private EditText et_item_name,et_item_price;

    DatabaseReference myRef;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        et_item_name=findViewById(R.id.et_item_name);
        et_item_price=findViewById(R.id.et_item_price);

        //loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void saveRecord(View view){
        try {
            if(et_item_name.getText().toString().isEmpty()){
                et_item_name.setError("required");
            } else  if(et_item_price.getText().toString().isEmpty()){
                et_item_price.setError("required");
            } else {
                loadingDialog.show();
                String id=createFavId().substring(0,8);
                myRef=  FirebaseDatabase.getInstance().getReference("Products").child(id);
                myRef.child("Name").setValue(et_item_name.getText().toString());
                myRef.child("ItemId").setValue(id);
                myRef.child("Price").setValue(et_item_price.getText().toString());
                loadingDialog.dismiss();
                Toast.makeText(AddItemActivity.this,"product added successfully",Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createFavId() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

}