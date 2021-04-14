package com.vads.springFeignBuilder.controller;

import com.vads.springFeignBuilder.model.Post;
import com.vads.springFeignBuilder.webclient.JsonplaceholderClient;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/posts")
public class PostController {

  @Autowired
  JsonplaceholderClient jsonplaceholderClient;

  @GetMapping(value = "")
  List<Post> getPosts() {
    return jsonplaceholderClient.getPosts();
  }

  @GetMapping(value = "/{postId}")
  Post getPostById(@PathVariable("postId") Long postId) {
    return jsonplaceholderClient.getPostById(postId);
  }

  @DeleteMapping(value = "/{postId}")
  void deletePostById(@PathVariable("postId") Long postId) {
    jsonplaceholderClient.deletePostById(postId);
  }
}