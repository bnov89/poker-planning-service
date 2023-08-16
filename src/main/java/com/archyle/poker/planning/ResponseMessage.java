package com.archyle.poker.planning;

import lombok.Data;

@Data
public class ResponseMessage {
    private final Room room;
    private final MessageType messageType;
}
