package com.example.learnspring.repo;

import com.example.learnspring.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
}
