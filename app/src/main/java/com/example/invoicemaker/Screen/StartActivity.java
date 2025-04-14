package com.example.invoicemaker.Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.invoicemaker.MainActivity;
import com.example.invoicemaker.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void viewInvoice(View view){
        startActivity(new Intent(this, InvoiceActivity.class));
    }

    public void viewItem(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

}