package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.Bill;
import fa.training.movietheater_mockproject.model.entity.Seat;
import fa.training.movietheater_mockproject.model.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    List<Ticket> getTicketListByBillId(Bill bill);

    List<Ticket> getTicketListByBill(Bill bill);


}
