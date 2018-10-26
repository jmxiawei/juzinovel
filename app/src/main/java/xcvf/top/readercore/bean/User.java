package xcvf.top.readercore.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.util.List;

/**
 * 用户
 * Created by xiaw on 2018/10/25.
 */
@Table(name = "users")
public class User extends SugarRecord implements Parcelable {

    @Column(name = "nickname")
    String nickname;

    @Unique
    @Column(name = "uid")
    String uid;
    @Column(name = "avatar")
    String avatar;
    @Column(name = "account")
    String account;
    @Column(name = "gender")
    String gender;
    @Column(name = "token")
    String token;
    @Column(name = "update_time")
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

    public static User currentUser() {
        List<User> users = User.find(User.class, null, null, null, " update_time DESC", "1");
        if (users != null && users.size() > 0) {
            return users.get(0);
        }
        return new User();
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
        dest.writeString(this.uid);
        dest.writeString(this.avatar);
        dest.writeString(this.account);
        dest.writeString(this.gender);
        dest.writeString(this.token);
        dest.writeString(this.update_time);
    }

    protected User(Parcel in) {
        this.nickname = in.readString();
        this.uid = in.readString();
        this.avatar = in.readString();
        this.account = in.readString();
        this.gender = in.readString();
        this.token = in.readString();
        this.update_time = in.readString();
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
