package com.ghostwan.dtake;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import com.ghostwan.dtake.fragment.*;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EActivity
@OptionsMenu(R.menu.main2)
public class DTakeActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "DTakeActivity";
    private Intent serviceIntent;

    @ViewById
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtake);

        serviceIntent = new Intent(this, DTakeService.class);
        startService(serviceIntent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBroadcast(new Intent(DTakeService.ACTION_COUNT));
                Snackbar.make(view, getString(R.string.pill_taken), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new MainFragment_();
        fragmentManager.beginTransaction().addToBackStack(fragment.getTag()).replace(R.id.content_main2, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @OptionsItem
    void actionSettings(){
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @OptionsItem
    void actionQuit(){
        stopService(serviceIntent);
        finish();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;

        switch (id){
            case R.id.nav_main: fragmentClass = MainFragment_.class; setTitle(R.string.main); break;
            case R.id.nav_stats: fragmentClass = StatsFragment_.class; setTitle(R.string.stats); break;
            case R.id.nav_manage: fragmentClass = ManageFragment_.class; setTitle(R.string.manage); break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(fragment.getTag()).replace(R.id.content_main2, fragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

}
