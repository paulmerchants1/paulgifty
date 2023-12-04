package com.blog.repositories;

import com.blog.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //Spring Boot will automatically understand that the record had to be found by email...because we are using find by name...spring boot is smart.
//    Comment findByEmail(String email);

    List<Comment> findByPostId(long postId);


}
