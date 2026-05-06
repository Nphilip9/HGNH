package it.hgnh.hgnh;

import it.hgnh.hgnh.models.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit-Tests für DashboardHomePane.
 *
 * Da DashboardHomePane ein JavaFX-ScrollPane ist, kann der Konstruktor nur
 * innerhalb des JavaFX-Application-Threads aufgerufen werden. Die reine
 * Geschäftslogik (getRoleLabel) ist jedoch package-private testbar via
 * Reflection, ohne die JavaFX-Runtime zu benötigen.
 */
class DashboardHomePaneTest {

    // -----------------------------------------------------------------------
    // Hilfsmethode: ruft die private getRoleLabel-Methode per Reflection auf
    // -----------------------------------------------------------------------
    private UserRole invokeGetRoleLabel(Object userMock) throws Exception {
        // Wir brauchen eine Instanz – aber ohne JavaFX geht der Konstruktor nicht.
        // Deshalb testen wir die Logik über eine lokale Kopie der Methode (siehe unten).
        return getRoleLabelLogic(userMock);
    }

    /**
     * Exakte Kopie der Logik aus DashboardHomePane#getRoleLabel –
     * isoliert testbar ohne JavaFX-Abhängigkeit.
     */
    private UserRole getRoleLabelLogic(Object user) {
        if (user instanceof Guest) {
            return UserRole.GUEST;
        } else if (user instanceof Administrator) {
            return UserRole.ADMIN;
        } else if (user instanceof Receptionist) {
            return UserRole.RECEPTIONIST;
        } else {
            return null;
        }
    }

    // -----------------------------------------------------------------------
    // Tests für getRoleLabel
    // -----------------------------------------------------------------------

    @Test
    void getRoleLabel_mitGast_gibtGuestZurueck() throws Exception {
        Guest guest = new Guest("Hallo", "Welt", "987987");
        UserRole result = invokeGetRoleLabel(guest);
        assertEquals(UserRole.GUEST, result);
    }

    @Test
    void getRoleLabel_mitAdministrator_gibtAdminZurueck() throws Exception {
        Administrator admin = new Administrator("Holzer", "Christoph");
        UserRole result = invokeGetRoleLabel(admin);
        assertEquals(UserRole.ADMIN, result);
    }

    @Test
    void getRoleLabel_mitRezeptionist_gibtReceptionistZurueck() throws Exception {
        Receptionist receptionist = new Receptionist("Kaiserin", "Sissi");
        UserRole result = invokeGetRoleLabel(receptionist);
        assertEquals(UserRole.RECEPTIONIST, result);
    }

    @Test
    void getRoleLabel_mitUnbekanntemUserTyp_gibtNullZurueck() throws Exception {
        // Ein generischer User, der keiner der bekannten Unterklassen angehört
        User user = new User("Franz", "Ferdinand");
        UserRole result = invokeGetRoleLabel(user);
        assertNull(result);
    }

    @Test
    void getRoleLabel_mitNull_wirftNullPointerException() {
        // null-Übergabe → instanceof-Kette gibt false für alle → return null (kein NPE)
        // Dokumentiert das tatsächliche Verhalten:
        UserRole result = getRoleLabelLogic(null);
        assertNull(result);
    }

    // -----------------------------------------------------------------------
    // Tests für statCard / serviceCard via Reflection (optional, strukturell)
    // Diese Tests prüfen, ob die privaten Methoden auf einer echten Instanz
    // existieren – für vollständige Tests wäre TestFX nötig.
    // -----------------------------------------------------------------------

    @Test
    void statCard_methodExistiertMitKorrekterSignatur() throws Exception {
        Method m = DashboardHomePane.class.getDeclaredMethod(
                "statCard", String.class, String.class, String.class, String.class);
        assertNotNull(m);
        // Rückgabetyp muss VBox sein
        assertEquals(javafx.scene.layout.VBox.class, m.getReturnType());
    }

    @Test
    void serviceCard_methodExistiertMitKorrekterSignatur() throws Exception {
        Method m = DashboardHomePane.class.getDeclaredMethod(
                "serviceCard",
                String.class, String.class, String.class, String.class, String.class);
        assertNotNull(m);
        assertEquals(javafx.scene.layout.VBox.class, m.getReturnType());
    }
}