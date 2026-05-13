package it.hgnh.hgnh;

import it.hgnh.hgnh.models.Administrator;
import it.hgnh.hgnh.models.Guest;
import it.hgnh.hgnh.models.Receptionist;
import it.hgnh.hgnh.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController {

    @FXML private HBox      card;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label     forgotLabel;
    @FXML private Label     errorLabel;
    @FXML private Button    signInBtn;
    @FXML private Button    guestBtn;
    @FXML private Button    signUpBtn;


    @FXML
    public void initialize() {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.15));
        shadow.setRadius(30);
        shadow.setOffsetY(8);
        card.setEffect(shadow);

        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(820, 500);
        clip.setArcWidth(16);
        clip.setArcHeight(16);
        card.setClip(clip);

        usernameField.focusedProperty().addListener((obs, wasF, isF) ->
                usernameField.setStyle(isF ? focusStyle() : baseStyle()));

        passwordField.focusedProperty().addListener((obs, wasF, isF) ->
                passwordField.setStyle(isF ? focusStyle() : baseStyle()));
    }

    // ── Event handlers ───────────────────────────────────────────────────────

    /** Sign In button clicked */
    @FXML
    protected void onSignIn() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Bitte Benutzername und Passwort eingeben.");
            return;
        }

        try {
            Stage current = (Stage) signInBtn.getScene().getWindow();

            if (username.equals("admin") && password.equals("admin123")) {
                Administrator admin = new Administrator("Hotel", "Administrator");
                new AdminView(admin).start(new Stage());
                current.close();
            } else if (username.equals("rezeption") && password.equals("rezept123")) {
                Receptionist receptionist = new Receptionist("Klaus", "Huber");
                new ReceptionistView(receptionist).start(new Stage());  // ReceptionistView wenn vorhanden
                current.close();
            } else {
                showError("Ungültiger Benutzername oder Passwort.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Fehler beim Öffnen der Ansicht.");
        }
    }

    @FXML
    protected void onGuest() {
        try {
            Stage current = (Stage) guestBtn.getScene().getWindow();
            Guest guest = new Guest("Gast", "Benutzer", "");
            new UserView(guest).start(current);
            current.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    protected void onSignUp() {
        // TODO: open a registration view
        showInfo("Redirecting to Sign Up…");
    }

    @FXML protected void onSignInHover() {
        signInBtn.setStyle(
                "-fx-background-color: #C4943A;" +
                        "-fx-background-radius: 25; -fx-border-radius: 25;" +
                        "-fx-font-family: 'Segoe UI'; -fx-font-weight: semi-bold;" +
                        "-fx-font-size: 13; -fx-text-fill: white; -fx-cursor: hand;");
    }

    @FXML protected void onSignInExit() {
        signInBtn.setStyle(
                "-fx-background-color: #D4A45A;" +
                        "-fx-background-radius: 25; -fx-border-radius: 25;" +
                        "-fx-font-family: 'Segoe UI'; -fx-font-weight: semi-bold;" +
                        "-fx-font-size: 13; -fx-text-fill: white; -fx-cursor: hand;");
    }

    @FXML protected void onGuestHover() {
        guestBtn.setStyle(
                "-fx-background-color: #F5F5F5;" +
                        "-fx-background-radius: 25; -fx-border-radius: 25;" +
                        "-fx-border-color: #AAAAAA; -fx-border-width: 1.5;" +
                        "-fx-font-family: 'Segoe UI'; -fx-font-weight: semi-bold;" +
                        "-fx-font-size: 13; -fx-text-fill: #555555; -fx-cursor: hand;");
    }

    @FXML protected void onGuestExit() {
        guestBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background-radius: 25; -fx-border-radius: 25;" +
                        "-fx-border-color: #CCCCCC; -fx-border-width: 1.5;" +
                        "-fx-font-family: 'Segoe UI'; -fx-font-weight: semi-bold;" +
                        "-fx-font-size: 13; -fx-text-fill: #888888; -fx-cursor: hand;");
    }


    @FXML protected void onSignUpHover() {
        signUpBtn.setStyle(
                "-fx-background-color: rgba(255,255,255,0.18);" +
                        "-fx-background-radius: 22; -fx-border-radius: 22;" +
                        "-fx-border-color: white; -fx-border-width: 2;" +
                        "-fx-font-family: 'Segoe UI'; -fx-font-weight: semi-bold;" +
                        "-fx-font-size: 13; -fx-text-fill: white; -fx-cursor: hand;");
    }

    @FXML protected void onSignUpExit() {
        signUpBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background-radius: 22; -fx-border-radius: 22;" +
                        "-fx-border-color: white; -fx-border-width: 2;" +
                        "-fx-font-family: 'Segoe UI'; -fx-font-weight: semi-bold;" +
                        "-fx-font-size: 13; -fx-text-fill: white; -fx-cursor: hand;");
    }


    @FXML protected void onForgotHover() {
        forgotLabel.setStyle(
                "-fx-font-family: 'Segoe UI'; -fx-font-size: 12;" +
                        "-fx-text-fill: #888888; -fx-cursor: hand;");
    }

    @FXML protected void onForgotExit() {
        forgotLabel.setStyle(
                "-fx-font-family: 'Segoe UI'; -fx-font-size: 12;" +
                        "-fx-text-fill: #AAAAAA; -fx-cursor: hand;");
    }


    private void showError(String msg) {
        errorLabel.setText(msg);
    }

    private void clearError() {
        errorLabel.setText("");
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private String baseStyle() {
        return "-fx-background-color: transparent;" +
                "-fx-border-color: transparent transparent #CCCCCC transparent;" +
                "-fx-border-width: 0 0 1.5 0;" +
                "-fx-font-size: 12;" +
                "-fx-text-fill: #333333;" +
                "-fx-prompt-text-fill: #AAAAAA;" +
                "-fx-padding: 6 0 6 0;";
    }

    private String focusStyle() {
        return "-fx-background-color: transparent;" +
                "-fx-border-color: transparent transparent #D4A45A transparent;" +
                "-fx-border-width: 0 0 1.5 0;" +
                "-fx-font-size: 12;" +
                "-fx-text-fill: #333333;" +
                "-fx-prompt-text-fill: #AAAAAA;" +
                "-fx-padding: 6 0 6 0;";
    }
}