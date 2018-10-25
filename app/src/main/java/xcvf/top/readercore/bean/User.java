package xcvf.top.readercore.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * 用户
 * Created by xiaw on 2018/10/25.
 */

public class User  extends SugarRecord implements Parcelable {

    String nickname;
    String uid;
    String avatar;
    String account;
    String gender;
    String token;





    public String getNickname() {
        return nickname;
    }

    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public User setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public User setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public User setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public User setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getToken() {
        return token;
    }

    public User setToken(String token) {
        this.token = token;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nickname);
        dest.writeString(this.uid);
        dest.writeString(this.avatar);
        dest.writeString(this.account);
        dest.writeString(this.gender);
        dest.writeString(this.token);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.nickname = in.readString();
        this.uid = in.readString();
        this.avatar = in.readString();
        this.account = in.readString();
        this.gender = in.readString();
        this.token = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
