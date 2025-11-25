package com.ozalp.ticket.sales2.services;

public interface TicketService {

    int getCurrentStock(Long ticketId);

    String purchaseTicket(Long ticketId, int quantity);

    void createInitialTicket(String eventName, int initialStock);
}
