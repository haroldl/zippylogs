package net.hotelling.harold.zippylogs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * Some programs don't clean up after themselves, creating directories of ever-growing log files
 * that eventually consume large amounts of disk storage. ZippyLogs is here to help you keep an
 * eye on those log files to make sure they don't take up too much space.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("zippylogsmain.fxml"));
        primaryStage.setTitle("ZippyLogs");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("zippylogsmain.css").toExternalForm());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
