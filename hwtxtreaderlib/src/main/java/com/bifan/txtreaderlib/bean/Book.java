package com.bifan.txtreaderlib.bean;

import java.util.List;

/**书籍
 * Created by xiaw on 2018/6/26.
 */
public class Book {

    String name;
    String extern_bookid;
    String author;
    List<Chapter> chapterList;
    String cover;
    String desc;
    Chapter currentChapter;



}
