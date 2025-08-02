package org.study.hydrowarehouse.entity;

/**
 * Defines the set of roles available in the system.
 * <p>
 * Each role represents a specific level of access and responsibilities within the application, and can be assigned
 * to users to control permissions and functionality they can access.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
public enum ERole {

    ADMIN,
    CEO,
    MANAGER,

    USER,

    CUSTOMER;
}
