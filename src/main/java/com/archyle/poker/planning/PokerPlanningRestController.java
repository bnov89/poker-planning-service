package com.archyle.poker.planning;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class PokerPlanningRestController {

    private final PokerPlanningService pokerPlanningService;

    @PostMapping
    public ResponseEntity<Integer> createRoom(@RequestBody CreateRoomRequest request) {
        int generatedRoomNumber = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
        Room room = new Room(generatedRoomNumber);
        pokerPlanningService.addRoom(room);
        return ResponseEntity.created(URI.create("/" + generatedRoomNumber)).body(generatedRoomNumber);
    }

    @GetMapping("/{roomNumber}")
    public ResponseEntity<Room> findRoom(@PathVariable Integer roomNumber) {
        Room room = pokerPlanningService.getRoom(roomNumber);
        if(room != null) {
            return ResponseEntity.ok().body(room);
        }
        throw new ResourceNotFoundException("Bla bla");
    }

    @GetMapping("/{roomNumber}/guests")
    public ResponseEntity<Room> findGuestsInRoom(@PathVariable Integer roomNumber) {
        Room room = pokerPlanningService.getRoom(roomNumber);
        if(room != null) {
            return ResponseEntity.ok().body(room);
        }
        throw new ResourceNotFoundException("Bla bla");
    }
}
