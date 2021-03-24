package com.cactus.instagrama.main.presentation;

import android.Manifest;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cactus.instagrama.R;
import com.cactus.instagrama.common.view.AbstractActivity;
import com.cactus.instagrama.login.presentation.LoginActivity;
import com.cactus.instagrama.main.camera.presentation.AddActivity;
import com.cactus.instagrama.main.home.datasource.HomeDataSource;
import com.cactus.instagrama.main.home.datasource.HomeFireDataSource;
import com.cactus.instagrama.main.home.presentation.HomeFragment;
import com.cactus.instagrama.main.home.presentation.HomePresenter;
import com.cactus.instagrama.main.profile.datasource.ProfileDataSource;
import com.cactus.instagrama.main.profile.datasource.ProfileFireDataSource;
import com.cactus.instagrama.main.profile.presentation.ProfileFragment;
import com.cactus.instagrama.main.profile.presentation.ProfilePresenter;
import com.cactus.instagrama.main.search.datasource.SearchDataSource;
import com.cactus.instagrama.main.search.datasource.SearchFireDataSource;
import com.cactus.instagrama.main.search.presentation.SearchFragment;
import com.cactus.instagrama.main.search.presentation.SearchPresenter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AbstractActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MainView {

  public static final String ACT_SOURCE = "act_source";
  public static final int LOGIN_ACTIVITY = 0;
  public static final int REGISTER_ACTIVITY = 1;

  private ProfilePresenter profilePresenter;
  private HomePresenter homePresenter;
  private SearchPresenter searchPresenter;

  Fragment homeFragment;
  Fragment profileFragment;
//  Fragment cameraFragment;
  Fragment searchFragment;
  Fragment active;

  ProfileFragment profileDetailFragment;

  public static void launch(Context context, int source) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra(ACT_SOURCE, source);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }



  @Override
  protected void onInject() {



    setContentView(R.layout.activity_main);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    Toolbar toolbar = findViewById(R.id.main_toolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      Drawable drawable = getResources().getDrawable(R.drawable.ic_insta_camera);
      getSupportActionBar().setHomeAsUpIndicator(drawable);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    ProfileDataSource profileDataSource = new ProfileFireDataSource();
    HomeDataSource homeDataSource = new HomeFireDataSource();

    profilePresenter = new ProfilePresenter(profileDataSource);
    homePresenter = new HomePresenter(homeDataSource);

    homeFragment = HomeFragment.newInstance(this, homePresenter);
    profileFragment = ProfileFragment.newInstance(this, profilePresenter);

    SearchDataSource searchDataSource = new SearchFireDataSource();
    searchPresenter = new SearchPresenter(searchDataSource);

//    cameraFragment = new CameraFragment();
    searchFragment = SearchFragment.newInstance(this, searchPresenter);

    active = homeFragment;

    FragmentManager fm = getSupportFragmentManager();
    fm.beginTransaction().add(R.id.main_fragment, profileFragment).hide(profileFragment).commit();
//    fm.beginTransaction().add(R.id.main_fragment, cameraFragment).hide(cameraFragment).commit();
    fm.beginTransaction().add(R.id.main_fragment, searchFragment).hide(searchFragment).commit();
    fm.beginTransaction().add(R.id.main_fragment, homeFragment).commit();
  }

  @Override
  public void showProgressBar() {
    findViewById(R.id.main_progress).setVisibility(View.VISIBLE);
  }

  @Override
  public void hideProgressBar() {
    findViewById(R.id.main_progress).setVisibility(View.GONE);
  }

  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);

    BottomNavigationView bv = findViewById(R.id.main_bottom_nav);
    bv.setOnNavigationItemSelectedListener(this);

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      int source = extras.getInt(ACT_SOURCE);
      if (source == REGISTER_ACTIVITY) {
        getSupportFragmentManager().beginTransaction().hide(active).show(profileFragment).commit();
        active = profileFragment;
        scrollToolbarEnabled(true);
        profilePresenter.findUser();
      }
    }
  }

  @Override
  public void scrollToolbarEnabled(boolean enabled) {
    Toolbar toolbar = findViewById(R.id.main_toolbar);
    AppBarLayout appBarLayout = findViewById(R.id.main_appbar);

    AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
    CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();

    if (enabled) {
      params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
      appBarLayoutParams.setBehavior(new AppBarLayout.Behavior());
      appBarLayout.setLayoutParams(appBarLayoutParams);
    } else {
      params.setScrollFlags(0);
      appBarLayoutParams.setBehavior(null);
      appBarLayout.setLayoutParams(appBarLayoutParams);
    }
  }

  @Override
  public void showProfile(String user) {
    ProfileDataSource profileDataSource = new ProfileFireDataSource();
    ProfilePresenter profilePresenter = new ProfilePresenter(profileDataSource, user);

    profileDetailFragment = ProfileFragment.newInstance(this, profilePresenter);

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.add(R.id.main_fragment, profileDetailFragment, "detail");
    transaction.hide(active);
    transaction.commit();

    scrollToolbarEnabled(true);

    if (getSupportActionBar() != null) {
      Drawable drawable = findDrawable(R.drawable.ic_arrow_back);
      getSupportActionBar().setHomeAsUpIndicator(drawable);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override
  public void disposeProfileDetail() {
    if (getSupportActionBar() != null) {

      Drawable drawable = findDrawable(R.drawable.ic_insta_camera);
      getSupportActionBar().setHomeAsUpIndicator(drawable);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.remove(profileDetailFragment);
    transaction.show(active);
    transaction.commit();

    profileDetailFragment = null;
  }

  @Override
  public void logout() {
    LoginActivity.launch(this);
  }

  @Override
  protected int getLayout() {
    return R.layout.activity_main;
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
    FragmentManager fm = getSupportFragmentManager();
    switch (menuItem.getItemId()) {
      case R.id.menu_bottom_home:
        if (profileDetailFragment != null)
          disposeProfileDetail();

        fm.beginTransaction().hide(active).show(homeFragment).commit();
        active = homeFragment;
        homePresenter.findFeed();
        scrollToolbarEnabled(false);
        return true;

      case R.id.menu_bottom_search:
        if (profileDetailFragment == null) {
          fm.beginTransaction().hide(active).show(searchFragment).commit();
          active = searchFragment;
          scrollToolbarEnabled(false);
        }
        return true;

      case R.id.menu_bottom_add:
//        fm.beginTransaction().hide(active).show(cameraFragment).commit();
//        active = cameraFragment;
        if(askPermission()) AddActivity.launch(this);
        return true;

      case R.id.menu_bottom_profile:
        if (profileDetailFragment != null)
          disposeProfileDetail();

        fm.beginTransaction().hide(active).show(profileFragment).commit();
        active = profileFragment;
        scrollToolbarEnabled(true);
        profilePresenter.findUser();
        return true;
    }
    return false;
  }

  public boolean askPermission(){
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    if (!hasPermissions(this, PERMISSIONS)) {
      ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);


      return false;
    }

    return true;
  }

  public static boolean hasPermissions(Context context, String... permissions) {
    if (context != null && permissions != null) {
      for (String permission : permissions) {
        if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if(requestCode ==1) AddActivity.launch(this);
  }
}
