package com.ozalp.ticket.sales2.controllers;

import com.ozalp.ticket.sales2.services.TicketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/purchase/{ticketId}")
    public String purchase(@PathVariable Long ticketId, @RequestParam int quantity) {
        return ticketService.purchaseTicket(ticketId, quantity);
    }

    @GetMapping("/stock/{ticketId}")
    public int getCurrentStock(@PathVariable Long ticketId) {
        return ticketService.getCurrentStock(ticketId);
    }
}