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

public class MainActivity extends AppCompatActivity  {

    FirebaseAuth auth;
    final Fragment settingsFragment = new SettingsFragment();
    final Fragment dashboardFragment = new DashboardFragment();
    final Fragment availibilityFragment = new AvailabilityFragment();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = settingsFragment;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_availibility:
                    fm.beginTransaction().hide(active).commit();
                    active = availibilityFragment;
                    fm.beginTransaction().show(availibilityFragment).commit();
                    getSupportActionBar().setTitle(R.string.title_avilibility);

                    return true;

                case R.id.navigation_dashboard:
                    fm.beginTransaction().hide(active).commit();
                    active = dashboardFragment;
                    fm.beginTransaction().show(dashboardFragment).commit();
                    getSupportActionBar().setTitle(R.string.app_name);
                    return true;

                case R.id.navigation_settings:
                    fm.beginTransaction().hide(active).commit();
                    active = settingsFragment;
                    fm.beginTransaction().show(settingsFragment).commit();
                    getSupportActionBar().setTitle(R.string.title_settings);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        AddFragment();
        active = dashboardFragment;
        fm.beginTransaction().show(dashboardFragment).commit();
        fm.beginTransaction().hide(availibilityFragment).commit();
        fm.beginTransaction().hide(settingsFragment).commit();



    }




    public void AddFragment(){
        fm.beginTransaction().add(R.id.main_container, dashboardFragment, "1").add(R.id.main_container, settingsFragment, "2").add(R.id.main_container, availibilityFragment, "3").commit();
    }
}
