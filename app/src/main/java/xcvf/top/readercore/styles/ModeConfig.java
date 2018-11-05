package xcvf.top.readercore.styles;

import android.os.Parcel;
import android.os.Parcelable;

import xcvf.top.readercore.bean.Mode;

/**日间模式和夜间模式配置
 * Created by xiaw on 2018/10/29.
 */
public class ModeConfig implements Parcelable {

    int textColorResId;
    int bgResId;
    Mode mode;
    int id;
    int notifyTxtColor;
    boolean checked = false;
    public int getId() {
        return id;
    }

    public boolean isChecked() {
        return checked;
    }

    public ModeConfig setChecked(boolean checked) {
        this.checked = checked;
        return this;
    }

    public ModeConfig setId(int id) {
        this.id = id;
        return this;
    }

    public int getTextColorResId() {
        return textColorResId;
    }

    public ModeConfig setTextColorResId(int textColorResId) {
        this.textColorResId = textColorResId;
        return this;
    }

    public int getBgResId() {
        return bgResId;
    }

    public ModeConfig setBgResId(int bgResId) {
        this.bgResId = bgResId;
        return this;
    }



    public Mode getMode() {
        return mode;
    }

    public ModeConfig setMode(Mode mode) {
        this.mode = mode;
        return this;
    }

    public int getNotifyTxtColor() {
        return notifyTxtColor;
    }

    public ModeConfig setNotifyTxtColor(int notifyTxtColor) {
        this.notifyTxtColor = notifyTxtColor;
        return this;
    }

    public ModeConfig() {
    }

    public static ModeConfig newInstance(int id,Mode mode,int bgResId,int textColorResId,int notifyTxtColor){
        ModeConfig config = new ModeConfig();
        config.setId(id);
        config.setMode(mode);
        config.setBgResId(bgResId);
        config.setTextColorResId(textColorResId);
        config.setNotifyTxtColor(notifyTxtColor);
        return config;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.textColorResId);
        dest.writeInt(this.bgResId);
        dest.writeInt(this.mode == null ? -1 : this.mode.ordinal());
        dest.writeInt(this.id);
    }

    protected ModeConfig(Parcel in) {
        this.textColorResId = in.readInt();
        this.bgResId = in.readInt();
        int tmpMode = in.readInt();
        this.mode = tmpMode == -1 ? null : Mode.values()[tmpMode];
        this.id = in.readInt();
    }

    public static final Creator<ModeConfig> CREATOR = new Creator<ModeConfig>() {
        @Override
        public ModeConfig createFromParcel(Parcel source) {
            return new ModeConfig(source);
        }

        @Override
        public ModeConfig[] newArray(int size) {
            return new ModeConfig[size];
        }
    };
}
