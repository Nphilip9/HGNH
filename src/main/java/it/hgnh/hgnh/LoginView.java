package it.hgnh.hgnh;

import it.hgnh.hgnh.AdminView;
import it.hgnh.hgnh.ReceptionistView;
import it.hgnh.hgnh.UserView;
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

public class LoginView extends Application {

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();

        Rectangle bg = new Rectangle(900, 620);
        bg.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1a1a2e")),
                new Stop(1, Color.web("#16213e"))));
        root.getChildren().add(bg);

        HBox mainLayout = new HBox();
        mainLayout.setPrefSize(900, 620);

        VBox leftPanel = new VBox(20);
        leftPanel.setPrefWidth(420);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPadding(new Insets(60));
        leftPanel.setStyle("-fx-background-color: rgba(255,255,255,0.03);");

        Label hotelTitle = new Label("LARCHER");
        hotelTitle.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: #c9a84c; -fx-font-family: 'Georgia';");

        Label hotelSub = new Label("GRAND HOTEL");
        hotelSub.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(255,255,255,0.5); -fx-letter-spacing: 6; -fx-font-family: 'Georgia';");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #c9a84c; -fx-opacity: 0.4;");
        sep.setMaxWidth(120);

        Label tagline = new Label("Ihr Zuhause für besondere Momente");
        tagline.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(255,255,255,0.35); -fx-font-style: italic;");
        tagline.setWrapText(true);
        tagline.setTextAlignment(TextAlignment.CENTER);

        leftPanel.getChildren().addAll(hotelTitle, hotelSub, sep, tagline);

        VBox rightPanel = new VBox(18);
        rightPanel.setPrefWidth(480);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(60, 70, 60, 70));
        rightPanel.setStyle("-fx-background-color: #0f0f23;");

        Label loginTitle = new Label("Anmeldung");
        loginTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: 'Georgia';");

        Label loginSub = new Label("Bitte melden Sie sich an, um fortzufahren");
        loginSub.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(255,255,255,0.4);");

        VBox usernameBox = new VBox(6);
        Label userLabel = new Label("BENUTZERNAME");
        userLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #c9a84c; -fx-font-weight: bold;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Benutzername eingeben...");
        usernameField.setStyle(
                "-fx-background-color: rgba(255,255,255,0.05);" +
                        "-fx-border-color: rgba(255,255,255,0.12);" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: rgba(255,255,255,0.25);" +
                        "-fx-padding: 12 15;"
        );
        usernameBox.getChildren().addAll(userLabel, usernameField);

        VBox passwordBox = new VBox(6);
        Label passLabel = new Label("PASSWORT");
        passLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #c9a84c; -fx-font-weight: bold;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Passwort eingeben...");
        passwordField.setStyle(
                "-fx-background-color: rgba(255,255,255,0.05);" +
                        "-fx-border-color: rgba(255,255,255,0.12);" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: rgba(255,255,255,0.25);" +
                        "-fx-padding: 12 15;"
        );
        passwordBox.getChildren().addAll(passLabel, passwordField);

        VBox roleBox = new VBox(6);
        Label roleLabel = new Label("ROLLE");
        roleLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #c9a84c; -fx-font-weight: bold;");
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Gast", "Rezeptionist", "Administrator");
        roleCombo.setPromptText("Rolle auswählen...");
        roleCombo.setMaxWidth(Double.MAX_VALUE);
        roleCombo.setStyle(
                "-fx-background-color: rgba(255,255,255,0.05);" +
                        "-fx-border-color: rgba(255,255,255,0.12);" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 3 5;"
        );
        roleBox.getChildren().addAll(roleLabel, roleCombo);

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");

        Button loginBtn = new Button("ANMELDEN");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle(
                "-fx-background-color: #c9a84c;" +
                        "-fx-text-fill: #0f0f23;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 14;" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;"
        );

        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle(
                "-fx-background-color: #e0ba5c;" +
                        "-fx-text-fill: #0f0f23;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 14;" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;"
        ));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle(
                "-fx-background-color: #c9a84c;" +
                        "-fx-text-fill: #0f0f23;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 14;" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;"
        ));

        loginBtn.setOnAction(e -> {
            String user = usernameField.getText().trim();
            String pass = passwordField.getText().trim();
            String role = roleCombo.getValue();

            if (user.isEmpty() || pass.isEmpty() || role == null) {
                errorLabel.setText("Bitte alle Felder ausfüllen.");
                return;
            }

            Stage current = (Stage) loginBtn.getScene().getWindow();
            switch (role) {
                case "Administrator" -> new AdminView().start(new Stage());
                case "Rezeptionist"  -> new ReceptionistView().start(new Stage());
                case "Gast"          -> new UserView().start(new Stage());
            }
            current.close();
        });

        rightPanel.getChildren().addAll(
                loginTitle, loginSub,
                new Region() {{ setMinHeight(8); }},
                usernameBox, passwordBox, roleBox,
                errorLabel, loginBtn
        );

        mainLayout.getChildren().addAll(leftPanel, rightPanel);
        root.getChildren().add(mainLayout);

        Scene scene = new Scene(root, 900, 620);
        stage.setScene(scene);
        stage.setTitle("Hotel Larcher – Anmeldung");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}