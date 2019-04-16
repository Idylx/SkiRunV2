package hevs.it.SkiRunV2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import hevs.it.SkiRunV2.Settings.SettingsFragment;
import hevs.it.SkiRunV2.availability.AvailabilityFragment;
import hevs.it.SkiRunV2.permissions.PermissionHelper;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";

    FirebaseAuth auth;
    final Fragment settingsFragment = new SettingsFragment();
    final Fragment dashboardFragment = new DashboardFragment();
    final Fragment availibilityFragment = new AvailabilityFragment();

    final FragmentManager fm = getSupportFragmentManager();
    String activeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        AddFragment();

        setFragmentDashboard();

        if (!PermissionHelper.hasCameraPermission(this)) {
            PermissionHelper.requestCameraPermission(this, true);
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_availibility:
                    setFragmentAvailability();
                    return true;

                case R.id.navigation_dashboard:
                    setFragmentDashboard();
                    return true;

                case R.id.navigation_settings:
                    setFragmentSettings();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (!PermissionHelper.hasCameraPermission(this)) {
            PermissionHelper.requestCameraPermission(this, true);
        }
        reloadCorrectFragment();
    }

    public void AddFragment(){
        fm.beginTransaction().add(R.id.main_container, dashboardFragment, "1").add(R.id.main_container, settingsFragment, "2").add(R.id.main_container, availibilityFragment, "3").commit();
    }

    private void reloadCorrectFragment(){
        switch (activeString){
            case "dashboard":
                setFragmentDashboard();

                break;
            case "settings":
                setFragmentSettings();
                break;
            case "availability":
                setFragmentAvailability();
                break;
            default :
                setFragmentDashboard();
                break;
        }
    }
    private void setFragmentSettings(){
        fm.beginTransaction().hide(availibilityFragment).commit();
        fm.beginTransaction().hide(dashboardFragment).commit();
        activeString = "settings";
        fm.beginTransaction().show(settingsFragment).commit();
        getSupportActionBar().setTitle(R.string.title_settings);
    }

    private void setFragmentDashboard(){
        fm.beginTransaction().hide(availibilityFragment).commit();
        fm.beginTransaction().hide(settingsFragment).commit();
        activeString = "dashboard";
        fm.beginTransaction().show(dashboardFragment).commit();
        getSupportActionBar().setTitle(R.string.app_name);
    }

    private void setFragmentAvailability(){
        fm.beginTransaction().hide(settingsFragment).commit();
        fm.beginTransaction().hide(dashboardFragment).commit();
        activeString = "availability";
        fm.beginTransaction().show(availibilityFragment).commit();
        getSupportActionBar().setTitle(R.string.title_avilibility);
    }


}
