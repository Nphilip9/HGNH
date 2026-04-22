package it.hgnh.hgnh;

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
            /*DashboardHomePane pane = new DashboardHomePane(
                    new Administrator("Philip", "neumair"),
                    () -> {
                        System.out.println("on leistungen");
                    },
                    () -> {
                        System.out.println("on buchungen");
                    },
                    () -> {
                        System.out.println("on neu buchung");
                    }
            );*/


            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
