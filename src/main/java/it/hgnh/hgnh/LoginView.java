package it.hgnh.hgnh;

import it.hgnh.hgnh.models.Administrator;
import it.hgnh.hgnh.models.Receptionist;
import it.hgnh.hgnh.models.Guest;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;

/**
 * Anmelde-Bildschirm.
 * Nach erfolgreichem Login wird je nach gewählter Rolle
 * AdminView, ReceptionistView oder UserView geöffnet.
 *
 * Dummy-Zugangsdaten (bis echte DB-Anbindung existiert):
 *   Administrator  → Benutzer: admin      Passwort: admin123
 *   Rezeptionist   → Benutzer: rezeption  Passwort: rezept123
 *   Gast           → kein Passwort nötig, nur Vor- und Nachname
 */
public class LoginView extends Application {

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();

        // ── Hintergrund ───────────────────────────────────────────────────
        Rectangle bg = new Rectangle(900, 620);
        bg.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1a1a2e")),
                new Stop(1, Color.web("#16213e"))));
        root.getChildren().add(bg);

        HBox mainLayout = new HBox();
        mainLayout.setPrefSize(900, 620);

        // ── Linkes Panel – Hotel-Branding ─────────────────────────────────
        VBox leftPanel = new VBox(20);
        leftPanel.setPrefWidth(380);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPadding(new Insets(60));
        leftPanel.setStyle("-fx-background-color: rgba(255,255,255,0.03);");

        Label hotelTitle = new Label("LARCHER");
        hotelTitle.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; " +
                "-fx-text-fill: #c9a84c; -fx-font-family: 'Georgia';");

        Label hotelSub = new Label("GRAND HOTEL");
        hotelSub.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(255,255,255,0.5); " +
                "-fx-font-family: 'Georgia';");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #c9a84c; -fx-opacity: 0.4;");
        sep.setMaxWidth(120);

        Label tagline = new Label("Ihr Zuhause für besondere Momente");
        tagline.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(255,255,255,0.35); " +
                "-fx-font-style: italic;");
        tagline.setWrapText(true);
        tagline.setTextAlignment(TextAlignment.CENTER);

        leftPanel.getChildren().addAll(hotelTitle, hotelSub, sep, tagline);

        VBox rightPanel = new VBox(16);
        rightPanel.setPrefWidth(520);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(60, 70, 60, 70));
        rightPanel.setStyle("-fx-background-color: #0f0f23;");

        Label loginTitle = new Label("Anmeldung");
        loginTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; " +
                "-fx-text-fill: white; -fx-font-family: 'Georgia';");

        Label loginSub = new Label("Bitte melden Sie sich an, um fortzufahren");
        loginSub.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(255,255,255,0.4);");

        VBox roleBox = new VBox(6);
        Label roleLabel = fieldLabel("ROLLE");
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Gast", "Rezeptionist", "Administrator");
        roleCombo.setPromptText("Rolle auswählen...");
        roleCombo.setMaxWidth(Double.MAX_VALUE);
        styleCombo(roleCombo);
        roleBox.getChildren().addAll(roleLabel, roleCombo);

        VBox firstNameBox = new VBox(6);
        Label firstNameLabel = fieldLabel("VORNAME");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Vorname...");
        styleField(firstNameField);
        firstNameBox.getChildren().addAll(firstNameLabel, firstNameField);
        firstNameBox.setVisible(false);
        firstNameBox.setManaged(false);

        VBox lastNameBox = new VBox(6);
        Label lastNameLabel = fieldLabel("NACHNAME");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Nachname...");
        styleField(lastNameField);
        lastNameBox.getChildren().addAll(lastNameLabel, lastNameField);
        lastNameBox.setVisible(false);
        lastNameBox.setManaged(false);

        VBox usernameBox = new VBox(6);
        Label userLabel = fieldLabel("BENUTZERNAME");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Benutzername eingeben...");
        styleField(usernameField);
        usernameBox.getChildren().addAll(userLabel, usernameField);
        usernameBox.setVisible(false);
        usernameBox.setManaged(false);

        VBox passwordBox = new VBox(6);
        Label passLabel = fieldLabel("PASSWORT");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Passwort eingeben...");
        styleField(passwordField);
        passwordBox.getChildren().addAll(passLabel, passwordField);
        passwordBox.setVisible(false);
        passwordBox.setManaged(false);

        roleCombo.setOnAction(e -> {
            String selected = roleCombo.getValue();
            boolean isGuest = "Gast".equals(selected);
            boolean isStaff = "Rezeptionist".equals(selected) || "Administrator".equals(selected);

            firstNameBox.setVisible(isGuest);
            firstNameBox.setManaged(isGuest);
            lastNameBox.setVisible(isGuest);
            lastNameBox.setManaged(isGuest);
            usernameBox.setVisible(isStaff);
            usernameBox.setManaged(isStaff);
            passwordBox.setVisible(isStaff);
            passwordBox.setManaged(isStaff);
            stage.sizeToScene();
        });

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");
        errorLabel.setWrapText(true);

        Button loginBtn = new Button("ANMELDEN");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle(loginBtnStyle("#c9a84c"));
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle(loginBtnStyle("#e0ba5c")));
        loginBtn.setOnMouseExited(e  -> loginBtn.setStyle(loginBtnStyle("#c9a84c")));

        loginBtn.setOnAction(e -> {
            String role = roleCombo.getValue();
            if (role == null) {
                errorLabel.setText("Bitte eine Rolle auswählen.");
                return;
            }

            switch (role) {
                case "Administrator", "Rezeptionist" -> {
                    String user = usernameField.getText().trim();
                    String pass = passwordField.getText();
                    System.out.println("Rolle: " + role + " | User: " + user + " | Pass: " + pass);
                }
                case "Gast" -> {
                    String firstName = firstNameField.getText().trim();
                    String lastName  = lastNameField.getText().trim();
                    System.out.println("Rolle: Gast | Vorname: " + firstName + " | Nachname: " + lastName);
                }
            }
        });

        rightPanel.getChildren().addAll(
                loginTitle, loginSub,
                new Region() {{ setMinHeight(6); }},
                roleBox, firstNameBox, lastNameBox, usernameBox, passwordBox,
                errorLabel, loginBtn
        );

        mainLayout.getChildren().addAll(leftPanel, rightPanel);
        root.getChildren().add(mainLayout);

        Scene scene = new Scene(root, 900, 520);
        stage.setScene(scene);
        stage.setTitle("Hotel Larcher – Anmeldung");
        stage.setResizable(false);
        stage.show();
    }

    // ── Hilfsmethoden ─────────────────────────────────────────────────────

    private Label fieldLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 10px; -fx-text-fill: #c9a84c; -fx-font-weight: bold;");
        return l;
    }

    private void styleField(TextField tf) {
        tf.setStyle(
                "-fx-background-color: rgba(255,255,255,0.05);" +
                        "-fx-border-color: rgba(255,255,255,0.12);" +
                        "-fx-border-radius: 6; -fx-background-radius: 6;" +
                        "-fx-text-fill: white; -fx-prompt-text-fill: rgba(255,255,255,0.25);" +
                        "-fx-padding: 12 15;");
    }

    private void styleCombo(ComboBox<?> cb) {
        cb.setStyle(
                "-fx-background-color: rgba(255,255,255,0.05);" +
                        "-fx-border-color: rgba(255,255,255,0.12);" +
                        "-fx-border-radius: 6; -fx-background-radius: 6;" +
                        "-fx-text-fill: white; -fx-padding: 3 5;");
    }

    private String loginBtnStyle(String bg) {
        return "-fx-background-color: " + bg + ";" +
                "-fx-text-fill: #0f0f23; -fx-font-weight: bold;" +
                "-fx-font-size: 13px; -fx-padding: 14;" +
                "-fx-background-radius: 6; -fx-cursor: hand;";
    }

    public static void main(String[] args) {
        launch(args);
    }
}