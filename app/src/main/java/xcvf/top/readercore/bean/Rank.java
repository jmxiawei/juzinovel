package xcvf.top.readercore.bean;

/**
 * 排行榜
 */
public class Rank {

    public int rankid;
    public String listname;
    public String source;
    public String update_time;
    public String gender;
    public String icon;
    public String getGender() {
        return gender;
    }

    public Rank setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public int getRankid() {
        return rankid;
    }

    public Rank setRankid(int rankid) {
        this.rankid = rankid;
        return this;
    }

    public String getListname() {
        return listname;
    }

    public Rank setListname(String listname) {
        this.listname = listname;
        return this;
    }

    public String getSource() {
        return source;
    }

    public Rank setSource(String source) {
        this.source = source;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public Rank setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public Rank setUpdate_time(String update_time) {
        this.update_time = update_time;
        return this;
    }
}
