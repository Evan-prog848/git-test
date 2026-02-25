package com.textbook.service;

import com.textbook.vo.BookInfoVO;

/**
 * ISBN查询服务接口
 */
public interface IsbnService {

    /**
     * 根据ISBN查询书籍信息
     * @param isbn ISBN号（支持ISBN-10和ISBN-13）
     * @return 书籍信息
     */
    BookInfoVO queryByIsbn(String isbn);
}
