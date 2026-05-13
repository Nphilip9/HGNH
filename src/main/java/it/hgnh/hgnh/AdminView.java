package it.hgnh.hgnh;

import it.hgnh.hgnh.models.Administrator;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminView extends Application {

    private final Administrator admin;
    private BorderPane root;

    /**
     * Konstruktor für den normalen Anwendungsfall (Login → AdminView).
     * @param admin Administrator
     */
    public AdminView(Administrator admin) {
        this.admin = admin;
    }

    /**
     * Parameterloser Konstruktor für JavaFX launch() (wird nur bei
     * direktem Start als Application benötigt).
     */
    public AdminView() {
        this.admin = new Administrator("Hotel", "Administrator");
    }

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #0d1117;");

        buildSidebar(stage);
        buildTopBar();
        showDashboard();

        Scene scene = new Scene(root, 1200, 750);
        stage.setScene(scene);
        stage.setTitle("Hotel Larcher – Administrator");
        stage.show();
    }

    /**
     * Builds the sidebar with navigation buttons.
     * @param stage Stage: Only used to close the stage after successful logout
     */
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
        Label roleLabel = new Label("Administrator");
        roleLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.4);");
        logoBox.getChildren().addAll(logo, roleLabel);

        VBox navItems = new VBox(2);
        navItems.setPadding(new Insets(16, 8, 16, 8));

        String[]   labels  = {"Dashboard", "Rezeptionisten", "Buchungen", "Zimmer & Leistungen", "Berichte"};
        String[]   icons   = {"⊞", "👤", "📋", "🏨", "📊"};
        Runnable[] actions = {
                this::showDashboard,
                this::showReceptionists,
                this::showBookings,
                this::showServices,
                this::showReports
        };

        for (int i = 0; i < labels.length; i++) {
            final int idx = i;
            Button btn = createNavBtn(icons[i], labels[i]);
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
        logoutBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #e74c3c;" +
                        "-fx-font-size: 13px; -fx-padding: 10 16;" +
                        "-fx-alignment: center-left; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();
            stage.close();
        });
        bottomBox.getChildren().add(logoutBtn);

        sidebar.getChildren().addAll(logoBox, navItems, spacer, bottomBox);
        root.setLeft(sidebar);
    }

    /**
     * Builds the top bar of the admin view.
     */
    private void buildTopBar() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(16, 28, 16, 28));
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setStyle("-fx-background-color: #161b22; " +
                "-fx-border-color: #30363d; -fx-border-width: 0 0 1 0;");

        String name = admin.getFirstName() + " " + admin.getLastName();
        Label userInfo = new Label(name + "  |  Administrator  |  Hotel Larcher GmbH");
        userInfo.setStyle("-fx-text-fill: rgba(255,255,255,0.45); -fx-font-size: 12px;");
        topBar.getChildren().add(userInfo);
        root.setTop(topBar);
    }

    /**
     * Dashboard: Zimmer, Aktive Buchungen, Rezeptionisten, Einnahmen heute
     */
    private void showDashboard() {
        VBox content = baseContent("Dashboard");

        HBox cards = new HBox(16);
        cards.getChildren().addAll(
                statCard("Zimmer gesamt",    "30",       "#c9a84c"),
                statCard("Aktive Buchungen", "12",       "#2ecc71"),
                statCard("Rezeptionisten",   "4",        "#3498db"),
                statCard("Einnahmen heute",  "1.240 €",  "#9b59b6")
        );

        Label recentTitle = new Label("Letzte Buchungen");
        recentTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        TableView<String[]> table = buildBookingTable();
        content.getChildren().addAll(cards, recentTitle, table);
        setContent(content);
    }

    /**
     * Shows the list of receptionists on the GUI.
     */
    private void showReceptionists() {
        VBox content = baseContent("Rezeptionisten verwalten");

        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        Button addBtn    = goldBtn("+ Neuer Rezeptionist");
        Button editBtn   = grayBtn("Bearbeiten");
        Button deleteBtn = redBtn("Entlassen");
        toolbar.getChildren().addAll(addBtn, editBtn, deleteBtn);

        TableView<String[]> table = new TableView<>();
        table.setStyle("-fx-background-color: #161b22;");

        TableColumn<String[], String> nameCol     = col("Name",              d -> d[0]);
        TableColumn<String[], String> usernameCol = col("Benutzername",      d -> d[1]);
        TableColumn<String[], String> guestsCol   = col("Zugeteilte Gäste",  d -> d[2]);
        TableColumn<String[], String> statusCol   = col("Status",            d -> d[3]);
        table.getColumns().addAll(nameCol, usernameCol, guestsCol, statusCol);
        table.getItems().addAll(
                new String[]{"Klaus Huber",    "k.huber",    "3", "Aktiv"},
                new String[]{"Maria Gruber",   "m.gruber",   "2", "Aktiv"},
                new String[]{"Thomas Berger",  "t.berger",   "4", "Aktiv"},
                new String[]{"Sandra Müller",  "s.mueller",  "1", "Urlaub"}
        );
        VBox.setVgrow(table, Priority.ALWAYS);

        // Laut Lastenheft: Admin kann Rezeptionisten einstellen und entlassen
        addBtn.setOnAction(e    -> showReceptionistDialog(null));
        editBtn.setOnAction(e   -> {
            String[] selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showReceptionistDialog(selected);
        });
        deleteBtn.setOnAction(e -> {
            String[] selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Rezeptionist entlassen");
            confirm.setHeaderText("Rezeptionist wirklich entlassen?");
            confirm.setContentText(selected[0] + " wird aus dem System entfernt.");
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) table.getItems().remove(selected);
            });
        });

        content.getChildren().addAll(toolbar, table);
        setContent(content);
    }

    /**
     * Shows the list of guests on the GUI.
     */
    private void showBookings() {
        VBox content = baseContent("Alle Buchungen");

        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        TextField search = new TextField();
        search.setPromptText("Gast oder Zimmer suchen...");
        search.setPrefWidth(260);
        styleTextField(search);
        Button editBtn   = grayBtn("Bearbeiten");
        Button deleteBtn = redBtn("Löschen");
        toolbar.getChildren().addAll(search, editBtn, deleteBtn);

        // Admin sieht ALLE Buchungen (laut Lastenheft)
        TableView<String[]> table = buildBookingTable();
        table.getItems().addAll(
                new String[]{"#1039", "Sandra Müller",  "Einzelzimmer 03", "10.04 – 15.04", "Bezahlt",   "500 €"},
                new String[]{"#1038", "Thomas Berger",  "Suite 01",        "01.04 – 10.04", "Bezahlt",   "900 €"}
        );

        // Laut Lastenheft: Admin darf KEINE eigene Buchung vornehmen,
        // aber alle anderen bearbeiten/löschen
        editBtn.setOnAction(e -> {
            String[] sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) showBookingEditDialog(sel);
        });
        deleteBtn.setOnAction(e -> {
            String[] sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Buchung löschen");
            confirm.setHeaderText("Buchung " + sel[0] + " wirklich löschen?");
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) table.getItems().remove(sel);
            });
        });

        content.getChildren().addAll(toolbar, table);
        setContent(content);
    }

    /**
     * Shows the list of services offered by the hotel.
     */
    private void showServices() {
        VBox content = baseContent("Zimmer & Leistungen");

        Label zimmerLabel = new Label("Zimmerpreise");
        zimmerLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #c9a84c;");

        GridPane grid = new GridPane();
        grid.setHgap(40);
        grid.setVgap(10);
        grid.setPadding(new Insets(8, 0, 20, 0));

        // Laut Lastenheft: Hauptsaison Juni–August, Nebensaison Mai+September
        String[][] zimmer = {
                {"Einzelzimmer (Halbpension)", "5",  "100 €/Nacht", "70 €/Nacht"},
                {"Doppelzimmer (Halbpension)", "20", "70 €/Nacht",  "50 €/Nacht"},
                {"Suite (Halbpension)",        "5",  "150 €/Nacht", "90 €/Nacht"}
        };
        String[] headers = {"Zimmertyp", "Anzahl", "Hauptsaison", "Nebensaison"};
        for (int c = 0; c < headers.length; c++) {
            Label h = new Label(headers[c]);
            h.setStyle("-fx-text-fill: rgba(255,255,255,0.45); -fx-font-size: 11px;");
            grid.add(h, c, 0);
        }
        for (int r = 0; r < zimmer.length; r++) {
            for (int c = 0; c < zimmer[r].length; c++) {
                Label l = new Label(zimmer[r][c]);
                l.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");
                grid.add(l, c, r + 1);
            }
        }

        Label zusatzLabel = new Label("Zusatzleistungen");
        zusatzLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #c9a84c;");

        TableView<String[]> zusatzTable = new TableView<>();
        zusatzTable.setStyle("-fx-background-color: #161b22;");
        zusatzTable.getColumns().addAll(
                col("Leistung",         d -> d[0]),
                col("Öffnungszeiten",   d -> d[1]),
                col("Preis",            d -> d[2]),
                col("Plätze gesamt",    d -> d[3])
        );
        zusatzTable.getItems().addAll(
                new String[]{"Restaurant",  "12:00–14:00 / 18:00–22:00", "25 €/Person",  "50"},
                new String[]{"Sauna",       "15:00–22:00",               "3 €/Stunde",   "10"},
                new String[]{"Spielhalle",  "07:00–22:00",               "2 €/Stunde",   "10"},
                new String[]{"Schwimmbad",  "07:00–22:00",               "1 €/Stunde",   "30"}
        );
        VBox.setVgrow(zusatzTable, Priority.ALWAYS);

        content.getChildren().addAll(zimmerLabel, grid, zusatzLabel, zusatzTable);
        setContent(content);
    }

    /**
     * Shows the list of guests on the GUI.
     */
    private void showReports() {
        VBox content = baseContent("Berichte");

        Label label = new Label("Monatsübersicht – April 2026");
        label.setStyle("-fx-font-size: 15px; -fx-text-fill: rgba(255,255,255,0.6);");

        HBox cards = new HBox(16);
        cards.getChildren().addAll(
                statCard("Gesamtumsatz",     "18.420 €", "#2ecc71"),
                statCard("Buchungen gesamt", "48",       "#c9a84c"),
                statCard("Stornierungen",    "3",        "#e74c3c"),
                statCard("Auslastung",       "78 %",     "#3498db")
        );
        content.getChildren().addAll(label, cards);
        setContent(content);
    }

    /**
     * Shows the list of guests on the GUI.
     * @param data String[]: Receptionists used for displaying selected Receptionist, if null you can create a new one.
     */
    private void showReceptionistDialog(String[] data) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(data == null ? "Neuer Rezeptionist" : "Rezeptionist bearbeiten");
        dialog.getDialogPane().setStyle("-fx-background-color: #161b22;");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(14);
        form.setPadding(new Insets(20));

        String[] fieldLabels = {"Vorname", "Nachname", "Benutzername", "Passwort"};
        for (int i = 0; i < fieldLabels.length; i++) {
            Label l = new Label(fieldLabels[i]);
            l.setStyle("-fx-text-fill: rgba(255,255,255,0.6); -fx-font-size: 12px;");
            TextField tf = new TextField(data != null && i < data.length ? data[i] : "");
            styleTextField(tf);
            form.add(l, 0, i);
            form.add(tf, 1, i);
        }

        dialog.getDialogPane().setContent(form);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait();
    }

    /**
     * Remove or edit a booking.
     * @param data String[]: Data used for displaying selected booking
     */
    private void showBookingEditDialog(String[] data) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Buchung bearbeiten – " + data[0]);
        dialog.getDialogPane().setStyle("-fx-background-color: #161b22;");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(14);
        form.setPadding(new Insets(20));

        String[] fieldLabels = {"Buchungs-ID", "Gast", "Zimmer", "Zeitraum", "Status", "Betrag"};
        for (int i = 0; i < fieldLabels.length; i++) {
            Label l = new Label(fieldLabels[i]);
            l.setStyle("-fx-text-fill: rgba(255,255,255,0.6); -fx-font-size: 12px;");
            TextField tf = new TextField(i < data.length ? data[i] : "");
            styleTextField(tf);
            if (i == 0) tf.setEditable(false); // ID nicht änderbar
            form.add(l, 0, i);
            form.add(tf, 1, i);
        }

        dialog.getDialogPane().setContent(form);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait();
    }

    /**
     * Builds the booking table.
     * @return TableView<String[]>: Table with all bookings.
     */
    private TableView<String[]> buildBookingTable() {
        TableView<String[]> table = new TableView<>();
        table.setStyle("-fx-background-color: #161b22;");
        table.getColumns().addAll(
                col("ID",       d -> d[0]),
                col("Gast",     d -> d[1]),
                col("Zimmer",   d -> d[2]),
                col("Zeitraum", d -> d[3]),
                col("Status",   d -> d[4]),
                col("Betrag",   d -> d[5])
        );
        table.getItems().addAll(
                new String[]{"#1042", "Hans Meier",      "Doppelzimmer 12",  "20.04 – 25.04", "Aktiv",   "350 €"},
                new String[]{"#1041", "Petra Schneider", "Suite 02",         "18.04 – 22.04", "Aktiv",   "600 €"},
                new String[]{"#1040", "Georg Bauer",     "Einzelzimmer 07",  "15.04 – 20.04", "Bezahlt", "500 €"}
        );
        VBox.setVgrow(table, Priority.ALWAYS);
        return table;
    }

    /** Typsicherer Convenience-Helfer für TableColumn */
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

    private VBox statCard(String title, String value, String color) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setPrefWidth(180);
        card.setStyle("-fx-background-color: #161b22; -fx-background-radius: 8; " +
                "-fx-border-color: #30363d; -fx-border-radius: 8;");
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        Label lbl = new Label(title);
        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(255,255,255,0.45);");
        card.getChildren().addAll(val, lbl);
        return card;
    }

    private Button createNavBtn(String icon, String label) {
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
        tf.setStyle(
                "-fx-background-color: rgba(255,255,255,0.05); -fx-border-color: #30363d;" +
                        "-fx-border-radius: 6; -fx-background-radius: 6;" +
                        "-fx-text-fill: white; -fx-prompt-text-fill: rgba(255,255,255,0.25); -fx-padding: 8 12;");
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