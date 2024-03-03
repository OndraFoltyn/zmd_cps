package jpeg;

import Jama.Matrix;
import enums.ColorType;
import enums.SamplingType;
import graphics.Dialogs;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

import static enums.ColorType.*;


public class Process {
    private BufferedImage originalImage;
    private int imageHeight;
    private int imageWidth;


    private int [][] originalRed, modifiedRed;
    private int [][] originalGreen, modifiedGreen;
    private int [][] originalBlue, modifiedBlue;

    private Matrix originalY, modifiedY;
    private Matrix originalCb, modifiedCb;
    private Matrix originalCr, modifiedCr;

    public Process(String path) {
        this.originalImage = Dialogs.loadImageFromPath(path);

        imageWidth = originalImage.getWidth();
        imageHeight = originalImage.getHeight();

        originalRed = new int[imageHeight][imageWidth];
        originalGreen = new int[imageHeight][imageWidth];
        originalBlue = new int[imageHeight][imageWidth];

        originalY = new Matrix(imageHeight, imageWidth);
        originalCb = new Matrix(imageHeight, imageWidth);
        originalCr = new Matrix(imageHeight, imageWidth);

        setOriginalRGB();
    }


    private void setOriginalRGB() {
        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                Color color = new Color(originalImage.getRGB(w, h));
                originalRed[h][w] = color.getRed();
                originalBlue[h][w] = color.getBlue();
                originalGreen[h][w] = color.getGreen();
            }
        }
    }

    public BufferedImage getImageFromRGB() {
        BufferedImage bfImage = new BufferedImage(
                imageWidth, imageHeight,
                BufferedImage.TYPE_INT_RGB);
        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                bfImage.setRGB(w,h,
                        (new Color(modifiedRed[h][w],
                                   modifiedGreen[h][w],
                                   modifiedBlue[h][w])).getRGB());
            }
        }
        return bfImage;
    }

    public BufferedImage showOneColorImageFromRGB(int [][] color, ColorType type) {
        BufferedImage bfImage = new BufferedImage(
                imageWidth, imageHeight,
                BufferedImage.TYPE_INT_RGB);

        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                int rgbValue;
                switch (type) {
                    case RED:
                        rgbValue = new Color(color[h][w], 0, 0).getRGB();
                        break;
                    case BLUE:
                        rgbValue = new Color(0, 0, color[h][w]).getRGB();
                        break;
                    case GREEN:
                        rgbValue = new Color(0,color[h][w], 0).getRGB();
                        break;
                    default:
                        rgbValue = new Color(color[h][w], color[h][w], color[h][w]).getRGB();
                    }
                bfImage.setRGB(w, h, rgbValue);
            }
        }
        return bfImage;
    }

    public BufferedImage showOneColorImageFromYCbCr(Matrix color) {
        BufferedImage bfImage = new BufferedImage(
                color.getColumnDimension(), color.getRowDimension(),
                BufferedImage.TYPE_INT_RGB);

        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                // Zaokrouhlení a oříznutí hodnot mimo rozsah 0-255
//                int grey = (int) Math.min(Math.max(color.get(h, w), 0), 255);
                // Vložení hodnoty do všech barevných složek pro získání šedého obrázku
                bfImage.setRGB(w, h,
                        (new Color((int)color.get(h,w),
                                (int)color.get(h,w),
                                (int)color.get(h,w))).getRGB());
            }
        }
        return bfImage;
    }

    public void convertToYCbCr(){
        Matrix[] res = ColorTransform.convertOriginalRGBtoYcBcR(originalRed, originalGreen, originalBlue);
        originalY = res[0];
        originalCb = res[1];
        originalCr = res[2];

        modifiedY = originalY.copy();
        modifiedCb = originalCb.copy();
        modifiedCr = originalCr.copy();
    }
    public void convertToRGB(){
        int [][][] res = ColorTransform.convertModifiedYcBcRtoRGB(originalY, originalCb, originalCr);
        modifiedRed = res[0];
        modifiedGreen = res[1];
        modifiedBlue = res[2];
    }

    public BufferedImage showRed() {
        return showOneColorImageFromRGB(originalRed, RED);
    }
    public BufferedImage showGreen() {
        return showOneColorImageFromRGB(originalGreen, GREEN);
    }

    public BufferedImage showBlue() {
        return showOneColorImageFromRGB(originalBlue, BLUE);
    }

    public BufferedImage showModRed() {
        return showOneColorImageFromRGB(modifiedRed, RED);
    }

    public BufferedImage showModGreen() {
        return showOneColorImageFromRGB(modifiedGreen, GREEN);
    }

    public BufferedImage showModBlue() {
        return showOneColorImageFromRGB(modifiedBlue, BLUE);
    }

    public BufferedImage showY() {
        return showOneColorImageFromYCbCr(originalY);
    }

    public BufferedImage showCb() {
        return showOneColorImageFromYCbCr(originalCb);
    }

    public BufferedImage showCr() {
        return showOneColorImageFromYCbCr(originalCr);
    }

    public BufferedImage showModY() {
        return showOneColorImageFromYCbCr(modifiedY);
    }

    public BufferedImage showModCb() {
        return showOneColorImageFromYCbCr(modifiedCb);
    }

    public BufferedImage showModCr() {
        return showOneColorImageFromYCbCr(modifiedCr);
    }

    public void downSample(SamplingType samplingType) {
        modifiedCb = Sampling.sampleDown(modifiedCb, samplingType);
        modifiedCr = Sampling.sampleDown(modifiedCr, samplingType);
    }

    public void upSample(SamplingType samplingType) {
        modifiedCb = Sampling.sampleUp(modifiedCb, samplingType);
        modifiedCr = Sampling.sampleUp(modifiedCr, samplingType);
    }
}


