package com.cactus.instagrama.main.camera.datasource;

import android.net.Uri;

import com.cactus.instagrama.common.model.Database;
import com.cactus.instagrama.common.presenter.Presenter;


public class AddLocalDataSource implements AddDataSource {

  @Override
  public void savePost(Uri uri, String caption, Presenter presenter) {
    Database db = Database.getInstance();
    db.createPost(db.getUser().getUUID(), uri, caption)
            .addOnSuccessListener((Database.OnSuccessListener<Void>) presenter::onSuccess)
            .addOnFailureListener(e -> presenter.onError(e.getMessage()))
            .addOnCompleteListener(presenter::onComplete);
  }

}
