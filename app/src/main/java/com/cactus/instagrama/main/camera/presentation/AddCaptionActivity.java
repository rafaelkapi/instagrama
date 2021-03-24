package com.cactus.instagrama.main.camera.presentation;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;

import com.cactus.instagrama.R;
import com.cactus.instagrama.common.view.AbstractActivity;
import com.cactus.instagrama.main.camera.datasource.AddDataSource;
import com.cactus.instagrama.main.camera.datasource.AddFireDataSource;

import butterknife.BindView;


public class AddCaptionActivity extends AbstractActivity implements AddCaptionView {

  @BindView(R.id.main_add_caption_image_view)
  ImageView imageView;

  @BindView(R.id.main_add_caption_edit_text)
  EditText editText;

  @BindView(R.id.add_progress)
  ProgressBar progressBar;

  private Uri uri;
  private AddPresenter presenter;

  public static void launch(Context context, Uri uri) {
    Intent intent = new Intent(context, AddCaptionActivity.class);
    intent.putExtra("uri", uri);
    context.startActivity(intent);
  }



  @Override
  protected void onInject() {

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      Drawable drawable = findDrawable(R.drawable.ic_arrow_back);
      getSupportActionBar().setHomeAsUpIndicator(drawable);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }




    uri = getIntent().getExtras().getParcelable("uri");
    imageView.setImageURI(uri);

    AddDataSource dataSource = new AddFireDataSource();
    presenter = new AddPresenter(this, dataSource);
  }

  @Override
  public void showProgressBar() {
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideProgressBar() {
    progressBar.setVisibility(View.GONE);
  }

  @Override
  protected int getLayout() {
    return R.layout.activity_add_caption;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_share, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
      case R.id.action_share:
        presenter.createPost(uri, editText.getText().toString());
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void postSaved() {
    finish();
  }

}
