package xcvf.top.readercore.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import top.iscore.freereader.R;
import top.iscore.freereader.mode.Colorful;
import top.iscore.freereader.mode.setter.TextColorSetter;
import top.iscore.freereader.mode.setter.ViewBackgroundColorSetter;
import xcvf.top.readercore.styles.ModeProvider;

/**
 * 带确认和取消按钮的
 */
public class ContentDialog extends DialogFragment {


    @BindView(R.id.tv_book_name)
    TextView tvBookName;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    Unbinder unbinder;


    String title;
    String content;
    @BindView(R.id.tv_content)
    TextView tvContent;

    private View.OnClickListener negativeListener;
    private View.OnClickListener positiveListener;

    String confirmTxt;
    String cancelTxt;

    public View.OnClickListener getNegativeListener() {
        return negativeListener;
    }

    public ContentDialog setNegativeListener(View.OnClickListener negativeListener) {
        this.negativeListener = negativeListener;
        return this;
    }

    public View.OnClickListener getPositiveListener() {
        return positiveListener;
    }

    public ContentDialog setPositiveListener(View.OnClickListener positiveListener) {
        this.positiveListener = positiveListener;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ContentDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ContentDialog setContent(String content) {
        this.content = content;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_content, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tvBookName.setText(title);
        tvContent.setText(content);
        if (negativeListener != null) {
            tvCancel.setVisibility(View.VISIBLE);
        } else {
            tvCancel.setVisibility(View.GONE);
        }

        if (positiveListener != null) {
            tvOk.setVisibility(View.VISIBLE);
        } else {
            tvOk.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        changeMode();
    }

    private void changeMode() {

        new Colorful.Builder(getActivity()).setter(new ViewBackgroundColorSetter(getView(), R.attr.colorPrimary))
                .setter(new TextColorSetter(tvBookName, R.attr.colorAccent))
                .setter(new TextColorSetter(tvCancel, R.attr.text_second_color))
                .setter(new TextColorSetter(tvContent,R.attr.text_color))
                .setter(new TextColorSetter(tvOk, R.attr.colorAccent)).create().setTheme(ModeProvider.getCurrentModeTheme());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_cancel, R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                if (negativeListener != null) {
                    negativeListener.onClick(view);
                }
                dismiss();
                break;
            case R.id.tv_ok:
                if (positiveListener != null) {
                    positiveListener.onClick(view);
                }
                dismiss();
                break;
            default:
                break;
        }
    }
}
