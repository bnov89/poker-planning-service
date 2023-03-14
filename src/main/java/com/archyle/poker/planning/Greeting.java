package com.archyle.poker.planning;

import lombok.Data;

@Data
public class Greeting {
    private String content;
    public Greeting(String s) {
        this.content = s;
    }
}
