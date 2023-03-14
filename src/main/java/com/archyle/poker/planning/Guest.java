package com.archyle.poker.planning;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Guest {
    private String userId;
    private String userType;
    private String points;

}
