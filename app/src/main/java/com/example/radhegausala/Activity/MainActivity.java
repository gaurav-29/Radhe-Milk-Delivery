package com.example.radhegausala.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.radhegausala.Fragment.DashBoardFragment;
import com.example.radhegausala.Fragment.ProfileFragment;
import com.example.radhegausala.R;
import com.example.radhegausala.databinding.ActivityMainBinding;
import com.example.radhegausala.databinding.DrawerLayoutBinding;

public class MainActivity extends AppCompatActivity {
    public static ActivityMainBinding mBinding;
    MainActivity mcontext = MainActivity.this;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(mcontext, R.layout.activity_main);
        onClickListner();
        selectFirstItemAsDefault();
        getCustomerList();
    }

    public static void visiblity(int view) {
        mBinding.ivDashboard.setVisibility(view);
    }

    private void getCustomerList() {
    }

    private void onClickListner() {
        View headerView = mBinding.navView.inflateHeaderView(R.layout.drawer_layout);
        DrawerLayoutBinding binding = DrawerLayoutBinding.bind(headerView);

        binding.llDashboard.setOnClickListener(v -> {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            getCustomerList();
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new DashBoardFragment();
            fragmentManager.beginTransaction().replace(R.id.llContainLayout, fragment).commit();
        });
        binding.llProfile.setOnClickListener(v -> {
            visiblity(View.VISIBLE);
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new ProfileFragment();
            fragmentManager.beginTransaction().replace(R.id.llContainLayout, fragment).commit();
        });
        binding.llLogOut.setOnClickListener(v -> {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            builder = new AlertDialog.Builder(this);
            //Uncomment the below code to Set the message and title from the strings.xml file
            builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);
            //Setting message manually and performing action on button click
            builder.setMessage("Are you sure you want to Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences prefs = getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.clear().commit();
                            editor.apply();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();

                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Log Out");
            alert.show();
        });

        mBinding.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mBinding.drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        mBinding.ivDashboard.setOnClickListener(v -> {
            selectFirstItemAsDefault();
        });
    }

    private void selectFirstItemAsDefault() {
        visiblity(View.GONE);
        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        getCustomerList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new DashBoardFragment();
        fragmentManager.beginTransaction().replace(R.id.llContainLayout, fragment).commit();
    }
}