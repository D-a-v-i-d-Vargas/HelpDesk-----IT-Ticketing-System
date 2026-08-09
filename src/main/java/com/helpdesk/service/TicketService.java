package com.helpdesk.service;

import com.helpdesk.dto.TicketResponse;
import com.helpdesk.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<TicketResponse> getAllTickets(){
        ticketRepository.findAll().stream()
                .filter(ticket -> !ticket.getDeleted())
                .map(ticket -> {

                    UserResponse userResponse = new UserResponse();

                    TicketResponse ticketResponse = new TicketResponse(ticket.getTicketId(),
                            ticket.getTitle(),
                            ticket.getDescription(),
                            ticket.getTicketStatus(),
                            ticket.getTicketPriority(),
                            ticket.getCreatedBy(),
                            ticket.getAssignedAgent());
                })

    }
}
