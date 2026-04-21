package it.hgnh.hgnh;

import it.hgnh.hgnh.models.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DashboardHomePane extends ScrollPane {

    public DashboardHomePane(User user, Runnable onLeistungen, Runnable onBuchungen, Runnable onNeuBuchung) {
        setStyle("-fx-background:transparent; -fx-background-color:transparent;");
        setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);

        VBox content = new VBox(28);
        content.setPadding(new Insets(36, 40, 36, 40));

        HBox titleRow = new HBox();
        titleRow.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(4);
        Label greeting = Styles.heading("Willkommen, " + user.getFirstName() + "!");
        Label sub = Styles.caption("Hotel Verwaltungssoftware — " + getRoleLabel(user));
        titleBox.getChildren().addAll(greeting, sub);
        titleRow.getChildren().add(titleBox);

        HBox statsRow = new HBox(16);
        statsRow.getChildren().addAll(
            statCard("🏨", "30", "Zimmer gesamt", Styles.PRIMARY),
            statCard("✅", "23", "Belegt", Styles.SUCCESS),
            statCard("🔑", "7",  "Frei", Styles.ACCENT),
            statCard("📋", "12", "Buchungen heute", "#7B4FE6")
        );
        for (var node : statsRow.getChildren()) HBox.setHgrow(node, Priority.ALWAYS);

        Label leistTitle = Styles.subheading("Hotelleistungen");
        HBox leistRow = new HBox(16);
        leistRow.getChildren().addAll(
            serviceCard("🍽️", "Restaurant", "12:00–14:00\n18:00–22:00", "50 Plätze",   "25€/Person"),
            serviceCard("🧖", "Sauna",      "15:00–22:00",              "10 Plätze",   "3€/Stunde"),
            serviceCard("🎮", "Spielhalle", "07:00–22:00",              "10 Plätze",   "2€/Stunde"),
            serviceCard("🏊", "Schwimmbad", "07:00–22:00",              "30 Plätze",   "1€/Stunde")
        );
        for (var node : leistRow.getChildren()) HBox.setHgrow(node, Priority.ALWAYS);

        HBox actionsTitle = new HBox();
        actionsTitle.setAlignment(Pos.CENTER_LEFT);
        Label actLabel = Styles.subheading("Schnellzugriff");

        HBox actions = new HBox(12);
        Button b1 = Styles.primaryBtn("➕  Neue Buchung");
        Button b2 = Styles.ghostBtn("📋  Buchungen anzeigen");
        Button b3 = Styles.ghostBtn("🏨  Leistungen");
        b1.setPrefHeight(42);
        b2.setPrefHeight(42);
        b3.setPrefHeight(42);
        b1.setOnAction(e -> onNeuBuchung.run());
        b2.setOnAction(e -> onBuchungen.run());
        b3.setOnAction(e -> onLeistungen.run());
        actions.getChildren().addAll(b1, b2, b3);

        content.getChildren().addAll(titleRow, statsRow, leistTitle, leistRow, actLabel, actions);
        setContent(content);
    }

    private VBox statCard(String icon, String value, String label, String color) {
        VBox card = new VBox(6);
        card.setStyle(Styles.card());
        card.setPadding(new Insets(22, 20, 22, 20));
        card.setAlignment(Pos.CENTER_LEFT);

        Label ico = new Label(icon);
        ico.setFont(Font.font("System", 22));

        Label val = new Label(value);
        val.setFont(Font.font("System", FontWeight.BOLD, 28));
        val.setTextFill(Color.web(color));

        Label lbl = Styles.caption(label);

        card.getChildren().addAll(ico, val, lbl);
        return card;
    }

    private VBox serviceCard(String icon, String name, String hours, String spots, String price) {
        VBox card = new VBox(10);
        card.setStyle(Styles.card());
        card.setPadding(new Insets(20, 18, 20, 18));

        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);
        Label ico = new Label(icon);
        ico.setFont(Font.font("System", 18));
        Label nLbl = new Label(name);
        nLbl.setFont(Font.font("System", FontWeight.BOLD, 14));
        nLbl.setTextFill(Color.web(Styles.TEXT_DARK));
        header.getChildren().addAll(ico, nLbl);

        Label hLbl = Styles.caption("⏰ " + hours);
        hLbl.setWrapText(true);
        Label sLbl = Styles.caption("👥 " + spots);

        Label pLbl = new Label(price);
        pLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        pLbl.setTextFill(Color.web(Styles.ACCENT));

        card.getChildren().addAll(header, hLbl, sLbl, pLbl);
        return card;
    }

    private UserRole getRoleLabel(User user) {
        if (user instanceof Guest) {
            return UserRole.GUEST;
        } else if (user instanceof Administrator) {
            return UserRole.ADMIN;
        } else if (user instanceof Receptionist) {
            return UserRole.RECEPTIONIST;
        } else {
            return null;
        }
    }
}
