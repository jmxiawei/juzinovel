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
     * 获取日间模式默认显示
     *
     * @return
     */
    public static ModeConfig getDayDefault() {
        int id = SPUtils.getInstance().getInt(getKetId(Mode.DayMode), 0);
        return configList.get(Mode.DayMode.toString()).get(id);
    }

    /**
     * 获取夜间模式默认显示
     *
     * @return
     */
    public static ModeConfig getNightDefault() {
        int id = SPUtils.getInstance().getInt(getKetId(Mode.NightMode), 0);
        return configList.get(Mode.NightMode.toString()).get(id);
    }


    /**
     * 拼接保存id的key
     * @param mode
     * @return
     */
    public static String getKetId(Mode mode) {
        return mode.toInt() + KET_ID;
    }


    /**
     * 保存设置的模式
     * @param id
     * @param mode
     */
    public static void save(int id, Mode mode) {
        SPUtils.getInstance().put(getKetId(mode), id);
        SPUtils.getInstance().put(KET_MODE, mode.toInt());
    }

    /**
     * 获取当前的模式
     * @return
     */
    public static Mode getCurrentMode() {
        return Mode.from(SPUtils.getInstance().getInt(KET_MODE, 0));
    }

    public static ModeConfig get(Mode mode) {
        int id = SPUtils.getInstance().getInt(getKetId(mode), 0);
        return configList.get(mode.toString()).get(id);
    }


    public static ModeConfig get(int id, Mode mode) {
        return configList.get(mode.toString()).get(id);
    }


    private static void addNightModelConfig() {
        List<ModeConfig> configs = new ArrayList<>();
        configs.add(ModeConfig.newInstance(0, Mode.NightMode, R.color.reader_styleclor4, R.color.reader_styletxtclor4));
        configs.add(ModeConfig.newInstance(1, Mode.NightMode, R.color.reader_styleclor5, R.color.reader_styletxtclor5));
        configList.put(Mode.NightMode.toString(), configs);
    }


    private static void addDayModeConfig() {
        List<ModeConfig> configs = new ArrayList<>();
        configs.add(ModeConfig.newInstance(0, Mode.DayMode, R.color.reader_styleclor1, R.color.reader_styletxtclor1));
        configs.add(ModeConfig.newInstance(1, Mode.DayMode, R.color.reader_styleclor2, R.color.reader_styletxtclor2));
        configs.add(ModeConfig.newInstance(2, Mode.DayMode, R.color.reader_styleclor3, R.color.reader_styletxtclor3));
        configList.put(Mode.DayMode.toString(), configs);
    }


}
