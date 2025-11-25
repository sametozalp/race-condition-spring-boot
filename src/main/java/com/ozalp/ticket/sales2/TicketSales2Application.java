package com.ozalp.ticket.sales2;

import com.ozalp.ticket.sales2.services.TicketService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class TicketSales2Application {

	private static final int INITIAL_STOCK = 100;
	private static final int CONCURRENT_REQUESTS = 200;

	public static void main(String[] args) {
		SpringApplication.run(TicketSales2Application.class, args);
	}

	@Bean
	public CommandLineRunner initData(TicketService ticketService) {
		return args -> {
			ticketService.createInitialTicket("Konser", INITIAL_STOCK);
			System.out.println("Başlangıçta " + INITIAL_STOCK + " bilet stoka eklendi.");

			System.out.println("-----------------------------------------");
			System.out.println(CONCURRENT_REQUESTS + " adet eşzamanlı bilet satın alma isteği başlatılıyor...");

			var executor = Executors.newFixedThreadPool(CONCURRENT_REQUESTS);
			Long ticketId = 1L;
			int quantity = 1;

			for (int i = 0; i < CONCURRENT_REQUESTS; i++) {
				executor.submit(() -> {
					ticketService.purchaseTicket(ticketId, quantity);
				});
			}

			executor.shutdown();
			executor.awaitTermination(30, TimeUnit.SECONDS);

			int finalStock = ticketService.getCurrentStock(ticketId);
			long successfulSales = INITIAL_STOCK - finalStock;

			System.out.println("-----------------------------------------");
			System.out.println("DURUM:");
			System.out.println("   - Tahmini Satılan Bilet Sayısı: " + successfulSales);
			System.out.println("   - Gerçek Kalan Stok: " + finalStock);
		};
	}
}
