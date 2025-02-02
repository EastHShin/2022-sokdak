package com.wooteco.sokdak.post.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.post.dto.MyPostsResponse;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostDetailResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.post.service.PostService;
import com.wooteco.sokdak.support.token.Login;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDetailResponse> findPost(@PathVariable Long id, @Login AuthInfo authInfo) {
        PostDetailResponse postResponse = postService.findPost(id, authInfo);
        return ResponseEntity.ok(postResponse);
    }

    @PostMapping("/boards/{boardId}/posts")
    public ResponseEntity<Void> addPost(@PathVariable Long boardId,
                                        @Valid @RequestBody NewPostRequest newPostRequest,
                                        @Login AuthInfo authInfo) {
        Long postId = postService.addPost(boardId, newPostRequest, authInfo);
        return ResponseEntity.created(URI.create("/posts/" + postId)).build();
    }

    @GetMapping("/boards/{boardId}/posts")
    public ResponseEntity<PostsResponse> findPosts(
            @PathVariable Long boardId,
            @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        PostsResponse postsResponse = postService.findPostsByBoard(boardId, pageable);
        return ResponseEntity.ok(postsResponse);
    }

    @GetMapping(path = "/posts/me")
    public ResponseEntity<MyPostsResponse> findMyPosts(@PageableDefault Pageable pageable, @Login AuthInfo authInfo) {
        MyPostsResponse myPosts = postService.findMyPosts(pageable, authInfo);
        return ResponseEntity.ok(myPosts);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable Long id,
                                           @Valid @RequestBody PostUpdateRequest postUpdateRequest,
                                           @Login AuthInfo authInfo) {
        postService.updatePost(id, postUpdateRequest, authInfo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @Login AuthInfo authInfo) {
        postService.deletePost(id, authInfo);
        return ResponseEntity.noContent().build();
    }
}
