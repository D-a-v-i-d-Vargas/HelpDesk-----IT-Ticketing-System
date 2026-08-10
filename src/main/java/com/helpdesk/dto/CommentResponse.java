package com.helpdesk.dto;

import java.time.LocalDateTime;

public class CommentResponse {

    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private UserResponse user;

    public CommentResponse() {
    }

    public CommentResponse(
            Long commentId,
            String content,
            LocalDateTime createdAt,
            UserResponse user) {

        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
        this.user = user;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

}
