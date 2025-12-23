module UAS_PBO {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    

    requires transitive javafx.base; 
    requires transitive javafx.graphics; 

    // EXPORTS & OPENS
    exports application;
    exports controller;
    exports model;

    opens application to javafx.graphics, javafx.fxml;
    opens controller to javafx.fxml;
    opens model to javafx.base;
}