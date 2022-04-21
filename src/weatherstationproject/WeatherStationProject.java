package weatherstationproject;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class WeatherStationProject extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("view/UI.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("Weather Station V2.0");
            stage.setScene(scene);
            stage.getIcons().add(new Image("/images/appIcon.png"));
            stage.setOnCloseRequest((WindowEvent event) -> {
                Platform.exit();
                System.exit(0);
            });
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
