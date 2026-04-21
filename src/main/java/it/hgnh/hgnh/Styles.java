package it.hgnh.hgnh;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Styles {

    public static final String PRIMARY   = "#1A3C5E";
    public static final String ACCENT    = "#C8A96E";
    public static final String BG        = "#F5F5F0";
    public static final String CARD_BG   = "#FFFFFF";
    public static final String TEXT_DARK = "#1A1A2E";
    public static final String TEXT_GRAY = "#6B7280";
    public static final String SUCCESS   = "#2D6A4F";
    public static final String DANGER    = "#C0392B";
    public static final String SIDEBAR   = "#152D4A";

    public static String root() {
        return "-fx-background-color:" + BG + ";";
    }

    public static Button primaryBtn(String text) {
        Button b = new Button(text);
        b.setStyle(
                "-fx-background-color:" + PRIMARY + ";" +
                        "-fx-text-fill:white;" +
                        "-fx-font-size:13px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-padding:8 20;" +
                        "-fx-background-radius:6;" +
                        "-fx-cursor:hand;"
        );
        b.setOnMouseEntered(e -> b.setStyle(
                "-fx-background-color:#24527A;" +
                        "-fx-text-fill:white;" +
                        "-fx-font-size:13px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-padding:8 20;" +
                        "-fx-background-radius:6;" +
                        "-fx-cursor:hand;"
        ));
        b.setOnMouseExited(e -> b.setStyle(
                "-fx-background-color:" + PRIMARY + ";" +
                        "-fx-text-fill:white;" +
                        "-fx-font-size:13px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-padding:8 20;" +
                        "-fx-background-radius:6;" +
                        "-fx-cursor:hand;"
        ));
        return b;
    }

    public static Button accentBtn(String text) {
        Button b = new Button(text);
        b.setStyle(
                "-fx-background-color:" + ACCENT + ";" +
                        "-fx-text-fill:" + TEXT_DARK + ";" +
                        "-fx-font-size:13px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-padding:8 20;" +
                        "-fx-background-radius:6;" +
                        "-fx-cursor:hand;"
        );
        return b;
    }

    public static Button dangerBtn(String text) {
        Button b = new Button(text);
        b.setStyle(
                "-fx-background-color:" + DANGER + ";" +
                        "-fx-text-fill:white;" +
                        "-fx-font-size:13px;" +
                        "-fx-padding:8 20;" +
                        "-fx-background-radius:6;" +
                        "-fx-cursor:hand;"
        );
        return b;
    }

    public static Button ghostBtn(String text) {
        Button b = new Button(text);
        b.setStyle(
                "-fx-background-color:transparent;" +
                        "-fx-text-fill:" + TEXT_GRAY + ";" +
                        "-fx-font-size:13px;" +
                        "-fx-padding:8 16;" +
                        "-fx-background-radius:6;" +
                        "-fx-cursor:hand;" +
                        "-fx-border-color:#D1D5DB;" +
                        "-fx-border-radius:6;"
        );
        return b;
    }

    public static Label heading(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("System", FontWeight.BOLD, 22));
        l.setTextFill(Color.web(TEXT_DARK));
        return l;
    }

    public static Label subheading(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("System", FontWeight.BOLD, 15));
        l.setTextFill(Color.web(TEXT_DARK));
        return l;
    }

    public static Label caption(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("System", 12));
        l.setTextFill(Color.web(TEXT_GRAY));
        return l;
    }

    public static String card() {
        return "-fx-background-color:" + CARD_BG + ";" +
                "-fx-background-radius:10;" +
                "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.08),8,0,0,2);";
    }

    public static String fieldStyle() {
        return "-fx-background-color:white;" +
                "-fx-border-color:#D1D5DB;" +
                "-fx-border-radius:6;" +
                "-fx-background-radius:6;" +
                "-fx-padding:8 12;" +
                "-fx-font-size:13px;";
    }

    public static HBox statusBadge(String text, String color) {
        Label l = new Label(text);
        l.setStyle(
                "-fx-background-color:" + color + "22;" +
                        "-fx-text-fill:" + color + ";" +
                        "-fx-font-size:11px;" +
                        "-fx-font-weight:bold;" +
                        "-fx-padding:3 10;" +
                        "-fx-background-radius:20;"
        );
        HBox box = new HBox(l);
        return box;
    }
}

