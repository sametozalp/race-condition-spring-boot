package com.ozalp.ticket.sales2.repositories;

import com.ozalp.ticket.sales2.entities.Ticket;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT t FROM Ticket t WHERE t.id = :id")
    Ticket findByIdForUpdate(@Param("id") Long id);

}