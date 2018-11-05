package xcvf.top.readercore.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
    int id;
    String name;
    int resid;
    String resUrl;
    int intValue;
    public int getId() {
        return id;
    }

    public int getIntValue() {
        return intValue;
    }

    public Category setIntValue(int intValue) {
        this.intValue = intValue;
        return this;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResid() {
        return resid;
    }

    public void setResid(int resid) {
        this.resid = resid;
    }

    public String getResUrl() {
        return resUrl;
    }

    public void setResUrl(String resUrl) {
        this.resUrl = resUrl;
    }

    public Category(int id, String name, int resid) {
        this.id = id;
        this.name = name;
        this.resid = resid;
    }


    public Category() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.resid);
        dest.writeString(this.resUrl);
        dest.writeInt(this.intValue);
    }

    protected Category(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.resid = in.readInt();
        this.resUrl = in.readString();
        this.intValue = in.readInt();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
