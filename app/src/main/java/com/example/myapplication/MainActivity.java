package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar ;
    private int clickedNavItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout   drawerLayout = findViewById(R.id.drawer);
        NavigationView navigation_view =findViewById(R.id.navigation_view);
        ConstraintLayout content =findViewById(R.id.content);
        toolbar.setTitle(Html.fromHtml("<font color='#6838ED'> Home </font>"));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);

            }
        };
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final Snackbar snackbar = Snackbar
                        .make(drawerLayout, "Coming On next Version", Snackbar.LENGTH_SHORT).setTextColor(getResources().getColor(R.color.backgroud_color)).setBackgroundTint(getResources().getColor(R.color.white));
                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        clickedNavItem = R.id.nav_profile;
                        break;
                    case R.id.nav_settings:
                        clickedNavItem = R.id.nav_settings;
                        break;

                    case R.id.nav_logout:
                        snackbar.show();
                        clickedNavItem = R.id.nav_logout;
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

                switch (clickedNavItem) {
                    case R.id.nav_profile:
                        //startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        break;
                    case R.id.nav_settings:
                      //  startActivity(new Intent(HomePageActivity.this, ChallengesActivity.class));
                        break;
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(MainActivity.this, "Log Out Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        finish();
                        break;



                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }


    public void Logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Log out success", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, SignInActivity.class));
        finish();
    }

}