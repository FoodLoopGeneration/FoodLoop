package com.generation.foodloop.security;

public final class ApiRoutes {

    private ApiRoutes() {}

    //PUBLIC
    public static final String[] PUBLIC = {
        "/",
        "/login",
        "/css/**",
        "/js/**"
    };

    //USER
    public static final String[] USER = {
        "/user/**",
        "/profilo/**"
    };

    //ADMIN
    public static final String[] ADMIN = {
        "/admin/**",
        "/gestione/**"
    };
}

