package xcvf.top.readercore.interfaces;

import java.util.List;

import xcvf.top.readercore.bean.Chapter;

/**
 * 解析章节内容
 */
public interface IChapterContentParser {

    String parser(Chapter chapter,List<String> filelist);

}
