package top.iscore.freereader.adapter.holders;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import top.iscore.freereader.R;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import xcvf.top.readercore.bean.Book;

/**
 * 推荐书籍
 * Created by xiaw on 2018/9/18.
 */
public class PriorityBookHolder extends CommonViewHolder<Book> {


    @BindView(R.id.img_cover)
    ImageView imgCover;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.book_content)
    LinearLayout llContent;

    public PriorityBookHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.item_holder_priority_book);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindData(Book book, int position) {

        RoundedCornersTransformation roundedCornersTransformation
                = new RoundedCornersTransformation(itemView.getContext(), 10, 0);
        Glide.with(itemView.getContext()).load(book.cover).placeholder(R.color.text_gray_light).bitmapTransform(roundedCornersTransformation).into(imgCover);
        tvName.setText(Html.fromHtml(book.name));

    }
}
