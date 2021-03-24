package com.cactus.instagrama.main.home.datasource;

import com.cactus.instagrama.common.model.Database;
import com.cactus.instagrama.common.model.Feed;
import com.cactus.instagrama.common.presenter.Presenter;

import java.util.List;


public class HomeLocalDataSource implements HomeDataSource {

  @Override
  public void findFeed(Presenter<List<Feed>> presenter) {
    Database db = Database.getInstance();
    db.findFeed(db.getUser().getUUID())
            .addOnSuccessListener(presenter::onSuccess)
            .addOnFailureListener(e -> presenter.onError(e.getMessage()))
            .addOnCompleteListener(presenter::onComplete);
  }

}
