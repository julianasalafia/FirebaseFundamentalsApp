package com.cursoandroid.firebasefundamentalsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //createUser();
        createProduct();
    }

    private void createUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference users = reference.child("users");

        User user = new User();
        user.setName("Denis");
        user.setSurname("Sato");
        user.setAge(22);

        users.child("002").setValue(user);
    }

    private void createProduct() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference products = reference.child("products");

        Product product = new Product();
        product.setDescription("Acer Aspire");
        product.setBrand("Acer");
        product.setPrice(999.99);

        products.child("002").setValue(product);
    }
}
