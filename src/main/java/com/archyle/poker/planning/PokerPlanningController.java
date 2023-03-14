package com.archyle.poker.planning;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class PokerPlanningController {

    private final PokerPlanningService pokerPlanningService;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }


    @MessageMapping("/room/{roomNumber}/join")
    @SendTo("/topic/room/{roomNumber}/join")
    public Room join(@DestinationVariable String roomNumber, @Payload JoinMessage joinMessage) {
        Room room = pokerPlanningService.getRoom(Integer.valueOf(roomNumber));
        room.getGuests().add(new Guest(joinMessage.getUserId(), joinMessage.getUserType(), null));
        return room;
    }

    @MessageMapping("/room/{roomNumber}/vote")
    @SendTo("/topic/room/{roomNumber}/vote")
    public Set<Guest> vote(@DestinationVariable String roomNumber, @Payload VoteMessage voteMessage) {
        Room room = pokerPlanningService.getRoom(Integer.valueOf(roomNumber));
        room.getGuests().stream().filter(guest -> guest.getUserId().equals(voteMessage.getUserId())).findFirst().get().setPoints(voteMessage.getPoints().toString());
        return room.getGuests();
    }

    @MessageMapping("/room/{roomNumber}/leave")
    @SendTo("/topic/room/{roomNumber}/leave")
    public Set<Guest> leave(@DestinationVariable String roomNumber, @Payload JoinMessage joinMessage) {
        Room room = pokerPlanningService.getRoom(Integer.valueOf(roomNumber));
        room.getGuests().removeIf(guest -> guest.getUserId().equals(joinMessage.getUserId()));
        return room.getGuests();
    }

    @MessageMapping("/room/{roomNumber}/revealCards")
    @SendTo("/topic/room/{roomNumber}/revealCards")
    public Room revealCards(@DestinationVariable String roomNumber, @Payload JoinMessage joinMessage) {
        Room room = pokerPlanningService.getRoom(Integer.valueOf(roomNumber));
        room.setStatistics(pokerPlanningService.calculateStatistics(room));
        room.setCardsAreRevealed(true);
        return room;
    }

    @MessageMapping("/room/{roomNumber}/hideCards")
    @SendTo("/topic/room/{roomNumber}/hideCards")
    public Set<Guest> hideCards(@DestinationVariable String roomNumber, @Payload JoinMessage joinMessage) {
        Room room = pokerPlanningService.getRoom(Integer.valueOf(roomNumber));
        room.setCardsAreRevealed(false);
        room.getGuests().forEach(guest -> guest.setPoints(null));
        return room.getGuests();
    }


}
