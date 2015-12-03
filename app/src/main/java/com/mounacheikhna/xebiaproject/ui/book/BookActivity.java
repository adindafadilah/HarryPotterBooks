package com.mounacheikhna.xebiaproject.ui.book;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Transition;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mounacheikhna.xebiaproject.HenriPotierApp;
import com.mounacheikhna.xebiaproject.R;
import com.mounacheikhna.xebiaproject.api.model.Book;
import com.mounacheikhna.xebiaproject.transition.MophFabDialogHelper;
import com.mounacheikhna.xebiaproject.ui.buy.BuyBook;
import com.mounacheikhna.xebiaproject.util.Animations.EmptyTransitionListener;
import com.mounacheikhna.xebiaproject.util.Colors;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

import static com.mounacheikhna.xebiaproject.util.ApiLevels.isAtLeastLollipop;
import static com.mounacheikhna.xebiaproject.util.ApiLevels.isAtLeastM;

/**
 * Created by cheikhnamouna on 12/3/15.
 */
public class BookActivity extends AppCompatActivity {

  private static final String BOOK_EXTRA = "book_extra";
  private static final int REQUEST_CODE_BUY_BOOK = 1;

  @Bind(R.id.book_image) ImageView mBookImage;
  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout;
  @Bind(R.id.book_fab) FloatingActionButton mBookFab;

  @Inject Picasso mPicasso;

  private Transition.TransitionListener mReturnTransitionListener = new EmptyTransitionListener() {
    @SuppressLint("NewApi") @Override public void onTransitionStart(Transition transition) {
      super.onTransitionStart(transition);
      mBookFab.setVisibility(View.INVISIBLE);
    }
  };
  private int mFabColor;

  public static Intent getIntent(Context context, Book book) {
    Intent intent = new Intent(context, BookActivity.class);
    intent.putExtra(BOOK_EXTRA, book);
    return intent;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.book_activity);

    HenriPotierApp.get(this).getComponent().injectBooksActivity(this);
    ButterKnife.bind(this);

    mFabColor = ContextCompat.getColor(this, R.color.accent);
    final Book book = getIntent().getParcelableExtra(BOOK_EXTRA);
    setupToolbar();
    setupTransitions();
    displayBook(book);
  }

  @SuppressLint("NewApi") private void displayBook(Book book) {
    mCollapsingToolbarLayout.setTitle(book.getTitle());

    if (isAtLeastLollipop()) {
      postponeEnterTransition();
      mBookImage.getViewTreeObserver()
          .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override public boolean onPreDraw() {
              mBookImage.getViewTreeObserver().removeOnPreDrawListener(this);
              enterAnimation();
              startPostponedEnterTransition();
              return true;
            }
          });
    }

    if (!TextUtils.isEmpty(book.getCover())) {
      mPicasso.load(book.getCover())
          //.placeholder(R.drawable.people) //temp
          //.error(R.drawable.people) //temp
          .fit().into(mBookImage, new Callback() {
        @Override public void onSuccess() {
          final Bitmap bitmap = ((BitmapDrawable) mBookImage.getDrawable()).getBitmap();
          Palette.from(bitmap)
              .maximumColorCount(3)
              .clearFilters()
              .generate(new Palette.PaletteAsyncListener() {
                @Override public void onGenerated(Palette palette) {
                  boolean isDark;
                  final int lightness = Colors.isDark(palette);
                  if (lightness == Colors.LIGHTNESS_UNKNOWN) {
                    isDark = Colors.isDark(bitmap, bitmap.getWidth() / 2, 0);
                  } else {
                    isDark = lightness == Colors.IS_DARK;
                  }
                  applyToStatusBar(isDark, palette);
                  applyToToolbar(palette);
                  applyToFab(palette);
                }
              });
        }

        @Override public void onError() {
        }
      });
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) private void enterAnimation() {
    ObjectAnimator showFab = ObjectAnimator.ofPropertyValuesHolder(mBookFab,
        PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f),
        PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f),
        PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, 1f));
    showFab.setStartDelay(300L);
    showFab.setDuration(300L);
    showFab.setInterpolator(
        AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in));
    showFab.start();
  }

  private void setupToolbar() {
    setSupportActionBar(mToolbar);
  }

  @SuppressLint("NewApi") private void setupTransitions() {
    if (isAtLeastLollipop()) {
      setExitSharedElementCallback(new SharedElementCallback() {
        @Override public Parcelable onCaptureSharedElementSnapshot(View sharedElement,
            Matrix viewToGlobalMatrix, RectF screenBounds) {
          int bitmapWidth = Math.round(screenBounds.width());
          int bitmapHeight = Math.round(screenBounds.height());
          Bitmap bitmap = null;
          if (bitmapWidth > 0 && bitmapHeight > 0) {
            bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
            sharedElement.draw(new Canvas(bitmap));
          }
          return bitmap;
        }
      });
      getWindow().getSharedElementReturnTransition().addListener(mReturnTransitionListener);
    }
  }

  private void applyToFab(Palette palette) {
    Palette.Swatch topColor = palette.getVibrantSwatch();
    if (topColor == null) return;
    mFabColor = topColor.getRgb();
    mBookFab.setBackgroundTintList(ColorStateList.valueOf(mFabColor));
  }

  /**
   * Applies palette to status bar with a light or dark color on M
   * and with status bar icons matching the same color.
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP) private void applyToStatusBar(Boolean isDark,
      Palette palette) {
    int statusBarColor = getWindow().getStatusBarColor();
    Palette.Swatch topColor = Colors.getMostPopulousSwatch(palette);
    if (topColor != null && (isDark || isAtLeastM())) {
      statusBarColor = Colors.scrimify(topColor.getRgb(), isDark, 0.075f);
      if (!isDark && isAtLeastM()) {
        setLightStatusBar(mBookImage);
      }
    }

    if (statusBarColor != getWindow().getStatusBarColor()) {
      ValueAnimator statusBarColorAnim =
          ValueAnimator.ofArgb(getWindow().getStatusBarColor(), statusBarColor);
      statusBarColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override public void onAnimationUpdate(ValueAnimator animation) {
          getWindow().setStatusBarColor((Integer) animation.getAnimatedValue());
        }
      });
      statusBarColorAnim.setDuration(1000);
      statusBarColorAnim.setInterpolator(
          AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in));
      statusBarColorAnim.start();
    }
  }

  @TargetApi(Build.VERSION_CODES.M) private void setLightStatusBar(View view) {
    if (isAtLeastM()) {
      int flags = view.getSystemUiVisibility();
      flags = flags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
      view.setSystemUiVisibility(flags);
    }
  }

  private void applyToToolbar(Palette palette) {
    int primaryDark = ContextCompat.getColor(this, R.color.primary_dark);
    int primary = ContextCompat.getColor(this, R.color.primary);
    if (mCollapsingToolbarLayout == null) {
      mToolbar.setBackgroundColor(palette.getMutedColor(primary));
    } else {
      mCollapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
      mCollapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
    }
  }

  @OnClick(R.id.book_fab) @TargetApi(Build.VERSION_CODES.LOLLIPOP) public void onBookFabClick() {
    Intent buyIntent = new Intent(this, BuyBook.class);
    if (isAtLeastLollipop()) {
      buyIntent.putExtra(MophFabDialogHelper.EXTRA_SHARED_ELEMENT_START_COLOR, mFabColor);
      ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, mBookFab,
          getString(R.string.transition_buy_book));
      startActivity(buyIntent, /*REQUEST_CODE_BUY_BOOK, */ options.toBundle());
    } else {
      startActivity(buyIntent);
    }
  }
}
