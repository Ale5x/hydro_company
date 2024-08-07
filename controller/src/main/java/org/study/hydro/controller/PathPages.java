package org.study.hydro.controller;

import org.springframework.stereotype.Component;

/**
 * This class {@link PathPages} has URI pages.
 *
 * @author Aliaksandr Pishchala
 */
@Component
public class PathPages {

    public static final String API = "/api";
    public static final String AUTH = API + "/auth";

    public static final String ADMIN_BRANCH = API + "/admin";
    public static final String USER_BRANCH = API + "/user";
    public static final String AUTH_AUTHENTICATION = AUTH + "/authentication";
    public static final String AUTH_CREATE = AUTH + "/create";


    public static final String COMPANY = USER_BRANCH + "/company";
    public static final String COMPANY_CREATE = COMPANY + "/create";
    public static final String COMPANY_ID = COMPANY + "/id";
    public static final String COMPANY_ALL = COMPANY + "/get_all";
    public static final String COMPANY_BY_NAME = COMPANY + "/find_company";

    public static final String COMPANY_UPDATE = ADMIN_BRANCH + "/find_company";


    public static final String USER = API + "/user";
    public static final String USER_ALL = ADMIN_BRANCH + "/get_all";
    public static final String USER_ID = USER_BRANCH + "/id";


    public static final String ALL_RESOURCES = "/**";

    public static final String API_ALL_RESOURCES = API + ALL_RESOURCES;
    public static final String AUTH_ALL_RESOURCES = AUTH + ALL_RESOURCES;
    public static final String USER_ALL_RESOURCES = USER + ALL_RESOURCES;
    public static final String COMPANY_ALL_RESOURCES = COMPANY + ALL_RESOURCES;
}
