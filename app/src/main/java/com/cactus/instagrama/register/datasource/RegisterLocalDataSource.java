package com.cactus.instagrama.register.datasource;

import android.net.Uri;

import com.cactus.instagrama.common.model.Database;
import com.cactus.instagrama.common.model.UserAuth;
import com.cactus.instagrama.common.presenter.Presenter;


public class RegisterLocalDataSource implements RegisterDataSource {

  @Override
  public void createUser(String name, String email, String password, Presenter presenter) {
    Database.getInstance().createUser(name, email, password)
            .addOnSuccessListener((Database.OnSuccessListener<UserAuth>) presenter::onSuccess)
            .addOnFailureListener(e -> presenter.onError(e.getMessage()))
            .addOnCompleteListener(presenter::onComplete);
  }

  @Override
  public void addPhoto(Uri uri, Presenter presenter) {
    Database db = Database.getInstance();
    db.addPhoto(db.getUser().getUUID(), uri)
            .addOnSuccessListener((Database.OnSuccessListener<Boolean>) presenter::onSuccess);
  }

}
