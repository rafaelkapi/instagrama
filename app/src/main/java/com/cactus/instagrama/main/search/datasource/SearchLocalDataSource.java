package com.cactus.instagrama.main.search.datasource;

import com.cactus.instagrama.common.model.Database;
import com.cactus.instagrama.common.model.User;
import com.cactus.instagrama.common.presenter.Presenter;

import java.util.List;


public class SearchLocalDataSource implements SearchDataSource {

  @Override
  public void findUser(String query, Presenter<List<User>> presenter) {
    Database db = Database.getInstance();
    db.findUsers(db.getUser().getUUID(), query)
            .addOnSuccessListener(presenter::onSuccess)
            .addOnFailureListener(e -> presenter.onError(e.getMessage()))
            .addOnCompleteListener(presenter::onComplete);
  }

}
