package com.bifan.txtreaderlib.interfaces;

import com.bifan.txtreaderlib.main.TxtReaderContext;

/**
 * Created by xiaw on 2018/6/26.
 */
public interface IBookLoader {


    public void load(String text, TxtReaderContext readerContext, ILoadListener callBack);
}
