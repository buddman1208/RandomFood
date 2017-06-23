package moe.kotohana.randomfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapCalloutCustomOverlay;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.ArrayList;

import moe.kotohana.randomfood.databinding.ActivityNearFoodMapBinding;
import moe.kotohana.randomfood.models.Restaurant;
import moe.kotohana.randomfood.utils.GeoTrans;
import moe.kotohana.randomfood.utils.GeoTransPoint;
import moe.kotohana.randomfood.utils.NMapCalloutCustomOldOverlay;
import moe.kotohana.randomfood.utils.NMapCalloutCustomOverlayView;
import moe.kotohana.randomfood.utils.NMapPOIflagType;
import moe.kotohana.randomfood.utils.NMapViewerResourceProvider;

public class NearFoodMapActivity extends AppCompatActivity {

    ActivityNearFoodMapBinding binding;
    public static final String CLIENT_ID = "yQwvCnEirmlpHCICpzIU";

    private static boolean mIsMapEnlared = false;
    public NMapContext nMapContext;
    public NMapView mMapView;
    public NMapController mMapController;
    public SharedPreferences mPreferences;
    public NMapOverlayManager mOverlayManager;
    public NMapMyLocationOverlay mMyLocationOverlay;
    public NMapLocationManager mMapLocationManager;
    public NMapCompassManager mMapCompassManager;
    public NMapPOIdataOverlay mFloatingPOIdataOverlay;
    public NMapPOIitem mFloatingPOIitem;
    private NMapViewerResourceProvider mMapViewerResourceProvider;
    ArrayList<? extends Restaurant> arrayList;
    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_near_food_map);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("toolbar"));
        nMapContext = new NMapContext(this);
        nMapContext.onCreate();
        setDefault();
    }

    public void setDefault() {
        intent = getIntent();
        mMapView = binding.mapView;
        nMapContext.setupMapView(mMapView);
        mMapView.setClientId(CLIENT_ID);
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();
        mMapController = mMapView.getMapController();
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
        mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);
        mOverlayManager.setOnCalloutOverlayViewListener(onCalloutOverlayViewListener);

        if (mMapView.getMapProjection().isProjectionScaled()) {
            if (mMapView.getMapProjection().isMapHD()) {
                mMapView.setScalingFactor(2.0F, false);
            } else {
                mMapView.setScalingFactor(1.0F, false);
            }
        } else {
            mMapView.setScalingFactor(2.0F, true);
        }
        mIsMapEnlared = mMapView.getMapProjection().isProjectionScaled();

        setGeoPoint();
    }

    public int findPositionInArrayList(String query) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getRealTitle().equals(query)) return i;
        }
        return -1;
    }

    private void setGeoPoint() {
        arrayList = intent.getParcelableArrayListExtra("restaurants");
        int markerId = NMapPOIflagType.PIN;

        if (arrayList.size() == 1) {
            setUI(arrayList.get(0));
            setMode(false);
            GeoTransPoint oGeo = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, new GeoTransPoint(Double.parseDouble(arrayList.get(0).getMapx()), Double.parseDouble(arrayList.get(0).getMapy())));
            mMapController.setMapCenter(new NGeoPoint(oGeo.getX(), oGeo.getY()));
            NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
            poiData.beginPOIdata(2);
            NMapPOIitem item = poiData.addPOIitem(oGeo.getX(), oGeo.getY(), arrayList.get(0).getRealTitle(), markerId, 0);
            item.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
            poiData.endPOIdata();
            NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
            poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
            poiDataOverlay.selectPOIitem(0, true);
        } else {
            setMode(true);
            NMapPOIdata poiData = new NMapPOIdata(arrayList.size(), mMapViewerResourceProvider);
            poiData.beginPOIdata(arrayList.size());
            for (int i = 0; i < arrayList.size(); i++) {
                GeoTransPoint oGeo = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, new GeoTransPoint(Double.parseDouble(arrayList.get(i).getMapx()), Double.parseDouble(arrayList.get(i).getMapy())));
                poiData.addPOIitem(oGeo.getX(), oGeo.getY(), arrayList.get(i).getRealTitle(), markerId, 0);

            }
            poiData.endPOIdata();
            NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
            poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
            poiDataOverlay.showAllPOIdata(11);
        }
    }

    private void setMode(boolean isMultiMode) {
        binding.container.setVisibility((isMultiMode) ? View.GONE : View.VISIBLE);
    }

    private void setUI(final Restaurant restaurant) {
        binding.container.setVisibility(View.VISIBLE);
        binding.resTitle.setText(restaurant.getRealTitle());
        binding.resPhone.setText(
                (restaurant.getTelephone().trim().equals("")) ? "번호 정보가 없습니다" : restaurant.getTelephone()
        );
        binding.resPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (restaurant.getTelephone().trim().equals(""))
                    return;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + restaurant.getTelephone()));
                startActivity(intent);
            }
        });
        binding.resAddress.setText(restaurant.getRealAddress());
    }

    /* POI data State Change Listener*/
    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            setUI(arrayList.get(findPositionInArrayList(item.getTitle())));

        }

        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
        }
    };
    private final NMapOverlayManager.OnCalloutOverlayViewListener onCalloutOverlayViewListener = new NMapOverlayManager.OnCalloutOverlayViewListener() {

        @Override
        public View onCreateCalloutOverlayView(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {

            if (overlayItem != null) {
                String title = overlayItem.getTitle();
                if (title != null && title.length() > 5) {
                    return new NMapCalloutCustomOverlayView(NearFoodMapActivity.this, itemOverlay, overlayItem, itemBounds);
                }
            }
            return null;
        }

    };
    private final NMapOverlayManager.OnCalloutOverlayListener onCalloutOverlayListener = new NMapOverlayManager.OnCalloutOverlayListener() {

        @Override
        public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay itemOverlay, NMapOverlayItem overlayItem,
                                                         Rect itemBounds) {

            Log.e("asdf", "onCreateCalloutOverlay");
            // handle overlapped items
            if (itemOverlay instanceof NMapPOIdataOverlay) {
                NMapPOIdataOverlay poiDataOverlay = (NMapPOIdataOverlay) itemOverlay;

                // check if it is selected by touch event
                if (!poiDataOverlay.isFocusedBySelectItem()) {
                    int countOfOverlappedItems = 1;

                    NMapPOIdata poiData = poiDataOverlay.getPOIdata();
                    for (int i = 0; i < poiData.count(); i++) {
                        NMapPOIitem poiItem = poiData.getPOIitem(i);

                        // skip selected item
                        if (poiItem == overlayItem) {
                            continue;
                        }

                        // check if overlapped or not
                        if (Rect.intersects(poiItem.getBoundsInScreen(), overlayItem.getBoundsInScreen())) {
                            countOfOverlappedItems++;
                        }
                    }


                }
            }

            if (overlayItem instanceof NMapPOIitem) {
                NMapPOIitem poiItem = (NMapPOIitem) overlayItem;

                if (poiItem.showRightButton()) {
                    return new NMapCalloutCustomOldOverlay(itemOverlay, overlayItem, itemBounds,
                            mMapViewerResourceProvider);
                }
            }

            return new NMapCalloutCustomOverlay(itemOverlay, overlayItem, itemBounds, mMapViewerResourceProvider);

        }

    };

    public void startMyLocation() {

        if (mMyLocationOverlay != null) {
            if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {
                mOverlayManager.addOverlay(mMyLocationOverlay);
            }

            if (mMapLocationManager.isMyLocationEnabled()) {

                if (!mMapView.isAutoRotateEnabled()) {
                    mMyLocationOverlay.setCompassHeadingVisible(true);

                    mMapCompassManager.enableCompass();

                    mMapView.setAutoRotateEnabled(true, false);

                } else {
                    stopMyLocation();
                }

                mMapView.postInvalidate();
            } else {
                boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
                if (!isMyLocationEnabled) {
                    Toast.makeText(getApplicationContext(), "Please enable a My Location source in system settings",
                            Toast.LENGTH_LONG).show();

                    Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(goToSettings);
                }
            }
        }
    }

    public void stopMyLocation() {
        if (mMyLocationOverlay != null) {
            mMapLocationManager.disableMyLocation();

            if (mMapView.isAutoRotateEnabled()) {
                mMyLocationOverlay.setCompassHeadingVisible(false);

                mMapCompassManager.disableCompass();

                mMapView.setAutoRotateEnabled(false, false);

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        nMapContext.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nMapContext.onResume();
    }

    @Override
    protected void onStop() {

        stopMyLocation();
        nMapContext.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        nMapContext.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onContextItemSelected(item);
    }
}
