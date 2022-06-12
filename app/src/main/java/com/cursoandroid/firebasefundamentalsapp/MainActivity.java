package com.cursoandroid.firebasefundamentalsapp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference users = reference.child("users");
    private DatabaseReference products = reference.child("products");
    private FirebaseAuth user = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createUser();
        //createProduct();
        //recoveringData();
        //registerUserWithEmail();
        //signInUser();
        //signOutUser();
        //isUserLoggedIn();
    }

    private void createUser() {
        User user = new User();

        user.setName("Maria Eduarda");
        user.setSurname("Silva");
        user.setAge(25);

        users.push().setValue(user);
    }

    private void createProduct() {
        Product product = new Product();

        product.setDescription("Acer Aspire");
        product.setBrand("Acer");
        product.setPrice(998.99);

        products.child("002").setValue(product);
    }

    private void recoveringData() {
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("FIREBASE_DATA", dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void registerUserWithEmail() {
        user.createUserWithEmailAndPassword("andresantos@gmail.com", "as12345")
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("REGISTER_USER", "User registered successfully");
                        } else {
                            Log.i("REGISTER_USER", "Unable to register user");
                        }
                    }
                });
    }

    private void isUserLoggedIn() {
        if (user.getCurrentUser() != null) {
            Log.i("LOGGED_IN", "User is logged-in");
        } else {
            Log.i("LOGGED_OUT", "User is logged-out");
        }
    }

    private void signOutUser() {
        user.signOut();
    }

    private void signInUser() {
        user.signInWithEmailAndPassword("andresantos@gmail.com", "as12345")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("SIGN_IN", "User signed in successfully");
                        } else {
                            Log.i("SIGN_IN_ERROR", "Error: Unable to sign in user");
                        }
                    }
                });
    }
}
