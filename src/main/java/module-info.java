module com.project.concessionario {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.project.concessionario to javafx.fxml;
    exports com.project.concessionario;
}