module com.project.concessionario {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.project.concessionario to javafx.fxml;
    exports com.project.concessionario;
}