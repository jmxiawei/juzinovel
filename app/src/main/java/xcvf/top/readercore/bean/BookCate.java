package xcvf.top.readercore.bean;

public class BookCate {


    String name;
    int booknum;
    int gender;
    int order;

    public String getName() {
        return name;
    }

    public BookCate setName(String name) {
        this.name = name;
        return this;
    }

    public int getBooknum() {
        return booknum;
    }

    public BookCate setBooknum(int booknum) {
        this.booknum = booknum;
        return this;
    }

    public int getGender() {
        return gender;
    }

    public BookCate setGender(int gender) {
        this.gender = gender;
        return this;
    }

    public int getOrder() {
        return order;
    }

    public BookCate setOrder(int order) {
        this.order = order;
        return this;
    }
}
