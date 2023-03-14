package com.archyle.poker.planning;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PokerPlanningService {
    private final Map<Integer, Room> roomByRoomNumberMap = new HashMap<>();

    public void addRoom(final Room room) {
        roomByRoomNumberMap.put(room.getRoomNumber(), room);
    }

    public Room getRoom(Integer roomNumber) {
        return roomByRoomNumberMap.get(roomNumber);
    }

    public Statistics calculateStatistics(Room room) {
        Statistics statistics = new Statistics();
        List<BigDecimal> collect = room.getGuests().stream().map(Guest::getPoints).filter(this::isNumber).map(BigDecimal::new).collect(Collectors.toList());
        BigDecimal sum = collect.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal average = collect.size() == 0 ? BigDecimal.ZERO : sum.divide(BigDecimal.valueOf(collect.size()), 2, RoundingMode.HALF_UP);
        List<BigDecimal> qaPoints = room.getGuests().stream().filter(guest -> UserType.QA.equals(guest.getUserType())).map(Guest::getPoints).filter(this::isNumber).map(BigDecimal::new).collect(Collectors.toList());
        BigDecimal qaSum = qaPoints.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal qaAverage = qaPoints.size() == 0 ? BigDecimal.ZERO : qaSum.divide(BigDecimal.valueOf(qaPoints.size()), 2, RoundingMode.HALF_UP);
        List<BigDecimal> devPoints = room.getGuests().stream().filter(guest -> UserType.DEV.equals(guest.getUserType())).map(Guest::getPoints).filter(this::isNumber).map(BigDecimal::new).collect(Collectors.toList());
        BigDecimal devSum = devPoints.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal devAverage = devPoints.size() == 0 ? BigDecimal.ZERO : devSum.divide(BigDecimal.valueOf(devPoints.size()), 2, RoundingMode.HALF_UP);
        statistics.setQaAverage(qaAverage);
        statistics.setDevAverage(devAverage);
        statistics.setAverage(average);
        return statistics;
    }

    private boolean isNumber(String stringToCheck) {
        try {
            Integer.parseInt(stringToCheck);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
