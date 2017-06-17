package moe.kotohana.randomfood;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.nitrico.lastadapter.Holder;
import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import moe.kotohana.randomfood.databinding.ActivityNearFoodBinding;
import moe.kotohana.randomfood.databinding.ListItemBinding;
import moe.kotohana.randomfood.models.Location;
import moe.kotohana.randomfood.models.Place;
import moe.kotohana.randomfood.utils.GPSService;
import moe.kotohana.randomfood.utils.NetworkHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearFoodActivity extends AppCompatActivity {

    /*
    * 한식 0
    * 중식 1
    * 분식 2
    * 치킨 3
    * 패스트푸드 4
    * 피자 5
    * 일식 6
    * */

    ActivityNearFoodBinding binding;
    private ArrayList<Place> arrayList = new ArrayList<>();
    private String[] typeList = new String[]{
            "한식",
            "중식",
            "분식",
            "치킨",
            "패스트푸드",
            "피자",
            "일식"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_near_food);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("이 주변의 " + typeList[getIntent().getIntExtra("foodType", 0)]);
        getPlace();
    }

    private void getPlace() {
        GPSService service = new GPSService(this);
        if (service.canGetLocation()) {
            double latitude = service.getLatitude();
            double longitude = service.getLongitude();
            NetworkHelper.Companion.getNetworkInstance().getAddressByGeocode(longitude + "," + latitude).enqueue(new Callback<Location>() {
                @Override
                public void onResponse(Call<Location> call, Response<Location> response) {
                }

                @Override
                public void onFailure(Call<Location> call, Throwable t) {
                    Log.e("asdf", "onfailure  : " + t.getLocalizedMessage());

                }
            });
        } else {
            service.showSettingsAlert();
        }
        for (int i = 0; i < 20; i++) {
            arrayList.add(new Place("Place " + i));
        }
        initializeLayout();
    }

    private void initializeLayout() {
        binding.nearRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        new LastAdapter(arrayList, BR.content)
                .map(Place.class, new ItemType<ListItemBinding>(R.layout.list_item) {
                    @Override
                    public void onBind(Holder<ListItemBinding> holder) {
                        super.onBind(holder);
                        holder.getBinding().setActivity(NearFoodActivity.this);
                        holder.getBinding().setPosition(holder.getLayoutPosition());
                    }
                })
                .into(binding.nearRecyclerView);
    }

    public void onListClick(int position) {
        Toast.makeText(this, position + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.near_food_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.map:
                startActivity(new Intent(getApplicationContext(), NearFoodMapActivity.class)
                        .putExtra("foodList", arrayList));
        }
        return super.onOptionsItemSelected(item);
    }
}
