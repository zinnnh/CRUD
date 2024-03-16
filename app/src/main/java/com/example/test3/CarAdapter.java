package com.example.test3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ProductCar> carList;
    private OnItemClickListener listener;

    public CarAdapter(Context context, ArrayList<ProductCar> carList) {
        this.context = context;
        this.carList = carList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onEditClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.car_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductCar car = carList.get(position);
        holder.txtBrand.setText("Brand: " + car.getBrand());
        holder.txtModel.setText("Model: " + car.getModel());
        holder.txtLicensePlate.setText("License Plate: " + car.getLicensePlate());
        holder.txtSeats.setText("Seats: " + car.getSeats());
        holder.txtTransmission.setText("Transmission: " + car.getTransmission());
        holder.txtFuelType.setText("Fuel Type: " + car.getFuelType());
        Glide.with(context).load(car.getImageUrl()).into(holder.imgCar);
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCar;
        TextView txtBrand, txtModel, txtLicensePlate, txtSeats, txtTransmission, txtFuelType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCar = itemView.findViewById(R.id.imgCar);
            txtBrand = itemView.findViewById(R.id.txtBrand);
            txtModel = itemView.findViewById(R.id.txtModel);
            txtLicensePlate = itemView.findViewById(R.id.txtLicensePlate);
            txtSeats = itemView.findViewById(R.id.txtSeats);
            txtTransmission = itemView.findViewById(R.id.txtTransmission);
            txtFuelType = itemView.findViewById(R.id.txtFuelType);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            // Handle click event on Edit button
            itemView.findViewById(R.id.btnEdit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            // Handle click event on Delete button
            itemView.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}

