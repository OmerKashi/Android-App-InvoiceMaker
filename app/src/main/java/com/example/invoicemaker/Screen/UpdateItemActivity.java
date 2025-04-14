package com.example.invoicemaker.Screen;

import static com.example.invoicemaker.MainActivity.itemArrayList;

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

public class UpdateItemActivity extends AppCompatActivity {

    private EditText et_item_name,et_item_price;
    DatabaseReference myRef;
    private Dialog loadingDialog;
    int index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);
        index=getIntent().getIntExtra("index",-1);
        et_item_name=findViewById(R.id.et_item_name);
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
        super.onStart();
    }

    public void updateRecord(View view){
        try {
            loadingDialog.show();
            myRef=  FirebaseDatabase.getInstance().getReference("Products").child(itemArrayList.get(index).getItemId());
            myRef.child("Name").setValue(et_item_name.getText().toString());
            myRef.child("Price").setValue(et_item_price.getText().toString());
            loadingDialog.dismiss();
            Toast.makeText(UpdateItemActivity.this,"product updated successfully",Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}