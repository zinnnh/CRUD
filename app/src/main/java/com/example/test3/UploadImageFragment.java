package com.example.test3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class UploadImageFragment extends Fragment {

    private ImageView imageView;
    private Button selectImagesButton, saveButton;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ProductCar car;

    public static UploadImageFragment newInstance(ProductCar car) {
        UploadImageFragment fragment = new UploadImageFragment();
        Bundle args = new Bundle();
        args.putSerializable("car", car);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_image, container, false);

        imageView = view.findViewById(R.id.imageView);
        selectImagesButton = view.findViewById(R.id.selectImages);
        saveButton = view.findViewById(R.id.saveButton);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (getArguments() != null) {
            car = (ProductCar) getArguments().getSerializable("car");
        }

        selectImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        return view;
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(imageView);
        }
    }

    private void uploadImage() {
        if (imageUri != null) {
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(getActivity(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            car.setImageUrl(imageUrl);
                            // Lưu thông tin của xe vào Firestore
                            FirebaseFirestore.getInstance().collection("items").document(car.getDocumentId()).set(car)
                                    .addOnSuccessListener(aVoid -> {
                                        // Chuyển sang CarListFragment sau khi lưu thông tin thành công
                                        ((MainActivity) getActivity()).navigateToCarListFragment();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Xử lý khi lưu thất bại
                                        Toast.makeText(getActivity(), "Failed to save car: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
}
