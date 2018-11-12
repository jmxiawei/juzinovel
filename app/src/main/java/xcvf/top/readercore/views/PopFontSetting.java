package xcvf.top.readercore.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.iscore.freereader.R;
import top.iscore.freereader.fragment.adapters.BaseRecyclerAdapter;
import top.iscore.freereader.fragment.adapters.CommonViewHolder;
import top.iscore.freereader.fragment.adapters.OnRecyclerViewItemClickListener;
import top.iscore.freereader.fragment.adapters.ViewHolderCreator;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.bean.TextConfig;
import xcvf.top.readercore.impl.BrightnessHandler;
import xcvf.top.readercore.interfaces.OnTextConfigChangedListener;
import xcvf.top.readercore.styles.ModeConfig;
import xcvf.top.readercore.styles.ModeProvider;

/**
 * 字体设置
 * Created by xiaw on 2018/11/1.
 */
public class PopFontSetting extends PopupWindow {


    public static final int MIN_BRIGHTNESS = 10;

    public static final int MAX_BRIGHTNESS = 240;


    @BindView(R.id.seek_brightness)
    SeekBar seekBrightness;
    @BindView(R.id.tv_brightness_system)
    TextView tvBrightnessSystem;
    @BindView(R.id.tv_font_reduce)
    TextView tvFontReduce;
    @BindView(R.id.tv_font)
    TextView tvFont;
    @BindView(R.id.tv_font_add)
    TextView tvFontAdd;
    @BindView(R.id.style_recycler)
    RecyclerView styleRecycler;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    ModelColorAdapter modelColorAdapter;
    ModeConfig mModeConfig;
    OnTextConfigChangedListener onTextConfigChangedListener;
    @BindView(R.id.img_min_brightness)
    ImageView imgMinBrightness;
    @BindView(R.id.img_max_brightness)
    ImageView imgMaxBrightness;
    Activity mActivity;

    public PopFontSetting setOnTextConfigChangedListener(OnTextConfigChangedListener onTextConfigChangedListener) {
        this.onTextConfigChangedListener = onTextConfigChangedListener;
        return this;
    }

    public PopFontSetting(Context context) {
        super(context);
        mActivity = (Activity) context;
        Mode mode = ModeProvider.getCurrentMode();
        mModeConfig = ModeProvider.getModeConfig(mode);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_font_setting, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(0x22000000));
        setOutsideTouchable(true);
        setTouchable(true);
        ButterKnife.bind(this, view);
        llContent.setBackgroundResource(R.drawable.bg_menu_nightmode);
        initStyle();
        initBrightnessView();
        getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initBrightnessView() {
        int brightness = BrightnessHandler.getScreenBrightness(mActivity);
        seekBrightness.setMax(255);
        seekBrightness.setProgress(brightness);
        seekBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void showBrightness(float progress) {
        seekBrightness.setProgress((int) progress);
        BrightnessHandler.setWindowBrightness(mActivity, progress);
    }

    private void initStyle() {
        tvFont.setText(TextConfig.getConfig().getTextSize() + "");
        modelColorAdapter = new ModelColorAdapter();
        styleRecycler.setLayoutManager(new GridLayoutManager(getContentView().getContext(), 5));
        modelColorAdapter.setDataList(ModeProvider.getAllConfig());
        modelColorAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<ModeConfig>() {
            @Override
            public void onRecyclerViewItemClick(CommonViewHolder holder, int position, ModeConfig item) {

                for (ModeConfig config :
                        modelColorAdapter.getDataList()) {
                    if (config.getMode() == item.getMode()
                            && config.getId() == item.getId()) {
                        config.setChecked(true);
                    } else {
                        config.setChecked(false);
                    }
                }
                modelColorAdapter.notifyDataSetChanged();
                TextConfig.getConfig().setBackgroundColor(item.getBgResId());
                TextConfig.getConfig().setTextColor(item.getTextColorResId());
                if (onTextConfigChangedListener != null) {
                    onTextConfigChangedListener.onChanged(TextConfig.TYPE_FONT_COLOR);
                }
            }
        });
        styleRecycler.setAdapter(modelColorAdapter);
    }

    @OnClick({R.id.tv_font_reduce, R.id.tv_font_add, R.id.img_min_brightness, R.id.img_max_brightness, R.id.tv_brightness_system})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_font_reduce:
                TextConfig config = TextConfig.getConfig();
                config.setTextSize(config.getTextSize() - 1);
                tvFont.setText(config.getTextSize() + "");
                if (onTextConfigChangedListener != null) {
                    onTextConfigChangedListener.onChanged(TextConfig.TYPE_FONT_SIZE);
                }
                break;
            case R.id.tv_font_add:
                TextConfig configadd = TextConfig.getConfig();
                configadd.setTextSize(configadd.getTextSize() + 1);
                tvFont.setText(configadd.getTextSize() + "");
                if (onTextConfigChangedListener != null) {
                    onTextConfigChangedListener.onChanged(TextConfig.TYPE_FONT_SIZE);
                }
                break;
            case R.id.img_min_brightness:
                showBrightness(MIN_BRIGHTNESS);
                break;
            case R.id.img_max_brightness:
                showBrightness(MAX_BRIGHTNESS);
                break;
            case R.id.tv_brightness_system:
                int brightness1 = BrightnessHandler.getScreenBrightness(mActivity);
                //float brightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
                showBrightness(brightness1);
                break;
            default:
                break;
        }
    }


    /***
     * 模式颜色选择
     */
    public static class ModelColorAdapter extends BaseRecyclerAdapter<ModeConfig> {

        @Override
        public ViewHolderCreator createViewHolderCreator() {
            return new ViewHolderCreator() {
                @Override
                public CommonViewHolder<ModeConfig> createByViewGroupAndType(ViewGroup parent, int viewType, Object... p) {
                    return new CommonViewHolder<ModeConfig>(parent.getContext(), parent, R.layout.item_mode_config) {
                        @Override
                        public void bindData(ModeConfig modeConfig, int position) {
                            View view = itemView.findViewById(R.id.ll_bg);
                            ImageView img = itemView.findViewById(R.id.img);
                            ImageView img_color = itemView.findViewById(R.id.img_color);

                            if (modeConfig.isChecked()) {
                                img.setVisibility(View.VISIBLE);
                            } else {
                                img.setVisibility(View.INVISIBLE);
                            }
                            img_color.setImageResource(modeConfig.getBgResId());
                        }
                    };
                }
            };
        }
    }
}
