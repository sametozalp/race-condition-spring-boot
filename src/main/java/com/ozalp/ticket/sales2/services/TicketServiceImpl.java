package com.ozalp.ticket.sales2.services;

import com.ozalp.ticket.sales2.entities.Ticket;
import com.ozalp.ticket.sales2.repositories.TicketRepository;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public void createInitialTicket(String eventName, int initialStock) {
        if (ticketRepository.count() == 0) {
            ticketRepository.save(new Ticket(eventName, initialStock));
        }
    }

    @Transactional
    public String purchaseTicket(Long ticketId, int quantity) {
        Ticket ticket = ticketRepository.findByIdForUpdate(ticketId);

        if (ticket == null) {
            return "Hata: Bilet bulunamadı.";
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (ticket.getStock() >= quantity) {
            ticket.setStock(ticket.getStock() - quantity);
            ticketRepository.save(ticket);
            return String.format("Başarılı! %d adet bilet alındı. Kalan stok: %d",
                    quantity, ticket.getStock());
        } else {
            throw new RuntimeException("Hata: Yetersiz stok. İstenen: %d, Kalan: %d");
        }
    }

    public int getCurrentStock(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .map(Ticket::getStock)
                .orElse(0);
    }
}