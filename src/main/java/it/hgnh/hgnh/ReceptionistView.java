package it.hgnh.hgnh;

import it.hgnh.hgnh.models.Receptionist;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Hauptfenster für den Rezeptionisten.
 *
 * Rechte laut Lastenheft:
 *  - Eigene Buchungen und Buchungen für zugeteilte Gäste erstellen/ändern/löschen
 *  - Gäste anlegen und verwalten (nur eigene zugeteilte Gäste)
 *  - Kein Zugriff auf Gäste anderer Rezeptionisten
 *  - Keine Verwaltung von Rezeptionisten-Konten (nur Admin)
 */
public class ReceptionistView extends Application {

    private final Receptionist receptionist;
    private BorderPane root;

    public ReceptionistView(Receptionist receptionist) {
        this.receptionist = receptionist;
    }

    /** Parameterloser Konstruktor für JavaFX launch() */
    public ReceptionistView() {
        this.receptionist = new Receptionist("Klaus", "Huber");
    }

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #0d1117;");

        buildSidebar(stage);
        buildTopBar();
        showGuestOverview();

        Scene scene = new Scene(root, 1200, 750);
        stage.setScene(scene);
        stage.setTitle("Hotel Larcher – Rezeptionist");
        stage.show();
    }

    // ── Sidebar ───────────────────────────────────────────────────────────

    private void buildSidebar(Stage stage) {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(230);
        sidebar.setStyle("-fx-background-color: #161b22; " +
                "-fx-border-color: #30363d; -fx-border-width: 0 1 0 0;");

        VBox logoBox = new VBox(4);
        logoBox.setPadding(new Insets(28, 24, 28, 24));
        logoBox.setStyle("-fx-border-color: #30363d; -fx-border-width: 0 0 1 0;");
        Label logo = new Label("LARCHER");
        logo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; " +
                "-fx-text-fill: #c9a84c; -fx-font-family: 'Georgia';");
        Label roleLabel = new Label("Rezeptionist");
        roleLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.4);");
        logoBox.getChildren().addAll(logo, roleLabel);

        VBox navItems = new VBox(2);
        navItems.setPadding(new Insets(16, 8, 16, 8));

        String[]   labels  = {"Meine Gäste", "Buchungen", "Neue Buchung", "Leistungsübersicht"};
        String[]   icons   = {"👥", "📋", "➕", "🏨"};
        Runnable[] actions = {
                this::showGuestOverview,
                this::showBookings,
                this::showNewBooking,
                this::showServices
        };

        for (int i = 0; i < labels.length; i++) {
            final int idx = i;
            Button btn = navBtn(icons[i], labels[i]);
            btn.setOnAction(e -> {
                navItems.getChildren().forEach(c -> {
                    if (c instanceof Button b) b.setStyle(navBtnStyle(false));
                });
                btn.setStyle(navBtnStyle(true));
                actions[idx].run();
            });
            navItems.getChildren().add(btn);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox bottomBox = new VBox(8);
        bottomBox.setPadding(new Insets(16, 8, 24, 8));
        bottomBox.setStyle("-fx-border-color: #30363d; -fx-border-width: 1 0 0 0;");
        Button logoutBtn = new Button("⏻  Abmelden");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; " +
                "-fx-font-size: 13px; -fx-padding: 10 16; -fx-alignment: center-left; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> {
            new LoginView().start(new Stage());
            stage.close();
        });
        bottomBox.getChildren().add(logoutBtn);

        sidebar.getChildren().addAll(logoBox, navItems, spacer, bottomBox);
        root.setLeft(sidebar);
    }

    // ── Top-Bar ───────────────────────────────────────────────────────────

    private void buildTopBar() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(16, 28, 16, 28));
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setStyle("-fx-background-color: #161b22; " +
                "-fx-border-color: #30363d; -fx-border-width: 0 0 1 0;");

        String name = receptionist.getFirstName() + " " + receptionist.getLastName();
        Label info = new Label(name + " – Rezeptionist  |  Hotel Larcher GmbH");
        info.setStyle("-fx-text-fill: rgba(255,255,255,0.45); -fx-font-size: 12px;");
        topBar.getChildren().add(info);
        root.setTop(topBar);
    }

    // ── Inhalts-Seiten ────────────────────────────────────────────────────

    /**
     * Übersicht aller diesem Rezeptionisten zugewiesenen Gäste.
     * Laut Lastenheft: Rezeptionist sieht NUR seine eigenen Gäste.
     */
    private void showGuestOverview() {
        VBox content = baseContent("Meine Gäste");

        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        Button addGuestBtn      = goldBtn("+ Gast anlegen");
        Button bookForGuestBtn  = goldBtn("Buchung erstellen");
        Button editBtn          = grayBtn("Bearbeiten");
        Button cancelBookingBtn = redBtn("Buchung stornieren");
        toolbar.getChildren().addAll(addGuestBtn, bookForGuestBtn, editBtn, cancelBookingBtn);

        TableView<String[]> table = new TableView<>();
        table.setStyle("-fx-background-color: #161b22;");
        table.getColumns().addAll(
                col("Name",        d -> d[0]),
                col("Ausweisnr.", d -> d[1]),
                col("Zimmer",      d -> d[2]),
                col("Aufenthalt",  d -> d[3]),
                col("Status",      d -> d[4])
        );
        table.getItems().addAll(
                new String[]{"Hans Meier",      "AT1234567", "Doppelzimmer 12",  "20.04 – 25.04.2026", "Eingecheckt"},
                new String[]{"Petra Schneider", "DE9876543", "Suite 02",         "18.04 – 22.04.2026", "Eingecheckt"},
                new String[]{"Lukas Weiß",      "IT5551234", "Einzelzimmer 07",  "23.04 – 26.04.2026", "Erwartet"}
        );
        VBox.setVgrow(table, Priority.ALWAYS);

        addGuestBtn.setOnAction(e     -> showGuestDialog());
        bookForGuestBtn.setOnAction(e -> showNewBooking());
        editBtn.setOnAction(e -> {
            String[] sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) showGuestEditDialog(sel);
        });
        cancelBookingBtn.setOnAction(e -> {
            String[] sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Buchung stornieren");
            confirm.setHeaderText("Buchung für " + sel[0] + " stornieren?");
            // Laut Lastenheft: 10 % Stornierungsgebühr
            confirm.setContentText("Es wird eine Stornierungsgebühr von 10 % erhoben.");
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    Alert done = new Alert(Alert.AlertType.INFORMATION);
                    done.setHeaderText(null);
                    done.setContentText("Buchung wurde storniert.");
                    done.showAndWait();
                }
            });
        });

        content.getChildren().addAll(toolbar, table);
        setContent(content);
    }

    /**
     * Buchungsübersicht aller zugewiesenen Gäste.
     */
    private void showBookings() {
        VBox content = baseContent("Buchungen meiner Gäste");

        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        TextField search = new TextField();
        search.setPromptText("Gast suchen...");
        search.setPrefWidth(250);
        styleTextField(search);
        Button editBtn   = grayBtn("Bearbeiten");
        Button cancelBtn = redBtn("Stornieren");
        toolbar.getChildren().addAll(search, editBtn, cancelBtn);

        TableView<String[]> table = new TableView<>();
        table.setStyle("-fx-background-color: #161b22;");
        table.getColumns().addAll(
                col("Buchungs-ID", d -> d[0]),
                col("Gast",        d -> d[1]),
                col("Leistung",    d -> d[2]),
                col("Zeitraum",    d -> d[3]),
                col("Betrag",      d -> d[4]),
                col("Status",      d -> d[5])
        );
        table.getItems().addAll(
                new String[]{"#1042", "Hans Meier",      "Doppelzimmer 12 (Halbpension)", "20.04 – 25.04.2026", "350 €", "Aktiv"},
                new String[]{"#1043", "Hans Meier",      "Sauna",                         "21.04.2026",          "9 €",   "Bezahlt"},
                new String[]{"#1044", "Petra Schneider", "Suite 02 (Halbpension)",        "18.04 – 22.04.2026", "600 €", "Aktiv"},
                new String[]{"#1045", "Lukas Weiß",      "Einzelzimmer 07 (Halbpension)","23.04 – 26.04.2026", "210 €", "Erwartet"}
        );
        VBox.setVgrow(table, Priority.ALWAYS);

        cancelBtn.setOnAction(e -> {
            String[] sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Buchung stornieren");
            confirm.setHeaderText("Buchung " + sel[0] + " stornieren?");
            confirm.setContentText("Stornierungsgebühr: 10 % des Buchungsbetrags.");
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) table.getItems().remove(sel);
            });
        });
        editBtn.setOnAction(e -> {
            String[] sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) showBookingEditDialog(sel);
        });

        content.getChildren().addAll(toolbar, table);
        setContent(content);
    }

    /**
     * Formular für eine neue Buchung.
     * Rezeptionist kann für einen seiner Gäste oder für sich selbst buchen.
     */
    private void showNewBooking() {
        VBox content = baseContent("Neue Buchung");

        GridPane form = new GridPane();
        form.setHgap(20);
        form.setVgap(14);
        form.setPadding(new Insets(8, 0, 0, 0));

        // Gast auswählen (laut Lastenheft: nur eigene zugewiesene Gäste)
        Label guestLbl = fieldLabel("Gast");
        ComboBox<String> guestCombo = new ComboBox<>();
        guestCombo.getItems().addAll("Hans Meier", "Petra Schneider", "Lukas Weiß",
                receptionist.getFirstName() + " " + receptionist.getLastName() + " (ich selbst)");
        guestCombo.setMaxWidth(Double.MAX_VALUE);
        styleCombo(guestCombo);

        // Zimmertyp (Grundleistung)
        Label zimmerLbl = fieldLabel("Zimmer");
        ComboBox<String> zimmerCombo = new ComboBox<>();
        zimmerCombo.getItems().addAll(
                "Einzelzimmer (Halbpension) – 100 €/Nacht",
                "Einzelzimmer (Vollpension) – 120 €/Nacht",
                "Doppelzimmer (Halbpension) – 70 €/Nacht",
                "Doppelzimmer (Vollpension) – 90 €/Nacht",
                "Suite (Halbpension) – 150 €/Nacht",
                "Suite (Vollpension) – 170 €/Nacht",
                "Nur Zusatzleistungen (kein Zimmer)"
        );
        zimmerCombo.setMaxWidth(Double.MAX_VALUE);
        styleCombo(zimmerCombo);

        // Anreise / Abreise
        Label anrLbl = fieldLabel("Anreise");
        DatePicker anreise = new DatePicker();
        anreise.setMaxWidth(Double.MAX_VALUE);
        anreise.setStyle("-fx-background-color: rgba(255,255,255,0.05); " +
                "-fx-border-color: #30363d; -fx-border-radius: 6; -fx-text-fill: white;");

        Label abrLbl = fieldLabel("Abreise");
        DatePicker abreise = new DatePicker();
        abreise.setMaxWidth(Double.MAX_VALUE);
        abreise.setStyle("-fx-background-color: rgba(255,255,255,0.05); " +
                "-fx-border-color: #30363d; -fx-border-radius: 6; -fx-text-fill: white;");

        // Notbett (laut Lastenheft: +10 €/Nacht, max. 1 pro Zimmer)
        Label notbettLbl = fieldLabel("Notbett (+10 €/Nacht)");
        CheckBox notbettCheck = new CheckBox();
        notbettCheck.setStyle("-fx-text-fill: white;");

        form.add(guestLbl,    0, 0); form.add(guestCombo,   1, 0);
        form.add(zimmerLbl,   0, 1); form.add(zimmerCombo,  1, 1);
        form.add(anrLbl,      0, 2); form.add(anreise,      1, 2);
        form.add(abrLbl,      0, 3); form.add(abreise,      1, 3);
        form.add(notbettLbl,  0, 4); form.add(notbettCheck, 1, 4);

        // Zusatzleistungen (laut Lastenheft: kombinierbar mit Grundleistung)
        Label zusatzTitle = new Label("Zusatzleistungen");
        zusatzTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #c9a84c;");

        HBox zusatzBox = new HBox(16);
        zusatzBox.setPadding(new Insets(4, 0, 0, 0));
        for (String z : new String[]{"Restaurant (25 €/Person)", "Sauna (3 €/Std.)",
                "Spielhalle (2 €/Std.)", "Schwimmbad (1 €/Std.)"}) {
            CheckBox cb = new CheckBox(z);
            cb.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");
            zusatzBox.getChildren().add(cb);
        }

        // Zahlungsart (laut Lastenheft: Bar, Überweisung, Karte)
        Label zahlungTitle = new Label("Zahlungsart");
        zahlungTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #c9a84c;");
        ComboBox<String> zahlungCombo = new ComboBox<>();
        zahlungCombo.getItems().addAll("Bar", "Karte", "Überweisung");
        zahlungCombo.setPromptText("Wählen...");
        zahlungCombo.setPrefWidth(200);
        styleCombo(zahlungCombo);

        HBox btnRow = new HBox(12);
        Button submitBtn = goldBtn("Buchung speichern");
        Button cancelBtn = grayBtn("Abbrechen");
        btnRow.getChildren().addAll(submitBtn, cancelBtn);

        submitBtn.setOnAction(e -> {
            if (guestCombo.getValue() == null || zimmerCombo.getValue() == null
                    || anreise.getValue() == null || abreise.getValue() == null) {
                Alert err = new Alert(Alert.AlertType.WARNING);
                err.setHeaderText(null);
                err.setContentText("Bitte alle Pflichtfelder ausfüllen.");
                err.showAndWait();
                return;
            }
            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Buchung gespeichert");
            ok.setHeaderText(null);
            ok.setContentText("Die Buchung für " + guestCombo.getValue() + " wurde erfolgreich gespeichert.");
            ok.showAndWait();
            showGuestOverview();
        });
        cancelBtn.setOnAction(e -> showGuestOverview());

        content.getChildren().addAll(form, zusatzTitle, zusatzBox, zahlungTitle, zahlungCombo, btnRow);
        setContent(content);
    }

    /**
     * Leistungsübersicht (Öffnungszeiten, Preise, freie Plätze).
     * Laut Lastenheft müssen immer Öffnungszeiten, Preis und freie Plätze angezeigt werden.
     */
    private void showServices() {
        VBox content = baseContent("Leistungsübersicht");

        TableView<String[]> table = new TableView<>();
        table.setStyle("-fx-background-color: #161b22;");
        table.getColumns().addAll(
                col("Leistung",        d -> d[0]),
                col("Öffnungszeiten",  d -> d[1]),
                col("Preis",           d -> d[2]),
                col("Freie Plätze",    d -> d[3])
        );
        table.getItems().addAll(
                new String[]{"Restaurant", "12:00–14:00 / 18:00–22:00", "25 €/Person",  "38"},
                new String[]{"Sauna",      "15:00–22:00",               "3 €/Stunde",   "7"},
                new String[]{"Spielhalle", "07:00–22:00",               "2 €/Stunde",   "10"},
                new String[]{"Schwimmbad", "07:00–22:00",               "1 €/Stunde",   "22"}
        );
        VBox.setVgrow(table, Priority.ALWAYS);

        content.getChildren().add(table);
        setContent(content);
    }

    // ── Dialoge ───────────────────────────────────────────────────────────

    /**
     * Gast anlegen.
     * Laut Lastenheft werden gespeichert: Vorname, Nachname, Ausweisnummer, Steuernummer.
     */
    private void showGuestDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Neuen Gast anlegen");
        dialog.getDialogPane().setStyle("-fx-background-color: #161b22;");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(14);
        form.setPadding(new Insets(20));

        // Laut Lastenheft: Vorname, Nachname, Ausweisnummer, Steuernummer
        // (oder alternativ Geburtsort, Geburtsdatum, Geschlecht zur Berechnung)
        String[] labels = {"Vorname", "Nachname", "Ausweisnummer", "Steuernummer"};
        for (int i = 0; i < labels.length; i++) {
            Label l = new Label(labels[i]);
            l.setStyle("-fx-text-fill: rgba(255,255,255,0.6); -fx-font-size: 12px;");
            TextField tf = new TextField();
            styleTextField(tf);
            form.add(l, 0, i);
            form.add(tf, 1, i);
        }

        dialog.getDialogPane().setContent(form);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait();
    }

    private void showGuestEditDialog(String[] data) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Gast bearbeiten");
        dialog.getDialogPane().setStyle("-fx-background-color: #161b22;");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(14);
        form.setPadding(new Insets(20));

        String[] labels = {"Name", "Ausweisnr.", "Zimmer", "Aufenthalt", "Status"};
        for (int i = 0; i < labels.length; i++) {
            Label l = new Label(labels[i]);
            l.setStyle("-fx-text-fill: rgba(255,255,255,0.6); -fx-font-size: 12px;");
            TextField tf = new TextField(i < data.length ? data[i] : "");
            styleTextField(tf);
            form.add(l, 0, i);
            form.add(tf, 1, i);
        }

        dialog.getDialogPane().setContent(form);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait();
    }

    private void showBookingEditDialog(String[] data) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Buchung bearbeiten – " + data[0]);
        dialog.getDialogPane().setStyle("-fx-background-color: #161b22;");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(14);
        form.setPadding(new Insets(20));

        String[] labels = {"Buchungs-ID", "Gast", "Leistung", "Zeitraum", "Betrag", "Status"};
        for (int i = 0; i < labels.length; i++) {
            Label l = new Label(labels[i]);
            l.setStyle("-fx-text-fill: rgba(255,255,255,0.6); -fx-font-size: 12px;");
            TextField tf = new TextField(i < data.length ? data[i] : "");
            styleTextField(tf);
            if (i == 0) tf.setEditable(false);
            form.add(l, 0, i);
            form.add(tf, 1, i);
        }

        dialog.getDialogPane().setContent(form);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait();
    }

    // ── Hilfsmethoden ─────────────────────────────────────────────────────

    private TableColumn<String[], String> col(String title,
                                              java.util.function.Function<String[], String> getter) {
        TableColumn<String[], String> c = new TableColumn<>(title);
        c.setCellValueFactory(d -> new SimpleStringProperty(getter.apply(d.getValue())));
        return c;
    }

    private Label fieldLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        return l;
    }

    private VBox baseContent(String title) {
        VBox box = new VBox(20);
        box.setPadding(new Insets(32, 36, 32, 36));
        box.setStyle("-fx-background-color: #0d1117;");
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; " +
                "-fx-text-fill: white; -fx-font-family: 'Georgia';");
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #30363d;");
        box.getChildren().addAll(titleLabel, sep);
        return box;
    }

    private Button navBtn(String icon, String label) {
        Button btn = new Button(icon + "  " + label);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle(navBtnStyle(false));
        return btn;
    }

    private String navBtnStyle(boolean active) {
        if (active) return
                "-fx-background-color: rgba(201,168,76,0.12); -fx-text-fill: #c9a84c;" +
                        "-fx-font-size: 13px; -fx-padding: 10 16; -fx-alignment: center-left;" +
                        "-fx-background-radius: 6; -fx-border-color: #c9a84c;" +
                        "-fx-border-width: 0 0 0 3; -fx-cursor: hand;";
        return "-fx-background-color: transparent; -fx-text-fill: rgba(255,255,255,0.65);" +
                "-fx-font-size: 13px; -fx-padding: 10 16; -fx-alignment: center-left;" +
                "-fx-background-radius: 6; -fx-cursor: hand;";
    }

    private Button goldBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: #c9a84c; -fx-text-fill: #0d1117; " +
                "-fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6; -fx-cursor: hand;");
        return b;
    }

    private Button grayBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: #30363d; -fx-text-fill: white; " +
                "-fx-padding: 8 16; -fx-background-radius: 6; -fx-cursor: hand;");
        return b;
    }

    private Button redBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6; -fx-cursor: hand;");
        return b;
    }

    private void styleTextField(TextField tf) {
        tf.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-border-color: #30363d;" +
                "-fx-border-radius: 6; -fx-background-radius: 6;" +
                "-fx-text-fill: white; -fx-prompt-text-fill: rgba(255,255,255,0.25); -fx-padding: 8 12;");
    }

    private void styleCombo(ComboBox<?> cb) {
        cb.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-border-color: #30363d;" +
                "-fx-border-radius: 6; -fx-background-radius: 6; -fx-text-fill: white;");
    }

    private void setContent(VBox content) {
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #0d1117; -fx-background: #0d1117;");
        root.setCenter(scroll);
    }

    public static void main(String[] args) {
        launch(args);
    }
}