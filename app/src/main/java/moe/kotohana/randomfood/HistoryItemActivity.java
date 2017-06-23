package moe.kotohana.randomfood;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.support.v4.database.DatabaseUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;

import com.github.nitrico.lastadapter.Holder;
import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import moe.kotohana.randomfood.databinding.ActivityHistoryBinding;
import moe.kotohana.randomfood.databinding.ActivityHistoryItemBinding;
import moe.kotohana.randomfood.databinding.HistoryItemBinding;
import moe.kotohana.randomfood.models.History;
import moe.kotohana.randomfood.models.Restaurant;
import moe.kotohana.randomfood.models.RestaurantContent;

public class HistoryItemActivity extends AppCompatActivity {

    ActivityHistoryItemBinding binding;
    int type;
    Realm realm;
    ArrayList<Restaurant> arrayList = new ArrayList<>();
    ArrayList<RestaurantContent> arrayListForListView = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history_item);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle("최근 기록 보기");
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmList<Restaurant> restaurants = realm.where(History.class).findAll().get(0).getHistoryList();
        type = getIntent().getIntExtra("type", -1);
        type = type - 1;
        if (type == -1) {
            arrayList.addAll(restaurants);
        } else {
            arrayList.addAll(restaurants.where().equalTo("type", type).findAll());
        }
        for (Restaurant r : arrayList) {
            arrayListForListView.add(new RestaurantContent().convertToThis(r));
        }
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new LastAdapter(arrayListForListView, BR.content)
                .map(RestaurantContent.class, new ItemType<HistoryItemBinding>(R.layout.history_item) {

                    @Override
                    public void onBind(Holder<HistoryItemBinding> holder) {
                        super.onBind(holder);
                        holder.getBinding().setPosition(holder.getLayoutPosition());
                        holder.getBinding().setActivity(HistoryItemActivity.this);
                    }
                })
                .into(binding.recyclerView);
        realm.commitTransaction();
    }

    public void onListClick(int position) {
        ArrayList<Restaurant> tempArr = new ArrayList<>();
        tempArr.add(
                new Restaurant(
                        arrayList.get(position).getTitle(),
                        arrayList.get(position).getLink(),
                        arrayList.get(position).getCategory(),
                        arrayList.get(position).getDescription(),
                        arrayList.get(position).getTelephone(),
                        arrayList.get(position).getAddress(),
                        arrayList.get(position).getRoadAddress(),
                        arrayList.get(position).getMapx(),
                        arrayList.get(position).getMapy()

                ).setRealType(arrayList.get(position).getType())
        );

        startActivity(new Intent(getApplicationContext(), NearFoodMapActivity.class)
                .putExtra("restaurants", tempArr)
                .putExtra("toolbar", getSupportActionBar().getTitle()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
