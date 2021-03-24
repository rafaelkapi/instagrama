package com.cactus.instagrama.main.camera.presentation;

import android.net.Uri;

import com.cactus.instagrama.common.presenter.Presenter;
import com.cactus.instagrama.main.camera.datasource.AddDataSource;


public class AddPresenter implements Presenter<Void> {

  private final AddCaptionView view;
  private final AddDataSource dataSource;

  public AddPresenter(AddCaptionView view, AddDataSource dataSource) {
    this.view = view;
    this.dataSource = dataSource;
  }

  void createPost(Uri uri, String caption) {
    view.showProgressBar();
    dataSource.savePost(uri, caption, this);
  }

  @Override
  public void onSuccess(Void response) {
    view.postSaved();
  }

  @Override
  public void onError(String message) {
    // TODO: 29/04/19
  }

  @Override
  public void onComplete() {
    view.hideProgressBar();
  }

}
