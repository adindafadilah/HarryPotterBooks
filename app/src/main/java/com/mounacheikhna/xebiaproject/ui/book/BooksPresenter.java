package com.mounacheikhna.xebiaproject.ui.book;

import com.mounacheikhna.xebiaproject.api.henripotier.HenriPotierAPi;
import com.mounacheikhna.xebiaproject.api.henripotier.model.Book;
import com.mounacheikhna.xebiaproject.ui.base.BasePresenter;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;

/**
 * Created by mouna on 02/12/15.
 */
@Singleton // singleton for now -> TODO: make it later scoped per component
public class BooksPresenter extends BasePresenter<BooksScreen> {

  private HenriPotierAPi mApi;

  @Inject public BooksPresenter(HenriPotierAPi api) {
    mApi = api;
  }

  public Observable<List<Book>> loadBooks() {
    return mApi.fetchBooks();
  }
}
