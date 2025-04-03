module fxui {

    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive java.net.http; 

    requires transitive core;
    requires transitive com.fasterxml.jackson.databind;
    requires javafx.graphics;
 


    opens fxui.loginpage to javafx.graphics, javafx.fxml;
    opens fxui.mainpage to javafx.graphics, javafx.fxml;
    opens fxui.newuserpage to javafx.graphics, javafx.fxml;

    exports fxui;
    exports fxui.loginpage;
    exports fxui.mainpage;
    exports fxui.newuserpage;
    exports fxui.remote;

}
