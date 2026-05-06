package it.hgnh.hgnh.models;

import it.hgnh.hgnh.AdminView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class GuestTest {
    @Test
    @DisplayName("Calculate IDCardNumber")
    void calculateIDCarNumberTest() {
        Guest guest = new Guest("Günter", "Gostner", "676767");

    }
}