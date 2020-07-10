package service;

import model.Comment;

import java.util.List;

public interface ICommentService {
    List<Comment> findAll();

    Comment findById(Long id);

    Comment save(Comment comment);

    Comment newComment(Comment comment);
}
