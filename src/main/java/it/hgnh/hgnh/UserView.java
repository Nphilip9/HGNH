package it.hgnh.hgnh;

import it.hgnh.hgnh.models.Guest;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Hauptfenster für den Gast.
 *
 * Rechte laut Lastenheft:
 *  - Nur eigene Buchungen einsehen
 *  - Buchungen nur für sich selbst vornehmen (Grund- und Zusatzleistungen)
 *  - Buchungen stornieren (10 % Gebühr)
 *  - Zusatzleistungen ohne Zimmer möglich → sofort zu bezahlen
 */
public class UserView extends Application {

    private final Guest guest;
    private BorderPane root;

    public UserView(Guest guest) {
        this.guest = guest;
    }

    /** Parameterloser Konstruktor für JavaFX launch() */
    public UserView() {
        this.guest = new Guest("Hans", "Meier", "AT1234567");
    }

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #0d1117;");

        buildSidebar(stage);
        buildTopBar();
        showMyBookings();

        Scene scene = new Scene(root, 1100, 720);
        stage.setScene(scene);
        stage.setTitle("Hotel Larcher – Gast");
        stage.show();
    }

    // ── Sidebar ───────────────────────────────────────────────────────────

    private void buildSidebar(Stage stage) {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #161b22; " +
                "-fx-border-color: #30363d; -fx-border-width: 0 1 0 0;");

        VBox logoBox = new VBox(4);
        logoBox.setPadding(new Insets(28, 24, 28, 24));
        logoBox.setStyle("-fx-border-color: #30363d; -fx-border-width: 0 0 1 0;");
        Label logo = new Label("LARCHER");
        logo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; " +
                "-fx-text-fill: #c9a84c; -fx-font-family: 'Georgia';");
        Label roleLabel = new Label("Gast");
        roleLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.4);");
        logoBox.getChildren().addAll(logo, roleLabel);

        VBox navItems = new VBox(2);
        navItems.setPadding(new Insets(16, 8, 16, 8));

        String[]   labels  = {"Meine Buchungen", "Leistung buchen", "Leistungsübersicht"};
        String[]   icons   = {"📋", "➕", "🏨"};
        Runnable[] actions = {
                this::showMyBookings,
                this::showBookServices,
                this::showServicesOverview
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

        String name = guest.getFirstName() + " " + guest.getLastName();
        Label info = new Label(name + " – Gast  |  Hotel Larcher GmbH");
        info.setStyle("-fx-text-fill: rgba(255,255,255,0.45); -fx-font-size: 12px;");
        topBar.getChildren().add(info);
        root.setTop(topBar);
    }

    // ── Inhalts-Seiten ────────────────────────────────────────────────────

    /**
     * Laut Lastenheft: Gast hat nur Einsicht in seine eigenen Buchungen.
     */
    private void showMyBookings() {
        VBox content = baseContent("Meine Buchungen");

        // Aktueller Aufenthalt – Infocard
        HBox infoCard = new HBox(16);
        infoCard.setAlignment(Pos.CENTER_LEFT);
        infoCard.setPadding(new Insets(16, 20, 16, 20));
        infoCard.setStyle("-fx-background-color: rgba(201,168,76,0.08); " +
                "-fx-background-radius: 8; -fx-border-color: rgba(201,168,76,0.3); -fx-border-radius: 8;");
        Label infoText = new Label(
                "🏨  Aktueller Aufenthalt: Doppelzimmer 12  |  20.04 – 25.04.2026  |  Halbpension");
        infoText.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 13px;");
        infoCard.getChildren().add(infoText);

        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        Button cancelBtn = redBtn("Buchung stornieren");
        // Laut Lastenheft: Stornierungsgebühr 10 %
        Label hinweis = new Label("Stornierungsgebühr: 10 % des Buchungsbetrags");
        hinweis.setStyle("-fx-text-fill: rgba(255,255,255,0.35); -fx-font-size: 11px;");
        toolbar.getChildren().addAll(cancelBtn, hinweis);

        TableView<String[]> table = new TableView<>();
        table.setStyle("-fx-background-color: #161b22;");
        table.getColumns().addAll(
                col("Buchungs-ID",       d -> d[0]),
                col("Leistung",          d -> d[1]),
                col("Datum / Zeitraum",  d -> d[2]),
                col("Betrag",            d -> d[3]),
                col("Status",            d -> d[4])
        );
        table.getItems().addAll(
                new String[]{"#1042", "Doppelzimmer 12 (Halbpension)", "20.04 – 25.04.2026", "350 €", "Aktiv"},
                new String[]{"#1043", "Sauna",                         "21.04.2026",          "9 €",   "Bezahlt"},
                new String[]{"#1044", "Restaurant (Abendessen)",        "21.04.2026",          "25 €",  "Aktiv"}
        );
        VBox.setVgrow(table, Priority.ALWAYS);

        cancelBtn.setOnAction(e -> {
            String[] sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                Alert warn = new Alert(Alert.AlertType.WARNING);
                warn.setHeaderText(null);
                warn.setContentText("Bitte zuerst eine Buchung auswählen.");
                warn.showAndWait();
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Buchung stornieren");
            confirm.setHeaderText("Buchung " + sel[0] + " wirklich stornieren?");
            confirm.setContentText("Es wird eine Stornierungsgebühr von 10 % erhoben.");
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    table.getItems().remove(sel);
                    Alert done = new Alert(Alert.AlertType.INFORMATION);
                    done.setHeaderText(null);
                    done.setContentText("Buchung " + sel[0] + " wurde storniert.");
                    done.showAndWait();
                }
            });
        });

        content.getChildren().addAll(infoCard, toolbar, table);
        setContent(content);
    }

    /**
     * Gast bucht Leistungen nur für sich selbst.
     * Laut Lastenheft: Zusatzleistungen ohne Zimmer → sofortige Bezahlung.
     */
    private void showBookServices() {
        VBox content = baseContent("Leistung buchen");

        Label subtitle = new Label(
                "Wählen Sie eine Grundleistung (Zimmer) oder eine Zusatzleistung.");
        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.45); -fx-font-size: 13px;");
        subtitle.setWrapText(true);

        Label hinweis = new Label(
                "ℹ️  Zusatzleistungen ohne Zimmer müssen direkt bei der Buchung bezahlt werden.");
        hinweis.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 12px;");
        hinweis.setWrapText(true);

        // Zimmer / Grundleistung
        Label zimmerTitle = new Label("Zimmer (Grundleistung)");
        zimmerTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #c9a84c;");

        GridPane zimmerCards = new GridPane();
        zimmerCards.setHgap(16);
        zimmerCards.setVgap(16);

        String[][] zimmer = {
                {"🛏", "Einzelzimmer",  "Halbpension: 100 €\nVollpension: 120 €", "Plätze frei: 3"},
                {"🛏🛏", "Doppelzimmer", "Halbpension: 70 €\nVollpension: 90 €",   "Plätze frei: 8"},
                {"🌟", "Suite",         "Halbpension: 150 €\nVollpension: 170 €", "Plätze frei: 2"}
        };
        for (int i = 0; i < zimmer.length; i++) {
            String[] z = zimmer[i];
            VBox card = serviceCard(z[0], z[1], z[2], z[3]);
            Button btn = goldBtn("Zimmer buchen");
            btn.setMaxWidth(Double.MAX_VALUE);
            final String name = z[1];
            btn.setOnAction(e -> showRoomBookingDialog(name));
            card.getChildren().add(btn);
            zimmerCards.add(card, i % 2, i / 2);
        }

        // Zusatzleistungen
        Label zusatzTitle = new Label("Zusatzleistungen");
        zusatzTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #c9a84c;");

        GridPane zusatzCards = new GridPane();
        zusatzCards.setHgap(16);
        zusatzCards.setVgap(16);

        // Laut Lastenheft: Restaurant, Sauna, Spielhalle, Schwimmbad
        String[][] services = {
                {"🍽️", "Restaurant",  "25 €/Person",  "12:00–14:00\n18:00–22:00", "Freie Plätze: 38"},
                {"🧖",  "Sauna",      "3 €/Stunde",   "15:00–22:00",              "Freie Plätze: 7"},
                {"🎮",  "Spielhalle", "2 €/Stunde",   "07:00–22:00",              "Freie Plätze: 10"},
                {"🏊",  "Schwimmbad", "1 €/Stunde",   "07:00–22:00",              "Freie Plätze: 22"}
        };
        for (int i = 0; i < services.length; i++) {
            String[] s = services[i];
            VBox card = serviceCard(s[0], s[1], s[2] + "\n" + s[3], s[4]);
            Button btn = goldBtn("Buchen");
            btn.setMaxWidth(Double.MAX_VALUE);
            final String name = s[1];
            btn.setOnAction(e -> showServiceBookingDialog(name));
            card.getChildren().add(btn);
            zusatzCards.add(card, i % 2, i / 2);
        }

        content.getChildren().addAll(subtitle, hinweis, zimmerTitle, zimmerCards, zusatzTitle, zusatzCards);
        setContent(content);
    }

    /**
     * Leistungsübersicht mit Preisen und Öffnungszeiten.
     * Laut Lastenheft müssen immer angezeigt werden: Öffnungszeiten, Preis, freie Plätze.
     */
    private void showServicesOverview() {
        VBox content = baseContent("Leistungsübersicht");

        Label note = new Label(
                "Alle Preise verstehen sich pro Person. " +
                        "Zusatzleistungen ohne Zimmer sind sofort zu bezahlen.");
        note.setStyle("-fx-text-fill: rgba(255,255,255,0.4); -fx-font-size: 12px;");
        note.setWrapText(true);

        // Zimmerpreise
        Label zimmerTitle = new Label("Zimmerpreise (Halbpension)");
        zimmerTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #c9a84c;");

        TableView<String[]> zimmerTable = new TableView<>();
        zimmerTable.setStyle("-fx-background-color: #161b22;");
        zimmerTable.setPrefHeight(140);
        zimmerTable.getColumns().addAll(
                col("Zimmertyp",    d -> d[0]),
                col("Hauptsaison", d -> d[1]),
                col("Nebensaison", d -> d[2])
        );
        // Laut Lastenheft: Vollpension = Halbpension + 20 €
        zimmerTable.getItems().addAll(
                new String[]{"Einzelzimmer (Halbpension / Vollpension)", "100 € / 120 €", "70 € / 90 €"},
                new String[]{"Doppelzimmer (Halbpension / Vollpension)", "70 € / 90 €",   "50 € / 70 €"},
                new String[]{"Suite (Halbpension / Vollpension)",        "150 € / 170 €", "90 € / 110 €"}
        );

        // Notbett
        Label notbettNote = new Label("Notbett: +10 €/Nacht (max. 1 pro Zimmer)");
        notbettNote.setStyle("-fx-text-fill: rgba(255,255,255,0.4); -fx-font-size: 12px;");

        // Zusatzleistungen
        Label zusatzTitle = new Label("Zusatzleistungen");
        zusatzTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #c9a84c;");

        TableView<String[]> zusatzTable = new TableView<>();
        zusatzTable.setStyle("-fx-background-color: #161b22;");
        zusatzTable.setPrefHeight(170);
        zusatzTable.getColumns().addAll(
                col("Leistung",        d -> d[0]),
                col("Öffnungszeiten",  d -> d[1]),
                col("Preis",           d -> d[2]),
                col("Freie Plätze",    d -> d[3])
        );
        zusatzTable.getItems().addAll(
                new String[]{"Restaurant", "12:00–14:00 / 18:00–22:00", "25 €/Person und Mahlzeit", "38"},
                new String[]{"Sauna",      "15:00–22:00",               "3 €/Stunde",               "7"},
                new String[]{"Spielhalle", "07:00–22:00",               "2 €/Stunde",               "10"},
                new String[]{"Schwimmbad", "07:00–22:00",               "1 €/Stunde",               "22"}
        );

        content.getChildren().addAll(note, zimmerTitle, zimmerTable, notbettNote, zusatzTitle, zusatzTable);
        setContent(content);
    }

    // ── Dialoge ───────────────────────────────────────────────────────────

    private void showRoomBookingDialog(String roomName) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(roomName + " buchen");
        dialog.getDialogPane().setStyle("-fx-background-color: #161b22;");

        VBox form = new VBox(14);
        form.setPadding(new Insets(20));
        form.setPrefWidth(380);

        Label intro = new Label("Zimmer: " + roomName);
        intro.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 14px; -fx-font-weight: bold;");

        // Pension
        Label pensionLbl = new Label("Verpflegung");
        pensionLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        ComboBox<String> pensionCombo = new ComboBox<>();
        pensionCombo.getItems().addAll("Halbpension", "Vollpension");
        pensionCombo.setPromptText("Wählen...");
        pensionCombo.setMaxWidth(Double.MAX_VALUE);
        styleCombo(pensionCombo);

        // Anreise / Abreise
        Label anrLbl = new Label("Anreise");
        anrLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        DatePicker anreise = new DatePicker();
        anreise.setMaxWidth(Double.MAX_VALUE);

        Label abrLbl = new Label("Abreise");
        abrLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        DatePicker abreise = new DatePicker();
        abreise.setMaxWidth(Double.MAX_VALUE);

        // Notbett
        Label notbettLbl = new Label("Notbett (+10 €/Nacht)");
        notbettLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        CheckBox notbett = new CheckBox();
        notbett.setStyle("-fx-text-fill: white;");

        // Zahlungsart
        Label zahlungLbl = new Label("Zahlungsart");
        zahlungLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        ComboBox<String> zahlungCombo = new ComboBox<>();
        zahlungCombo.getItems().addAll("Bar", "Karte", "Überweisung");
        zahlungCombo.setPromptText("Wählen...");
        zahlungCombo.setMaxWidth(Double.MAX_VALUE);
        styleCombo(zahlungCombo);

        form.getChildren().addAll(intro, pensionLbl, pensionCombo,
                anrLbl, anreise, abrLbl, abreise,
                notbettLbl, notbett, zahlungLbl, zahlungCombo);

        dialog.getDialogPane().setContent(form);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait();
    }

    private void showServiceBookingDialog(String serviceName) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(serviceName + " buchen");
        dialog.getDialogPane().setStyle("-fx-background-color: #161b22;");

        VBox form = new VBox(14);
        form.setPadding(new Insets(20));
        form.setPrefWidth(340);

        Label intro = new Label("Buchung für: " + serviceName);
        intro.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 14px; -fx-font-weight: bold;");

        // Hinweis: sofortige Bezahlung wenn kein Zimmer gebucht
        Label hinweis = new Label(
                "ℹ️  Falls Sie kein Zimmer gebucht haben, ist sofortige Bezahlung erforderlich.");
        hinweis.setStyle("-fx-text-fill: rgba(255,200,50,0.8); -fx-font-size: 11px;");
        hinweis.setWrapText(true);

        Label dateLbl = new Label("Datum");
        dateLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        DatePicker datePicker = new DatePicker();
        datePicker.setMaxWidth(Double.MAX_VALUE);

        Label vonLbl = new Label("Von (Uhrzeit)");
        vonLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        TextField vonField = new TextField();
        vonField.setPromptText("z. B. 15:00");
        styleTextField(vonField);

        Label bisLbl = new Label("Bis (Uhrzeit)");
        bisLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        TextField bisField = new TextField();
        bisField.setPromptText("z. B. 17:00");
        styleTextField(bisField);

        // Zahlungsart (laut Lastenheft: Bar, Karte, Überweisung)
        Label zahlungLbl = new Label("Zahlungsart");
        zahlungLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        ComboBox<String> zahlung = new ComboBox<>();
        zahlung.getItems().addAll("Bar", "Karte", "Überweisung");
        zahlung.setPromptText("Wählen...");
        zahlung.setMaxWidth(Double.MAX_VALUE);
        styleCombo(zahlung);

        form.getChildren().addAll(intro, hinweis,
                dateLbl, datePicker, vonLbl, vonField, bisLbl, bisField,
                zahlungLbl, zahlung);
        dialog.getDialogPane().setContent(form);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait();
    }

    // ── Hilfsmethoden ─────────────────────────────────────────────────────

    private VBox serviceCard(String icon, String name, String details, String availability) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(18));
        card.setPrefWidth(340);
        card.setStyle("-fx-background-color: #161b22; -fx-background-radius: 8; " +
                "-fx-border-color: #30363d; -fx-border-radius: 8;");

        Label ico = new Label(icon);
        ico.setStyle("-fx-font-size: 22px;");
        Label nameLbl = new Label(name);
        nameLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label detailLbl = new Label(details);
        detailLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(255,255,255,0.55);");
        detailLbl.setWrapText(true);
        Label availLbl = new Label(availability);
        availLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: #2ecc71;");

        card.getChildren().addAll(ico, nameLbl, detailLbl, availLbl);
        return card;
    }

    private TableColumn<String[], String> col(String title,
                                              java.util.function.Function<String[], String> getter) {
        TableColumn<String[], String> c = new TableColumn<>(title);
        c.setCellValueFactory(d -> new SimpleStringProperty(getter.apply(d.getValue())));
        return c;
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