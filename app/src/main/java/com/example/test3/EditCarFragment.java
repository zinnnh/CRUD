package com.example.test3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditCarFragment extends Fragment {

    private EditText editTextLicensePlate, editTextBrand, editTextModel, editTextSeats;
    private Spinner spinnerTransmission, spinnerFuelType;
    private Button buttonSave;
    private ProductCar car;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_car, container, false);

        editTextLicensePlate = view.findViewById(R.id.editTextLicensePlate);
        editTextBrand = view.findViewById(R.id.editTextBrand);
        editTextModel = view.findViewById(R.id.editTextModel);
        editTextSeats = view.findViewById(R.id.editTextSeats);
        spinnerTransmission = view.findViewById(R.id.spinnerTransmission);
        spinnerFuelType = view.findViewById(R.id.spinnerFuelType);
        buttonSave = view.findViewById(R.id.buttonSave);

        ArrayAdapter<CharSequence> transmissionAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.transmission_options, android.R.layout.simple_spinner_item);
        transmissionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTransmission.setAdapter(transmissionAdapter);

        ArrayAdapter<CharSequence> fuelTypeAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.fuel_type_options, android.R.layout.simple_spinner_item);
        fuelTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFuelType.setAdapter(fuelTypeAdapter);

        db = FirebaseFirestore.getInstance();

        Bundle bundle = getArguments();
        if (bundle != null) {
            car = (ProductCar) bundle.getSerializable("car");
            if (car != null) {
                editTextLicensePlate.setText(car.getLicensePlate());
                editTextBrand.setText(car.getBrand());
                editTextModel.setText(car.getModel());
                editTextSeats.setText(String.valueOf(car.getSeats()));
                // Set selected item for spinners
                int transmissionPosition = transmissionAdapter.getPosition(car.getTransmission());
                spinnerTransmission.setSelection(transmissionPosition);
                int fuelTypePosition = fuelTypeAdapter.getPosition(car.getFuelType());
                spinnerFuelType.setSelection(fuelTypePosition);
            }
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditedCarInfo();
            }
        });

        return view;
    }

    private void saveEditedCarInfo() {
        String licensePlate = editTextLicensePlate.getText().toString().trim();
        String brand = editTextBrand.getText().toString().trim();
        String model = editTextModel.getText().toString().trim();
        int seats = Integer.parseInt(editTextSeats.getText().toString().trim());
        String transmission = spinnerTransmission.getSelectedItem().toString();
        String fuelType = spinnerFuelType.getSelectedItem().toString();

        // Cập nhật thông tin vào đối tượng car
        car.setLicensePlate(licensePlate);
        car.setBrand(brand);
        car.setModel(model);
        car.setSeats(seats);
        car.setTransmission(transmission);
        car.setFuelType(fuelType);

        // Lưu thông tin đã chỉnh sửa vào Firestore
        db.collection("items").document(car.getDocumentId())
                .set(car)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireContext(), "Car info updated successfully", Toast.LENGTH_SHORT).show();
                    // Quay lại màn hình trước đó
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to update car info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

