package com.mounacheikhna.xebiaproject.ui.book;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mounacheikhna.xebiaproject.R;
import com.mounacheikhna.xebiaproject.api.model.Book;
import com.squareup.picasso.Picasso;

/**
 * Created by mouna on 02/12/15.
 */
public class BookItemView extends RelativeLayout {//maybe extend CardView instead ?

  @Bind(R.id.book_image) ImageView mImageView;
  @Bind(R.id.book_name) TextView mNameView;
  @Bind(R.id.book_author) TextView mAuthorView;

  public BookItemView(Context context) {
    super(context);
  }

  public BookItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void bindTo(Book book, Picasso picasso) {
    mNameView.setText(book.getTitle());
    //mAuthorView.setText(book.get);
    if (!TextUtils.isEmpty(book.getCover())) {
      picasso.load(book.getCover())
          //.placeholder(R.drawable.ic_city)
          //.error(R.drawable.ic_city)
          .fit()
          .into(mImageView);
    }
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }
}
