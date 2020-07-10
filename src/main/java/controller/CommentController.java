package controller;

import model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import service.ICommentService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private ICommentService commentService;

    @GetMapping
    public ModelAndView showComments() {
        ModelAndView modelAndView = new ModelAndView("/index");
        List<Comment> comments = commentService.findAll();
        List<Comment> dailyComment = new ArrayList<>();

        for (Comment comment: comments) {
            if (comment.getDate().equals(timeConvert())) {
                dailyComment.add(comment);
            }
        }
        modelAndView.addObject("comments", dailyComment);
        modelAndView.addObject("comment", new Comment());
        return modelAndView;
    }

    @GetMapping("/comment-like/{id}")
    public String like(@PathVariable Long id) {
        Comment comment = commentService.findById(id);
        comment.setUpVote(comment.getUpVote() + 1);
        commentService.save(comment);
        return "redirect:/comments";
    }

    @PostMapping("/comment-save")
    public String save(Comment comment) {
        comment.setDate(timeConvert());
        commentService.newComment(comment);
        return "redirect:/comments";
    }

    private String timeConvert() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return myDateObj.format(myFormatObj);
    }
}
