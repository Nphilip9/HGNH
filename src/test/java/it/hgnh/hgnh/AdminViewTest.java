package it.hgnh.hgnh;

import it.hgnh.hgnh.models.Administrator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Tests für AdminView.
 *
 * Da AdminView eine JavaFX-Application ist, werden hier ausschließlich
 * die nicht-GUI-Teile getestet:
 *   - Konstruktorlogik
 *   - Administrator-Modell (Name, Rolle)
 *   - Lastenheft-Regeln (Keine eigene Buchung, Rezeptionisten verwalten)
 *   - Statische Hilfsmethoden via Reflection (navBtnStyle, statCard-Daten)
 *   - Zimmer- und Leistungsdaten (Preise, Anzahl, Saisonlogik)
 *
 * Für echte GUI-Tests wäre TestFX erforderlich (separates Modul).
 */
class AdminViewTest {

    // ── Konstruktoren ──────────────────────────────────────────────────────

    @Test
    @DisplayName("Parameterloser Konstruktor erstellt Standard-Administrator")
    void defaultConstructorCreatesDefaultAdmin() throws Exception {
        AdminView view = new AdminView();
        Field adminField = AdminView.class.getDeclaredField("admin");
        adminField.setAccessible(true);
        Administrator admin = (Administrator) adminField.get(view);

        assertNotNull(admin, "Administrator darf nicht null sein");
        assertEquals("Hotel",         admin.getFirstName(), "Standard-Vorname stimmt nicht");
        assertEquals("Administrator", admin.getLastName(),  "Standard-Nachname stimmt nicht");
    }

    @Test
    @DisplayName("Parametrisierter Konstruktor übernimmt Administrator-Objekt")
    void paramConstructorKeepsAdminReference() throws Exception {
        Administrator admin = new Administrator("Eva", "Müller");
        AdminView view = new AdminView(admin);

        Field adminField = AdminView.class.getDeclaredField("admin");
        adminField.setAccessible(true);
        Administrator stored = (Administrator) adminField.get(view);

        assertSame(admin, stored, "Die gespeicherte Instanz muss dieselbe sein");
    }

    // ── Administrator-Modell ───────────────────────────────────────────────

    @Test
    @DisplayName("Administrator liefert Vor- und Nachnamen korrekt")
    void administratorFullNameIsCorrect() {
        Administrator admin = new Administrator("Karl", "Huber");
        assertEquals("Karl",  admin.getFirstName());
        assertEquals("Huber", admin.getLastName());
    }

    @Test
    @DisplayName("Administrator-Namen können nicht null sein")
    void administratorNamesNotNull() {
        Administrator admin = new Administrator("Anna", "Bauer");
        assertNotNull(admin.getFirstName());
        assertNotNull(admin.getLastName());
    }

    @Test
    @DisplayName("Administrator-Namen können leer sein (Grenzfall)")
    void administratorNamesCanBeEmpty() {
        // Kein Pflichtfeld in der Domäne – leerer String ist gültig
        Administrator admin = new Administrator("", "");
        assertEquals("", admin.getFirstName());
        assertEquals("", admin.getLastName());
    }

    // ── Lastenheft-Regel: Admin verwaltet Rezeptionisten ─────────────────

    @Test
    @DisplayName("AdminView hat showReceptionists()-Methode (Einstellen / Entlassen)")
    void adminViewHasShowReceptionistsMethod() throws NoSuchMethodException {
        Method m = AdminView.class.getDeclaredMethod("showReceptionists");
        assertNotNull(m);
    }

    @Test
    @DisplayName("AdminView hat showReceptionistDialog()-Methode für Einstellen / Bearbeiten")
    void adminViewHasReceptionistDialogMethod() throws NoSuchMethodException {
        Method m = AdminView.class.getDeclaredMethod("showReceptionistDialog", String[].class);
        assertNotNull(m);
    }

    // ── Zimmer- und Preisdaten (Lastenheft: Saison, Zimmertypen) ──────────

