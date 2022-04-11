package com.example.learnspring.controlles;


import com.example.learnspring.Service.PostService;
import com.example.learnspring.models.Comment;
import com.example.learnspring.models.Permission;
import com.example.learnspring.models.Post;
import com.example.learnspring.repo.CommentRepository;
import com.example.learnspring.repo.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {
    @Autowired
    private PostRepository postRepository;

    private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

    private PostService postService;
    private CommentRepository commentRepository;

    public BlogController(PostService postService, CommentRepository commentRepository) {
        this.postService = postService;
        this.commentRepository = commentRepository;
    }


    @GetMapping("/blog")
    @PreAuthorize("hasAuthority('developers:read')")
    public String blogMain(Model model){
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    @GetMapping("/blog/add")
    @PreAuthorize("hasAuthority('developers:write')")
    public String blogAdd(Model model){
        return "blog-add";
    }

    @PostMapping("/blog/add")
    @PreAuthorize("hasAuthority('developers:write')")
    public String blogPostAdd(@RequestParam String title,@RequestParam String anons,@RequestParam String full_text, Model model){
        Post post = new Post(title, anons, full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    @PreAuthorize("hasAuthority('developers:read')")
    public String blogDetails(@PathVariable(value = "id") long id, Model model){
        if(!postRepository.existsById(id)) return "redirect:/blog";
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "blog-details";
    }

    @GetMapping("/blog/{id}/edit")
    @PreAuthorize("hasAuthority('developers:write')")
    public String blogEdit(@PathVariable(value = "id") long id, Model model){
        if(!postRepository.existsById(id)) return "redirect:/blog";
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    @PreAuthorize("hasAuthority('developers:write')")
    public String blogPostUpdate(@PathVariable(value = "id") long id, @RequestParam String title,@RequestParam String anons,@RequestParam String full_text, Model model){
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/remove")
    @PreAuthorize("hasAuthority('developers:write')")
    public String blogPostDelete(@PathVariable(value = "id") long id, Model model){
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
        return "redirect:/blog";
    }
    @PostMapping("/blog/comments")
    @PreAuthorize("hasAuthority('developers:write')")
    public String addComment(Comment comment, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            logger.info("There was a problem adding a new comment.");
        } else {
            commentRepository.save(comment);
            logger.info("New comment was saved.");
        }
        return "redirect:/blog/" + comment.getPost().getId();
    }
}