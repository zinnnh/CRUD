package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;



public class MainActivity extends AppCompatActivity {

    private ProductCar car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mở fragment mặc định khi MainActivity được tạo
        navigateToCarInfoFragment();
    }

    public void navigateToCarInfoFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new CarInfoFragment())
                .commit();
    }

    public void navigateToUploadImageFragment(ProductCar car) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, UploadImageFragment.newInstance(car))
                .addToBackStack(null)
                .commit();
    }

    public void navigateToCarListFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new CarListFragment())
                .commit();
    }

    public ProductCar getCar() {
        if (car == null) {
            car = new ProductCar();
        }
        return car;
    }

    public void setCar(ProductCar car) {
        this.car = car;
    }
}
