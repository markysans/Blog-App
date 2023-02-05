package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;
import org.springframework.data.web.ProjectedPayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") long postId, @RequestBody CommentDto commentDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(postId, commentDto));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPostID(@PathVariable(value ="postId") long postId) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(commentService.getCommentsByPostID(postId));
    }

    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(value = "postId") long postId,
                                                     @PathVariable(value = "id") long id) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(commentService.getCommentsById(postId, id));
    }

    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "postId") long postId,
                                                    @PathVariable(value = "id") long id,
                                                    @RequestBody CommentDto commentDto) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(commentService.updateComment(postId, id, commentDto));
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") long postId,
                                                @PathVariable(value = "id") long id) {
        commentService.deleteComment(postId, id);
        return ResponseEntity.ok("Comment Deleted Successfully");
    }
}
