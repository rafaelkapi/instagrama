package com.cactus.instagrama.main.search.presentation;

import com.cactus.instagrama.common.model.User;
import com.cactus.instagrama.common.presenter.Presenter;
import com.cactus.instagrama.main.presentation.MainView;
import com.cactus.instagrama.main.search.datasource.SearchDataSource;

import java.util.List;

public class SearchPresenter implements Presenter<List<User>> {

  private final SearchDataSource dataSource;
  private MainView.SearchView view;

  public SearchPresenter(SearchDataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void setView(MainView.SearchView view) {
    this.view = view;
  }

  public void findUsers(String newText) {
    dataSource.findUser(newText, this);
  }

  @Override
  public void onSuccess(List<User> response) {
    view.showUsers(response);
  }

  @Override
  public void onError(String message) {
    // TODO: 29/04/19
  }

  @Override
  public void onComplete() {
  }

}
