package it.hgnh.hgnh;

import it.hgnh.hgnh.models.Administrator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class AdminViewTest {

    @Test
    @DisplayName("AdminView")
    void constructor_storesAdministrator() throws Exception {
        Administrator admin = new Administrator("Eva", "Mayr");
        AdminView view = new AdminView(admin);

        Field f = AdminView.class.getDeclaredField("admin");
        f.setAccessible(true);
        assertSame(admin, f.get(view));
    }

    @Test
    @DisplayName("Administrator mit Standardwerten")
    void defaultConstructor_createsDefaultAdministrator() throws Exception {
        AdminView view = new AdminView();

        Field f = AdminView.class.getDeclaredField("admin");
        f.setAccessible(true);
        Administrator admin = (Administrator) f.get(view);

        assertNotNull(admin);
        assertEquals("Hotel", admin.getFirstName());
        assertEquals("Administrator", admin.getLastName());
    }
}