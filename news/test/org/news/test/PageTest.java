package org.news.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.news.dao.NewsDao;
import org.news.dao.impl.NewsDaoImpl;
import org.news.entity.News;
import org.news.util.DatabaseUtil;

public class PageTest {
    @Test
    public void pageTest() {
        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();

            NewsDao newsDao = new NewsDaoImpl();
            int totalCount = newsDao.getTotalCount();

            List<News> newsList = newsDao
                .getPageNewsList(1, 5);
            for (News news : newsList) {
                System.out.println(news.getNid() + "\t" + news.getNtitle()
                                    + "\t" + news.getNcreatedate());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.closeAll(conn, null, null);
        }
    }
}
