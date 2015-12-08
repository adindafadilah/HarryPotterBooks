package com.mounacheikhna.harrypotterbooks.ui.book;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mounacheikhna.harrypotterbooks.R;
import com.mounacheikhna.harrypotterbooks.api.harrypotter.model.Book;
import com.mounacheikhna.harrypotterbooks.ui.base.BindableBookItem;
import com.mounacheikhna.harrypotterbooks.util.PriceFormatter;
import com.squareup.picasso.Picasso;

/**
 * Created by mouna on 02/12/15.
 */
public class BookItemView extends RelativeLayout implements BindableBookItem {

  @Bind(R.id.book_image) ImageView mImageView;
  @Bind(R.id.book_name) TextView mNameView;
  @Bind(R.id.book_author) TextView mAuthorView;
  @Bind(R.id.book_price) TextView mBookPriceView;

  public BookItemView(Context context) {
    super(context);
  }

  public BookItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void bindTo(Book book, Picasso picasso) {
    mNameView.setText(book.getTitle());
    mBookPriceView.setText(PriceFormatter.formatEuro(book.getPrice()));
    if (!TextUtils.isEmpty(book.getCover())) {
      picasso.load(book.getCover())
          //.placeholder(R.drawable.avatar)
          //.error(R.drawable.avatar)
          .fit().into(mImageView);
    }
  }

  @Override public View getImageView() {
    return mImageView;
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }
}
