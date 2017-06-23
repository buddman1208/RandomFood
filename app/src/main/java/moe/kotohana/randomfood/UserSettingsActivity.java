package moe.kotohana.randomfood;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.intrusoft.scatter.ChartData;

import java.util.ArrayList;
import java.util.Collections;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import moe.kotohana.randomfood.databinding.ActivityUserSettingsBinding;
import moe.kotohana.randomfood.models.History;
import moe.kotohana.randomfood.models.Restaurant;
import moe.kotohana.randomfood.utils.MathHelper;

public class UserSettingsActivity extends AppCompatActivity {

    Realm realm;
    ActivityUserSettingsBinding binding;
    ArrayList<ChartData> arrayList = new ArrayList<>();
    boolean canGoHistory = false;

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
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/kotohana5706")));
            }
        });
        binding.showHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canGoHistory) {
                    startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                } else
                    Toast.makeText(UserSettingsActivity.this, "사용자 데이터가 없습니다!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setData() {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<History> history = realm.where(History.class).findAll();
        if (history.size() == 0) {
            canGoHistory = false;
            binding.errorText.setVisibility(View.VISIBLE);
            binding.chart.setVisibility(View.INVISIBLE);
            History data = realm.createObject(History.class);
            data.setHistoryList(new RealmList<Restaurant>());
        } else {
            RealmList<Restaurant> data = history.get(0).getHistoryList();
            if (data.size() == 0) {
                binding.errorText.setVisibility(View.VISIBLE);
                binding.chart.setVisibility(View.INVISIBLE);
                canGoHistory = false;
            } else {
                canGoHistory = true;
                binding.errorText.setVisibility(View.INVISIBLE);
                binding.chart.setVisibility(View.VISIBLE);
                ArrayList<Float> percents = MathHelper.Companion.calculatePercent(
                        data.where().equalTo("type", 0).findAll().size(),
                        data.where().equalTo("type", 1).findAll().size(),
                        data.where().equalTo("type", 2).findAll().size(),
                        data.where().equalTo("type", 3).findAll().size(),
                        data.where().equalTo("type", 4).findAll().size(),
                        data.where().equalTo("type", 5).findAll().size(),
                        data.where().equalTo("type", 6).findAll().size()
                );
                Collections.addAll(arrayList,
                        new ChartData("", Float.parseFloat(String.format("%.2f", percents.get(0))), Color.WHITE, ContextCompat.getColor(this, R.color.koreanColor)),
                        new ChartData("", Float.parseFloat(String.format("%.2f", percents.get(1))), Color.WHITE, ContextCompat.getColor(this, R.color.chineseColor)),
                        new ChartData("", Float.parseFloat(String.format("%.2f", percents.get(2))), Color.WHITE, ContextCompat.getColor(this, R.color.boonsikColor)),
                        new ChartData("", Float.parseFloat(String.format("%.2f", percents.get(3))), Color.WHITE, ContextCompat.getColor(this, R.color.chickenColor)),
                        new ChartData("", Float.parseFloat(String.format("%.2f", percents.get(4))), Color.WHITE, ContextCompat.getColor(this, R.color.fastfoodColor)),
                        new ChartData("", Float.parseFloat(String.format("%.2f", percents.get(5))), Color.WHITE, ContextCompat.getColor(this, R.color.pizzaColor)),
                        new ChartData("", Float.parseFloat(String.format("%.2f", percents.get(6))), Color.WHITE, ContextCompat.getColor(this, R.color.japaneseColor))
                );
                binding.chart.setCenterCircleColor(Color.parseColor("#8fFFFFFF"));
                binding.chart.setChartData(arrayList);
            }
        }
        realm.commitTransaction();
    }
}
