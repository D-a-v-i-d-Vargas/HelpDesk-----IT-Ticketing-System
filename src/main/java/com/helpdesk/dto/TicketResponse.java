package com.helpdesk.dto;

import com.helpdesk.model.TicketPriority;
import com.helpdesk.model.TicketStatus;

import java.time.LocalDateTime;

public class TicketResponse {

    private Long ticketId;
    private String title;
    private String description;
    private TicketStatus ticketStatus;
    private TicketPriority ticketPriority;
    private LocalDateTime createdAt;

    private UserResponse createdBy;
    private UserResponse assignedAgent;

    public TicketResponse(Long ticketId, String title, String description, TicketStatus ticketStatus, TicketPriority ticketPriority, LocalDateTime createdAt, UserResponse createdBy, UserResponse assignedAgent) {
        this.ticketId = ticketId;
        this.title = title;
        this.description = description;
        this.ticketStatus = ticketStatus;
        this.ticketPriority = ticketPriority;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.assignedAgent = assignedAgent;
    }

    public TicketResponse() {
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
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

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public TicketPriority getTicketPriority() {
        return ticketPriority;
    }

    public void setTicketPriority(TicketPriority ticketPriority) {
        this.ticketPriority = ticketPriority;
    }

    public UserResponse getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserResponse createdBy) {
        this.createdBy = createdBy;
    }

    public UserResponse getAssignedAgent() {
        return assignedAgent;
    }

    public void setAssignedAgent(UserResponse assignedAgent) {
        this.assignedAgent = assignedAgent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
