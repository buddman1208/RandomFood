package moe.kotohana.randomfood;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.github.nitrico.lastadapter.Holder;
import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;

import java.util.ArrayList;

import moe.kotohana.randomfood.databinding.ActivityNearFoodBinding;
import moe.kotohana.randomfood.databinding.ListItemBinding;
import moe.kotohana.randomfood.models.Place;

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
        getSupportActionBar().setTitle("이 주변의 " + typeList[getIntent().getIntExtra("foodType", 0)]);
        getPlace();
    }

    private void getPlace() {
        for (int i = 0; i < 20; i++) {
            arrayList.add(new Place("Place " + i));
        }
        initializeLayout();
    }

    private void initializeLayout() {
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
}
