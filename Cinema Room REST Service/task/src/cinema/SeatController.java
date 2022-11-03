package cinema;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
public class SeatController {

    CinemaRoom cinemaRoom = new CinemaRoom(9, 9);
    List<OrderedSeats> reservedSeatsList = new ArrayList<>();

    @GetMapping("/seats")
    public CinemaRoom getCinemaRoom() {
        return cinemaRoom;
    }

    @PostMapping("/purchase")
    public OrderedSeats reserveSeat(@RequestBody Seat seat) {

        if (seat.getColumn() > 9 || seat.getRow() > 9 || seat.getRow() < 0 || seat.getColumn() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The number of a row or a column is out of bounds!");
        }

        for (OrderedSeats orderedSeats : reservedSeatsList) {
            if (orderedSeats.getTicket().equals(seat)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The ticket has been already purchased!");
            }
        }
            seat.setPrice();
            reservedSeatsList.add(new OrderedSeats(UUID.randomUUID(), seat));
            return reservedSeatsList.get(reservedSeatsList.size() - 1);
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnTicket(@RequestBody Token token) {
        for(OrderedSeats orderedSeats : reservedSeatsList) {
            if(orderedSeats.getToken().equals(token.getToken())) {
                Seat tempSeat = orderedSeats.getTicket();
                reservedSeatsList.remove(orderedSeats);
                return new ResponseEntity<>(Map.of("returned_ticket",tempSeat),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(Map.of("error","Wrong token!"),HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/stats")
    public TicketService returnStats (@RequestParam(required = false) String password){
        if ("super_secret".equals(password)) {
            TicketService ticketService = new TicketService();
            ticketService.count(reservedSeatsList, cinemaRoom);
            return ticketService;

        }
        else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"The password is wrong!");
        }
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleRSE(ResponseStatusException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getReason());
        return new ResponseEntity<>(body, ex.getStatus());
    }
}