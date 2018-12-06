package top.iscore.freereader.mvp.presenters;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.iscore.freereader.http.BaseHttpHandler;
import top.iscore.freereader.http.BaseModel;
import top.iscore.freereader.http.BookService;
import top.iscore.freereader.mvp.view.RankListView;
import xcvf.top.readercore.bean.Rank;

public class RankListPresenter extends MvpBasePresenter<RankListView> {


    /**
     * 排行
     */
    public void onloadRankList() {
        ifViewAttached(new ViewAction<RankListView>() {
            @Override
            public void run(@NonNull RankListView view) {
                view.showLoading(false);
            }
        });

        Call<BaseModel<ArrayList<Rank>>> call = BaseHttpHandler.create().getProxy(BookService.class).rankList("Book.ranklist");
        call.enqueue(new Callback<BaseModel<ArrayList<Rank>>>() {
            @Override
            public void onResponse(Call<BaseModel<ArrayList<Rank>>> call, Response<BaseModel<ArrayList<Rank>>> response) {

                if (response != null && response.isSuccessful()) {
                    final ArrayList<Rank> ranks = response.body().getData();

                    int size = ranks == null ? 0 : ranks.size();

                    ArrayList<Rank> gg_rank = new ArrayList<>();
                    ArrayList<Rank> mm_rank = new ArrayList<>();
                    ArrayList<Rank> other = new ArrayList<>();
                    final ArrayList<Rank> new_rank = new ArrayList<>();

                    for (int i = 0; i < size; i++) {
                        if ("mm".equals(ranks.get(i).getGender())) {
                            mm_rank.add(ranks.get(i));
                        } else if("gg".equals(ranks.get(i).getGender())) {
                            gg_rank.add(ranks.get(i));
                        }else {
                            other.add(ranks.get(i));
                        }
                    }


                    if (gg_rank.size() > 0) {
                        Rank rank = new Rank();
                        rank.setRankid(-1);
                        rank.setListname("男生最热榜单");
                        new_rank.add(rank);
                        new_rank.addAll(gg_rank);
                    }

                    if (mm_rank.size() > 0) {
                        Rank rank = new Rank();
                        rank.setRankid(-2);
                        rank.setListname("女生最热榜单");
                        new_rank.add(rank);
                        new_rank.addAll(mm_rank);
                    }

                    if (other.size() > 0) {
                        Rank rank = new Rank();
                        rank.setRankid(-3);
                        rank.setListname("其他榜单");
                        new_rank.add(rank);
                        new_rank.addAll(mm_rank);
                    }

                    ifViewAttached(new ViewAction<RankListView>() {
                        @Override
                        public void run(@NonNull RankListView view) {
                            view.setData(new_rank);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<BaseModel<ArrayList<Rank>>> call, Throwable t) {
                ifViewAttached(new ViewAction<RankListView>() {
                    @Override
                    public void run(@NonNull RankListView view) {
                        view.setData(null);
                    }
                });
            }
        });
    }
}
