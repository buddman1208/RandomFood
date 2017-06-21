package moe.kotohana.randomfood;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.nhn.android.maps.NMapActivity;

import moe.kotohana.randomfood.databinding.ActivityNearFoodBinding;
import moe.kotohana.randomfood.databinding.ActivityNearFoodMapBinding;

public class NearFoodMapActivity extends AppCompatActivity  {

    ActivityNearFoodMapBinding binding;
    LinearLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_near_food_map);
        setSupportActionBar(binding.toolbar);
        container = binding.mapView;

    }
}
