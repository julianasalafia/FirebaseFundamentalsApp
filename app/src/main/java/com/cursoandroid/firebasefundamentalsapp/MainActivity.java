package com.cursoandroid.firebasefundamentalsapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference users = reference.child("users");
    private DatabaseReference products = reference.child("products");
    private FirebaseAuth user = FirebaseAuth.getInstance();

    private ImageView imagePicture;
    private Button buttonUpload;
    private Button buttonDelete;
    private Button buttonDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagePicture = findViewById(R.id.imagePicture);
        buttonUpload = findViewById(R.id.buttonUpload);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonDownload = findViewById(R.id.buttonDownload);

        //createUser();
        //createProduct();
        //recoveringData();
        //registerUserWithEmail();
        //signInUser();
        //signOutUser();
        //isUserLoggedIn();
        //filterUser();
        //imageUpload();
        deletingImages();
        downloadingImages();
    }

    private void createUser() {
        User user = new User();

        user.setName("Rodrigo");
        user.setSurname("Matos");
        user.setAge(35);

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

    private void filterUser() {
        //DatabaseReference userSearch = users.child("-N4NbXD9JgGcKcgyfGbx");
        //Query userSearch = users.orderByChild("name").equalTo("Marcelo");
        //Query userSearch = users.orderByKey().limitToFirst(2);
        //Query userSearch = users.orderByChild("age").startAt(30);
        //Query userSearch = users.orderByChild("age").endAt(22);
        //Query userSearch = users.orderByChild("age").startAt(18).endAt(22);
        Query userSearch = users.orderByChild("name").startAt("J").endAt("J" + "\uf8ff");

        userSearch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
/*              User userData = dataSnapshot.getValue(User.class);
                Log.i("User data", "name: " + userData.getName());*/
                Log.i("User data", dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void imageUpload() {
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicture.setDrawingCacheEnabled(true);
                imagePicture.buildDrawingCache();

                Bitmap bitmap = imagePicture.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);

                byte[] imageData = baos.toByteArray();

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference images = storageReference.child("images");

                String fileName = UUID.randomUUID().toString();
                StorageReference imageRef = images.child(fileName + ".jpeg");

                UploadTask uploadTask = imageRef.putBytes(imageData);
                uploadTask.addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,
                                "Upload failed: " + e.getMessage().toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Uri url = taskSnapshot.getDownloadUrl();
                        Toast.makeText(MainActivity.this,
                                "Upload done successfully: " + url.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void deletingImages() {
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference images = storageReference.child("images");
                StorageReference imageRef = images.child("106f5626-efb4-4729-9c0b-454be958eb3b.jpeg");

                imageRef.delete().addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,
                                "Unable to delete",
                                Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(MainActivity.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,
                                "Image deleted",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void downloadingImages() {
        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference images = storageReference.child("images");
                StorageReference imageRef = images.child("106f5626-efb4-4729-9c0b-454be958eb3b.jpeg");

                Glide.with(MainActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(imageRef)
                        .into(imagePicture);
            }
        });
    }
}
