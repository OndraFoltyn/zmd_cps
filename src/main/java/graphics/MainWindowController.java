package graphics;

import core.FileBindings;
import core.Helper;
import enums.SamplingType;
import enums.TransformType;
import enums.QualityType;
import enums.YCbCrType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jpeg.Process;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    public MenuItem closeButton;
    public Button countPsnrButt;
    public ComboBox<QualityType> psnrComboBox;
    public ComboBox<YCbCrType> ssimComboBox;
    public TextField mseTextField;
    public TextField maeTextField;
    public TextField psnrTextField;
    public TextField saeTextField;
    public Button countSsimButton;
    public TextField ssimTextField;
    public TextField mssimTextField;
    @FXML
    Button rgbButt;

    @FXML
    Button buttonInverseQuantize;
    @FXML
    Button buttonInverseToRGB;
    @FXML
    Button buttonInverseSample;
    @FXML
    Button buttonInverseTransform;
    @FXML
    Button buttonQuantize;
    @FXML
    Button buttonSample;
    @FXML
    Button buttonToYCbCr;
    @FXML
    Button buttonTransform;

    @FXML
    TextField qualityMSE;
    @FXML
    TextField qualityPSNR;

    @FXML
    Slider quantizeQuality;

    @FXML
    TextField quantizeQualityField;

    @FXML
    CheckBox shadesOfGrey;
    @FXML
    CheckBox showSteps;
    @FXML
    private ComboBox<SamplingType> sampling;

    @FXML
    private CheckBox greyScale;

    @FXML
    private CheckBox showCheck;

    @FXML
    private Button showImage;

    @FXML
    private Button rgbToYcBcRButt;

    @FXML
    private Spinner<Integer> transformBlock;

    @FXML
    private ComboBox<TransformType> transformType;

    @FXML
    private Button yButt;

    @FXML
    private Button yButtModified;

    private Process process;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Nastaveni vsech hodnot do combo boxu
        sampling.getItems().setAll(SamplingType.values());
        transformType.getItems().setAll(TransformType.values());
        psnrComboBox.getItems().setAll(QualityType.values());
        ssimComboBox.getItems().setAll(YCbCrType.values());

        // Nastaveni vychozich hodnot
        sampling.getSelectionModel().select(SamplingType.S_4_4_4);
        transformType.getSelectionModel().select(TransformType.DCT);
        quantizeQuality.setValue(50);
        psnrComboBox.getSelectionModel().select(QualityType.RGB);
        ssimComboBox.getSelectionModel().select(YCbCrType.Y);

        //Vytvoren listu moznosti, ktere budou uvnitr spinneru
        ObservableList<Integer> blocks = FXCollections.observableArrayList(2, 4, 8, 16, 32, 64, 128, 256, 512);
        SpinnerValueFactory<Integer> spinnerValues = new SpinnerValueFactory.ListSpinnerValueFactory<>(blocks);
        spinnerValues.setValue(8);
        transformBlock.setValueFactory(spinnerValues);

        // Nastaveni formatu cisel v textovych polich, aby bylo mozne zadavat pouze cisla. Plus metoda, ktera je na konci souboru.
        quantizeQualityField.setTextFormatter(new TextFormatter<>(Helper.NUMBER_FORMATTER));

        // Propojei slideru s textovym polem
        quantizeQualityField.textProperty().bindBidirectional(quantizeQuality.valueProperty(), NumberFormat.getIntegerInstance());


        process = new Process(FileBindings.defaultImage);
    }

    public void close() {
        Stage stage = ((Stage) buttonSample.getScene().getWindow());
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void closeWindows() {
        Dialogs.closeAllWindows();
    }

    public void showOriginal() {
        File f = new File(FileBindings.defaultImage);

        try {
            Dialogs.loadImageFromPath("images/obrazky/Lenna(testImage).png");
            Dialogs.showImageInWindow(ImageIO.read(f), "Original", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeImage() {
        File file = Dialogs.openFile();
        process = new Process(file.getAbsolutePath());
        reset();
    }

    public void reset() {
        Dialogs.closeAllWindows();
//        buttonToYCbCr.setDisable(true);
//        buttonInverseToRGB.setDisable(true);
    }


    public void showRGBModified() {
        try {
            Dialogs.showImageInWindow(process.getImageFromRGB(), "Modified RGB", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void convertToRGB() {
        try {
            process.convertToRGB();
//            buttonToYCbCr.setDisable(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void convertToYCbCr() {
        try {
            process.convertToYCbCr();
//            buttonInverseToRGB.setDisable(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sample() {
        try {
            process.downSample(sampling.getValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void inverseSample() {
        try {
            process.upSample(sampling.getValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void transform() {

    }

    public void inverseTransform() {

    }

    public void quantize() {

    }

    public void inverseQuantize() {

    }

    public void countQuality() {
        try {
            double[] qualityValue = process.qualityCount(psnrComboBox.getValue());

            double scale = Math.pow(10, 4);
            qualityValue[0] = Math.round(qualityValue[0] * scale) / scale;
            qualityValue[1] = Math.round(qualityValue[1] * scale) / scale;
            qualityValue[2] = Math.round(qualityValue[2] * scale) / scale;
            qualityValue[3] = Math.round(qualityValue[3] * scale) / scale;

            psnrTextField.setText(String.valueOf(qualityValue[0]));
            mseTextField.setText(String.valueOf(qualityValue[1]));
            maeTextField.setText(String.valueOf(qualityValue[2]));
            saeTextField.setText(String.valueOf(qualityValue[3]));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void countSsim(ActionEvent actionEvent) {
        try {
            double[] qualityValue = process.ssimCount(ssimComboBox.getValue());
            ssimTextField.setText(String.valueOf(qualityValue[0]));
            mssimTextField.setText(String.valueOf(qualityValue[1]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showBlueModified() {
        try {
            Dialogs.showImageInWindow(process.showModBlue(), "Modified Blue", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showBlueOriginal() {
        try {
            Dialogs.showImageInWindow(process.showBlue(), "Original Blue", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showCbModified() {
        try {
            Dialogs.showImageInWindow(process.showModCb(), "Modified Cb", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showCbOriginal() {
        try {
            Dialogs.showImageInWindow(process.showCb(), "Original Cb", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showCrModified() {
        try {
            Dialogs.showImageInWindow(process.showModCr(), "Modified Cr", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showCrOriginal() {
        try {
            Dialogs.showImageInWindow(process.showCr(), "Original Cr", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showGreenModified() {
        try {
            Dialogs.showImageInWindow(process.showModGreen(), "Original Green", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showGreenOriginal() {
        try {
            Dialogs.showImageInWindow(process.showGreen(), "Original Green", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showRedModified() {
        try {
            Dialogs.showImageInWindow(process.showModRed(), "Modified Red", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showRedOriginal() {
        try {
            Dialogs.showImageInWindow(process.showRed(), "Original Red", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showYModified() {
        try {
            Dialogs.showImageInWindow(process.showModY(), "Modified Y", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void showYOriginal() {
        try {
            Dialogs.showImageInWindow(process.showY(), "Original Y", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
