package xcvf.top.readercore.styles;

import android.content.Context;

import com.blankj.utilcode.util.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import top.iscore.freereader.R;
import xcvf.top.readercore.bean.Mode;
import xcvf.top.readercore.utils.Constant;

/**
 * 模式设置
 * Created by xiaw on 2018/10/29.
 */
public class ModeProvider {


    public static final String KET_ID = "MODE_KEY_ID";
    public static final String KET_MODE = "MODE_KEY_MODE";

    static HashMap<String, List<ModeConfig>> configList = new HashMap<>();

    static {
        addDayModeConfig();
        addNightModelConfig();
    }


    /**
     * 所有的配置
     *
     * @return
     */
    public static List<ModeConfig> getAllConfig() {
        List<ModeConfig> list = new ArrayList<>();
        list.addAll(configList.get(Mode.DayMode.toString()));
        list.addAll(configList.get(Mode.NightMode.toString()));


        for (ModeConfig config : list) {
            config.setChecked(false);
        }
        ModeConfig modeConfig = getModeConfig(ModeProvider.getCurrentMode());
        modeConfig.setChecked(true);
        return list;
    }


    /**
     * 拼接保存id的key
     *
     * @param mode
     * @return
     */
    private static String getKetId(Mode mode) {
        return mode.toInt() + KET_ID;
    }


    /**
     * 保存设置的模式
     *
     * @param id
     * @param mode
     */
    public static void save(int id, Mode mode) {
        if (id >= 0) {
            SPUtils.getInstance().put(getKetId(mode), id);
        }
        SPUtils.getInstance().put(KET_MODE, mode.toInt());
    }

    /**
     * 获取当前的模式
     *
     * @return
     */
    public static Mode getCurrentMode() {
        return Mode.from(SPUtils.getInstance().getInt(KET_MODE, 0));
    }

    public static int getCurrentModeTheme() {
        return getCurrentMode() == Mode.NightMode ? R.style.NightTheme : R.style.DayTheme;
    }


    public static ModeConfig get(Mode mode) {
        int id = SPUtils.getInstance().getInt(getKetId(mode), 0);
        return configList.get(mode.toString()).get(id);
    }

    public static ModeConfig getModeConfig(Mode mode) {
        int id = SPUtils.getInstance().getInt(getKetId(mode), 0);
        return configList.get(mode.toString()).get(id);
    }


    public static ModeConfig get(int id, Mode mode) {
        return configList.get(mode.toString()).get(id);
    }


    private static void addNightModelConfig() {
        List<ModeConfig> configs = new ArrayList<>();
        configs.add(ModeConfig.newInstance(0, Mode.NightMode, R.color.reader_styleclor4, R.color.reader_styletxtclor4, R.color.reader_light_notify_clor));
        configs.add(ModeConfig.newInstance(1, Mode.NightMode, R.color.reader_styleclor5, R.color.reader_styletxtclor5, R.color.reader_light_notify_clor));
        configList.put(Mode.NightMode.toString(), configs);
    }


    private static void addDayModeConfig() {
        List<ModeConfig> configs = new ArrayList<>();
        configs.add(ModeConfig.newInstance(0, Mode.DayMode, R.color.reader_styleclor1, R.color.reader_styletxtclor1, R.color.reader_dark_notify_clor));
        configs.add(ModeConfig.newInstance(1, Mode.DayMode, R.color.reader_styleclor2, R.color.reader_styletxtclor2, R.color.reader_dark_notify_clor));
        configs.add(ModeConfig.newInstance(2, Mode.DayMode, R.color.reader_styleclor3, R.color.reader_styletxtclor3, R.color.reader_dark_notify_clor));
        configList.put(Mode.DayMode.toString(), configs);
    }


}
