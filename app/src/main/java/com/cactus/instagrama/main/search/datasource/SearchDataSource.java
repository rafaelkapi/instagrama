package com.cactus.instagrama.main.search.datasource;


import com.cactus.instagrama.common.model.User;
import com.cactus.instagrama.common.presenter.Presenter;

import java.util.List;

public interface SearchDataSource {

  void findUser(String query, Presenter<List<User>> presenter);


}
