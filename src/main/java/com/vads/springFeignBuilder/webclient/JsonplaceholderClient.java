package com.vads.springFeignBuilder.webclient;

import com.vads.springFeignBuilder.annotations.FilterHeaders;
import com.vads.springFeignBuilder.model.Post;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(url = "https://jsonplaceholder.typicode.com/", value = "jsonPlaceholderClient")
public interface JsonplaceholderClient {

  @RequestMapping(method = RequestMethod.GET, value = "/posts")
  List<Post> getPosts();

  @RequestMapping(method = RequestMethod.GET, value = "/posts/{postId}", produces = "application/json",
      consumes = {"text/plain", "application/*"},
      headers = {"content-type=application/*", "x-app-name=vads"})
  @FilterHeaders(exclude = {"password", "authorization"})
  Post getPostById(@PathVariable("postId") Long postId);

  @RequestMapping(method = RequestMethod.DELETE, value = "/posts/{postId}")
  void deletePostById(@PathVariable("postId") Long postId);

}
