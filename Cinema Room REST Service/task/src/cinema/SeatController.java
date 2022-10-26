package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SeatController {

    CinemaRoom cinemaRoom = new CinemaRoom(9, 9);
    List<Seat> reservedSeatsList = new ArrayList<>();

    @GetMapping("/seats")
    public CinemaRoom getCinemaRoom() {
        return cinemaRoom;
    }

    @PostMapping("/purchase")
    public Seat reserveSeat(@RequestBody Seat seat) {
        if (reservedSeatsList.contains(seat)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The ticket has been already purchased!");
        } else if (seat.getColumn() > 9 || seat.getRow() > 9 || seat.getRow() < 1 || seat.getColumn() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The number of a row or a column is out of bounds!");
        } else {
            seat.setPrice();
            reservedSeatsList.add(seat);
            return seat;
        }
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleRSE(ResponseStatusException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getReason());
        return new ResponseEntity<>(body, ex.getStatus());
    }
}