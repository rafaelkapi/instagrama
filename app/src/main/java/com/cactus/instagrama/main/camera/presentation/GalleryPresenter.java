package com.cactus.instagrama.main.camera.presentation;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.cactus.instagrama.common.presenter.Presenter;
import com.cactus.instagrama.main.camera.datasource.GalleryDataSource;

import java.util.ArrayList;
import java.util.List;



public class GalleryPresenter implements Presenter<List<String>> {

  private final GalleryDataSource dataSource;
  private GalleryView view;

  public GalleryPresenter(GalleryDataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void setView(GalleryView view) {
    this.view = view;
  }

  public void findPictures(Context context) {
    view.showProgressBar();
    dataSource.findPictures(context, this);
  }

  @Override
  public void onSuccess(List<String> response) {
    ArrayList<Uri> uris = new ArrayList<>();
    for (String res : response) {
      Uri uri = Uri.parse(res);
      uris.add(uri);
    }
    view.onPicturesLoaded(uris);
  }

  @Override
  public void onError(String message) {
    Log.d("Teste", " Aqui " + message);
  }

  @Override
  public void onComplete() {
    view.hideProgressBar();
  }
}
