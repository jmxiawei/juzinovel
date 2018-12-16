package xcvf.top.readercore.holders;

import android.view.ViewGroup;
import android.widget.TextView;

import top.iscore.freereader.R;
import top.iscore.freereader.fragment.adapters.BaseRecyclerAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.ViewHolderCreator;
import xcvf.top.readercore.bean.AnimItem;
import xcvf.top.readercore.transformer.PageTransformerFactory;

public class PageAnimAdapter extends BaseRecyclerAdapter<AnimItem> {
    @Override
    public ViewHolderCreator createViewHolderCreator() {
        return new ViewHolderCreator() {
            @Override
            public CommonViewHolder<AnimItem> createByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
                return new CommonViewHolder<AnimItem>(parent.getContext(),parent, R.layout.item_page_anim) {


                    private void updateSystemBrightnessView(boolean select,TextView tv) {

                        if (!select) {
                            tv.setBackgroundResource(R.drawable.bg_rectange_shape);
                            tv.setTextColor(tv.getResources().getColor(R.color.color_white));
                        } else {
                            tv.setBackgroundResource(R.drawable.bg_rectange_orange_shape);
                            tv.setTextColor(tv.getResources().getColor(R.color.colorAccent));
                        }
                    }

                    @Override
                    public void bindData(AnimItem animItem, int position) {
                        AnimItem animItem1 = PageTransformerFactory.get();
                        TextView tv = itemView.findViewById(R.id.tv);
                        updateSystemBrightnessView(animItem.key.equals(animItem1.key),tv);
                        tv.setText(animItem.name);

                    }
                };
            }
        };
    }
}
