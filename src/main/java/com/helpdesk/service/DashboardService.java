package com.helpdesk.service;

import com.helpdesk.dto.DashboardStatistics;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    public DashboardStatistics getStatistics() {

        return new DashboardStatistics(0, 0, 0, 0);
    }
}