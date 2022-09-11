module com.shifteleven {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.pdfsam.rxjavafx;

    opens com.shifteleven to javafx.fxml;
    exports com.shifteleven;
}
