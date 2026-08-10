package com.helpdesk.service;

import com.helpdesk.dto.TicketRequest;
import com.helpdesk.dto.TicketResponse;
import com.helpdesk.exception.ResourceNotFoundException;
import com.helpdesk.model.Ticket;

import static com.helpdesk.model.TicketStatus.OPEN;

import com.helpdesk.repository.TicketRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .filter(ticket -> !ticket.getDeleted())
                .map(this::convertToTicketResponse)
                .toList();
    }

    public TicketResponse createTicket(TicketRequest ticketRequest) {

        //Spring Security stores the authenticated principal in the SecurityContext.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("The email was not recognized, user not found"));

        Ticket requestedTicket = new Ticket(ticketRequest.getTitle(), ticketRequest.getDescription(), ticketRequest.getTicketPriority());
        requestedTicket.setTicketStatus(OPEN);
        requestedTicket.setCreatedAt(LocalDateTime.now());
        requestedTicket.setCreatedBy(currentUser);

        Ticket ticket = ticketRepository.save(requestedTicket);

        UserResponse userResponse = convertToUserResponse(ticket.getCreatedBy());

        UserResponse assignedAgent = ticket.getAssignedAgent() != null
                ? convertToUserResponse(ticket.getAssignedAgent())
                : null;


        TicketResponse ticketResponse = new TicketResponse(ticket.getTicketId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getTicketStatus(),
                ticket.getTicketPriority(),
                ticket.getCreatedAt(),
                userResponse,
                assignedAgent);

        return ticketResponse;
    }

    public TicketResponse findTicketById(Long id) {

        Ticket ticket = findEntityTicketById(id);
        return convertToTicketResponse(ticket);
    }

    private Ticket findEntityTicketById(Long id) {
        return ticketRepository.findById(id).
                filter(ticket -> !ticket.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("The ticket " + id + " was not found"));
    }

    public TicketResponse updateTicket(Long id, TicketRequest ticketRequest) {

        /*
        createdAt       unchanged
        createdBy       unchanged
        assignedAgent   unchanged
        ticketStatus    handled separately
        ticketId        never changes
         */
        Ticket ticket = findEntityTicketById(id);
            ticket.setTitle(ticketRequest.getTitle());
            ticket.setDescription(ticketRequest.getDescription());
            ticket.setTicketPriority(ticketRequest.getTicketPriority());

            ticketRepository.save(ticket);

            return convertToTicketResponse(ticket);
    }

    public void softDeleteTicketById(Long id){

        Ticket ticket = findEntityTicketById(id);
        ticket.setDeleted(true);
        ticketRepository.save(ticket);

    }

    public UserResponse convertToUserResponse(User user) {

        return new UserResponse(user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole()
        );
    }

    private TicketResponse convertToTicketResponse(Ticket ticket) {

        return new TicketResponse(ticket.getTicketId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getTicketStatus(),
                ticket.getTicketPriority(),
                ticket.getCreatedAt(),
                convertToUserResponse(ticket.getCreatedBy()),
                ticket.getAssignedAgent() != null
                        ? convertToUserResponse(ticket.getAssignedAgent())
                        : null);
    }
}
