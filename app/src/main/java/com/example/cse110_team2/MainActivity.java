package com.example.cse110_team2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private FriendManager friendManager;
    private PointRelation locationRelater;
    private OrientationService orientationService;
    private boolean firstLocUpdate;
    private MyLocation myloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = getSharedPreferences("IDvalue", 0);
        String name = preferences.getString("user", "N/A");
        if (name == "N/A") {
            Log.d("debug", name);
            Intent intent = new Intent(this, InputNameActivity.class);
            startActivity(intent);
        }
        friendManager = FriendManager.provide();


        var executor = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> poller = executor.scheduleAtFixedRate(()->{
            Log.d("Printing For time:", "Printing1");
            friendManager.updateFriendLocations();
            updateFunctions();
        }, 0, 5, TimeUnit.SECONDS);

    }

    public void updateFunctions(){
        //Might be necessary to calculate azimuth angle/zoom/etc
        compassUpdate();
    }
    private void compassUpdate() {
        String name;
        float longitude;
        float latitude;
        User curr_friend;
        ArrayList<User> friendList = friendManager.getFriends();
        Log.d("CU", "in compass update");
        for (int i = 0; i < friendList.size(); i++) {

            curr_friend = friendList.get(i);
            name = curr_friend.name;
            longitude = curr_friend.longitude;
            latitude = curr_friend.latitude;

            //TODO: Update friend name here with relative location (longitude and latitude)

        }

    }
        //firstLocUpdate = false;


//
//
//        orientationService = OrientationService.singleton(MainActivity.this);
//        orientationService.getOrientation().observe(this, azimuth -> {
//            compassUpdate(azimuth);
//        });
//
//        myloc = new MyLocation(0, 0);
//        locationRelater = new PointRelation(myloc);
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }
//        ImageView redCircle = (ImageView) findViewById(R.id.redImage);
//        ImageView blueCircle = (ImageView) findViewById(R.id.blueImage);
//        ImageView yellowCircle = (ImageView) findViewById(R.id.yellowImage);
//        redCircle.setVisibility(View.INVISIBLE);
//        blueCircle.setVisibility(View.INVISIBLE);
//        yellowCircle.setVisibility(View.INVISIBLE);
//
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
//            @Override
//            public void onLocationChanged(@NonNull Location location) {
//                firstLocUpdate = true;
//                myloc.setLon(location.getLongitude());
//                myloc.setLat(location.getLatitude());
//                compassUpdate(orientationService.getOrientation().getValue());
//                //lon.setText("Longitude: " + String.valueOf(myloc.getLon()));
//                //lat.setText("Latitude: " + String.valueOf(myloc.getLat()));
//            }
//        });
//
//        SharedPreferences preferences = getSharedPreferences("IDvalue", 0);
//        String locName = preferences.getString("locationOneName", "N/A");
//        if (locName == "N/A") {
//            Intent intent = new Intent(this, InputLocation.class);
//            startActivity(intent);
//        }
//    }
//




