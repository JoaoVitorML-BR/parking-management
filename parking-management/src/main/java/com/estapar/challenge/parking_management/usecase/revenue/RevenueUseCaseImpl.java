package com.estapar.challenge.parking_management.usecase.revenue;

import com.estapar.challenge.parking_management.dto.RevenueRequestDTO;
import com.estapar.challenge.parking_management.dto.RevenueResponseDTO;
import com.estapar.challenge.parking_management.models.Ticket;
import com.estapar.challenge.parking_management.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueUseCaseImpl implements RevenueUseCase {

    private final TicketRepository ticketRepository;

    @Override
    public RevenueResponseDTO execute(RevenueRequestDTO request) {

        LocalDate date = LocalDate.parse(request.getDate());
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Ticket> tickets = ticketRepository
                .findBySectorCodeAndExitTimeBetween(request.getSector(), start, end);

        BigDecimal total = tickets.stream()
                .filter(t -> t.getAmountPaid() != null)
                .map(Ticket::getAmountPaid)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String timestamp = LocalDateTime.now()
                .atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));

        return new RevenueResponseDTO(total, "BRL", timestamp);
    }
}