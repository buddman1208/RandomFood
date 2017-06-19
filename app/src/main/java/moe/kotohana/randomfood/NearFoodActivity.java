package moe.kotohana.randomfood;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.nitrico.lastadapter.Holder;
import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;

import java.util.ArrayList;

import moe.kotohana.randomfood.databinding.ActivityNearFoodBinding;
import moe.kotohana.randomfood.databinding.ListItemBinding;
import moe.kotohana.randomfood.models.Items;
import moe.kotohana.randomfood.models.Location;
import moe.kotohana.randomfood.models.Place;
import moe.kotohana.randomfood.models.Restaurant;
import moe.kotohana.randomfood.utils.GPSService;
import moe.kotohana.randomfood.utils.NetworkHelper;
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
    int type = 0;
    GPSService service;
    ActivityNearFoodBinding binding;
    MaterialDialog progressDialog;
    private ArrayList<Restaurant> arrayList = new ArrayList<>();
    private String[] typeList = new String[]{
            "한식",
            "중식",
            "분식",
            "치킨",
            "패스트푸드",
            "피자",
            "일식"
    };
    private String[] typeQueryList = new String[]{
            "한식 음식점",
            "중식 음식점",
            "분식 음식점",
            "치킨 음식점",
            "패스트푸드",
            "피자",
            "일식 음식점"
    };


    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_near_food);

        progressDialog = new MaterialDialog.Builder(this)
                .title("데이터를 로드하는 중입니다")
                .progress(true, 0)
                .content("잠시만 기다려주세요.")
                .cancelable(false)
                .show();
        setSupportActionBar(binding.toolbar);
        type = getIntent().getIntExtra("foodType", 0);
        binding.toolbar.setTitleTextColor(Color.WHITE);
        binding.toolbar.setSubtitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("이 주변의 " + typeList[type]);
        getPlace();
    }

    private void getPlace() {
        service = new GPSService(this);
        android.location.Location location = service.getLocation();
        if (location != null) {
            NetworkHelper.Companion.getNetworkInstance().getAddressByGeocode(longitude + "," + latitude).enqueue(new Callback<Location>() {
                @Override
                public void onResponse(Call<Location> call, Response<Location> response) {
                    switch (response.code()) {
                        case 200:
                            Items items = response.body().getResult().getItems().get(0);
                            getSupportActionBar().setSubtitle(
                                    items.getAddrdetail().getSido() + " " +
                                            items.getAddrdetail().getSigugun() + " " +
                                            items.getAddrdetail().getDongmyun()
                            );
                            NetworkHelper.Companion.getNetworkInstance().getRestaurant(items.getAddrdetail().getSigugun() + " " + items.getAddrdetail().getDongmyun() + " " + typeQueryList[type], 100).enqueue(new Callback<Place>() {
                                @Override
                                public void onResponse(Call<Place> call, Response<Place> response) {
                                    switch (response.code()) {
                                        case 200:
                                            arrayList = response.body().getItems();
                                            initializeLayout();
                                            break;
                                        default:
                                            Log.e("getRestaurant", "onResponse : " + response.code() + "");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Place> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Log.e("asdf", "onfailure  : " + t.getLocalizedMessage());
                                    finishWithFailure();
                                }
                            });
                            break;
                        default:
                            Log.e("getAddressByGeocode", "onResponse : " + response.code() + "");
                            finishWithFailure();
                    }
                }

                @Override
                public void onFailure(Call<Location> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("asdf", "onfailure  : " + t.getLocalizedMessage());
                    finishWithFailure();

                }
            });
        } else {
            service.showSettingsAlert();
        }
    }

    private void initializeLayout() {
        binding.nearRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        new LastAdapter(arrayList, BR.content)
                .map(Restaurant.class, new ItemType<ListItemBinding>(R.layout.list_item) {
                    @Override
                    public void onBind(Holder<ListItemBinding> holder) {
                        super.onBind(holder);
                        holder.getBinding().setActivity(NearFoodActivity.this);
                        holder.getBinding().setPosition(holder.getLayoutPosition());
                    }
                })
                .into(binding.nearRecyclerView);
        progressDialog.dismiss();
    }

    public void finishWithFailure() {
        Toast.makeText(this, "데이터 로드에 실패하였습니다.\n인터넷 연결 상태를 확인 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
        finish();
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
