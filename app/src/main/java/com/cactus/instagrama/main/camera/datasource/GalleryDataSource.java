package com.cactus.instagrama.main.camera.datasource;

import android.content.Context;

import com.cactus.instagrama.common.presenter.Presenter;


public interface GalleryDataSource {

  void findPictures(Context context, Presenter presenter);

}
