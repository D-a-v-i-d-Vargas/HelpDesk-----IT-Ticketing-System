package com.helpdesk.dto;

import com.helpdesk.model.TicketStatus;

public class TicketStatusRequest {

    private TicketStatus ticketStatus;

    public TicketStatusRequest(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public TicketStatusRequest() {
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }
}
