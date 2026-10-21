module com.example.proyecthilos {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.proyecthilos to javafx.fxml;
    exports com.example.proyecthilos;
}