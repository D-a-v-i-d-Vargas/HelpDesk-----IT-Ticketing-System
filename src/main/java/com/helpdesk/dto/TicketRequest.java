package com.helpdesk.dto;

import com.helpdesk.model.TicketPriority;
import com.helpdesk.model.TicketStatus;

public class TicketRequest {

    private String title;
    private String description;
    private TicketPriority ticketPriority;

    public TicketRequest(String title, String description, TicketPriority ticketPriority) {
        this.title = title;
        this.description = description;
        this.ticketPriority = ticketPriority;
    }

    public TicketRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketPriority getTicketPriority() {
        return ticketPriority;
    }

    public void setTicketPriority(TicketPriority ticketPriority) {
        this.ticketPriority = ticketPriority;
    }
}
