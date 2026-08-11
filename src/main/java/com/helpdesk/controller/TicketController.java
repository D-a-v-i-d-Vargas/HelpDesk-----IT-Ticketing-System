package com.helpdesk.controller;

import com.helpdesk.dto.TicketRequest;
import com.helpdesk.dto.TicketResponse;
import com.helpdesk.dto.TicketStatusRequest;
import com.helpdesk.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    /*
    Register -> Login -> Get identity token -> Create ticket
     */
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest ticketRequest) {
        TicketResponse ticketResponse = ticketService.createTicket(ticketRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ticketResponse.getTicketId())
                .toUri();
        return ResponseEntity.created(location)
                .body(ticketResponse);
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAllTickets(){
        List<TicketResponse> ticketResponses = ticketService.getAllTickets();
        return ResponseEntity.ok(ticketResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long id){

        TicketResponse ticketResponse = ticketService.findTicketById(id);
        return ResponseEntity.ok(ticketResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketById(@PathVariable Long id){
        ticketService.softDeleteTicketById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponse> updateTicketById(@PathVariable Long id, @RequestBody TicketRequest ticketRequest){
        TicketResponse ticketResponse = ticketService.updateTicket(id, ticketRequest);
        return ResponseEntity.ok(ticketResponse);
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponse> updateTicketStatusById(@PathVariable Long id, @RequestBody TicketStatusRequest ticketStatusRequest){

        TicketResponse response = ticketService.updateTicketStatus(id, ticketStatusRequest.getTicketStatus());

        return ResponseEntity.ok(response);
    }





}
