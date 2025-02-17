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

    public static final String PRODUCT_COMPANY = USER_BRANCH + "/product_company";

    public static final String PRODUCT_COMPANY_CREATE = PRODUCT_COMPANY + "/create";
    public static final String PRODUCT_COMPANY_UPDATE = PRODUCT_COMPANY + "/update";
    public static final String PRODUCT_COMPANY_ALL = PRODUCT_COMPANY + "/all";
    public static final String PRODUCT_COMPANY_ID = PRODUCT_COMPANY + "/id";
    public static final String PRODUCT_COMPANY_NAME = PRODUCT_COMPANY + "/name";

    public static final String PRODUCT_CONNECTION = USER_BRANCH + "/product_connection";

    public static final String PRODUCT_CONNECTION_CREATE = PRODUCT_CONNECTION + "/create";

    public static final String PRODUCT_CONNECTION_ALL = PRODUCT_CONNECTION + "/all";

    public static final String PRODUCT_CONNECTION_ID = PRODUCT_CONNECTION + "/id";

    //set into the admin's branch
    public static final String PRODUCT_CONNECTION_UPDATE = PRODUCT_CONNECTION + "/update";


    public static final String PRODUCT = USER_BRANCH + "/product";

    public static final String PRODUCT_CREATE = PRODUCT + "/create";
    public static final String PRODUCT_UPDATE = PRODUCT + "/update";
    public static final String PRODUCT_REMOVE = PRODUCT + "/remove";
    public static final String PRODUCT_BY_ID = PRODUCT + "/id";
    public static final String PRODUCT_ALL = PRODUCT + "/all";
    public static final String PRODUCT_ALL_BY_PRESSURE = PRODUCT + "/pressure";
    public static final String PRODUCT_ALL_BY_FLOW_RATE = PRODUCT + "/flow_rate";
    public static final String PRODUCT_ALL_BY_TYPE = PRODUCT + "/type";
    public static final String PRODUCT_ALL_BY_COMPANY = PRODUCT + "/company";
    public static final String PRODUCT_ALL_BY_STORAGE_RACK = PRODUCT + "/storage_rack";


    public static final String PRODUCT_TYPE = USER_BRANCH + "/product_type";
    public static final String PRODUCT_TYPE_CREATE = PRODUCT_TYPE + "/create";
    public static final String PRODUCT_TYPE_UPDATE = PRODUCT_TYPE + "/update";
    public static final String PRODUCT_TYPE_REMOVE = PRODUCT_TYPE + "/remove";
    public static final String PRODUCT_TYPE_ALL = PRODUCT_TYPE + "/all";
    public static final String PRODUCT_TYPE_BY_ID = PRODUCT_TYPE + "/id";
    public static final String PRODUCT_TYPE_BY_NAME = PRODUCT_TYPE + "/name";


    public static final String SHELF = USER_BRANCH + "/shelf";
    public static final String SHELF_CREATE = SHELF + "/create";
    public static final String SHELF_UPDATE = SHELF + "/update";
    public static final String SHELF_BY_ID = SHELF + "/id";
    public static final String SHELF_BY_NAME = SHELF + "/name";
    public static final String SHELF_REMOVE = SHELF + "/remove";


    public static final String STORAGE_RACK = USER_BRANCH + "/storage_rack";
    public static final String STORAGE_RACK_CREATE = STORAGE_RACK + "/create";
    public static final String STORAGE_RACK_UPDATE = STORAGE_RACK + "/update";
    public static final String STORAGE_RACK_ID = STORAGE_RACK + "/id";
    public static final String STORAGE_RACK_ALL = STORAGE_RACK + "/all";


    public static final String USER_COMPANY = USER_BRANCH + "/user_company";
    public static final String USER_COMPANY_CREATE = USER_COMPANY + "/create";
    public static final String USER_COMPANY_BY_NAME = USER_COMPANY + "/name";
    public static final String USER_COMPANY_BY_ID = USER_COMPANY + "/id";
    public static final String USER_COMPANY_ALL = USER_COMPANY + "/all";

    public static final String COUNTRY = USER_BRANCH + "/country";
    public static final String COUNTRY_ALL = COUNTRY + "/all";
    public static final String COUNTRY_ID = COUNTRY + "/id";
    public static final String COUNTRY_NAME = COUNTRY + "/name";

    public static final String USER = API + "/user";
    public static final String USER_ALL = ADMIN_BRANCH + "/get_all";
    public static final String USER_ID = USER_BRANCH + "/id";


    public static final String ALL_RESOURCES = "/**";

    public static final String API_ALL_RESOURCES = API + ALL_RESOURCES;
    public static final String AUTH_ALL_RESOURCES = AUTH + ALL_RESOURCES;
    public static final String USER_ALL_RESOURCES = USER + ALL_RESOURCES;
    public static final String COMPANY_ALL_RESOURCES = COMPANY + ALL_RESOURCES;
}
