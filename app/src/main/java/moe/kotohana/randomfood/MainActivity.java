package moe.kotohana.randomfood;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.security.Permission;
import java.util.ArrayList;

import moe.kotohana.randomfood.databinding.ActivityMainBinding;
import moe.kotohana.randomfood.utils.MathHelper;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setPermission();
        setCardViewClick();
        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserSettingsActivity.class));
            }
        });
        binding.selectRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NearFoodActivity.class)
                        .putExtra("foodType", MathHelper.Companion.getRandomNumber(6)));
            }
        });
        binding.restaurantStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FoodStackActivity.class));
            }
        });
    }

    private void setPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            new MaterialDialog.Builder(MainActivity.this)
                    .title("권한 허용")
                    .content("위치 기반 서비스를 사용하기 위해 권한이 필요합니다.")
                    .negativeText("거부")
                    .positiveText("확인")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    })
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            new TedPermission(MainActivity.this)
                                    .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                                    .setPermissionListener(new PermissionListener() {
                                        @Override
                                        public void onPermissionGranted() {

                                        }

                                        @Override
                                        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                                            new MaterialDialog.Builder(MainActivity.this)
                                                    .title("권한 허용")
                                                    .content("위치 기반 서비스를 사용하기 위해 권한이 필요합니다.\n거부 시 어플리케이션을 종료합니다.")
                                                    .negativeText("거부")
                                                    .positiveText("확인")
                                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            finish();
                                                        }
                                                    })
                                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            setPermission();
                                                        }
                                                    })
                                                    .show();

                                        }
                                    })
                                    .check();
                        }
                    })
                    .show();
        }

    }

    private void setCardViewClick() {
        binding.hansikFood.setOnClickListener(new CardClickListener().setPosition(0));
        binding.chineseFood.setOnClickListener(new CardClickListener().setPosition(1));
        binding.boonsikFood.setOnClickListener(new CardClickListener().setPosition(2));
        binding.chickenFood.setOnClickListener(new CardClickListener().setPosition(3));
        binding.fastFood.setOnClickListener(new CardClickListener().setPosition(4));
        binding.pizzaFood.setOnClickListener(new CardClickListener().setPosition(5));
        binding.japaneseFood.setOnClickListener(new CardClickListener().setPosition(6));
    }

    private class CardClickListener implements View.OnClickListener {
        int position;

        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), NearFoodActivity.class)
                    .putExtra("foodType", position));
        }

        View.OnClickListener setPosition(int position) {
            this.position = position;
            return this;
        }

    }

}
