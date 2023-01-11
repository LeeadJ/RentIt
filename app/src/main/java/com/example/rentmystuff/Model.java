package com.example.rentmystuff;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Observable;

public class Model extends Observable {

    protected FirebaseFirestore db;
    protected FirebaseAuth auth;
    protected StorageReference storageReference; // reference to the storage in firebase.
    protected String imageURL;

    public Model() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("images");

    }

    public void signOut() {
        auth.signOut();
    }

    /**
     * This function allows the user to upload an image to the app.
     * It checks the imageUri and uploads it to the firebase storage.
     * If the check fails, a message is displayed to the user asking him to select image.
     * Otherwise the function uploads the image to the firebase storage and saves the link as a variable.
     * @param imageUri
     * @param image_name
     */
    public void uploadImage(Uri imageUri, String image_name) {

        //Uploading the image to the firebase storage:
        UploadTask uploadTask = storageReference.child(image_name).putFile(imageUri);

        //If upload is successful, the link will be copied to the imageURL variable:
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                setChanged();
                notifyObservers("Image Uploaded Successfully");
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                        new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                imageURL = task.getResult().toString();
                            }
                        });
            }
        });
    }

}
