package xcvf.top.readercore.bean;

import android.os.Parcel;
import android.os.Parcelable;


import org.greenrobot.greendao.annotation.Entity;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

import xcvf.top.readercore.daos.DBManager;
import xcvf.top.readercore.daos.UserDao;

/**
 * 用户
 * Created by xiaw on 2018/10/25.
 */
@Entity
public class User implements Parcelable {

    String nickname;
    @Unique
    int uid;
    String avatar;
    String account;
    String gender;
    String token;
    String update_time;

    public String getUpdate_time() {
        return update_time;
    }

    public User setUpdate_time(String update_time) {
        this.update_time = update_time;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public  int getUid() {
        return uid;
    }

    public User setUid(int uid) {
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

    public static User currentUser() {
        User user = DBManager.getDaoSession().getUserDao().queryBuilder()
                .orderDesc(UserDao.Properties.Update_time).limit(1).build().unique();
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public void save() {
        update_time = String.valueOf(System.currentTimeMillis());
        DBManager.getDaoSession().getUserDao().insertOrReplace(this);
    }

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nickname);
        dest.writeInt(this.uid);
        dest.writeString(this.avatar);
        dest.writeString(this.account);
        dest.writeString(this.gender);
        dest.writeString(this.token);
        dest.writeString(this.update_time);
    }

    protected User(Parcel in) {
        this.nickname = in.readString();
        this.uid = in.readInt();
        this.avatar = in.readString();
        this.account = in.readString();
        this.gender = in.readString();
        this.token = in.readString();
        this.update_time = in.readString();
    }

    @Generated(hash = 1687928020)
    public User(String nickname, int uid, String avatar, String account, String gender,
            String token, String update_time) {
        this.nickname = nickname;
        this.uid = uid;
        this.avatar = avatar;
        this.account = account;
        this.gender = gender;
        this.token = token;
        this.update_time = update_time;
    }


    public static final Creator<User> CREATOR = new Creator<User>() {
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
