package org.study.hydrowarehouse.controller;

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
    public static final String CEO_BRANCH = API + "/ceo";
    public static final String GUEST_BRANCH = API + "/guest";
    public static final String USER_BRANCH = API + "/user";
    public static final String AUTH_AUTHENTICATION = AUTH + "/authentication";
    public static final String AUTH_CREATE = AUTH + "/create";


    public static final String COMPANY = "/company";
    public static final String COMPANY_BRANCH = GUEST_BRANCH + COMPANY;
    public static final String COMPANY_CREATE = ADMIN_BRANCH + COMPANY + "/create";
    public static final String COMPANY_ID = COMPANY_BRANCH + "/id";
    public static final String COMPANY_ALL = COMPANY_BRANCH + "/get_all";
    public static final String COMPANY_BY_NAME = COMPANY_BRANCH + "/find_company";

    public static final String COMPANY_UPDATE = ADMIN_BRANCH + COMPANY + "/update_company";

    public static final String PRODUCT_COMPANY = "/product_company";
    public static final String PRODUCT_COMPANY_BRANCH = GUEST_BRANCH + PRODUCT_COMPANY;

    public static final String PRODUCT_COMPANY_CREATE = ADMIN_BRANCH + PRODUCT_COMPANY + "/create";
    public static final String PRODUCT_COMPANY_UPDATE = ADMIN_BRANCH + PRODUCT_COMPANY + "/update";
    public static final String PRODUCT_COMPANY_ALL = PRODUCT_COMPANY_BRANCH + "/all";
    public static final String PRODUCT_COMPANY_ID = PRODUCT_COMPANY_BRANCH + "/id";
    public static final String PRODUCT_COMPANY_NAME = PRODUCT_COMPANY_BRANCH + "/name";

    public static final String PRODUCT_CONNECTION = "/product_connection";
    public static final String PRODUCT_CONNECTION_BRANCH = GUEST_BRANCH + PRODUCT_CONNECTION;

    public static final String PRODUCT_CONNECTION_CREATE = ADMIN_BRANCH + PRODUCT_CONNECTION + "/create";

    public static final String PRODUCT_CONNECTION_ALL = PRODUCT_CONNECTION_BRANCH + "/all";

    public static final String PRODUCT_CONNECTION_ID = PRODUCT_CONNECTION_BRANCH + "/id";

    //set into the admin's branch
    public static final String PRODUCT_CONNECTION_UPDATE = ADMIN_BRANCH + PRODUCT_CONNECTION + "/update";


    public static final String PRODUCT = "/product";
    public static final String PRODUCT_BRANCH = GUEST_BRANCH + PRODUCT;

    public static final String PRODUCT_CREATE = ADMIN_BRANCH + PRODUCT + "/create";
    public static final String PRODUCT_UPDATE = ADMIN_BRANCH + PRODUCT + "/update";
    public static final String PRODUCT_UPDATE_IMAGES = PRODUCT_UPDATE + "/images";
    public static final String PRODUCT_REMOVE = ADMIN_BRANCH + PRODUCT + "/remove";
    public static final String PRODUCT_BY_ID = PRODUCT_BRANCH + "/id";
    public static final String PRODUCT_ALL = PRODUCT_BRANCH + "/all";
    public static final String PRODUCT_ALL_BY_PRESSURE = PRODUCT_BRANCH + "/pressure";
    public static final String PRODUCT_ALL_BY_FLOW_RATE = PRODUCT_BRANCH + "/flow_rate";
    public static final String PRODUCT_ALL_BY_TYPE = PRODUCT_BRANCH + "/type";
    public static final String PRODUCT_ALL_BY_COMPANY = PRODUCT_BRANCH + "/company";
    public static final String PRODUCT_ALL_BY_STORAGE_RACK = PRODUCT_BRANCH + "/storage_rack";


    public static final String PRODUCT_TYPE = "/product_type";
    public static final String PRODUCT_TYPE_BRANCH = GUEST_BRANCH + PRODUCT_TYPE;
    public static final String PRODUCT_TYPE_CREATE = ADMIN_BRANCH + PRODUCT_TYPE + "/create";
    public static final String PRODUCT_TYPE_UPDATE = ADMIN_BRANCH + PRODUCT_TYPE + "/update";
    public static final String PRODUCT_TYPE_REMOVE = ADMIN_BRANCH + PRODUCT_TYPE + "/remove";
    public static final String PRODUCT_TYPE_ALL = PRODUCT_TYPE_BRANCH + "/all";
    public static final String PRODUCT_TYPE_BY_ID = PRODUCT_TYPE_BRANCH + "/id";
    public static final String PRODUCT_TYPE_BY_NAME = PRODUCT_TYPE_BRANCH + "/name";


    public static final String SHELF = ADMIN_BRANCH + "/shelf";
    public static final String SHELF_CREATE = SHELF + "/create";
    public static final String SHELF_UPDATE = SHELF + "/update";
    public static final String SHELF_BY_ID = SHELF + "/id";
    public static final String SHELF_BY_NAME = SHELF + "/name";
    public static final String SHELF_REMOVE = SHELF + "/remove";
    public static final String SHELF_ALL = SHELF + "/all";


    public static final String STORAGE_RACK = ADMIN_BRANCH + "/storage_rack";
    public static final String STORAGE_RACK_CREATE = STORAGE_RACK + "/create";
    public static final String STORAGE_RACK_UPDATE = STORAGE_RACK + "/update";
    public static final String STORAGE_RACK_ID = STORAGE_RACK + "/id";
    public static final String STORAGE_RACK_ALL = STORAGE_RACK + "/all";


    public static final String USER_COMPANY = "/user_company";
    public static final String USER_COMPANY_BRANCH = GUEST_BRANCH + USER_COMPANY;
    public static final String USER_COMPANY_CREATE = ADMIN_BRANCH + USER_COMPANY + "/create";
    public static final String USER_COMPANY_UPDATE = ADMIN_BRANCH + USER_COMPANY + "/update";
    public static final String USER_COMPANY_BY_NAME = USER_COMPANY_BRANCH + "/name";
    public static final String USER_COMPANY_BY_ID = USER_COMPANY_BRANCH + "/id";
    public static final String USER_COMPANY_ALL = USER_COMPANY_BRANCH + "/all";

    public static final String COUNTRY = GUEST_BRANCH + "/country";
    public static final String COUNTRY_ALL = COUNTRY + "/all";
    public static final String COUNTRY_ID = COUNTRY + "/id";
    public static final String COUNTRY_NAME = COUNTRY + "/name";
    public static final String COUNTRY_BY_PRODUCT = COUNTRY + "/by_product";

    public static final String USER = "/user";
    public static final String USER_CURRENT = API + USER + "/me";
    public static final String USER_UPDATE =USER_BRANCH +  "/update";
    public static final String USER_ALL = ADMIN_BRANCH + USER + "/get_all";
    public static final String USER_UPDATE_STATUS = ADMIN_BRANCH + USER + "/update_status";
    public static final String USER_UPDATE_ROLE = ADMIN_BRANCH + USER + "/update_role";
    public static final String USER_FIND_BY_STATUS = ADMIN_BRANCH + USER + "/get_all_by_status";
    public static final String USER_FIND_BY_ROLE = ADMIN_BRANCH + USER + "/get_all_by_role";
    public static final String USER_ID = ADMIN_BRANCH + USER + "/id";

    public static final String PICTURE_BRANCH = ADMIN_BRANCH + "/picture";
    public static final String PICTURE_CREATE = PICTURE_BRANCH + "/create";
    public static final String PICTURE_UPDATE = PICTURE_BRANCH + "/update";
    public static final String PICTURE_REMOVE = PICTURE_BRANCH + "/remove";
    public static final String PICTURE_BY_PRODUCT = PICTURE_BRANCH + "/product_id";
    public static final String PICTURE_BY_ID = PICTURE_BRANCH + "/id";
    public static final String PICTURE_ALL = PICTURE_BRANCH + "/all";


    public static final String SKU_STATUS = "/sku_status";
    public static final String SKU_STATUS_ADMIN_BRANCH = ADMIN_BRANCH + SKU_STATUS;
    public static final String SKU_STATUS_CEO_BRANCH = CEO_BRANCH + SKU_STATUS;
    public static final String SKU_STATUS_CREATE = SKU_STATUS_CEO_BRANCH + "/create";
    public static final String SKU_STATUS_UPDATE = SKU_STATUS_CEO_BRANCH + "/update";
    public static final String SKU_STATUS_REMOVE = SKU_STATUS_CEO_BRANCH + "/remove";
    public static final String SKU_STATUS_BY_ID = SKU_STATUS_ADMIN_BRANCH + "/id";
    public static final String SKU_STATUS_BY_STATUS = SKU_STATUS_ADMIN_BRANCH + "/status";
    public static final String SKU_STATUS_ALL = SKU_STATUS_ADMIN_BRANCH + "/all";

    public static final String SKU_BRANCH = ADMIN_BRANCH + "/product_sku";
    public static final String SKU_CREATE = SKU_BRANCH + "/create";
    public static final String SKU_UPDATE = SKU_BRANCH + "/update";
    public static final String SKU_REMOVE = SKU_BRANCH + "/remove";
    public static final String SKU_BY_ID = SKU_BRANCH + "/id";
    public static final String SKU_BY_CODE = SKU_BRANCH + "/code";
    public static final String SKU_ALL_BY_STATUS = SKU_BRANCH + "/by_status";
    public static final String SKU_ALL_BY_PRODUCT = SKU_BRANCH + "/by_product";

    public static final String ALL_RESOURCES = "/**";

    public static final String API_ALL_RESOURCES = API + ALL_RESOURCES;
    public static final String AUTH_ALL_RESOURCES = AUTH + ALL_RESOURCES;
    public static final String USER_ALL_RESOURCES = USER_BRANCH + ALL_RESOURCES;
    public static final String ADMIN_ALL_RESOURCES = ADMIN_BRANCH + ALL_RESOURCES;
    public static final String CEO_ALL_RESOURCES = CEO_BRANCH + ALL_RESOURCES;
    public static final String GUEST_ALL_RESOURCES = GUEST_BRANCH + ALL_RESOURCES;
//    public static final String COMPANY_ALL_RESOURCES = COMPANY + ALL_RESOURCES;
}
