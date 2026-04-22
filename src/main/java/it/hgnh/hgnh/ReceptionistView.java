package it.hgnh.hgnh;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ReceptionistView extends Application {

    private BorderPane root;

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #0d1117;");

        buildSidebar();
        buildTopBar();
        showGuestOverview();

        Scene scene = new Scene(root, 1200, 750);
        stage.setScene(scene);
        stage.setTitle("Hotel Larcher – Rezeptionist");
        stage.show();
    }

    private void buildSidebar() {
        VBox sidebar = new VBox(0);
        sidebar.setPrefWidth(230);
        sidebar.setStyle("-fx-background-color: #161b22; -fx-border-color: #30363d; -fx-border-width: 0 1 0 0;");

        VBox logoBox = new VBox(4);
        logoBox.setPadding(new Insets(28, 24, 28, 24));
        logoBox.setStyle("-fx-border-color: #30363d; -fx-border-width: 0 0 1 0;");
        Label logo = new Label("LARCHER");
        logo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #c9a84c; -fx-font-family: 'Georgia';");
        Label role = new Label("Rezeptionist");
        role.setStyle("-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.4);");
        logoBox.getChildren().addAll(logo, role);

        VBox navItems = new VBox(2);
        navItems.setPadding(new Insets(16, 8, 16, 8));

        String[] labels = {"Meine Gäste", "Buchungen", "Neue Buchung", "Leistungsübersicht"};
        String[] icons  = {"👥", "📋", "➕", "🏨"};
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
        Label info = new Label("Klaus Huber – Rezeptionist  |  Hotel Larcher GmbH");
        info.setStyle("-fx-text-fill: rgba(255,255,255,0.45); -fx-font-size: 12px;");
        topBar.getChildren().add(info);
        root.setTop(topBar);
    }

    private void showGuestOverview() {
        VBox content = baseContent("Meine Gäste");

        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        Button addGuestBtn = goldBtn("+ Gast anlegen");
        Button bookForGuestBtn = grayBtn("Buchung erstellen");
        Button editBtn = grayBtn("Bearbeiten");
        Button deleteBookingBtn = redBtn("Buchung stornieren");
        toolbar.getChildren().addAll(addGuestBtn, bookForGuestBtn, editBtn, deleteBookingBtn);

        TableView<String[]> table = new TableView<>();
        table.setStyle("-fx-background-color: #161b22;");

        TableColumn<String[], String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        TableColumn<String[], String> auswCol = new TableColumn<>("Ausweisnr.");
        auswCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        TableColumn<String[], String> zimmerCol = new TableColumn<>("Zimmer");
        zimmerCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));
        TableColumn<String[], String> zeitCol = new TableColumn<>("Aufenthalt");
        zeitCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[3]));
        TableColumn<String[], String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[4]));

        table.getColumns().addAll(nameCol, auswCol, zimmerCol, zeitCol, statusCol);
        table.getItems().addAll(
                new String[]{"Hans Meier", "AT1234567", "Doppelzimmer 12", "20.04 – 25.04.2026", "Eingecheckt"},
                new String[]{"Petra Schneider", "DE9876543", "Suite 02", "18.04 – 22.04.2026", "Eingecheckt"},
                new String[]{"Lukas Weiß", "IT5551234", "Einzelzimmer 07", "23.04 – 26.04.2026", "Erwartet"}
        );
        VBox.setVgrow(table, Priority.ALWAYS);

        addGuestBtn.setOnAction(e -> showGuestDialog());
        bookForGuestBtn.setOnAction(e -> showNewBooking());

        content.getChildren().addAll(toolbar, table);
        setContent(content);
    }

    private void showBookings() {
        VBox content = baseContent("Buchungen meiner Gäste");

        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        TextField search = new TextField();
        search.setPromptText("Gast suchen...");
        search.setPrefWidth(250);
        styleTextField(search);
        Button editBtn = grayBtn("Bearbeiten");
        Button cancelBtn = redBtn("Stornieren");
        toolbar.getChildren().addAll(search, editBtn, cancelBtn);

        TableView<String[]> table = new TableView<>();
        table.setStyle("-fx-background-color: #161b22;");

        TableColumn<String[], String> idCol = new TableColumn<>("Buchungs-ID");
        idCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        TableColumn<String[], String> guestCol = new TableColumn<>("Gast");
        guestCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        TableColumn<String[], String> leistungCol = new TableColumn<>("Leistung");
        leistungCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));
        TableColumn<String[], String> zeitCol = new TableColumn<>("Zeitraum");
        zeitCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[3]));
        TableColumn<String[], String> betragCol = new TableColumn<>("Betrag");
        betragCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[4]));
        TableColumn<String[], String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[5]));

        table.getColumns().addAll(idCol, guestCol, leistungCol, zeitCol, betragCol, statusCol);
        table.getItems().addAll(
                new String[]{"#1042", "Hans Meier", "Doppelzimmer + Sauna", "20.04 – 25.04", "350 €", "Aktiv"},
                new String[]{"#1043", "Hans Meier", "Restaurant (Abendessen)", "21.04", "25 €", "Aktiv"},
                new String[]{"#1041", "Petra Schneider", "Suite", "18.04 – 22.04", "600 €", "Aktiv"}
        );
        VBox.setVgrow(table, Priority.ALWAYS);

        content.getChildren().addAll(toolbar, table);
        setContent(content);
    }

    private void showNewBooking() {
        VBox content = baseContent("Neue Buchung erstellen");

        GridPane form = new GridPane();
        form.setHgap(20);
        form.setVgap(16);
        form.setPadding(new Insets(8, 0, 24, 0));
        form.setMaxWidth(650);

        Label guestLbl = new Label("Gast auswählen");
        guestLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        ComboBox<String> guestCombo = new ComboBox<>();
        guestCombo.getItems().addAll("Hans Meier", "Petra Schneider", "Lukas Weiß");
        guestCombo.setPromptText("Gast...");
        guestCombo.setMaxWidth(Double.MAX_VALUE);
        styleCombo(guestCombo);

        Label zimmerLbl = new Label("Zimmertyp");
        zimmerLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        ComboBox<String> zimmerCombo = new ComboBox<>();
        zimmerCombo.getItems().addAll("Einzelzimmer", "Doppelzimmer", "Suite");
        zimmerCombo.setPromptText("Zimmer wählen...");
        zimmerCombo.setMaxWidth(Double.MAX_VALUE);
        styleCombo(zimmerCombo);

        Label pensionLbl = new Label("Verpflegung");
        pensionLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        ComboBox<String> pensionCombo = new ComboBox<>();
        pensionCombo.getItems().addAll("Halbpension", "Vollpension");
        pensionCombo.setPromptText("Wählen...");
        pensionCombo.setMaxWidth(Double.MAX_VALUE);
        styleCombo(pensionCombo);

        Label anrLbl = new Label("Anreise");
        anrLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        DatePicker anreise = new DatePicker();
        anreise.setMaxWidth(Double.MAX_VALUE);
        anreise.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-border-color: #30363d; -fx-border-radius: 6; -fx-background-radius: 6; -fx-text-fill: white;");

        Label abrLbl = new Label("Abreise");
        abrLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        DatePicker abreise = new DatePicker();
        abreise.setMaxWidth(Double.MAX_VALUE);
        abreise.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-border-color: #30363d; -fx-border-radius: 6; -fx-background-radius: 6; -fx-text-fill: white;");

        Label notbettLbl = new Label("Notbett (+10 €/Nacht)");
        notbettLbl.setStyle("-fx-text-fill: rgba(255,255,255,0.5); -fx-font-size: 11px;");
        CheckBox notbettCheck = new CheckBox();
        notbettCheck.setStyle("-fx-text-fill: white;");

        form.add(guestLbl, 0, 0); form.add(guestCombo, 1, 0);
        form.add(zimmerLbl, 0, 1); form.add(zimmerCombo, 1, 1);
        form.add(pensionLbl, 0, 2); form.add(pensionCombo, 1, 2);
        form.add(anrLbl, 0, 3); form.add(anreise, 1, 3);
        form.add(abrLbl, 0, 4); form.add(abreise, 1, 4);
        form.add(notbettLbl, 0, 5); form.add(notbettCheck, 1, 5);

        Label zusatzTitle = new Label("Zusatzleistungen");
        zusatzTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #c9a84c;");

        HBox zusatzBox = new HBox(12);
        zusatzBox.setPadding(new Insets(4, 0, 0, 0));
        for (String z : new String[]{"Restaurant", "Sauna", "Spielhalle", "Schwimmbad"}) {
            CheckBox cb = new CheckBox(z);
            cb.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");
            zusatzBox.getChildren().add(cb);
        }

        HBox btnRow = new HBox(12);
        Button submitBtn = goldBtn("Buchung speichern");
        Button cancelBtn = grayBtn("Abbrechen");
        btnRow.getChildren().addAll(submitBtn, cancelBtn);

        submitBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Buchung gespeichert");
            alert.setHeaderText(null);
            alert.setContentText("Die Buchung wurde erfolgreich gespeichert.");
            alert.showAndWait();
        });
        cancelBtn.setOnAction(e -> showGuestOverview());

        content.getChildren().addAll(form, zusatzTitle, zusatzBox, btnRow);
        setContent(content);
    }

    private void showServices() {
        VBox content = baseContent("Leistungsübersicht");

        TableView<String[]> table = new TableView<>();
        table.setStyle("-fx-background-color: #161b22;");
        TableColumn<String[], String> nameCol = new TableColumn<>("Leistung");
        nameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        TableColumn<String[], String> oeffCol = new TableColumn<>("Öffnungszeiten");
        oeffCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        TableColumn<String[], String> preisCol = new TableColumn<>("Preis");
        preisCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));
        TableColumn<String[], String> freiCol = new TableColumn<>("Freie Plätze");
        freiCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[3]));
        table.getColumns().addAll(nameCol, oeffCol, preisCol, freiCol);
        table.getItems().addAll(
                new String[]{"Restaurant", "12:00–14:00 / 18:00–22:00", "25 €/Person", "38"},
                new String[]{"Sauna", "15:00–22:00", "3 €/Std.", "7"},
                new String[]{"Spielhalle", "07:00–22:00", "2 €/Std.", "10"},
                new String[]{"Schwimmbad", "07:00–22:00", "1 €/Std.", "22"}
        );
        VBox.setVgrow(table, Priority.ALWAYS);

        content.getChildren().add(table);
        setContent(content);
    }

    private void showGuestDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Neuen Gast anlegen");
        dialog.getDialogPane().setStyle("-fx-background-color: #161b22;");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(14);
        form.setPadding(new Insets(20));

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

    private Button grayBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: #30363d; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6; -fx-cursor: hand;");
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