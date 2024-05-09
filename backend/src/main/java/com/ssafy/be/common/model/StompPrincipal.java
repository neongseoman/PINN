package com.ssafy.be.common.model;

import lombok.AllArgsConstructor;

import java.security.Principal;

@AllArgsConstructor
public class StompPrincipal implements Principal {
    String name;

    @Override
    public String getName() {
        return name;
    }
}
