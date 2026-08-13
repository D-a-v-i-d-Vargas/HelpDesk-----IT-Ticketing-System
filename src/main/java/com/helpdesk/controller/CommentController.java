package com.helpdesk.controller;

import com.helpdesk.dto.CommentRequest;
import com.helpdesk.dto.CommentResponse;
import com.helpdesk.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/api/comments")
    public ResponseEntity<List<CommentResponse>> getAllComments(){
        List<CommentResponse> commentResponses = commentService.getAllComments();
        return ResponseEntity.ok(commentResponses);
    }

    @GetMapping("/api/comments/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id){
        CommentResponse response = commentService.getCommentById(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/tickets/{ticketId}/comments")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest, @PathVariable Long ticketId){

        CommentResponse response = commentService.createComment(ticketId, commentRequest);

        URI uriLocation = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/comments/{id}")
                .buildAndExpand(response.getCommentId())
                .toUri();

        return ResponseEntity.created(uriLocation).body(response);
    }

    @PutMapping("/api/comments/{id}")
    public ResponseEntity<CommentResponse> updateCommentById(@PathVariable Long id, @RequestBody CommentRequest commentRequest){

        CommentResponse response = commentService.updateComment(id, commentRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){

        commentService.softDeleteCommentById(id);
        return ResponseEntity.noContent().build();
    }

}
