package com.archyle.poker.planning;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PokerPlanningController {

    private final PokerPlanningService pokerPlanningService;

    @MessageMapping("/room/{roomNumber}/join")
    @SendTo("/topic/poker-planning/room/{roomNumber}")
    public ResponseMessage join(@DestinationVariable String roomNumber, @Payload JoinMessage joinMessage) {
        Room room = pokerPlanningService.getRoom(Integer.valueOf(roomNumber));
        room.getGuests().add(new Guest(joinMessage.getUserId(), joinMessage.getUserType(), null));
        return new ResponseMessage(room, MessageType.JOIN);
    }

    @MessageMapping("/room/{roomNumber}/vote")
    @SendTo("/topic/poker-planning/room/{roomNumber}")
    public ResponseMessage vote(@DestinationVariable String roomNumber, @Payload VoteMessage voteMessage) {
        Room room = pokerPlanningService.getRoom(Integer.valueOf(roomNumber));
        room.getGuests().stream().filter(guest -> guest.getUserId().equals(voteMessage.getUserId())).findFirst().get().setPoints(voteMessage.getPoints().toString());
        room.setStatistics(pokerPlanningService.calculateStatistics(room));
        return new ResponseMessage(room, MessageType.VOTE);
    }

    @MessageMapping("/room/{roomNumber}/leave")
    @SendTo("/topic/poker-planning/room/{roomNumber}")
    public ResponseMessage leave(@DestinationVariable String roomNumber, @Payload JoinMessage joinMessage) {
        Room room = pokerPlanningService.getRoom(Integer.valueOf(roomNumber));
        room.getGuests().removeIf(guest -> guest.getUserId().equals(joinMessage.getUserId()));
        room.setStatistics(pokerPlanningService.calculateStatistics(room));
        return new ResponseMessage(room, MessageType.LEAVE);
    }

    @MessageMapping("/room/{roomNumber}/revealCards")
    @SendTo("/topic/poker-planning/room/{roomNumber}")
    public ResponseMessage revealCards(@DestinationVariable String roomNumber) {
        Room room = pokerPlanningService.getRoom(Integer.valueOf(roomNumber));
        room.setStatistics(pokerPlanningService.calculateStatistics(room));
        room.setCardsAreRevealed(true);
        return new ResponseMessage(room, MessageType.REVEAL_CARDS);
    }

    @MessageMapping("/room/{roomNumber}/hideCards")
    @SendTo("/topic/poker-planning/room/{roomNumber}")
    public ResponseMessage hideCards(@DestinationVariable String roomNumber) {
        Room room = pokerPlanningService.getRoom(Integer.valueOf(roomNumber));
        room.getGuests().forEach(guest -> guest.setPoints(null));
        room.setStatistics(pokerPlanningService.calculateStatistics(room));
        room.setCardsAreRevealed(false);
        return new ResponseMessage(room, MessageType.HIDE_CARDS);
    }
}
