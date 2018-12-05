package top.iscore.freereader;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.iscore.freereader.adapter.SpaceItemDivider;
import top.iscore.freereader.fragment.adapters.BaseRecyclerAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.OnRecyclerViewItemClickListener;
import top.iscore.freereader.fragment.adapters.ViewHolderCreator;
import top.iscore.freereader.mode.Colorful;
import top.iscore.freereader.mode.setter.ViewBackgroundColorSetter;
import top.iscore.freereader.mode.setter.ViewGroupSetter;
import top.iscore.freereader.mvp.presenters.RankListPresenter;
import top.iscore.freereader.mvp.view.RankListView;
import xcvf.top.readercore.bean.Category;
import xcvf.top.readercore.bean.Rank;
import xcvf.top.readercore.styles.ModeProvider;

/**
 * 排行榜
 */
public class RankListActivity extends MvpActivity<RankListView, RankListPresenter> implements RankListView {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    RankAdapter mAdapter;

    @NonNull
    @Override
    public RankListPresenter createPresenter() {
        return new RankListPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_list);
        ButterKnife.bind(this);
        presenter.attachView(this);
        presenter.onloadRankList();
        recycler.addItemDecoration(new SpaceItemDivider());
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new RankAdapter();
        recycler.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Rank>() {
            @Override
            public void onRecyclerViewItemClick(CommonViewHolder holder, int position, Rank item) {
                if(item.getRankid() > 0){
                    BookListActivity.toBookList(RankListActivity.this, item.listname, 2, item.rankid + "");
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        changeMode();
    }

    private void changeMode() {

        new Colorful.Builder(this).setter(new ViewBackgroundColorSetter(llTitle, R.attr.colorAccent))
                .setter(new ViewGroupSetter(recycler, R.attr.colorPrimary)
                        .childViewTextColor(R.id.tv_name, R.attr.text_color)
                )
                .create()
                .setTheme(ModeProvider.getCurrentModeTheme());
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void showLoading(boolean pullToRefresh) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {

    }

    @Override
    public void setData(List<Rank> data) {
        mAdapter.setDataList(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {

    }


    public static final class RankAdapter extends BaseRecyclerAdapter<Rank> {

        static final int TYPE_HEADER = 0;
        static final int TYPE_ITEM = 1;


        @Override
        public int getItemViewType(int position) {
            Rank rank = getItem(position);
            if (rank.rankid <= 0) {
                return TYPE_HEADER;
            }
            return TYPE_ITEM;
        }

        @Override
        public ViewHolderCreator createViewHolderCreator() {
            return new ViewHolderCreator() {
                @Override
                public CommonViewHolder createByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {

                    if (viewType == TYPE_HEADER) {
                        return new CommonViewHolder<Rank>(parent.getContext(), parent, R.layout.item_holder_rank_header) {
                            @Override
                            public void bindData(Rank o, int position) {
                                TextView tv = itemView.findViewById(R.id.tv_name);
                                tv.setText(o.listname);
                            }
                        };
                    } else {
                        return new CommonViewHolder<Rank>(parent.getContext(), parent, R.layout.item_holder_rank_item) {
                            @Override
                            public void bindData(Rank o, int position) {

                                TextView tv = itemView.findViewById(R.id.tv_name);
                                TextView tv_source = itemView.findViewById(R.id.tv_source);
                                tv_source.setText(o.source);
                                ImageView img = itemView.findViewById(R.id.img);
                                Glide.with(itemView.getContext()).load(o.icon).placeholder(R.color.text_gray_light).into(img);
                                tv.setText(o.listname);
                            }
                        };
                    }
                }
            };
        }
    }


}
