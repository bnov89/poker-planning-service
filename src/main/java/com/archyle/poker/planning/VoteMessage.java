package com.archyle.poker.planning;

import lombok.Data;

@Data
public class VoteMessage {
    private String userId;
    private Integer points;
}
