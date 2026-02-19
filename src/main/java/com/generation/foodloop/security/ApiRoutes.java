package com.generation.foodloop.security;

public final class ApiRoutes {

    private ApiRoutes() {}

    //PUBLIC
    public static final String[] PUBLIC_ENDPOINTS = {
        "/",
        "/login",
        "/css/**",
        "/js/**",
        "/register",
        "/res/**"
    };

    //USER
    public static final String[] USER_ENDPOINTS = {
        "/user/**",
        "/profilo/**"
    };

    //ADMIN
    public static final String[] ADMIN_ENDPOINTS = {
        "/admin/**",
        "/gestione/**"
    };
}

