package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.Bill;
import fa.training.movietheater_mockproject.model.entity.Seat;
import fa.training.movietheater_mockproject.model.entity.Ticket;
import fa.training.movietheater_mockproject.repository.TicketRepository;
import fa.training.movietheater_mockproject.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    @Override
    public List<Ticket> getTicketListByBillId(Bill bill) {
        return ticketRepository.findAllByBillAndDeletedFalse(bill);
    }

    @Override
    public List<Ticket> getTicketListByBill(Bill bill) {
        return ticketRepository.findAllByBillAndDeletedFalse(bill);
    }


}
