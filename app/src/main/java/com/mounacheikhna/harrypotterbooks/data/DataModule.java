package com.mounacheikhna.harrypotterbooks.data;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.mounacheikhna.harrypotterbooks.data.prefs.GsonPreferenceAdapter;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by mouna on 05/12/15.
 *
 * Provides main data related dependencies.
 */
@Module public class DataModule {

  @Provides @Singleton @Named("cart")
  public Preference<Cart> provideCart(RxSharedPreferences rxPrefs, Gson gson) {
    GsonPreferenceAdapter<Cart> adapter = new GsonPreferenceAdapter<>(gson, Cart.class, null,
        GsonPreferenceAdapter.SyntaxExceptionBehavior.NULL);
    return rxPrefs.getObject("cart", null, adapter);
  }
}
