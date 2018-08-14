package org.news.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.news.dao.BaseDao;
import org.news.dao.CommentsDao;
import org.news.entity.Comment;
import org.news.util.DatabaseUtil;

public class CommentsDaoImpl extends BaseDao implements CommentsDao {

    public CommentsDaoImpl() {
        super();
    }

    // 通过nid查找评论
    public List<Comment> getCommentsByNid(int nid) throws SQLException {
        List<Comment> list = new ArrayList<Comment>();
        String sql = "SELECT * FROM comments WHERE cnid = ?"
                + " ORDER BY cdate desc";
        ResultSet rs = null;
        try {
            rs = executeQuery(sql, nid);
            Comment comment = null;
            while (rs.next()) {
                comment = new Comment();
                comment.setCid(rs.getInt("cid"));
                comment.setCnid(rs.getInt("cnid"));
                comment.setCauthor(rs.getString("cauthor"));
                comment.setCip(rs.getString("cip"));
                comment.setCcontent(rs.getString("ccontent"));
                comment.setCdate(rs.getTimestamp("cdate"));
                list.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseUtil.closeAll(null, null, rs);
        }
        return list;
    }

    // 添加评论
    public int addComment(Comment comment) throws SQLException {
        String sql = "INSERT INTO comments(CNID, CCONTENT, CDATE," +
        		"CIP,CAUTHOR) VALUES(?, ?, ?, ?, ?)";
        System.out.println(comment.getCnid() + ":" + comment.getCcontent()
                + ":" + comment.getCdate() + ":" + comment.getCip() + ":"
                + comment.getCauthor());
        Object[] params = new Object[] { comment.getCnid(),
                comment.getCcontent(), comment.getCdate(), comment.getCip(),
                comment.getCauthor() };
        int result = 0;
        try {
            result = executeUpdate(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }
    
    // 根据新闻id删除评论
    public int deleteCommentsByNid(int nid) throws SQLException {
        String sql = "DELETE FROM comments WHERE CNID = ?";
        int result = 0;
        try {
            result = executeUpdate(sql, nid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }
    
    // 删除评论
    public int deleteCommentsById(int cid) throws SQLException {
        String sql = "DELETE FROM comments WHERE CID = ?";
        int result = 0;
        try {
            result = executeUpdate(sql, cid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }
}
