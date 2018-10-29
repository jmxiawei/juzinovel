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

    public int getId() {
        return id;
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

    public ModeConfig() {
    }

    public static ModeConfig newInstance(int id,Mode mode,int bgResId,int textColorResId){
        ModeConfig config = new ModeConfig();
        config.setId(id);
        config.setMode(mode);
        config.setBgResId(bgResId);
        config.setTextColorResId(textColorResId);
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
