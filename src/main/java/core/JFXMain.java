package core;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JFXMain extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(FileBindings.GUIMain);

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setTitle("JPEG: Foltyn, 230245");
        primaryStage.getIcons().add(FileBindings.favicon);
        primaryStage.show();

        primaryStage.setOnCloseRequest((event -> {
            Platform.exit();
            System.exit(0);
        }));

    }

    public static void main(String[] args) {
        launch(args);
    }
}