//
//    private void updateSpecificCircle(String locName, String locLat, String locLon, ImageView circle, ImageView legendCircle, TextView legendText) {
//        if (locName != "N/A") {
//            Double newAngle = locationRelater.angleCalculation(Double.parseDouble(locLat), Double.parseDouble(locLon));
//            legendText.setText(locName);
//            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) circle.getLayoutParams();
//            layoutParams.circleAngle = newAngle.floatValue();
//            circle.setLayoutParams(layoutParams);
//            if (firstLocUpdate) {
//                legendCircle.setVisibility(View.VISIBLE);
//                legendText.setVisibility(View.VISIBLE);
//                circle.setVisibility(View.VISIBLE);
//                circle.bringToFront();
//            }
//        } else {
//            legendCircle.setVisibility(View.INVISIBLE);
//            legendText.setVisibility(View.INVISIBLE);
//            circle.setVisibility(View.INVISIBLE);
//        }
//    }


    /**
     * Rotates the selected image view about a certain angle based on heading.
     * @param img The image view compass to rotate
     * @param az Azimuth from current heading

    public void rotateImg(ImageView img, Float az) {
        if (az == null) { az = 0.0F; }
        double azDeg = Utilities.radToDeg(az);
        img.setRotation((float) -(azDeg));
    }
     */
    /**
     * Rotates location circles
     * @param img Location circle to rotate
     * @param az Azimuth from current heading

    public void rotateLoc(ImageView img, Float az) {
        if (az == null) { return; }
        double azDeg = Utilities.radToDeg(az);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) img.getLayoutParams();
        lp.circleAngle -= azDeg;
        img.setLayoutParams(lp);
    }

    private void solveOverlap() {
        final float scale = this.getResources().getDisplayMetrics().density;
        Log.i("overlap", "scale: " + scale);
        ImageView circleOne = findViewById(R.id.redImage);
        ImageView circleTwo = findViewById(R.id.blueImage);
        ImageView circleThree = findViewById(R.id.yellowImage);
        boolean overlay12 = checkAngleOverlap(circleOne, circleTwo);
        boolean overlay23 = checkAngleOverlap(circleTwo, circleThree);
        boolean overlay31 = checkAngleOverlap(circleThree, circleOne);
        Log.i("overlap", "overlay12: " + overlay12);
        Log.i("overlap", "overlay23: " + overlay23);
        Log.i("overlap", "overlay31: " + overlay31);
        if ((overlay12 && (overlay23 || overlay31)) || (overlay23 && overlay31)) {
            setCircleRadius(circleOne, (int) (360 + 0.5f));
            setCircleRadius(circleTwo, (int) (410 + 0.5f));
            setCircleRadius(circleThree, (int) (460 + 0.5f));
            setCircleSize(circleOne, (int) (50 + 0.5f));
            setCircleSize(circleTwo, (int) (50 + 0.5f));
            setCircleSize(circleThree, (int) (50 + 0.5f));
        } else if (overlay12) {
            setCircleRadius(circleOne, (int) (360 + 0.5f));
            setCircleRadius(circleTwo, (int) (410 + 0.5f));
            setCircleSize(circleOne, (int) (50 + 0.5f));
            setCircleSize(circleTwo, (int) (50 + 0.5f));
        } else if (overlay23) {
            setCircleRadius(circleTwo, (int) (360 + 0.5f));
            setCircleRadius(circleThree, (int) (410 + 0.5f));
            setCircleSize(circleTwo, (int) (50 + 0.5f));
            setCircleSize(circleThree, (int) (50 + 0.5f));
        } else if (overlay31) {
            setCircleRadius(circleOne, (int) (360 + 0.5f));
            setCircleRadius(circleThree, (int) (410 + 0.5f));
            setCircleSize(circleOne, (int) (50 + 0.5f));
            setCircleSize(circleThree, (int) (50 + 0.5f));
        } else {
            setCircleRadius(circleOne, (int) (410 + 0.5f));
            setCircleRadius(circleTwo, (int) (410 + 0.5f));
            setCircleRadius(circleThree, (int) (410 + 0.5f));
            setCircleSize(circleOne, (int) (50 + 0.5f));
            setCircleSize(circleTwo, (int) (50 + 0.5f));
            setCircleSize(circleThree, (int) (50 + 0.5f));

        }
    }

    private boolean checkAngleOverlap(ImageView circleOne, ImageView circleTwo) {
        ConstraintLayout.LayoutParams layoutParamsOne = (ConstraintLayout.LayoutParams) circleOne.getLayoutParams();
        ConstraintLayout.LayoutParams layoutParamsTwo = (ConstraintLayout.LayoutParams) circleTwo.getLayoutParams();
//        Log.i("overlap", "Angle diff: " + Math.abs(layoutParamsOne.circleAngle-layoutParamsTwo.circleAngle));
//        return false;
        return Math.abs(layoutParamsOne.circleAngle - layoutParamsTwo.circleAngle) < 10;
    }

    private void setCircleRadius(ImageView circle, int radius) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) circle.getLayoutParams();
        layoutParams.circleRadius = radius;
        circle.setLayoutParams(layoutParams);
    }

    private void setCircleSize(ImageView circle, int size) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) circle.getLayoutParams();
        layoutParams.width = size;
        layoutParams.height = size;
        circle.setLayoutParams(layoutParams);
    }

    public MyLocation getLocation() {
        return myloc;
    }

    public void setOrientationMock(MutableLiveData<Float> ld) {
        orientationService.setMockOrientationSource(ld);
    }

    public void mockCompassUpdate() {
        float az = this.orientationService.getOrientation().getValue();
        this.compassUpdate(az);
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationService.unregisterSensorListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        orientationService.registerSensorListeners();

        SharedPreferences preferences = getSharedPreferences("IDvalue", 0);
        String heading_string = preferences.getString("heading", "N/A");
        if (heading_string != "N/A") {

            float heading_float = Float.parseFloat(heading_string);
            preferences.edit().remove("heading").commit();

            MutableLiveData<Float> heading_data = new MutableLiveData<Float>();
            heading_data.setValue(heading_float);

            orientationService.setMockOrientationSource(heading_data);
        }


        compassUpdate(0.0F);
    }

    public void onNewLocationBtnClicked(View view) {
        Intent intent = new Intent(this, InputLocation.class);

//        SharedPreferences preferences = getSharedPreferences("IDvalue", 0);
//        String locationThreeName = preferences.getString("locationThreeName", "N/A");
//
//        Log.i("Location 3", locationThreeName);
//        if (locationThreeName != "N/A") {
//            Utilities.showAlert(this, "You cannot save any more locations");
//        } else {
        startActivity(intent);
//        }
     */



    }


    public void onAddFriendClicked(View view) {
        Intent intent = new Intent(this, AddFriendsActivity.class);
        startActivity(intent);

    }
}


