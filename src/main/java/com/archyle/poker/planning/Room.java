package com.archyle.poker.planning;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class Room {
    private final int roomNumber;
    private final Set<Guest> guests = new HashSet<>();
    private boolean cardsAreRevealed;
    private Statistics statistics = new Statistics();
}