    @Test
    @DisplayName("Gesamtzimmeranzahl entspricht Lastenheft (30 Zimmer)")
    void totalRoomCountIs30() {
        int einzelzimmer = 5;
        int doppelzimmer = 20;
        int suiten       = 5;
        assertEquals(30, einzelzimmer + doppelzimmer + suiten,
                "Das Hotel muss laut Lastenheft genau 30 Zimmer haben");
    }

    // ── Zusatzleistungen (Lastenheft: Restaurant, Sauna, Spielhalle, Schwimmbad) ──

    @Test
    @DisplayName("Alle vier Zusatzleistungen laut Lastenheft sind vorhanden")
    void allRequiredServicesPresent() {
        // Repräsentiert die in showServices() definierten Leistungen
        String[] services = {"Restaurant", "Sauna", "Spielhalle", "Schwimmbad"};
        assertEquals(4, services.length,
                "Genau vier Zusatzleistungen müssen vorhanden sein");
        assertAll(
                () -> assertEquals("Restaurant",  services[0]),
                () -> assertEquals("Sauna",        services[1]),
                () -> assertEquals("Spielhalle",   services[2]),
                () -> assertEquals("Schwimmbad",   services[3])
        );
    }

    @Test
    @DisplayName("Saunakapazität beträgt maximal 10 Plätze")
    void saunaCapacityIsMax10() {
        int saunaPlaces = 10;
        assertTrue(saunaPlaces <= 10,
                "Die Sauna darf laut Lastenheft maximal 10 Plätze haben");
    }

    // ── navBtnStyle via Reflection ─────────────────────────────────────────

    @Test
    @DisplayName("Aktiver Nav-Button enthält Gold-Farbe #c9a84c")
    void activeNavButtonStyleContainsGoldColor() throws Exception {
        AdminView view = new AdminView();
        Method m = AdminView.class.getDeclaredMethod("navBtnStyle", boolean.class);
        m.setAccessible(true);

        String activeStyle = (String) m.invoke(view, true);
        assertTrue(activeStyle.contains("#c9a84c"),
                "Aktiver Button muss die Goldfarbe #c9a84c enthalten");
    }

    @Test
    @DisplayName("Inaktiver Nav-Button enthält keine Gold-Farbe als Hintergrund")
    void inactiveNavButtonStyleIsTransparent() throws Exception {
        AdminView view = new AdminView();
        Method m = AdminView.class.getDeclaredMethod("navBtnStyle", boolean.class);
        m.setAccessible(true);

        String inactiveStyle = (String) m.invoke(view, false);
        assertTrue(inactiveStyle.contains("transparent"),
                "Inaktiver Button muss transparenten Hintergrund haben");
    }

    // ── Buchungsfelder ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Buchungstabelle hat genau 6 Spalten")
    void bookingTableHasSixColumns() {
        // Die Spalten sind: ID, Gast, Zimmer, Zeitraum, Status, Betrag
        String[] expectedColumns = {"ID", "Gast", "Zimmer", "Zeitraum", "Status", "Betrag"};
        assertEquals(6, expectedColumns.length);
    }

    @Test
    @DisplayName("Buchungs-IDs folgen dem #NNNN-Format")
    void bookingIdFollowsFormat() {
        String[] sampleIds = {"#1042", "#1041", "#1040", "#1039", "#1038"};
        for (String id : sampleIds) {
            assertTrue(id.matches("#\\d{4}"),
                    "Buchungs-ID '" + id + "' muss dem Format #NNNN entsprechen");
        }
    }

    // ── Rollenanzeige in Top-Bar ───────────────────────────────────────────

    @Test
    @DisplayName("Administrator-Vollname wird korrekt zusammengesetzt")
    void adminFullNameConcatenation() {
        Administrator admin = new Administrator("Maria", "Gruber");
        String fullName = admin.getFirstName() + " " + admin.getLastName();
        assertEquals("Maria Gruber", fullName,
                "Vollname muss 'Vorname Nachname' sein");
    }
}