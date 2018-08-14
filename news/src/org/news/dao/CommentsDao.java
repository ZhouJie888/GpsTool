package org.news.dao;

import java.sql.SQLException;
import java.util.List;
import org.news.entity.Comment;

public interface CommentsDao {
    // 通过新闻id查找评论
    public List<Comment> getCommentsByNid(int nid) throws SQLException;
    // 添加评论
    public int addComment(Comment comment) throws SQLException;
    // 根据新闻id删除评论
    public int deleteCommentsByNid(int nid) throws SQLException;
    // 删除评论
    public int deleteCommentsById(int cid) throws SQLException;
}