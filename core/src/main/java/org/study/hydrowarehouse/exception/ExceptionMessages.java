package org.study.hydrowarehouse.exception;

public final class ExceptionMessages {

    private ExceptionMessages() {
    }

    public final static String PICTURE_NOT_FOUND_BY_ID_MESSAGE = "Picture not found. [id = %s]";
    public final static String PICTURE_NOT_REMOVE_MESSAGE = "Picture don't remove. [id = %s]";
    public final static String PRODUCT_BY_ID_NOT_FOUND_FOR_PICTURES_MESSAGE = "Product not found for the picture. [id = %s]";
    public final static String COUNTRY_FOR_PRODUCT_COMPANY_NOT_FOUND_MESSAGE =
            "Country not found for product company. [id = %s, name = %s]";
    public final static String PRODUCT_BY_ID_NOT_FOUND_MESSAGE = "Product not found. [id = %s]";
    public final static String PRODUCT_TYPE_BY_ID_NOT_FOUND_MESSAGE = "Product Type not found. [id = %s]";
    public final static String PRODUCT_CONNECTION_BY_ID_NOT_FOUND_MESSAGE = "Product Connection not found. [id = %s]";
    public final static String PRODUCT_COMPANY_BY_ID_NOT_FOUND_MESSAGE = "Product Company not found. [id = %s]";
    public final static String STORAGE_RACK_BY_ID_NOT_FOUND_MESSAGE = "Storage rack not found. [id = %s]";
    public final static String COUNTRY_BY_ID_NOT_FOUND_MESSAGE = "Country not found. [id = %s]";
    public final static String FILED_REMOVING_FILES_ERROR = "Failed to remove some product's files: ";
    public final static String PRODUCT_SKU_BY_ID_NOT_FOUND_MESSAGE = "Product SKU not found. [id = %s]";
    public final static String INVALID_SKU_STATUS_MESSAGE = "Invalid SKU status: [SKU status = %s].";
    public final static String ID_IS_NULL_MESSAGE = "Id is null. [Type = %s].";
    public final static String OBJECT_IS_NULL_MESSAGE = "Object is null. [Type = %s].";
    public final static String SHELF_BY_ID_NOT_FOUND_MESSAGE = "Shelf not found. [id = %s, name = %s]";
    public final static String SKU_STATUS_BY_NAME_NOT_FOUND_MESSAGE = "SKU status not found. [name = %s]";
    public static final String SKU_STATUS_NOT_FOUND_BY_ID_MESSAGE = "Sku status not found. [id =%s].";
    public final static String STORAGE_RACK_NOT_FOUND_BY_ID_MESSAGE = "Storage rack not found. [id =%s]";
    public final static String USER_COMPANY_BY_ID_NOT_FOUND_MESSAGE = "User company not found. [id = %s]";
    public final static String COUNTRY_NOT_FOUND_MESSAGE = "Country not found. [id = %s, name = %s]";
    public static final String USER_NOT_FOUND_BY_ID_MESSAGE = "User not found. [id = %s]";
    public static final String USER_STATUS_NOT_FOUND_MESSAGE = "User Status not found. [status = %s]";
    public static final String USER_ROLE_NOT_EXIST_MESSAGE = "User Role doesn't exist. [role = %s]";
    public static final String USER_COMPANY_NOT_FOUND_MESSAGE = "User company not found. [id = %s, name = %s, address = %s]";

    public final static String PRODUCT_TYPE = "Product";
    public final static String PRODUCT_SKU_TYPE = "Product SKU";
    public final static String SHELF_TYPE = "Shelf";
    public final static String COUNTRY_TYPE = "Country";
    public final static String PRODUCT_SKU_STATUS_TYPE = "Product SKU Status";
}
