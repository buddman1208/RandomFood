package moe.kotohana.randomfood;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.intrusoft.scatter.ChartData;

import java.util.ArrayList;
import java.util.Collections;

import moe.kotohana.randomfood.databinding.ActivityUserSettingsBinding;
import moe.kotohana.randomfood.utils.MathHelper;

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

        ArrayList<Float> percents = MathHelper.Companion.calculatePercent(30, 20, 40, 50, 60, 70, 20);
        Collections.addAll(arrayList,
                new ChartData("", percents.get(0), Color.WHITE, ContextCompat.getColor(this, R.color.koreanColor)),
                new ChartData("", percents.get(1), Color.WHITE, ContextCompat.getColor(this, R.color.chineseColor)),
                new ChartData("", percents.get(2), Color.WHITE, ContextCompat.getColor(this, R.color.boonsikColor)),
                new ChartData("", percents.get(3), Color.WHITE, ContextCompat.getColor(this, R.color.chickenColor)),
                new ChartData("", percents.get(4), Color.WHITE, ContextCompat.getColor(this, R.color.fastfoodColor)),
                new ChartData("", percents.get(5), Color.WHITE, ContextCompat.getColor(this, R.color.pizzaColor)),
                new ChartData("", percents.get(6), Color.WHITE, ContextCompat.getColor(this, R.color.japaneseColor))
        );
        binding.chart.setCenterCircleColor(Color.parseColor("#8fFFFFFF"));
        binding.chart.setChartData(arrayList);
    }
}
