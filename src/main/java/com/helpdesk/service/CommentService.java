package com.helpdesk.service;

import com.helpdesk.dto.CommentRequest;
import com.helpdesk.dto.CommentResponse;
import com.helpdesk.exception.ResourceNotFoundException;
import com.helpdesk.model.Comment;
import com.helpdesk.model.Ticket;
import com.helpdesk.repository.CommentRepository;
import com.helpdesk.repository.TicketRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, TicketRepository ticketRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<CommentResponse> getAllComments(){

        return commentRepository.findAll()
                .stream()
                .filter(comment -> !comment.getDeleted())
                .map(this::convertToCommentResponse)
                .toList();
    }

    public CommentResponse getCommentById(Long id){

        Comment comment = getEntityCommentById(id);
        return convertToCommentResponse(comment);
    }

    public CommentResponse createComment(Long ticketId, CommentRequest commentRequest){

        Ticket foundTicket = ticketRepository.findById(ticketId)
                .filter(ticket -> !ticket.getDeleted())
                .orElseThrow(()-> new ResourceNotFoundException("The ticket "+ticketId+" was not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("The email was not recognized, user not found"));

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(currentUser);
        comment.setTicket(foundTicket);

        commentRepository.save(comment);

        return convertToCommentResponse(comment);
    }

    public CommentResponse updateComment(Long id, CommentRequest commentRequest){

        Comment comment = getEntityCommentById(id);
        comment.setContent(commentRequest.getContent());
        commentRepository.save(comment);

        return convertToCommentResponse(comment);
    }

    public void softDeleteCommentById(Long id){

        Comment comment = getEntityCommentById(id);
        comment.setDeleted(true);

        commentRepository.save(comment);
    }


    private Comment getEntityCommentById(Long id){

        return commentRepository.findById(id)
                .filter(comment -> !comment.getDeleted())
                .orElseThrow(()-> new ResourceNotFoundException("The comment id "+id+" was not found"));
    }

    private UserResponse convertToUserResponse(User user) {
        return new UserResponse(user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole()
        );
    }

    private CommentResponse convertToCommentResponse(Comment comment){
        return new CommentResponse(comment.getCommentId(),
                comment.getContent(),
                comment.getCreatedAt(),
                convertToUserResponse(comment.getUser())
        );
    }

}
