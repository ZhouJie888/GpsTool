package org.news.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.news.dao.BaseDao;
import org.news.dao.NewsDao;
import org.news.entity.News;
import org.news.util.DatabaseUtil;

public class NewsDaoImpl extends BaseDao implements NewsDao {
	public NewsDaoImpl() {
		super();
	}
	

    // 获取所有新闻
    public List<News> getAllnews() throws SQLException {
        List<News> list = new ArrayList<News>();
        ResultSet rs = null;
        String sql = "SELECT nid, ntid, ntitle, nauthor,"
                + " ncreateDate, nsummary, tname FROM NEWS, TOPIC"
                + " WHERE NEWS.ntid = TOPIC.tid"
                + " ORDER BY ncreateDate DESC";
        try {
            rs = this.executeQuery(sql);
            News news = null;
            while (rs.next()) {
                news = new News();
                news.setNid(rs.getInt("nid"));
                news.setNtid(rs.getInt("ntid"));
                news.setNtitle(rs.getString("ntitle"));
                news.setNauthor(rs.getString("nauthor"));
                news.setNcreatedate(rs.getTimestamp("ncreateDate"));
                news.setNsummary(rs.getString("nsummary"));
                news.setNtname(rs.getString("tname"));
                list.add(news);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseUtil.closeAll(null, null, rs);
        }
        return list;
    }

    // 获取某主题下的所有新闻
    public List<News> getAllnewsByTID(int tid) throws SQLException {
        List<News> list = new ArrayList<News>();
        ResultSet rs = null;
        String sql = "SELECT nid, ntid, ntitle, nauthor,"
                + " ncreateDate, nsummary, tname FROM NEWS, TOPIC"
                + " WHERE NEWS.ntid = TOPIC.tid AND NEWS.ntid = ?"
                + " ORDER BY ncreateDate DESC";
        try {
            rs = this.executeQuery(sql, tid);
            News news = null;
            while (rs.next()) {
                news = new News();
                news.setNid(rs.getInt("nid"));
                news.setNtid(rs.getInt("ntid"));
                news.setNtitle(rs.getString("ntitle"));
                news.setNauthor(rs.getString("nauthor"));
                news.setNcreatedate(rs.getTimestamp("ncreateDate"));
                news.setNsummary(rs.getString("nsummary"));
                news.setNtname(rs.getString("tname"));
                list.add(news);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseUtil.closeAll(null, null, rs);
        }
        return list;
    }

    // 获取某主题下的最新新闻
    public List<News> getLatestNewsByTID(int tid, int limit)
            throws SQLException {
        List<News> list = new ArrayList<News>();
        ResultSet rs = null;
        String sql = "SELECT top (?) nid, ntid, ntitle FROM NEWS WHERE"
                + " ntid = ? ORDER BY ncreatedate DESC";
        try {
            rs = this.executeQuery(sql, limit, tid);
            News news = null;
            while (rs.next()) {
                news = new News();
                news.setNid(rs.getInt("nid"));
                news.setNtid(rs.getInt("ntid"));
                news.setNtitle(rs.getString("ntitle"));
                list.add(news);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseUtil.closeAll(null, null, rs);
        }
        return list;
    }

    // 获取某主题下的新闻数量
    public int getNewsCountByTID(int tid) throws SQLException {
        ResultSet rs = null;
        String sql = "SELECT COUNT(ntid) FROM news WHERE ntid = ?";
        int count = -1;
        try {
            rs = this.executeQuery(sql, tid);
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseUtil.closeAll(null, null, rs);
        }
        return count;
    }

    // 获取某条新闻
    public News getNewsByNID(int nid) throws SQLException {
        ResultSet rs = null;
        String sql = "SELECT * FROM NEWS, TOPIC"
                + " WHERE NEWS.ntid = TOPIC.tid AND NEWS.nid = ?"
                + " ORDER BY ncreateDate DESC";
        News news = null;
        try {
            rs = this.executeQuery(sql, nid);
            if (rs.next()) {
                news = new News();
                news.setNid(rs.getInt("nid"));
                news.setNtid(rs.getInt("ntid"));
                news.setNtitle(rs.getString("ntitle"));
                news.setNauthor(rs.getString("nauthor"));
                news.setNcreatedate(rs.getTimestamp("ncreateDate"));
                news.setNpicpath(rs.getString("npicPath"));
                news.setNcontent(rs.getString("ncontent"));
                news.setNmodifydate(rs.getTimestamp("nmodifyDate"));
                news.setNsummary(rs.getString("nsummary"));
                news.setNtname(rs.getString("tname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseUtil.closeAll(null, null, rs);
        }
        return news;
    }

    public List<News> getAllnewsByTname(String tname) throws SQLException {
        List<News> list = new ArrayList<News>();
        ResultSet rs = null;
        // 获取某主题下的所有新闻
        String sql = "SELECT nid, ntid, ntitle, nauthor,"
                + " ncreateDate, nsummary, tname FROM NEWS, TOPIC"
                + " WHERE NEWS.ntid = TOPIC.tid AND TOPIC.tname = ?"
                + " ORDER BY ncreateDate DESC";
        try {
            rs = this.executeQuery(sql, tname);
            News news = null;
            while (rs.next()) {
                news = new News();
                news.setNid(rs.getInt("nid"));
                news.setNtid(rs.getInt("ntid"));
                news.setNtitle(rs.getString("ntitle"));
                news.setNauthor(rs.getString("nauthor"));
                news.setNcreatedate(rs.getTimestamp("ncreateDate"));
                news.setNsummary(rs.getString("nsummary"));
                news.setNtname(rs.getString("tname"));
                list.add(news);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseUtil.closeAll(null, null, rs);
        }
        return list;
    }

    // 删除某条新闻
    public int deleteNews(int nid) throws SQLException {
        String sql = "DELETE FROM NEWS WHERE NID = ?";
        int result = 0;
        try {
            result = executeUpdate(sql, nid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    // 获得所有新闻的数量
    public int getTotalCount() throws SQLException {
        ResultSet rs = null;
        String sql = "SELECT COUNT(nid) FROM news";
        int count = -1;
        try {
            rs = this.executeQuery(sql);
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseUtil.closeAll(null, null, rs);
        }
        return count;
    }

    // 分页获得新闻
    //pageNo:页号，pageSize:每页显示数量
    public List<News> getPageNewsList(int pageNo, int pageSize)
            throws SQLException {
    	String sql = "select top %d * from NEWS " +
    			"where nid not in (select top %d nid from NEWS order by ncreateDate desc) "+
    			"order by ncreateDate desc ";
    	int n = (pageNo-1)*pageSize;
    	sql = String.format(sql, pageSize,n);
    	System.out.println(sql);
    	
    	Connection con = DatabaseUtil.getConnection();
    	PreparedStatement pst = con.prepareStatement(sql);
    	
    	ResultSet rs = pst.executeQuery();
    	List<News> result = new ArrayList<News>();
    	while(rs.next()){
    		News news = new News();
    		news.setNid(rs.getInt("nid"));
    		news.setNtitle(rs.getString("ntitle"));
    		news.setNcreatedate(rs.getDate("ncreatedate"));
    		news.setNauthor(rs.getString("nauthor"));
    		result.add(news);
    	}
        return result;
    }

    @Override
    public int addNews(News news) throws SQLException {
        String sql = "insert into news(NTID,NTITLE,NAUTHOR,NCONTENT,NSUMMARY,"
                + "NCREATEDATE,NMODIFYDATE,NPICPATH) values(?,?,?,?,?,?,?,?)";
        int result = 0;
        try {
            result = executeUpdate(sql, new Object[] { news.getNtid(), news.getNtitle(),
                            news.getNauthor(), news.getNcontent(),
                            news.getNsummary(), news.getNcreatedate(),
                            news.getNmodifydate(), news.getNpicpath() });
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }
    
    @Override
    public int updateNews(News news) throws SQLException {
        String sql = "UPDATE news SET NTID=?,NTITLE=?,NAUTHOR=?,NCONTENT=?,NSUMMARY=?,"
                + "NMODIFYDATE=?,NPICPATH=? where NID=?";
        int result = 0;
        try {
            result = executeUpdate(sql, new Object[] { news.getNtid(), news.getNtitle(),
                    news.getNauthor(), news.getNcontent(),
                    news.getNsummary(), news.getNmodifydate(),
                    news.getNpicpath(), news.getNid() });
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }


	@Override
	public List<News> getPageNewsListByTID(int pageNo, int pageSize, int tid)
			throws SQLException {
		String sql = "select top %d * from NEWS " +
    			"where ntid=%d and nid not in (select top %d nid from NEWS where ntid=%d order by ncreateDate desc) "+
    			"order by ncreateDate desc ";
    	int n = (pageNo-1)*pageSize;
    	sql = String.format(sql, pageSize,tid,n,tid);
    	System.out.println(sql);
    	
    	Connection con = DatabaseUtil.getConnection();
    	PreparedStatement pst = con.prepareStatement(sql);
    	
    	ResultSet rs = pst.executeQuery();
    	List<News> result = new ArrayList<News>();
    	while(rs.next()){
    		News news = new News();
    		news.setNid(rs.getInt("nid"));
    		news.setNtitle(rs.getString("ntitle"));
    		news.setNcreatedate(rs.getDate("ncreatedate"));
    		news.setNauthor(rs.getString("nauthor"));
    		result.add(news);
    	}
        return result;
	}
}
