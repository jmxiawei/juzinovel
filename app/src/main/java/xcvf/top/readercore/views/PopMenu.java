package xcvf.top.readercore.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import top.iscore.freereader.R;
import top.iscore.freereader.fragment.adapters.BaseRecyclerAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.OnRecyclerViewItemClickListener;
import top.iscore.freereader.fragment.adapters.ViewHolderCreator;
import top.iscore.freereader.mode.SwitchModeListener;
import xcvf.top.readercore.bean.Category;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.bean.User;
import xcvf.top.readercore.styles.ModeProvider;
import xcvf.top.readercore.utils.Constant;

/**
 * 首页menu弹出
 * Created by xiaw on 2018/11/1.
 */
public class PopMenu extends PopupWindow {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    MenuAdapter adapter;
    SwitchModeListener switchModeListener;
    FragmentActivity fragmentActivity;
    public void setSwitchModeListener(SwitchModeListener switchModeListener) {
        this.switchModeListener = switchModeListener;
    }

    public PopMenu(Context context) {
        super(context);

        fragmentActivity = (FragmentActivity) context;
        Mode mode = ModeProvider.getCurrentMode();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pop_menu, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(0x99000000));
        setOutsideTouchable(true);
        setTouchable(true);
        ButterKnife.bind(this, view);
        View llcontent = view.findViewById(R.id.ll_content);
        if (mode == Mode.DayMode) {
            llcontent.setBackgroundResource(R.drawable.bg_menu_daymode);
        } else {
            llcontent.setBackgroundResource(R.drawable.bg_menu_nightmode);
        }
        getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        adapter = new MenuAdapter();
        recycler.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Category>() {
            @Override
            public void onRecyclerViewItemClick(CommonViewHolder holder, int position, Category item) {
                if (item.getId() == 1) {
                    Mode mode = ModeProvider.getCurrentMode();
                    Mode dest;
                    if (mode == Mode.DayMode) {
                        dest = Mode.NightMode;
                    } else {
                        dest = Mode.DayMode;
                    }
                    ModeProvider.save(-1, dest);
                    LocalBroadcastManager.getInstance(getContentView().getContext())
                            .sendBroadcast(new Intent().setAction(Constant.ACTION_SWITCH_MODE));
                }else if(item.getId() == 0){
                    LoginDialog loginDialog = new LoginDialog();
                    loginDialog.show(fragmentActivity.getSupportFragmentManager(),"LoginDialog");
                }
                dismiss();
            }
        });

        init();

    }

    private void init() {
        List<Category> list = new ArrayList<>();

        Category category = new Category();
        category.setId(0);
        User user = User.currentUser();
        if (user.getUid() > 0) {
            category.setName(user.getNickname());
            category.setResUrl(user.getAvatar());
            category.setResid(R.mipmap.ic_user);
        } else {
            category.setName("未登录");
            category.setResid(R.mipmap.ic_user);
        }
        list.add(category);

        Category modeCate = new Category();
        modeCate.setId(1);
        Mode mode = ModeProvider.getCurrentMode();
        if (mode == Mode.DayMode) {
            modeCate.setName("夜间模式");
            modeCate.setResid(R.mipmap.ic_mode_night);
        } else {
            modeCate.setName("日间模式");
            modeCate.setResid(R.mipmap.ic_mode_day);
        }

        list.add(modeCate);

        adapter.setDataList(list);

    }

    private static final class MenuAdapter extends BaseRecyclerAdapter<Category> {


        @Override
        public ViewHolderCreator createViewHolderCreator() {
            return new ViewHolderCreator() {
                @Override
                public CommonViewHolder createByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
                    return new CommonViewHolder<Category>(parent.getContext(), parent, R.layout.item_setting_menu) {
                        @Override
                        public void bindData(Category category, int position) {

                            ImageView imageView = itemView.findViewById(R.id.img);
                            TextView tvName = itemView.findViewById(R.id.tv_name);
                            tvName.setText(category.getName());
                            if (TextUtils.isEmpty(category.getResUrl())) {
                                imageView.setImageResource(category.getResid());
                            } else {
                                RoundedCornersTransformation roundedCornersTransformation
                                        = new RoundedCornersTransformation(itemView.getContext(), 10, 0);
                                Glide.with(itemView.getContext())
                                        .load(category.getResUrl())
                                        .placeholder(category.getResid())
                                        .bitmapTransform(roundedCornersTransformation)
                                        .into(imageView);
                            }


                            View view = itemView.findViewById(R.id.line_bottom);

                            if (position == getItemCount() - 1) {
                                view.setVisibility(View.INVISIBLE);
                            } else {
                                view.setVisibility(View.VISIBLE);
                            }

                        }
                    };
                }
            };
        }
    }

}
