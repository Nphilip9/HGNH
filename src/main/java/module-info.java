module it.hgnh.hgnh {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens it.hgnh.hgnh to javafx.fxml;
    exports it.hgnh.hgnh;
}