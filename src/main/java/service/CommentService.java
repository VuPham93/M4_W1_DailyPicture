package service;

import model.Comment;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CommentService implements ICommentService{
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Comment> findAll() {
        String queryStr = "SELECT c FROM Comment AS c";
        TypedQuery<Comment> query = entityManager.createQuery(queryStr, Comment.class);
        return query.getResultList();
    }

    @Override
    public Comment findById(Long id) {
        String queryStr = "SELECT c FROM Comment AS c WHERE c.id = :id";
        TypedQuery<Comment> query = entityManager.createQuery(queryStr, Comment.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Comment save(Comment comment) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = entityManager.unwrap(Session.class);
            transaction = session.beginTransaction();
            Comment origin = findById(comment.getId());
            origin.setPoint(comment.getPoint());
            origin.setCommenter(comment.getCommenter());
            origin.setDetail(comment.getDetail());
            origin.setDate(comment.getDate());
            origin.setUpVote(comment.getUpVote());

            session.saveOrUpdate(origin);
            transaction.commit();
            return origin;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return null;
    }

    @Override
    public Comment newComment(Comment comment) {
        Session session;
        Transaction transaction = null;
        try {
            session = entityManager.unwrap(Session.class);
            transaction = session.beginTransaction();

            session.save(comment);
            transaction.commit();
            return comment;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return null;
    }
}
