package it.hgnh.hgnh;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class UserView extends Application {

    private BorderPane root;

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #0d1117;");

        buildSidebar();
        buildTopBar();
        showMyBookings();

        Scene scene = new Scene(root, 1100, 720);
        stage.setScene(scene);
        stage.setTitle("Hotel Larcher – Gast");
        stage.show();
    }

    private void buildSidebar() {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #161b22; -fx-border-color: #30363d; -fx-border-width: 0 1 0 0;");

        VBox logoBox = new VBox(4);
        logoBox.setPadding(new Insets(28, 24, 28, 24));
        logoBox.setStyle("-fx-border-color: #30363d; -fx-border-width: 0 0 1 0;");
        Label logo = new Label("LARCHER");
        logo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #c9a84c; -fx-font-family: 'Georgia';");
        Label role = new Label("Gast");
        role.setStyle("-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.4);");
        logoBox.getChildren().addAll(logo, role);

        VBox navItems = new VBox(2);
        navItems.setPadding(new Insets(16, 8, 16, 8));

        String[] labels = {"Meine Buchungen", "Leistungen buchen", "Leistungsübersicht"};
        String[] icons  = {"📋", "➕", "🏨"};
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
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; -fx-font-size: 13px; -fx-padding: 10 16; -fx-alignment: center-left; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> {
            new LoginView().start(new Stage());
            ((Stage) logoutBtn.getScene().getWindow()).close();
        });
        bottomBox.getChildren().add(logoutBtn);

        sidebar.getChildren().addAll(logoBox, navItems, spacer, bottomBox);
        root.setLeft(sidebar);
    }

    private void buildTopBar() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(16, 28, 16, 28));
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setStyle("-fx-background-color: #161b22; -fx-border-color: #30363d; -fx-border-width: 0 0 1 0;");
        Label info = new Label("Hans Meier – Gast  |  Hotel Larcher GmbH");
        info.setStyle("-fx-text-fill: rgba(255,255,255,0.45); -fx-font-size: 12px;");
        topBar.getChildren().add(info);
        root.setTop(topBar);
    }

    private void showMyBookings() {
        VBox content = baseContent("Meine Buchungen");

        HBox infoCard = new HBox(16);
        infoCard.setAlignment(Pos.CENTER_LEFT);
        infoCard.setPadding(new Insets(16, 20, 16, 20));
        infoCard.setStyle("-fx-background-color: rgba(201,168,76,0.08); -fx-background-radius: 8; -fx-border-color: rgba(201,168,76,0.3); -fx-border-radius: 8;");
        Label infoText = new Label("🏨  Aktueller Aufenthalt: Doppelzimmer 12  |  20.04 – 25.04.2026  |  Halbpension");
        infoText.setStyle("-fx-text-fill: #c9a84c; -fx-font-size: 13px;");
        infoCard.getChildren().add(infoText);

        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        Button cancelBtn = redBtn("Buchung stornieren");
        Label hinweis = new Label("Stornierungsgebühr: 10 % des Buchungsbetrags");
        hinweis.setStyle("-fx-text-fill: rgba(255,255,255,0.35); -fx-font-size: 11px;");
        toolbar.getChildren().addAll(cancelBtn, hinweis);

        TableView<String[]> table = new TableView<>();
        table.setStyle("-fx-background-color: #161b22;");

        TableColumn<String[], String> idCol = new TableColumn<>("Buchungs-ID");
        idCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        TableColumn<String[], String> leistungCol = new TableColumn<>("Leistung");
        leistungCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        TableColumn<String[], String> zeitCol = new TableColumn<>("Datum / Zeitraum");
        zeitCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));
        TableColumn<String[], String> betragCol = new TableColumn<>("Betrag");
        betragCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[3]));
        TableColumn<String[], String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[4]));

        table.getColumns().addAll(idCol, leistungCol, zeitCol, betragCol, statusCol);
        table.getItems().addAll(
                new String[]{"#1042", "Doppelzimmer 12 (Halbpension)", "20.04 – 25.04.2026", "350 €", "Aktiv"},
                new String[]{"#1043", "Sauna", "21.04.2026", "9 €", "Bezahlt"},
                new String[]{"#1044", "Restaurant (Abendessen)", "21.04.2026", "25 €", "Aktiv"}
        );
        VBox.setVgrow(table, Priority.ALWAYS);

        cancelBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Buchung stornieren");
            confirm.setHeaderText("Buchung wirklich stornieren?");
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

        content.getChildren().addAll(infoCard, toolbar, table);
        setContent(content);
    }

    private void showBookServices() {
        VBox content = baseContent("Leistung buchen");

        Label subtitle = new Label("Wählen Sie eine Zusatzleistung, die Sie buchen möchten.");
        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.45); -fx-font-size: 13px;");

        GridPane cards = new GridPane();
        cards.setHgap(16);
        cards.setVgap(16);
        cards.setPadding(new Insets(8, 0, 0, 0));

        String[][] services = {
                {"🍽️", "Restaurant", "25 €/Person", "12:00–14:00 / 18:00–22:00", "38 Plätze frei"},
                {"🧖", "Sauna", "3 €/Std.", "15:00–22:00", "7 Plätze frei"},
                {"🎮", "Spielhalle", "2 €/Std.", "07:00–22:00", "10 Plätze frei"},
                {"🏊", "Schwimmbad", "1 €/Std.", "07:00–22:00", "22 Plätze frei"}
        };

        for (int i = 0; i < services.length; i++) {
            String[] s = services[i];
            VBox card = new VBox(10);
            card.setPrefWidth(220);
            card.setPadding(new Insets(20));
            card.setStyle("-fx-background-color: #161b22; -fx-background-radius: 10; -fx-border-color: #30363d; -fx-border-radius: 10;");

            Label icon = new Label(s[0]);
            icon.setStyle("-fx-font-size: 28px;");
            Label name = new Label(s[1]);
            name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
            Label preis = new Label(s[2]);
            preis.setStyle("-fx-font-size: 14px; -fx-text-fill: #c9a84c; -fx-font-weight: bold;");
            Label oeff = new Label(s[3]);
            oeff.setStyle("-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.4);");
            Label plaetze = new Label(s[4]);
            plaetze.setStyle("-fx-font-size: 11px; -fx-text-fill: #2ecc71;");
            Button bookBtn = goldBtn("Buchen");
            bookBtn.setMaxWidth(Double.MAX_VALUE);

            final String serviceName = s[1];
            bookBtn.setOnAction(e -> showServiceBookingDialog(serviceName));

            card.getChildren().addAll(icon, name, preis, oeff, plaetze, bookBtn);
            cards.add(card, i % 2, i / 2);
        }

        content.getChildren().addAll(subtitle, cards);
        setContent(content);
    }

    private void showServicesOverview() {
        VBox content = baseContent("Leistungsübersicht");

        Label note = new Label("Alle Preise verstehen sich pro Person. Zusatzleistungen ohne Zimmer sind sofort zu bezahlen.");
        note.setStyle("-fx-text-fill: rgba(255,255,255,0.4); -fx-font-size: 12px;");
        note.setWrapText(true);

        Label zimmerTitle = new Label("Zimmerpreise");
        zimmerTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #c9a84c;");

        TableView<String[]> zimmerTable = new TableView<>();
        zimmerTable.setStyle("-fx-background-color: #161b22;");
        zimmerTable.setPrefHeight(140);

        TableColumn<String[], String> typCol = new TableColumn<>("Zimmertyp");
        typCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        TableColumn<String[], String> hauptCol = new TableColumn<>("Hauptsaison");
        hauptCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        TableColumn<String[], String> nebenCol = new TableColumn<>("Nebensaison");
        nebenCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));
        zimmerTable.getColumns().addAll(typCol, hauptCol, nebenCol);
        zimmerTable.getItems().addAll(
                new String[]{"Einzelzimmer (Halbpension)", "100 €/Nacht", "70 €/Nacht"},
                new String[]{"Doppelzimmer (Halbpension)", "70 €/Nacht", "50 €/Nacht"},
                new String[]{"Suite (Halbpension)", "150 €/Nacht", "90 €/Nacht"}
        );

        Label zusatzTitle = new Label("Zusatzleistungen");
        zusatzTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #c9a84c;");

        TableView<String[]> zusatzTable = new TableView<>();
        zusatzTable.setStyle("-fx-background-color: #161b22;");
        zusatzTable.setPrefHeight(170);

        TableColumn<String[], String> nameCol = new TableColumn<>("Leistung");
        nameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        TableColumn<String[], String> oeffCol = new TableColumn<>("Öffnungszeiten");
        oeffCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        TableColumn<String[], String> preisCol = new TableColumn<>("Preis");
        preisCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));
        zusatzTable.getColumns().addAll(nameCol, oeffCol, preisCol);
        zusatzTable.getItems().addAll(
                new String[]{"Restaurant", "12:00–14:00 / 18:00–22:00", "25 €/Person und Mahlzeit"},
                new String[]{"Sauna", "15:00–22:00", "3 €/Stunde"},
                new String[]{"Spielhalle", "07:00–22:00", "2 €/Stunde"},
                new String[]{"Schwimmbad", "07:00–22:00", "1 €/Stunde"}
        );

        content.getChildren().addAll(note, zimmerTitle, zimmerTable, zusatzTitle, zusatzTable);
        setContent(content);
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

        Label zahlungLbl = new Label("Zahlungsart");
        zahlungLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        ComboBox<String> zahlung = new ComboBox<>();
        zahlung.getItems().addAll("Bar", "Karte", "Überweisung");
        zahlung.setPromptText("Wählen...");
        zahlung.setMaxWidth(Double.MAX_VALUE);
        styleCombo(zahlung);

        form.getChildren().addAll(intro, dateLbl, datePicker, vonLbl, vonField, bisLbl, bisField, zahlungLbl, zahlung);
        dialog.getDialogPane().setContent(form);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait();
    }

    private Button navBtn(String icon, String label) {
        Button btn = new Button(icon + "  " + label);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle(navBtnStyle(false));
        return btn;
    }

    private String navBtnStyle(boolean active) {
        if (active) {
            return "-fx-background-color: rgba(201,168,76,0.12); -fx-text-fill: #c9a84c; -fx-font-size: 13px; -fx-padding: 10 16; -fx-alignment: center-left; -fx-background-radius: 6; -fx-border-color: #c9a84c; -fx-border-width: 0 0 0 3; -fx-cursor: hand;";
        }
        return "-fx-background-color: transparent; -fx-text-fill: rgba(255,255,255,0.65); -fx-font-size: 13px; -fx-padding: 10 16; -fx-alignment: center-left; -fx-background-radius: 6; -fx-cursor: hand;";
    }

    private VBox baseContent(String title) {
        VBox box = new VBox(20);
        box.setPadding(new Insets(32, 36, 32, 36));
        box.setStyle("-fx-background-color: #0d1117;");
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: 'Georgia';");
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #30363d;");
        box.getChildren().addAll(titleLabel, sep);
        return box;
    }

    private Button goldBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: #c9a84c; -fx-text-fill: #0d1117; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6; -fx-cursor: hand;");
        return b;
    }

    private Button redBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6; -fx-cursor: hand;");
        return b;
    }

    private void styleTextField(TextField tf) {
        tf.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-border-color: #30363d; -fx-border-radius: 6; -fx-background-radius: 6; -fx-text-fill: white; -fx-prompt-text-fill: rgba(255,255,255,0.25); -fx-padding: 8 12;");
    }

    private void styleCombo(ComboBox<?> cb) {
        cb.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-border-color: #30363d; -fx-border-radius: 6; -fx-background-radius: 6; -fx-text-fill: white;");
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