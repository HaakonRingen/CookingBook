module tests {

    requires transitive java.net.http;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    requires core;
    requires fxui;
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires javafx.graphics;

    opens tests to javafx.graphics, javafx.fxml;

}
