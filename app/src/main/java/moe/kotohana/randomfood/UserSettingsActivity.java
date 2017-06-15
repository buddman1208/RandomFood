package moe.kotohana.randomfood;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.intrusoft.scatter.ChartData;

import java.util.ArrayList;
import java.util.Collections;

import moe.kotohana.randomfood.databinding.ActivityUserSettingsBinding;

public class UserSettingsActivity extends AppCompatActivity {

    ActivityUserSettingsBinding binding;
    ArrayList<ChartData> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_settings);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setTitleTextColor(Color.WHITE);
        binding.toolbar.setContentInsetStartWithNavigation(0);
        getSupportActionBar().setTitle("유저 정보");
        setData();
        binding.developerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity();
            }
        });
        binding.showHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity();
            }
        });
    }

    private void setData() {
        Collections.addAll(arrayList,
                new ChartData("한식", 20, Color.WHITE, Color.BLUE),
                new ChartData("한식", 40, Color.WHITE, Color.RED),
                new ChartData("한식", 20, Color.WHITE, Color.GRAY),
                new ChartData("한식", 20, Color.WHITE, Color.GREEN)
        );
        binding.chart.setChartData(arrayList);
    }
}
