package com.example.test3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CarListFragment extends Fragment {

    private RecyclerView recyclerView;
    private CarAdapter carAdapter;
    private ArrayList<ProductCar> carList = new ArrayList<>();
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        db = FirebaseFirestore.getInstance();

        loadCarsFromFirebase();

        return view;
    }

    private void loadCarsFromFirebase() {
        db.collection("items").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                carList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    ProductCar car = document.toObject(ProductCar.class);
                    carList.add(car);
                }
                // Initialize and set adapter for RecyclerView
                carAdapter = new CarAdapter(requireContext(), carList);
                recyclerView.setAdapter(carAdapter);

                // Handle item click events in RecyclerView
                carAdapter.setOnItemClickListener(new CarAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        // Get the clicked car object
                        ProductCar car = carList.get(position);
                        // Navigate to EditCarFragment and pass car information
                        navigateToEditCarFragment(car);
                    }

                    @Override
                    public void onEditClick(int position) {
                        // Custom logic when Edit button is clicked
                        ProductCar car = carList.get(position);
                        navigateToEditCarFragment(car);
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        // Custom logic when Delete button is clicked
                        showDeleteConfirmationDialog(position);
                    }
                });
            }
        });
    }

    // Method to navigate to EditCarFragment and pass car information
    private void navigateToEditCarFragment(ProductCar car) {
        EditCarFragment editCarFragment = new EditCarFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("car", car); // Pass ProductCar object via bundle
        editCarFragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editCarFragment)
                .addToBackStack(null)
                .commit();
    }

    // Method to show delete confirmation dialog
    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to delete this car?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCar(position);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Method to delete a car from the list
    private void deleteCar(int position) {
        // Delete car from Firestore
        ProductCar car = carList.get(position);


        db.collection("items").document(car.getDocumentId()).delete()
                .addOnSuccessListener(aVoid -> {
                    // Delete successful, update car list
                    carList.remove(position);
                    carAdapter.notifyItemRemoved(position);
                    Toast.makeText(requireContext(), "Car deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Error occurred while deleting
                    Toast.makeText(requireContext(), "Failed to delete car: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}



