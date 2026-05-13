package it.hgnh.hgnh.models;

/**
 * Define the state of a room or a reservation.
 */
public enum State {
    OCCUPIED,
    AVAILABLE,
    RESERVED,
    CANCELLED,
    CHECKED_IN,
    CHECKED_OUT,
    WAITING_FOR_CHECK_IN;
}
