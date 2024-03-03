package core;

import javax.swing.*;
import javafx.scene.image.Image;
import java.awt.*;
import java.net.URL;

public class FileBindings {

    public static final String defaultImage = "images/obrazky/Lenna(testImage).png";

    public static final URL GUIMain = FileBindings.class.getClassLoader().getResource("graphics/MainWindow.fxml");

    public static Image favicon = new Image(FileBindings.class.getClassLoader().getResourceAsStream("favicon.png"));
}
