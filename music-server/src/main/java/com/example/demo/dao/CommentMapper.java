package com.example.demo.dao;

import com.example.demo.domain.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    int addComment(Comment comment);

    int updateCommentMsg(Comment record);

    int deleteComment(Integer id);

    List<Comment> songListComment();

    List<Comment> songComments(Integer songId);

    List<Comment> songListComments(Integer songListId);
}