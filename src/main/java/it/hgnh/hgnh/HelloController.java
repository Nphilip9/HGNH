package it.hgnh.hgnh;

import it.hgnh.hgnh.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Login");
            DashboardHomePane pane = new DashboardHomePane(
                    new User("Philip", "neumair"),
                    () -> {
                        System.out.println("on leistungen");
                    },
                    () -> {
                        System.out.println("on buchungen");
                    },
                    () -> {
                        System.out.println("on neu buchung");
                    }
            );
            stage.setScene(new Scene(pane, 1000, 700));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
